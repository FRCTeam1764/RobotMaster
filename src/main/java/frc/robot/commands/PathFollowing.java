/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import java.util.List;
import java.util.function.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.Subsystems.PathFollower;
import frc.robot.constants.PathfinderConstants;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PathFollowing extends CommandBase {
  /**
   * Creates a new PathFollowing.
   */

  PathFollower pathfollower = new PathFollower();

  public PathFollowing() {
    // Use addRequirements() here to declare subsystem dependencies.

    addRequirements(pathfollower);
    pathfollower.resetEncoders();
  }

  DifferentialDrive diffDrive = pathfollower.diffDrive;


  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  // Create a voltage constraint to ensure we don't accelerate too fast
  DifferentialDriveVoltageConstraint autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
      new SimpleMotorFeedforward(PathfinderConstants.ksVolts, PathfinderConstants.kvVoltSecondsPerMeter,
          PathfinderConstants.kaVoltSecondsSquaredPerMeter),
      PathfinderConstants.kDriveKinematics, 10);

  // Create config for trajectory
  TrajectoryConfig config = new TrajectoryConfig(PathfinderConstants.kMaxSpeedMetersPerSecond,
      PathfinderConstants.kMaxAccelerationMetersPerSecondSquared)
          // Add kinematics to ensure max speed is actually obeyed
          .setKinematics(PathfinderConstants.kDriveKinematics)
          // Apply the voltage constraint
          .addConstraint(autoVoltageConstraint);

  // An example trajectory to follow. All units in meters.
  Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
      // Start at the origin facing the +X direction
      new Pose2d(0, 0, new Rotation2d(0)),
      // Pass through these two interior waypoints, making an 's' curve path
      List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
      // End 3 meters straight ahead of where we started, facing forward
      new Pose2d(3, 0, new Rotation2d(0)),
      // Pass config
      config);

  // Run path following command, then stop at the end.

  Supplier<DifferentialDriveWheelSpeeds> getWheelSpeed = () -> pathfollower.getWheelSpeeds();
  BiConsumer<Double, Double> setTankDriveVoltage = (leftVolts, rightVolts) -> pathfollower.tankDriveVolts(leftVolts, rightVolts);

 public Command runCommand(){

  pathfollower.resetEncoders();
  pathfollower.resetOdometry(new Pose2d());
  pathfollower.zeroHeading();


 RamseteCommand ramseteCommand = new RamseteCommand(
  exampleTrajectory,
  pathfollower::getPose,
  new RamseteController(PathfinderConstants.kRamseteB, PathfinderConstants.kRamseteZeta),
  new SimpleMotorFeedforward(PathfinderConstants.ksVolts,
                            PathfinderConstants.kvVoltSecondsPerMeter,
                            PathfinderConstants.kaVoltSecondsSquaredPerMeter),
                            PathfinderConstants.kDriveKinematics,
                            getWheelSpeed,
  new PIDController(PathfinderConstants.kPDriveVel, 0, 0),
  new PIDController(PathfinderConstants.kPDriveVel, 0, 0),
  // RamseteCommand passes volts to the callback
  setTankDriveVoltage,
  pathfollower 
  );

  return ramseteCommand;

//ramseteCommand.andThen(() -> pathfollower.tankDriveVolts(0, 0));
}
}
