// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.commands;

import org.frcteam1764.robot.state.ClimberState;
import org.frcteam1764.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class GoUpCommand extends CommandBase {
  /** Creates a new ConveyorCommand. */
 private Climber climber;
 private ClimberState climberState;
 private boolean isPistonsDeployed;
 private int position;
  public GoUpCommand(Climber climber, ClimberState climberState, boolean isPistonsDeployed, int position) {
    this.climber = climber;
    this.climberState = climberState;
    this.isPistonsDeployed = isPistonsDeployed;
    this.position = position;
    addRequirements(climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    climber.setPosition(position);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(climberState.isClimberPistonsDeployed() == isPistonsDeployed){
      climber.climb();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climber.climberOff();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    int tolerance = 500;
    return (climber.getPosition() > (position - tolerance) && climber.getPosition() < (position + tolerance))
           || climberState.isClimberPistonsDeployed() == !isPistonsDeployed;
  }
}
