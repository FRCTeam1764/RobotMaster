/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.LimeLightValues;
import frc.robot.Robot;

public class LimelightDrive extends Command {
  public LimelightDrive() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.drivetrain);
    requires(Robot.limelight);
  }

 LimeLightValues values = new LimeLightValues();

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  
  }

  private final double speedCoef = 0.1;
  private double turningSpeed;
  
  @Override
  protected void execute() {
    // Aligns robot to target, turning it
    while(values.xDeg>1.5 || values.xDeg<-1.5){
      turningSpeed = speedCoef * values.xDeg;
      Robot.drivetrain._diffD.arcadeDrive(0, turningSpeed);
    }

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
    //Robot.drivetrain.drive(0,0);
  }
}
