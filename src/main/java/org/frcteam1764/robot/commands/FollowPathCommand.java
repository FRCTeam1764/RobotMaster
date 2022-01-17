package org.frcteam1764.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frcteam1764.robot.subsystems.SwerveDrivetrain;
import org.frcteam2910.common.control.Trajectory;
import org.frcteam2910.common.math.RigidTransform2;
import org.frcteam2910.common.math.Vector2;

/**
 * 
 * This is just a sample command that follow a swerve path
 * Could just be documented in the readme instead
 * 
 */

public class FollowPathCommand extends CommandBase {
  private SwerveDrivetrain drivetrain;
  private Trajectory trajectory;
  
  public FollowPathCommand(SwerveDrivetrain drivetrain, Trajectory trajectory) {
    this.drivetrain = drivetrain;
    this.trajectory = trajectory;

    addRequirements(drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    /**
     * can start and stop subsystems here
     */
    drivetrain.resetPose(RigidTransform2.ZERO);
    drivetrain.resetWheelAngles();
    if (trajectory != null) {
      drivetrain.getFollower().follow(trajectory);
    }
  }

  @Override
  public void end(boolean interrupted) {
    drivetrain.drive(Vector2.ZERO, 0, false);
  }

  @Override
  public boolean isFinished() {
    /**
     * can start and stop subsystems here
     */

    // Only finish when the trajectory is completed
    return drivetrain.getFollower().getCurrentTrajectory().isEmpty();
  }
}
