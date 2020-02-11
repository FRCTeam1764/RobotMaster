/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.constants.PIDConstants;
import frc.robot.constants.PathfinderConstants;

/**
 * Add your docs here.
 */
public class Drivetrain extends SubsystemBase {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
 
  public WPI_TalonFX _rightMaster = new WPI_TalonFX(11);
  public WPI_TalonFX _rightFollower = new WPI_TalonFX(12);
  public WPI_TalonFX _rightFollower2 = new WPI_TalonFX(13);
  public WPI_TalonFX _leftMaster = new WPI_TalonFX(14);
  public WPI_TalonFX _leftFollower = new WPI_TalonFX(15);
  public WPI_TalonFX _leftFollower2 = new WPI_TalonFX(16);

  public final DifferentialDrive diffDrive = new DifferentialDrive(_leftMaster, _rightMaster);

  private final TalonFXSensorCollection m_leftEncoder = _leftMaster.getSensorCollection();
  private final TalonFXSensorCollection m_rightEncoder = _rightMaster.getSensorCollection();

  private AHRS navx = navxSetUp();

  private DifferentialDriveOdometry m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));;

  public Drivetrain(){

    /* factory default values */
  _rightMaster.configFactoryDefault();
  _rightFollower.configFactoryDefault();
  _leftMaster.configFactoryDefault();
  _leftFollower.configFactoryDefault();
  _rightFollower2.configFactoryDefault();
  _leftFollower2.configFactoryDefault();

  _leftMaster.configOpenloopRamp(0.5);
  _rightMaster.configOpenloopRamp(0.5);

  _leftMaster.setNeutralMode(NeutralMode.Brake);
  _rightMaster.setNeutralMode(NeutralMode.Brake);

  /* set up followers */
  _rightFollower.follow(_rightMaster);
  _leftFollower.follow(_leftMaster);

  _rightFollower2.follow(_rightMaster);
  _leftFollower2.follow(_leftMaster);

  /* [3] flip values so robot moves forward when stick-forward/LEDs-green */
  _rightMaster.setInverted(true); // !< Update this
  _leftMaster.setInverted(false); // !< Update this

  /*
   * set the invert of the followers to match their respective master controllers
   */
  _rightFollower.setInverted(true);
  _leftFollower.setInverted(false);
 _rightFollower2.setInverted(true);
  _leftFollower2.setInverted(false);  
     /* Disable all motor controllers */
		_rightMaster.set(ControlMode.PercentOutput, 0);
		_leftMaster.set(ControlMode.PercentOutput, 0);

		/* Factory Default all hardware to prevent unexpected behaviour */
		_rightMaster.configFactoryDefault();
		_leftMaster.configFactoryDefault();
		
		/* Set Neutral Mode */
		_leftMaster.setNeutralMode(NeutralMode.Brake);
		_rightMaster.setNeutralMode(NeutralMode.Brake);
		
		/** Feedback Sensor Configuration */
		
		/* Configure the left Talon's selected sensor as local QuadEncoder */
		_leftMaster.configSelectedFeedbackSensor(	FeedbackDevice.IntegratedSensor,				// Local Feedback Source
													PIDConstants.PID_PRIMARY,					// PID Slot for Source [0, 1]
													PIDConstants.kTimeoutMs);					// Configuration Timeout

		/* Configure the Remote Talon's selected sensor as a remote sensor for the right Talon */
		_rightMaster.configRemoteFeedbackFilter(_leftMaster.getDeviceID(),					// Device ID of Source
												RemoteSensorSource.TalonSRX_SelectedSensor,	// Remote Feedback Source
												PIDConstants.REMOTE_0,							// Source number [0, 1]
												PIDConstants.kTimeoutMs);						// Configuration Timeout
		
		/* Setup Sum signal to be used for Distance */
		_rightMaster.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.IntegratedSensor, PIDConstants.kTimeoutMs);				// Feedback Device of Remote Talon
		_rightMaster.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.CTRE_MagEncoder_Relative, PIDConstants.kTimeoutMs);	// Quadrature Encoder of current Talon
		
		/* Setup Difference signal to be used for Turn */
		_rightMaster.configSensorTerm(SensorTerm.Diff1, FeedbackDevice.RemoteSensor0, PIDConstants.kTimeoutMs);
		_rightMaster.configSensorTerm(SensorTerm.Diff0, FeedbackDevice.CTRE_MagEncoder_Relative, PIDConstants.kTimeoutMs);
		
		/* Configure Sum [Sum of both QuadEncoders] to be used for Primary PID Index */
		_rightMaster.configSelectedFeedbackSensor(	FeedbackDevice.IntegratedSensor, 
													PIDConstants.PID_PRIMARY,
													PIDConstants.kTimeoutMs);
		
		/* Scale Feedback by 0.5 to half the sum of Distance */
		_rightMaster.configSelectedFeedbackCoefficient(	0.5, 						// Coefficient
														PIDConstants.PID_PRIMARY,		// PID Slot of Source 
														PIDConstants.kTimeoutMs);		// Configuration Timeout
		
		/* Configure Difference [Difference between both QuadEncoders] to be used for Auxiliary PID Index */
		_rightMaster.configSelectedFeedbackSensor(	FeedbackDevice.SensorDifference, 
													PIDConstants.PID_TURN, 
													PIDConstants.kTimeoutMs);
		
		/* Scale the Feedback Sensor using a coefficient */
		_rightMaster.configSelectedFeedbackCoefficient(	1,
														PIDConstants.PID_TURN, 
														PIDConstants.kTimeoutMs);
		/* Configure output */
		_leftMaster.setSensorPhase(true);
		_rightMaster.setSensorPhase(true);
		
		/* Set status frame periods to ensure we don't have stale data */
		_rightMaster.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20, PIDConstants.kTimeoutMs);
		_rightMaster.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20, PIDConstants.kTimeoutMs);
		_rightMaster.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 20, PIDConstants.kTimeoutMs);
		_rightMaster.setStatusFramePeriod(StatusFrame.Status_10_Targets, 20, PIDConstants.kTimeoutMs);
		_leftMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, PIDConstants.kTimeoutMs);

		/* Configure neutral deadband */
		_rightMaster.configNeutralDeadband(PIDConstants.kNeutralDeadband, PIDConstants.kTimeoutMs);
		_leftMaster.configNeutralDeadband(PIDConstants.kNeutralDeadband, PIDConstants.kTimeoutMs);
		
		/* Motion Magic Configurations */
		_rightMaster.configMotionAcceleration(2000, PIDConstants.kTimeoutMs);
		_rightMaster.configMotionCruiseVelocity(2000, PIDConstants.kTimeoutMs);

		/**
		 * Max out the peak output (for all modes).  
		 * However you can limit the output of a given PID object with configClosedLoopPeakOutput().
		 */
		_leftMaster.configPeakOutputForward(+1.0, PIDConstants.kTimeoutMs);
		_leftMaster.configPeakOutputReverse(-1.0, PIDConstants.kTimeoutMs);
		_rightMaster.configPeakOutputForward(+1.0, PIDConstants.kTimeoutMs);
		_rightMaster.configPeakOutputReverse(-1.0, PIDConstants.kTimeoutMs);

		/* FPID Gains for distance servo */
		_rightMaster.config_kP(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kP, PIDConstants.kTimeoutMs);
		_rightMaster.config_kI(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kI, PIDConstants.kTimeoutMs);
		_rightMaster.config_kD(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kD, PIDConstants.kTimeoutMs);
		_rightMaster.config_kF(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kF, PIDConstants.kTimeoutMs);
		_rightMaster.config_IntegralZone(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kIzone, PIDConstants.kTimeoutMs);
		_rightMaster.configClosedLoopPeakOutput(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kPeakOutput, PIDConstants.kTimeoutMs);
		_rightMaster.configAllowableClosedloopError(PIDConstants.kSlot_Distanc, 0, PIDConstants.kTimeoutMs);

		/* FPID Gains for turn servo */
		_rightMaster.config_kP(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kP, PIDConstants.kTimeoutMs);
		_rightMaster.config_kI(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kI, PIDConstants.kTimeoutMs);
		_rightMaster.config_kD(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kD, PIDConstants.kTimeoutMs);
		_rightMaster.config_kF(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kF, PIDConstants.kTimeoutMs);
		_rightMaster.config_IntegralZone(PIDConstants.kSlot_Turning, (int)PIDConstants.kGains_Turning.kIzone, PIDConstants.kTimeoutMs);
		_rightMaster.configClosedLoopPeakOutput(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kPeakOutput, PIDConstants.kTimeoutMs);
		_rightMaster.configAllowableClosedloopError(PIDConstants.kSlot_Turning, 0, PIDConstants.kTimeoutMs);

		/**
		 * 1ms per loop.  PID loop can be slowed down if need be.
		 * For example,
		 * - if sensor updates are too slow
		 * - sensor deltas are very small per update, so derivative error never gets large enough to be useful.
		 * - sensor movement is very slow causing the derivative error to be near zero.
		 */
		int closedLoopTimeMs = 1;
		_rightMaster.configClosedLoopPeriod(0, closedLoopTimeMs, PIDConstants.kTimeoutMs);
		_rightMaster.configClosedLoopPeriod(1, closedLoopTimeMs, PIDConstants.kTimeoutMs);

		/**
		 * configAuxPIDPolarity(boolean invert, int timeoutMs)
		 * false means talon's local output is PID0 + PID1, and other side Talon is PID0 - PID1
		 * true means talon's local output is PID0 - PID1, and other side Talon is PID0 + PID1
		 */
		_rightMaster.configAuxPIDPolarity(false, PIDConstants.kTimeoutMs);

		/* Initialize */
		_rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_Targets, 10);
		resetEncoders();
	}

	@Override
  public void periodic(){
    m_odometry.update(Rotation2d.fromDegrees(getHeading()), m_leftEncoder.getIntegratedSensorPosition(),
    m_rightEncoder.getIntegratedSensorPosition());
  }

   /**
   * Returns the currently-estimated pose of the robot.
   *
   * @return The pose.
   */
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  private AHRS navxSetUp() {
    try {
      return new AHRS(SPI.Port.kMXP);
    } catch (final RuntimeException ex) {
      DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
      return null;
    }
  }

  // units/100ms to degrees/sec; for wheel speed
  final double speedConversion = (.1)*(360)/(2048*9.5);

  /**
   * Returns the current wheel speeds of the robot.
   *
   * @return The current wheel speeds.
   */
  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(m_leftEncoder.getIntegratedSensorVelocity()*speedConversion,
        m_rightEncoder.getIntegratedSensorVelocity()*speedConversion);
  }

  /**
   * Resets the odometry to the specified pose.
   *
   * @param pose The pose to which to set the odometry.
   */
  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    m_odometry.resetPosition(pose, Rotation2d.fromDegrees(getHeading()));
  }

  /**
   * Drives the robot using arcade controls.
   *
   * @param fwd the commanded forward movement
   * @param rot the commanded rotation
   */
  public void arcadeDrive(double fwd, double rot) {
    diffDrive.arcadeDrive(fwd, rot);
  }

  /**
   * Controls the left and right sides of the drive directly with voltages.
   *
   * @param leftVolts  the commanded left output
   * @param rightVolts the commanded right output
   */
  public void tankDriveVolts(double leftVolts, double rightVolts) {
    Robot.drivetrain._leftMaster.setVoltage(leftVolts/12);
    Robot.drivetrain._rightMaster.setVoltage(rightVolts/12);

    System.out.println("L: " + leftVolts);
    System.out.println("R: " + rightVolts);
    
  }

  /**
   * Resets the drive encoders to currently read a position of 0.
   */
  public void resetEncoders() {
    m_leftEncoder.setIntegratedSensorPosition(0, PIDConstants.kTimeoutMs);
	m_rightEncoder.setIntegratedSensorPosition(0, PIDConstants.kTimeoutMs);
	System.out.println("All sensors are zeroed.\n");
  }

  final double inchesToMetersFactor = 2.54/100;
  final double unitToInchFactor = (6 * Math.PI) / (2048*9.5);
  final double unitToMeterFactor = unitToInchFactor*inchesToMetersFactor;

  /**
   * Gets the average distance of the two encoders.
   *
   * @return the average of the two encoder readings
   */
  public double getAverageEncoderDistance() {
    return (m_leftEncoder.getIntegratedSensorPosition()*unitToMeterFactor + m_rightEncoder.getIntegratedSensorPosition()*unitToMeterFactor) / 2.0;
  }

  /**
   * Gets the left drive encoder.
   *
   * @return the left drive encoder
   */
  public TalonFXSensorCollection getLeftEncoder() {
    return m_leftEncoder;
  }

  /**
   * Gets the right drive encoder.
   *
   * @return the right drive encoder
   */
  public TalonFXSensorCollection getRightEncoder() {
    return m_rightEncoder;
  }

  /**
   * Sets the max output of the drive.  Useful for scaling the drive to drive more slowly.
   *
   * @param maxOutput the maximum output to which the drive will be constrained
   */
  public void setMaxOutput(double maxOutput) {
    diffDrive.setMaxOutput(maxOutput);
  }

  /**
   * Zeroes the heading of the robot.
   */
  public void zeroHeading() {
    navx.reset();
  }

  /**
   * Returns the heading of the robot.
   *
   * @return the robot's heading in degrees, from -180 to 180
   */
  public double getHeading() {
    return Math.IEEEremainder(navx.getAngle(), 360) * (PathfinderConstants.kGyroReversed ? -1.0 : 1.0);
  }

  /**
   * Returns the turn rate of the robot.
   *
   * @return The turn rate of the robot, in degrees per second
   */
  public double getTurnRate() {
    return navx.getRate();// * (PathfinderConstants.kGyroReversed ? -1.0 : 1.0);
  }
}
