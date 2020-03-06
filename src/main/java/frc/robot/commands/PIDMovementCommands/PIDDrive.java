/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands.PIDMovementCommands;

import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.Subsystems.AutoSubsystems.PIDMovement;

public class PIDDrive extends CommandBase {
  /**
   * Creates a new PIDDrive.
   */
  public enum PIDDriveControlType{
    STRAIGHT, TURN
  }

  double units;
  double target_adjust;
  PIDDriveControlType controlType;
  PIDMovement pidMovement = new PIDMovement();
  WPI_TalonFX rightMaster = Robot.drivetrain.rightTalons[0];
  WPI_TalonFX leftMaster = Robot.drivetrain.leftTalons[0];


  public PIDDrive(double units, PIDDriveControlType controlType) {
    this.units = units;
    this.controlType = controlType;

    addRequirements(Robot.drivetrain, pidMovement);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(controlType == PIDDriveControlType.STRAIGHT){
      Robot.drivetrain.setDrivetrainInverted(false, true);
      pidMovement.selectDistancePIDSlots(rightMaster);
      target_adjust = rightMaster.getSelectedSensorVelocity(1);
    }
    else if(controlType == PIDDriveControlType.TURN){
      Robot.drivetrain.setDrivetrainInverted(false, false);
      pidMovement.selectDistancePIDSlots(rightMaster);
      target_adjust = rightMaster.getSelectedSensorVelocity(1);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(controlType == PIDDriveControlType.STRAIGHT || controlType == PIDDriveControlType.TURN){
      rightMaster.set(TalonFXControlMode.MotionMagic, units, DemandType.AuxPID, target_adjust);
      leftMaster.follow(rightMaster, FollowerType.AuxOutput1);
    }
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
