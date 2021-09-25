package org.frcteam1764.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frcteam1764.robot.state.DrivetrainState;
import org.frcteam1764.robot.subsystems.SwerveDrivetrain;
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

public class AutonomousDrivePath extends CommandBase {
  private SwerveDrivetrain drivetrainSubsystem;
  private DrivetrainState drivetrainState;
  private Trajectory trajectory;
  
  public AutonomousDrivePath(SwerveDrivetrain drivetrain, DrivetrainState drivetrainState) {
    drivetrainSubsystem = drivetrain;
    this.drivetrainState = drivetrainState;

    addRequirements(drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrainState.resetGyroAngle(Rotation2.ZERO);
    drivetrainSubsystem.resetWheelAngles();
    //try {
      /*Reader reader = new FileReader("TestPath.path");
      PathReader pathReader = new PathReader(reader);
      Path path = pathReader.read();
      pathReader.close();*/
      Path path = new SimplePathBuilder(Vector2.ZERO, Rotation2.ZERO)
                // When using hermite splines we must specify a position and a heading. We can also optionally specify
                // a rotation.
                .lineTo(new Vector2(200.0, 0.0), Rotation2.fromRadians(90))
                .lineTo(new Vector2(200.0, -200.0), Rotation2.fromRadians(180))
                .lineTo(new Vector2(0.0, -200.0), Rotation2.fromRadians(-90))
                .lineTo(Vector2.ZERO, Rotation2.ZERO)
                // Once we've added all the splines we can then build the path.
                .build();

      //Do this one pathat a time
      //Path path = new SplinePathBuilder(Vector2.ZERO, Rotation2.ZERO, Rotation2.ZERO)
      //.hermite(new Vector2(100.0, -100.0), Rotation2.fromDegrees(-90.0), Rotation2.ZERO)
      //.hermite(new Vector2(0.0, -200.0), Rotation2.fromDegrees(-90.0), Rotation2.ZERO)
      //.hermite(new Vector2(-100.0, -100.0), Rotation2.fromDegrees(-90.0), Rotation2.ZERO)
      //.hermite(new Vector2(0.0, 0.0), Rotation2.fromDegrees(-90.0), Rotation2.ZERO)
      //-90 to right
      //=90 to the left
      //180 semi circle
      //.build();

      // Once we have our path we need to then specify some constraints for our trajectory.
      TrajectoryConstraint[] constraints = {
              // Lets specify a maximum acceleration of 10.0 units/s^2
              new MaxAccelerationConstraint(30.0), //30 spins out without carpet check voltage ramp on talon config
              // And lets have a maximum velocity of 12.0 units/s
              new MaxVelocityConstraint(60.0) //75 seems like max speed but slides
      };
      
      // Now that we have both our path and our constraints we can create a trajectory.
      // When creating a trajectory we pass in our path and our constraints.
      // We also have to pass in a third parameter called sample distance. This sample distance
      // determines how often the trajectory makes sure that the velocity and acceleration are within
      // the limits determined by the constraints. Smaller values will create a smoother and more accurate path
      // but they will take much longer to generate.
    
      trajectory = new Trajectory(path, constraints, 1e-2);
    /*}
    catch(IOException e) {
      e.printStackTrace();
    }*/
    if (trajectory != null) {
      //System.out.print("Generated trajectories");
      //drivetrainSubsystem.resetGyroAngle(Rotation2.ZERO);
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
