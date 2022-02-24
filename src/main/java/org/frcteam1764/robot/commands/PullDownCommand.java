// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.commands;

import org.frcteam1764.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class PullDownCommand extends CommandBase {
  /** Creates a new ConveyorCommand. */
 private Climber climber;
 private double climberSpeed;

  public PullDownCommand(Climber climber, double climberSpeed) {
    this.climber = climber;
    this.climberSpeed = climberSpeed; 
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    climber.climberOn(climberSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climber.climberOff();
    climber.resetFalcon();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(climber.checkLimitSwitches()){
      return true;
    }
    else{
      return false;
    }
  
  }
}
