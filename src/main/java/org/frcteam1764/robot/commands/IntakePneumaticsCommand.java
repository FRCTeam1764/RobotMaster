// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.commands;

import org.frcteam1764.robot.state.IntakeState;
import org.frcteam1764.robot.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class IntakePneumaticsCommand extends CommandBase {
  /** Creates a new ConveyorCommand. */
 private Intake intake;
 private IntakeState intakeState;
 private boolean finishedState;

  public IntakePneumaticsCommand(Intake intake, IntakeState intakeState, boolean finishedState) {
    this.intake = intake;
    this.intakeState = intakeState;
    this.finishedState = finishedState;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(finishedState){
      intake.intakeOn(0, true);
    }
    else {
      intake.intakeOff();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (intakeState.isIntakeDeployed() && finishedState) || (!intakeState.isIntakeDeployed() && !finishedState);
  }
}
