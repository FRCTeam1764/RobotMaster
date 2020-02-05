/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands.LimelightMovement;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class LimelightTurn extends Command {

  AHRS navx;
  
  public LimelightTurn() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.drivetrain);
    requires(Robot.limelight);

    try {
      navx = new AHRS(SPI.Port.kMXP);
    } catch (final RuntimeException ex) {
      DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
    }
  }

  final double kP = 0.11 / 29.8; // (kP/MaxDegreesOutputted)
  final double constantSpeed = 0.03; // Constant added during turning
  final double deadband = 0.45;
  double turningSpeed = 0;
  double xDegrees = 0;
  double navxDegrees = 0;

  boolean firstCallGetAngle = true;
  boolean secondCallGetAngle = false;
  boolean isAtTarget = false;

  double intAngle;

  TalonFX _rightMaster = Robot.drivetrain._rightMaster;
  TalonFX _leftMaster = Robot.drivetrain._leftMaster;

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    getXDeg();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    turnRobotToTarget();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    if(isAtTarget){
      return true;
    }
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.lldrive.start();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }

  public void getXDeg() {
    // Aligns robot to target; turning the robot

    // Gets angle from limelight and relates the angle to the navx

    if (firstCallGetAngle) {
      navx.zeroYaw();

      try {
        xDegrees = Robot.limelight.getAngle();
      } catch (final InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      navx.setAngleAdjustment(xDegrees);
      DriverStation.reportError("Angle = " + navx.getAngle() + ", xDegrees = " + xDegrees, true);

      navxDegrees = navx.getAngle();
      firstCallGetAngle = false;

      DriverStation.reportWarning("started", true);

      final double intSpeed = kP * navxDegrees + (navxDegrees > 0 ? constantSpeed : -constantSpeed);
      intAngle = navxDegrees;

      SmartDashboard.putNumber("intSpeed", intSpeed);
      SmartDashboard.putNumber("intAngle", intAngle);

      navx.zeroYaw();

      errorDegrees = navxDegrees;
    }
  }

  double errorDegrees;

  public void turnRobotToTarget() {

      turningSpeed = kP * errorDegrees + (errorDegrees > 0 ? constantSpeed : -constantSpeed);

      System.out.println(errorDegrees + "," + turningSpeed);

      errorDegrees = intAngle - navx.getYaw();
      _leftMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, +turningSpeed);
      _rightMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, -turningSpeed);

      if(errorDegrees < deadband && errorDegrees > -deadband){

        if(firstCallGetAngle){
          firstCallGetAngle = false; //Only to reset getXDeg()
          getXDeg();

          secondCallGetAngle = true;
        }
        else if(secondCallGetAngle){
        DriverStation.reportError("ended llturn", true);

        isFinished();
        }
      }
    }
}
