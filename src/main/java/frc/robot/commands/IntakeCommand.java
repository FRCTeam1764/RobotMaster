/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.Subsystems.TeleopSubsystems.Feeder;
import frc.robot.Subsystems.TeleopSubsystems.Intake;

public class IntakeCommand extends CommandBase {
  /**
   * Creates a new IntakeCommand.
   */

  // SharpIRSensor intakeIRSensor = new
  // SharpIRSensor(PortConstants.SHARP_IR_SENSOR_INTAKE_ANALOG_PORT);

  public Intake intake;
  public Feeder feeder;

  public double time = -1;

  public IntakeCommand(double intakeSpeed) {
    intake = new Intake(intakeSpeed);

    addRequirements(intake);
  }
  public IntakeCommand(double intakeSpeed, float timeDuration) {
    intake = new Intake(intakeSpeed);
    time = timeDuration;

    addRequirements(intake);
  }

  public IntakeCommand(double intakeSpeed, double feederSpeed) {
    intake = new Intake(intakeSpeed);
    feeder = new Feeder(0, feederSpeed);

    addRequirements(intake, feeder);
  }

  public IntakeCommand(double intakeSpeed, double conveyerSpeed, double feederSpeed) {
    intake = new Intake(intakeSpeed);
    feeder = new Feeder(conveyerSpeed, feederSpeed);

    addRequirements(intake, feeder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  boolean thereIsBall = false;

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(time>0){
      intake.timedIntake(time);
      end(false);
    }
    else if(feeder==null){
      intake.intake();
    }
    else{
      if(Robot.feederIRSensor.getVoltage() < 1.1){
        feeder.feederOn();
        feeder.conveyerOn();
        intake.intake();
        
      }
      else{
        feeder.feederStop();
        feeder.conveyerOn();
        intake.intake();
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.stopIntake();
    feeder.feederStop();
    feeder.conveyerStop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
