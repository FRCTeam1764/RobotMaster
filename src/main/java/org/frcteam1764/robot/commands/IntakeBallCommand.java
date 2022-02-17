// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.commands;

import org.frcteam1764.robot.subsystems.Conveyor;
import org.frcteam1764.robot.subsystems.Elevator;
import org.frcteam1764.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class IntakeBallCommand extends ParallelCommandGroup {
  /** Creates a new IntakeBallCommand. */
  public IntakeBallCommand(
    Intake intake, double intakeSpeed,
    Conveyor conveyor, double conveyorSpeed,
    Elevator elevator, double elevatorSpeed
  ) {
    DigitalInput breakBeamElevator = new DigitalInput(69);
    DigitalInput breakBeamConveyor = new DigitalInput(420);
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    if(!breakBeamElevator.get()) { //some break beam stuff
      addCommands(
      new IntakeCommand(intake, intakeSpeed),
      new ConveyorCommand(conveyor, conveyorSpeed),
      new ElevatorCommand(elevator, elevatorSpeed)
      );
      }
    else if(!breakBeamConveyor.get()){ //more break beam stuff
      addCommands(
      new IntakeCommand(intake, intakeSpeed),
      new ConveyorCommand(conveyor, conveyorSpeed)
      );
    }
  }
}
