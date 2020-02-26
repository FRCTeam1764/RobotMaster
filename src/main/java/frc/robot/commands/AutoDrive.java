/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FollowerType;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.Subsystems.AutoSubsystems.PIDDrivetrainControl;
import frc.robot.Subsystems.AutoSubsystems.PIDDrivetrainControl.DrivetrainSide;

public class AutoDrive extends CommandBase {
  /**
   * Creates a new AutoDrive.
   */
  PIDDrivetrainControl pidDrivetrainControl;
  double distance;
  double angle;

  public AutoDrive(double pidDistance, double pidAngle) {
    pidDrivetrainControl = new PIDDrivetrainControl();
    addRequirements(Robot.drivetrain);

    for(int i=0; i<Robot.drivetrain.leftTalons.length; i++){
      if(i==0){
        Robot.drivetrain.leftTalons[i] = pidDrivetrainControl.setDrivetraingPIDConfig(Robot.drivetrain.leftTalons[i], DrivetrainSide.LEFT, true, Robot.drivetrain.leftTalons[i]);
      }
      else{
        Robot.drivetrain.leftTalons[i] = pidDrivetrainControl.setDrivetraingPIDConfig(Robot.drivetrain.leftTalons[i], DrivetrainSide.LEFT, false, Robot.drivetrain.leftTalons[i]);
      }
    }

    for(int i=0; i<Robot.drivetrain.rightTalons.length; i++){
      if(i==0){
        Robot.drivetrain.rightTalons[i] = pidDrivetrainControl.setDrivetraingPIDConfig(Robot.drivetrain.leftTalons[i], DrivetrainSide.RIGHT, true, Robot.drivetrain.leftTalons[i]);
      }
      else{
        Robot.drivetrain.rightTalons[i] = pidDrivetrainControl.setDrivetraingPIDConfig(Robot.drivetrain.leftTalons[i], DrivetrainSide.RIGHT, false, Robot.drivetrain.leftTalons[i]);
      }
    }
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    /* Configured for Position Closed loop on Quad Encoders' Sum and Auxiliary PID on Quad Encoders' Difference */
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Robot.drivetrain.leftTalons[0].set(ControlMode.Position, 6000, DemandType.AuxPID, 0);
    Robot.drivetrain.rightTalons[0].follow(Robot.drivetrain.rightTalons[0], FollowerType.AuxOutput1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
