package frc.robot.subsystems;

import com.google.errorprone.annotations.concurrent.GuardedBy;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.RobotController;
import com.swervedrivespecialties.swervelib.Mk3SwerveModuleHelper;
import com.swervedrivespecialties.swervelib.SwerveModule;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.libraries.external.control.*;
import frc.robot.libraries.external.drivers.Gyroscope;
import frc.robot.libraries.external.kinematics.*;
import frc.robot.libraries.external.math.RigidTransform2;
import frc.robot.libraries.external.math.Rotation2;
import frc.robot.libraries.external.math.Vector2;
import frc.robot.libraries.external.robot.drivers.NavX;
import frc.robot.libraries.external.robot.UpdateManager;
import frc.robot.libraries.external.util.*;

import static frc.robot.constants.Constants.*;

import java.util.Optional;

public class DrivetrainSubsystem implements Subsystem, UpdateManager.Updatable {
  public static final double TRACKWIDTH = 1.0;
  public static final double WHEELBASE = 1.0;

  public static final DrivetrainFeedforwardConstants FEEDFORWARD_CONSTANTS = new DrivetrainFeedforwardConstants(
    0.042746,
    0.0032181,
    0.30764
  );

  public static final TrajectoryConstraint[] TRAJECTORY_CONSTRAINTS = {
    new FeedforwardConstraint(11.0, FEEDFORWARD_CONSTANTS.getVelocityConstant(), FEEDFORWARD_CONSTANTS.getAccelerationConstant(), false),
    new MaxAccelerationConstraint(12.5 * 12.0),
    new CentripetalAccelerationConstraint(15 * 12.0)
  };

  private static final int MAX_LATENCY_COMPENSATION_MAP_ENTRIES = 25;

  private final HolonomicMotionProfiledTrajectoryFollower follower = new HolonomicMotionProfiledTrajectoryFollower(
    new PidConstants(0.4, 0.0, 0.025),
    new PidConstants(5.0, 0.0, 0.0),
    new HolonomicFeedforward(FEEDFORWARD_CONSTANTS)
  );

  private final SwerveKinematics swerveKinematics = new SwerveKinematics(
    new Vector2(TRACKWIDTH / 2.0, WHEELBASE / 2.0),         //front left
    new Vector2(TRACKWIDTH / 2.0, -WHEELBASE / 2.0),        //front right
    new Vector2(-TRACKWIDTH / 2.0, WHEELBASE / 2.0),       //back left
    new Vector2(-TRACKWIDTH / 2.0, -WHEELBASE / 2.0)        //back right
  );

  private final SwerveDriveKinematics wpi_driveKinematics = new SwerveDriveKinematics(
    new Translation2d(-TRACKWIDTH / 2.0, WHEELBASE / 2.0), //front left
    new Translation2d(TRACKWIDTH / 2.0, WHEELBASE / 2.0), //front right
    new Translation2d(-TRACKWIDTH / 2.0, -WHEELBASE / 2.0), // back left
    new Translation2d(TRACKWIDTH / 2.0, -WHEELBASE / 2.0) // back right
  );


  private final SwerveModule[] modules;

  private final Object sensorLock = new Object();
  @GuardedBy("sensorLock")
  private final Gyroscope gyroscope = new NavX(SPI.Port.kMXP);; // NavX connected over MXP

  private final Object kinematicsLock = new Object();
  @GuardedBy("kinematicsLock")
  private final SwerveOdometry swerveOdometry = new SwerveOdometry(swerveKinematics, RigidTransform2.ZERO);
  @GuardedBy("kinematicsLock")
  private RigidTransform2 pose = RigidTransform2.ZERO;
  @GuardedBy("kinematicsLock")
  private final InterpolatingTreeMap<InterpolatingDouble, RigidTransform2> latencyCompensationMap = new InterpolatingTreeMap<>();
  @GuardedBy("kinematicsLock")
  private Vector2 velocity = Vector2.ZERO;
  @GuardedBy("kinematicsLock")
  private double angularVelocity = 0.0;

  private final Object stateLock = new Object();
  @GuardedBy("stateLock")
  private HolonomicDriveSignal driveSignal = null;

  // Logging
  private final NetworkTableEntry odometryXEntry;
  private final NetworkTableEntry odometryYEntry;
  private final NetworkTableEntry odometryAngleEntry;

