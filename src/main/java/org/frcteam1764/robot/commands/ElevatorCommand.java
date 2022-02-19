// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.commands;

import org.frcteam1764.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ElevatorCommand extends CommandBase {
  /** Creates a new ElevatorCommand. */
  private Elevator elevator;
  private double elevatorSpeed;
  private boolean override;

  public ElevatorCommand(Elevator elevator, double elevatorSpeed, boolean override) {
    this.elevator = elevator;
    this.elevatorSpeed = elevatorSpeed;
    this.override = override;

    //addRequirements(conveyor);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    elevator.elevatorOn(elevatorSpeed, override);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    elevator.elevatorOff();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

