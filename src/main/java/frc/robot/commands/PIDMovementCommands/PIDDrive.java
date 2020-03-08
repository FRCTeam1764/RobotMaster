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
import frc.robot.constants.PIDConstants;
import frc.robot.constants.RobotDimensionConstants;

public class PIDDrive extends CommandBase {
  
  public enum PIDDriveControlType{
    STRAIGHT, TURN
  }

  double units;
  double target_adjust;
  PIDDriveControlType controlType;

  WPI_TalonFX rightMaster = Robot.drivetrain.rightTalons[0];
  WPI_TalonFX leftMaster = Robot.drivetrain.leftTalons[0];


  public PIDDrive(double units, PIDDriveControlType controlType) {
    this.units = units;
    this.controlType = controlType;

    addRequirements(Robot.drivetrain);

    System.out.println("starting motion magic");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(controlType == PIDDriveControlType.STRAIGHT){
      Robot.drivetrain.setDrivetrainInverted(false, true);
     // PIDMovement.changeAuxDiffSetting(rightMaster);
     PIDMovement.setDistancePIDConfig(leftMaster, rightMaster);
      PIDMovement.selectDistancePIDSlots(rightMaster);
      Robot.drivetrain.resetEncoders();
      target_adjust = rightMaster.getSelectedSensorPosition(1);
      units *= PIDConstants.CLICKS_PER_INCH;
    }
    else if(controlType == PIDDriveControlType.TURN){
      Robot.drivetrain.setDrivetrainInverted(false, false);
      PIDMovement.changeAuxDiffSetting(rightMaster);
      PIDMovement.selectDistancePIDSlots(rightMaster);
      Robot.drivetrain.resetEncoders();
      target_adjust = rightMaster.getSelectedSensorPosition(1);
      units *= RobotDimensionConstants.ROBOT_ROTATION_CIRCUMFERENCE/360;
      units *= PIDConstants.CLICKS_PER_INCH;
    }

    System.out.println("Ticks = " + units + ", Aux = " + target_adjust);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
 //if(controlType == PIDDriveControlType.STRAIGHT || controlType == PIDDriveControlType.TURN){
      PIDMovement.setMotionMagic(rightMaster, leftMaster, units, target_adjust);
      System.out.println("Motion Magic is Active, Error = " + rightMaster.getClosedLoopError(0) + "," + rightMaster.getClosedLoopError(1));
   // }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("motion magic ended");
    rightMaster.set(TalonFXControlMode.PercentOutput, 0);
    leftMaster.set(TalonFXControlMode.PercentOutput, 0);
    leftMaster.follow(leftMaster);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return rightMaster.getSelectedSensorPosition() > units - PIDConstants.TICKS_ERROR;
  }
}
