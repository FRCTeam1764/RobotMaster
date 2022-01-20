// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.commands;

import org.frcteam1764.robot.subsystems.Conveyor;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ConveyorCommand extends CommandBase {
  /** Creates a new ConveyorCommand. */
 Conveyor conveyor;

  public ConveyorCommand(double conveyorSpeed) {
    conveyor = new Conveyor(conveyorSpeed);

    //addRequirements(conveyor);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    conveyor.conveyorOn();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    conveyor.conveyerOff();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
