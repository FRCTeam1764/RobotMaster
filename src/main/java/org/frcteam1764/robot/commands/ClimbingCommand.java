// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.frcteam1764.robot.subsystems.Climber;


public class ClimbingCommand extends SequentialCommandGroup {
  /** Creates a new ClimbingCommand. */
  public ClimbingCommand(Climber climber) {
    clearGroupedCommands();
    addCommands(
      new PullDownCommand(climber, .75),
      new GoUpCommand(climber, 1000),
      new ClimberPneumaticsCommand(climber),
      new GoUpCommand(climber, 2000),
      new ClimberPneumaticsCommand(climber),
      new PullDownCommand(climber, .75),
      new GoUpCommand(climber, 1000),
      new ClimberPneumaticsCommand(climber),
      new GoUpCommand(climber, 2000),
      new ClimberPneumaticsCommand(climber),
      new PullDownCommand(climber, .75)
    );

    //addRequirements(conveyor);
  }

}

