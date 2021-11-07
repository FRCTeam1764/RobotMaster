package org.frcteam1764.robot.commands;

import org.frcteam1764.robot.state.RobotState;
import org.frcteam1764.robot.subsystems.RobotSubsystems;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AutoGroupCommand extends SequentialCommandGroup {
  private RobotState robotState;
  private RobotSubsystems robotSubsystems;

  public AutoGroupCommand(RobotState robotState, RobotSubsystems robotSubsystems) {
    this.robotState = robotState;
    this.robotSubsystems = robotSubsystems;
    getAutoCommand();
  }

  private void getAutoCommand() {
    // this is where the auto chooser is checked and the function
    // to build the correct auto command group is called
    runDefaultAuto();
  }

  private void runDefaultAuto() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new FollowPathCommand(this.robotSubsystems.drivetrain, robotState.trajectories[0])
    );
  }
}
