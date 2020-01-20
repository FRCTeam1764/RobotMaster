/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Robot;
import frc.robot.Subsystems.Drivetrain;

public class PIDControls extends Command{

AHRS navx = new AHRS();

  Drivetrain diffDrive;
  PIDController turnController;

  static final double kP = 0.004;
  static final double kI = 0.002;
  static final double kD = 0;
  static final double kF = 0;

  static final double kToleranceDegrees = 2;
  static final double kTargetAngleDegrees = 135;

  public PIDControls() {
    // Use requires() here to declare subsystem dependencies
   requires(Robot.drivetrain);

   try {
    navx = new AHRS(SPI.Port.kMXP); 
} catch (RuntimeException ex ) {
    DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
}

   diffDrive = Robot.drivetrain;
    navx.zeroYaw();

  //  turnController = new PIDController(kP, kI, kD, kF, navx, this);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  double turningSpeed = 0;
  double yawDegrees = navx.getYaw();
  double speedSum = 0;

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(yawDegrees> kTargetAngleDegrees + kToleranceDegrees || yawDegrees< kTargetAngleDegrees - kToleranceDegrees){
      turningSpeed = kP * yawDegrees + kI * speedSum;
      DriverStation.reportError("Im right here " + navx.getYaw() + " " + turningSpeed, true);
    }

    //diffDrive._diffD.arcadeDrive(0, turningSpeed);
    yawDegrees = navx.getYaw();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
