/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import java.util.function.*;

import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
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
public class PathFollower extends SubsystemBase {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  WPI_TalonFX leftMaster = Robot.drivetrain._leftMaster;
  WPI_TalonFX rightMaster = Robot.drivetrain._rightMaster;

  public final DifferentialDrive diffDrive = new DifferentialDrive(leftMaster, rightMaster);

  private final TalonFXSensorCollection m_leftEncoder = leftMaster.getSensorCollection();
  private final TalonFXSensorCollection m_rightEncoder = rightMaster.getSensorCollection();

  // The gyro sensor
  private AHRS navx = navxSetUp();

  // Odometry class for tracking robot pose
  private DifferentialDriveOdometry m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));;

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