  public DrivetrainSubsystem() {
    synchronized (sensorLock) {
      gyroscope.setInverted(false);
    }

    ShuffleboardTab tab = Shuffleboard.getTab("Drivetrain");

    SwerveModule frontLeftModule = Mk3SwerveModuleHelper.createFalcon500(
      // This parameter is optional, but will allow you to see the current state of the module on the dashboard.
      tab.getLayout("Front Left Module", BuiltInLayouts.kList)
        .withSize(2, 4)
        .withPosition(0, 0),
      // This can either be STANDARD or FAST depending on your gear configuration
      Mk3SwerveModuleHelper.GearRatio.STANDARD,
      FRONT_LEFT_MODULE_DRIVE_MOTOR,
      FRONT_LEFT_MODULE_STEER_MOTOR,
      FRONT_LEFT_MODULE_STEER_ENCODER,
      FRONT_LEFT_MODULE_STEER_OFFSET
    );

    // We will do the same for the other modules
    SwerveModule frontRightModule = Mk3SwerveModuleHelper.createFalcon500(
      tab.getLayout("Front Right Module", BuiltInLayouts.kList)
        .withSize(2, 4)
        .withPosition(2, 0),
      Mk3SwerveModuleHelper.GearRatio.STANDARD,
      FRONT_RIGHT_MODULE_DRIVE_MOTOR,
      FRONT_RIGHT_MODULE_STEER_MOTOR,
      FRONT_RIGHT_MODULE_STEER_ENCODER,
      FRONT_RIGHT_MODULE_STEER_OFFSET
    );

    SwerveModule backLeftModule = Mk3SwerveModuleHelper.createFalcon500(
      tab.getLayout("Back Left Module", BuiltInLayouts.kList)
        .withSize(2, 4)
        .withPosition(4, 0),
      Mk3SwerveModuleHelper.GearRatio.STANDARD,
      BACK_LEFT_MODULE_DRIVE_MOTOR,
      BACK_LEFT_MODULE_STEER_MOTOR,
      BACK_LEFT_MODULE_STEER_ENCODER,
      BACK_LEFT_MODULE_STEER_OFFSET
    );

    SwerveModule backRightModule = Mk3SwerveModuleHelper.createFalcon500(
      tab.getLayout("Back Right Module", BuiltInLayouts.kList)
        .withSize(2, 4)
        .withPosition(6, 0),
      Mk3SwerveModuleHelper.GearRatio.STANDARD,
      BACK_RIGHT_MODULE_DRIVE_MOTOR,
      BACK_RIGHT_MODULE_STEER_MOTOR,
      BACK_RIGHT_MODULE_STEER_ENCODER,
      BACK_RIGHT_MODULE_STEER_OFFSET
    );

    modules = new SwerveModule[]{frontLeftModule, frontRightModule, backLeftModule, backRightModule};

    odometryXEntry = tab.add("X", 0.0)
      .withPosition(0, 0)
      .withSize(1, 1)
      .getEntry();
    odometryYEntry = tab.add("Y", 0.0)
      .withPosition(0, 1)
      .withSize(1, 1)
      .getEntry();
    odometryAngleEntry = tab.add("Angle", 0.0)
      .withPosition(0, 2)
      .withSize(1, 1)
      .getEntry();
    tab.addNumber("Trajectory X", () -> {
        if (follower.getLastState() == null) {
            return 0.0;
        }
        return follower.getLastState().getPathState().getPosition().x;
      })
      .withPosition(1, 0)
      .withSize(1, 1);
    tab.addNumber("Trajectory Y", () -> {
        if (follower.getLastState() == null) {
            return 0.0;
        }
        return follower.getLastState().getPathState().getPosition().y;
      })
      .withPosition(1, 1)
      .withSize(1, 1);

    tab.addNumber("Rotation Voltage", () -> {
      HolonomicDriveSignal signal;
      synchronized (stateLock) {
        signal = driveSignal;
      }

      if (signal == null) {
        return 0.0;
      }

      return signal.getRotation() * RobotController.getBatteryVoltage();
    });

    tab.addNumber("Average Velocity", this::getAverageAbsoluteValueVelocity);
  }

  public RigidTransform2 getPose() {
    synchronized (kinematicsLock) {
      return pose;
    }
  }

  public Vector2 getVelocity() {
    synchronized (kinematicsLock) {
      return velocity;
    }
  }

  public double getAngularVelocity() {
    synchronized (kinematicsLock) {
      return angularVelocity;
    }
  }

  public void drive(Vector2 translationalVelocity, double rotationalVelocity, boolean isFieldOriented) {
    synchronized (stateLock) {
      driveSignal = new HolonomicDriveSignal(translationalVelocity, rotationalVelocity, isFieldOriented);
    }
  }

