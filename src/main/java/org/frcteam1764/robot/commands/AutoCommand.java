// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.commands;


import org.frcteam1764.robot.subsystems.Conveyor;
import org.frcteam1764.robot.subsystems.Elevator;
import org.frcteam1764.robot.subsystems.Intake;
import org.frcteam1764.robot.subsystems.SwerveDrivetrain;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoCommand extends SequentialCommandGroup {
  /** Creates a new AutoCommand. */
  public AutoCommand(
  double shooterSpeed,
  Intake intake, double intakeSpeed, 
  Conveyor conveyor, double conveyorSpeed,
  Elevator elevator, double elevatorSpeed,
  SwerveDrivetrain drivetrain
 // Trajectory trajectory1, Trajectory trajectory2
  ) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
     new ShooterCommand(shooterSpeed), //needs proper shooter workflow
  //   new AutoDriveCommand(intake, intakeSpeed, conveyor, conveyorSpeed, elevator, elevatorSpeed, drivetrain, trajectory1),
     new ShooterCommand(shooterSpeed),
    // new AutoDriveCommand(intake, intakeSpeed, conveyor, conveyorSpeed, elevator, elevatorSpeed, drivetrain, trajectory2),
     new ShooterCommand(shooterSpeed)
     );
  }
}
