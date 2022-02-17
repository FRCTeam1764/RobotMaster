// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.commands;

import org.frcteam1764.robot.subsystems.Intake;
import org.frcteam1764.robot.subsystems.SwerveDrivetrain;
import org.frcteam2910.common.control.Trajectory;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoDriveCommand extends ParallelRaceGroup {
  /** Creates a new AutoDriveCommand. */
  public AutoDriveCommand(Intake intake, double intakeSpeed, SwerveDrivetrain drivetrain, Trajectory trajectory) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new IntakeCommand(intake, intakeSpeed),new FollowPathCommand(drivetrain, trajectory));
  }
}