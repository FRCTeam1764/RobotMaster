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
public class IntakeBallCommand extends ParallelCommandGroup {
  /** Creates a new IntakeBallCommand. */

  public IntakeBallCommand(
    Intake intake, double intakeSpeed,
    Conveyor conveyor, double conveyorSpeed,
    Elevator elevator, double elevatorSpeed,
    IntakeState intakeState, boolean override
  ) {
    //DigitalInput breakBeamElevator = new DigitalInput(RobotConstants.ELEVATOR_BREAK_BEAM);
    //DigitalInput breakBeamConveyor = new DigitalInput(RobotConstants.CONVEYOR_BREAK_BEAM);
    clearGroupedCommands();
    // if(!breakBeamElevator.get() && breakBeamConveyor.get()) { //some break beam stuff
    //   intakeState.resetCount();
      
    //   addCommands(
    //   new IntakeCommand(intake, intakeSpeed),
    //   new ConveyorCommand(conveyor, conveyorSpeed)
    //   );
    //   }
    // else if(!breakBeamElevator.get() && !breakBeamConveyor.get() && intakeState.getCount() < 25){ //more break beam stuff
    //   intakeState.incrementCount();
    //   addCommands(
    //   new IntakeCommand(intake, intakeSpeed),
    //   new ConveyorCommand(conveyor, conveyorSpeed)
    //   );
    // }
    // else if(breakBeamElevator.get()){
    //   intakeState.resetCount();
      addCommands(
      new IntakeCommand(intake, intakeSpeed, override),
      new ConveyorCommand(conveyor, conveyorSpeed, override),
      new ElevatorCommand(elevator, elevatorSpeed, override)
      );
      
 //   }
  }
}
// if(!breakBeamElevator.get() && breakBeamConveyor.get()) { //some break beam stuff
//     count=0;
//     System.out.println("elevator off");
//     return new ParallelCommandGroup(
//         new IntakeCommand(intake, intakeSpeed),
//         new ConveyorCommand(conveyor, conveyorSpeed)
//     );
// }
// else if(!breakBeamElevator.get() && !breakBeamConveyor.get() && count < 25){ //more break beam stuff
//     count++;
//     System.out.println("timer");
//     return new ParallelCommandGroup(
//         new ConveyorCommand(conveyor, conveyorSpeed)
//     );
// }
// else if(breakBeamElevator.get()){
//     count=0;
//     System.out.println("default");
//     return new ParallelCommandGroup(
//         new IntakeCommand(intake, intakeSpeed),
//         new ConveyorCommand(conveyor, conveyorSpeed),
//         new ElevatorCommand(elevator, elevatorSpeed)
//     );
// }
// else {
//     System.out.println("1");
//     return new ParallelCommandGroup(
//         new IntakeCommand(intake, 0)
//     );
// }