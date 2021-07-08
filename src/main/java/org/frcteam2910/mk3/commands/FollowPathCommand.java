package org.frcteam2910.mk3.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frcteam2910.mk3.subsystems.DrivetrainSubsystem;
import org.frcteam2910.common.control.MaxVelocityConstraint;
import org.frcteam2910.common.control.MaxAccelerationConstraint;
import org.frcteam2910.common.control.Path;
import org.frcteam2910.common.control.SplinePathBuilder;
import org.frcteam2910.common.control.SimplePathBuilder;
import org.frcteam2910.common.control.Trajectory;
import org.frcteam2910.common.control.TrajectoryConstraint;
import org.frcteam2910.common.math.Rotation2;
import org.frcteam2910.common.math.Vector2;
import org.frcteam2910.common.io.PathReader;

import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;
import java.io.*;

// It is probably preferred that paths are generated using Pathviewer
// Formula for creating a path in code is as follows:
// -new SimplePathBuilder().lineTo().build() or
// -new SplinePathBuilder().hemite().build()
// lineTo() and hermite() can be chained for additional segments
// vectors for some reason read (y, x) instead of (x, y)

public class FollowPathCommand extends CommandBase {
  private DrivetrainSubsystem drivetrainSubsystem;
  private Trajectory trajectory;
  private Path path;
  
  public FollowPathCommand(DrivetrainSubsystem drivetrain, Path newPath) {
    drivetrainSubsystem = drivetrain;
    path = newPath;

    addRequirements(drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrainSubsystem.resetWheelAngles();

      // Specify some constraints for our trajectory.
      TrajectoryConstraint[] constraints = {
              // Both of these constraints are in seemingly arbitrary units
              new MaxAccelerationConstraint(30.0),
              new MaxVelocityConstraint(60.0)
      };
      
      // With a path and our constraints we can create a trajectory.
      // To create a trajectory, pass in a path and the constraints set above.
      // A third parameter is required called sample distance. This sample distance
      // determines how often the trajectory makes sure that the velocity and acceleration are within
      // the limits determined by the constraints. Smaller values will create a smoother and more accurate path
      // but they will take much longer to generate.
    
      trajectory = new Trajectory(this.path, constraints, 1e-2); //todo: this path generation needs to be done on robot boot, before auto is enabled.
    if (trajectory != null) {
      drivetrainSubsystem.getFollower().follow(trajectory);
    }
  }

  @Override
  public void end(boolean interrupted) {
    drivetrainSubsystem.drive(Vector2.ZERO, 0, false);
  }

  @Override
  public boolean isFinished() {
      // Only finish when the trajectory is completed
      return drivetrainSubsystem.getFollower().getCurrentTrajectory().isEmpty();
  }
}