  public void resetPose(RigidTransform2 pose) {
    synchronized (kinematicsLock) {
      this.pose = pose;
      swerveOdometry.resetPose(pose);
    }
  }

  public void resetGyroAngle(Rotation2 angle) {
    synchronized (sensorLock) {
      gyroscope.setAdjustmentAngle(
              gyroscope.getUnadjustedAngle().rotateBy(angle.inverse())
      );
    }
  }

  public double getAverageAbsoluteValueVelocity() {
    double averageVelocity = 0;
    for (var module : modules) {
      averageVelocity += Math.abs(module.getDriveVelocity());
    }
    return averageVelocity / 4;
  }

  private void updateOdometry(double time, double dt) {
    Vector2[] moduleVelocities = new Vector2[modules.length];
    for (int i = 0; i < modules.length; i++) {
      var module = modules[i];

      moduleVelocities[i] = Vector2.fromAngle(Rotation2.fromRadians(module.getSteerAngle())).scale(module.getDriveVelocity() * 39.37008);
    }

    Rotation2 angle;
    double angularVelocity;
    synchronized (sensorLock) {
      angle = gyroscope.getAngle();
      angularVelocity = gyroscope.getRate();
    }

    ChassisVelocity velocity = swerveKinematics.toChassisVelocity(moduleVelocities);

    synchronized (kinematicsLock) {

      this.pose = swerveOdometry.update(angle, dt, moduleVelocities);
      if (latencyCompensationMap.size() > MAX_LATENCY_COMPENSATION_MAP_ENTRIES) {
          latencyCompensationMap.remove(latencyCompensationMap.firstKey());
      }
      latencyCompensationMap.put(new InterpolatingDouble(time), pose);
      this.velocity = velocity.getTranslationalVelocity();
      this.angularVelocity = angularVelocity;
    }
  }

  private void updateModules(HolonomicDriveSignal driveSignal, double dt) {
    ChassisVelocity chassisVelocity;
    if (driveSignal == null) {
      chassisVelocity = new ChassisVelocity(Vector2.ZERO, 0.0);
    } else if (driveSignal.isFieldOriented()) {
      chassisVelocity = new ChassisVelocity(
        driveSignal.getTranslation().rotateBy(getPose().rotation.inverse()),
        driveSignal.getRotation()
      );
    } else {
      chassisVelocity = new ChassisVelocity(
        driveSignal.getTranslation(),
        driveSignal.getRotation()
      );
    }

    Vector2[] moduleOutputs = swerveKinematics.toModuleVelocities(chassisVelocity);
    SwerveKinematics.normalizeModuleVelocities(moduleOutputs, 1);
    for (int i = 0; i < moduleOutputs.length; i++) {
      var module = modules[i];
      module.set(moduleOutputs[i].length * 12.0, moduleOutputs[i].getAngle().toRadians());
    }
  }

  public RigidTransform2 getPoseAtTime(double timestamp) {
    synchronized (kinematicsLock) {
      if (latencyCompensationMap.isEmpty()) {
          return RigidTransform2.ZERO;
      }
      return latencyCompensationMap.getInterpolated(new InterpolatingDouble(timestamp));
    }
  }

  @Override
  public void update(double time, double dt) {
    updateOdometry(time, dt);

    HolonomicDriveSignal driveSignal;
    Optional<HolonomicDriveSignal> trajectorySignal = follower.update(
      getPose(),
      getVelocity(),
      getAngularVelocity(),
      time,
      dt
    );
    if (trajectorySignal.isPresent()) {
      driveSignal = trajectorySignal.get();
      driveSignal = new HolonomicDriveSignal(
        driveSignal.getTranslation().scale(1.0 / RobotController.getBatteryVoltage()),
        driveSignal.getRotation() / RobotController.getBatteryVoltage(),
        driveSignal.isFieldOriented()
      );
    } else {
      synchronized (stateLock) {
        driveSignal = this.driveSignal;
      }
    }

    updateModules(driveSignal, dt);
  }

  @Override
  public void periodic() {
    RigidTransform2 pose = getPose();
    odometryXEntry.setDouble(pose.translation.x);
    odometryYEntry.setDouble(pose.translation.y);
    odometryAngleEntry.setDouble(getPose().rotation.toDegrees());
  }

  public HolonomicMotionProfiledTrajectoryFollower getFollower() {
    return follower;
  }
}
