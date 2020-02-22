/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands.PIDMovementCommands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.Commands.ShooterCommand.ShooterControlMode;
import frc.robot.Subsystems.TeleopSubsystems.Shooter;
import frc.robot.constants.PIDConstants;
import frc.robot.util.Limelight;

public class PIDDrive extends CommandBase {
  /**
   * Creates a new PIDDrive.
   */

  WPI_TalonFX leftMaster = Robot.drivetrain.leftTalons[0];
  WPI_TalonFX rightMaster = Robot.drivetrain.rightTalons[0];
  // Shooter motor is within the shooter object

  Shooter shooter;

  public enum MovementType {
    STRAIGHT, TURN, SHOOT, LIMELIGHT_TURN
  }

  private double units=0;
  private MovementType type;
  private double timeDuration = 0;

  /**
   * Used to move a part of the robot using PID loops. Currently used for moving
   * forward, turning, and for the shooter.
   *
   * @param inputUnits   Units given to motors. For moving forward: Inches (to
   *                     travel), for turning (regular or with limelight): Degrees
   *                     (to rotate), for the shooter: Inches/Second (the target
   *                     velocity of the shooter)
   * @param movementType Specified type of movement needed (ie.
   *                     MovementType.STRAIGHT)
   */
  public PIDDrive(double inputUnits, MovementType movementType) {
    addRequirements(Robot.pidMovement);
    addRequirements(Robot.drivetrain);

    Robot.pidMovement.setPIDConfig(leftMaster, false);
    Robot.pidMovement.setPIDConfig(rightMaster, false);
    // Shooter Master is already config in the shooter subsystem

    type = movementType;

    if (movementType == MovementType.STRAIGHT) {
      units = inputUnits * PIDConstants.CLICKS_PER_INCH;
      Robot.pidMovement.setDistancePIDConfig(rightMaster);
      Robot.pidMovement.setDistancePIDConfig(leftMaster);
    }

    else if (movementType == MovementType.TURN) {
      units = inputUnits * PIDConstants.CLICKS_PER_DEGREES;
      Robot.pidMovement.setTurningPIDConfig(rightMaster);
      Robot.pidMovement.setTurningPIDConfig(leftMaster);
    } else if (movementType == MovementType.SHOOT) {
      shooter = new Shooter(inputUnits * PIDConstants.TALON_VELOCITY_PER_ROBOT_VELOCITY, ShooterControlMode.PID);
    } else if (movementType == MovementType.LIMELIGHT_TURN) {
      try {
        units = Limelight.getAngle();
      } catch (InterruptedException e) {
        units = 0;
        e.printStackTrace();
      }
    }
  }

  /**
   * Used to move a part of the robot using PID loops.
   * Currently used for moving forward, turning, and for the shooter.
   *
   * @param inputUnits Units given to motors. 
   *                   For moving forward: Inches (to travel), for turning: Degrees (to rotate), 
   *                   for the shooter: Inches/Second (the target velocity of the shooter)
   * @param movementType Specified type of movement needed (ie. MovementType.STRAIGHT)
   * @param inputTimeDuration Time duration of movement, in seconds. Default is zero seconds.
   *                          Is only needed for shooter
   */
  public PIDDrive(double inputUnits, MovementType movementType, double inputTimeDuration) {
    addRequirements(Robot.pidMovement);
    addRequirements(Robot.drivetrain);

    type = movementType;
    timeDuration = inputTimeDuration;

    if(movementType == MovementType.STRAIGHT){
      units = inputUnits * PIDConstants.CLICKS_PER_INCH;
      Robot.pidMovement.setDistancePIDConfig(rightMaster);
      Robot.pidMovement.setDistancePIDConfig(leftMaster);
    }

    else if (movementType == MovementType.TURN){
      units = inputUnits * PIDConstants.CLICKS_PER_DEGREES;
      Robot.pidMovement.setTurningPIDConfig(rightMaster);
      Robot.pidMovement.setTurningPIDConfig(leftMaster);
    }
    else if(movementType == MovementType.SHOOT){
      shooter = new Shooter(inputUnits * PIDConstants.TALON_VELOCITY_PER_ROBOT_VELOCITY, ShooterControlMode.PID);
    }
    else if (movementType == MovementType.LIMELIGHT_TURN) {
      try {
        units = inputUnits * Limelight.getAngle();
      } catch (InterruptedException e) {
        units = 0;
        e.printStackTrace();
      }
    }
  }

  /**
   * Used to move a part of the robot using PID loops. Currently used for moving
   * forward, turning, and for the shooter. This constructor should only be used only for
   * movements that don't need the programmer to input units, such as the limelight drive.
   * The default units of movement is zero.
   *
   * @param movementType Specified type of movement needed (ie.
   *                     MovementType.STRAIGHT)
   */

  public PIDDrive(MovementType movementType) {
    addRequirements(Robot.pidMovement);
    addRequirements(Robot.drivetrain);

    TalonFXConfiguration configs = new TalonFXConfiguration();
    configs.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
    rightMaster.configAllSettings(configs);
    rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20);
    leftMaster.configAllSettings(configs);
    leftMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20);

    //Robot.pidMovement.setPIDConfig(leftMaster, false);
    //Robot.pidMovement.setPIDConfig(rightMaster, false);
    // Shooter Master is already config in the shooter subsystem

    type = movementType;

    if (movementType == MovementType.STRAIGHT) {
      //Robot.pidMovement.setDistancePIDConfig(rightMaster);
      //Robot.pidMovement.setDistancePIDConfig(leftMaster);
    }
    else if (movementType == MovementType.TURN) {
      Robot.pidMovement.setTurningPIDConfig(rightMaster);
      Robot.pidMovement.setTurningPIDConfig(leftMaster);
    } 
    else if (movementType == MovementType.SHOOT) {
      shooter = new Shooter(0, ShooterControlMode.STANDARD);
    } 
    else if (movementType == MovementType.LIMELIGHT_TURN) {
      try {
        units = Limelight.getAngle();
      } catch (InterruptedException e) {
        units = 0;
        e.printStackTrace();
      }
    }
  }
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(type == MovementType.STRAIGHT){
      rightMaster.set(ControlMode.Position, units);
      leftMaster.set(ControlMode.Position, units);
    }
    else if(type == MovementType.TURN || type == MovementType.LIMELIGHT_TURN){
      rightMaster.set(ControlMode.Position, -units);
      leftMaster.set(ControlMode.Position, units);
    }
    else if(type == MovementType.SHOOT){
      if(timeDuration>0){
        shooter.shoot(timeDuration);
        end(false);
      }
      else{
        shooter.shoot();
      }
    }
    
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stopShooter();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
     return (rightMaster.getClosedLoopError()<PIDConstants.TICKS_ERROR 
            && leftMaster.getClosedLoopError()<PIDConstants.TICKS_ERROR);

  }
}
