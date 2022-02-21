// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.commands;

import org.frcteam1764.robot.constants.RobotConstants;
import org.frcteam1764.robot.state.IntakeState;
import org.frcteam1764.robot.subsystems.Conveyor;
import org.frcteam1764.robot.subsystems.Elevator;
import org.frcteam1764.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class FeederCommand extends ParallelCommandGroup {
  /** Creates a new IntakeBallCommand. */

  public FeederCommand(
    Conveyor conveyor, double conveyorSpeed,
    Elevator elevator, double elevatorSpeed
  ) {
    //DigitalInput breakBeamElevator = new DigitalInput(RobotConstants.ELEVATOR_BREAK_BEAM);
    //DigitalInput breakBeamConveyor = new DigitalInput(RobotConstants.CONVEYOR_BREAK_BEAM);
    clearGroupedCommands();
    addCommands(
      new ConveyorCommand(conveyor, conveyorSpeed, true),
      new ElevatorCommand(elevator, elevatorSpeed, true)
    );
  }
}