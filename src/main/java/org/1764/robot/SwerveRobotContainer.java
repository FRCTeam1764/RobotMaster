package org.frcteam1764.robot;

import edu.wpi.first.wpilibj2.command.*;
import org.frcteam1764.robot.commands.SwerveDriveCommand;
import org.frcteam1764.robot.subsystems.SwerveDrivetrain;
import org.frcteam2910.common.control.Trajectory;
import org.frcteam2910.common.math.Rotation2;
import org.frcteam2910.common.robot.input.Axis;
import org.frcteam2910.common.robot.input.XboxController;
import org.frcteam1764.robot.common.Utilities;
import org.frcteam1764.robot.constants.ControllerConstants;
import org.frcteam1764.robot.state.DrivetrainState;
import org.frcteam1764.robot.state.RobotState;

public class SwerveRobotContainer {
    private final XboxController primaryController = new XboxController(Constants.PRIMARY_CONTROLLER_PORT);
    private final SwerveDrivetrain drivetrainSubsystem;

    public RobotState robotState = new RobotState();

    public SwerveRobotContainer() {
        primaryController.getLeftXAxis().setInverted(true);
        primaryController.getRightXAxis().setInverted(true);
        robotState.drivetrain = new DrivetrainState(getLeftTriggerAxis(), getRightTriggerAxis());
        drivetrainSubsystem = new SwerveDrivetrain(robotState.drivetrain);

        CommandScheduler.getInstance().setDefaultCommand(drivetrainSubsystem, new SwerveDriveCommand(drivetrainSubsystem, getDriveForwardAxis(), getDriveStrafeAxis(), getDriveRotationAxis(), this.robotState));

        configurePilotButtonBindings();
        configureCoPilotButtonBindings();
        getTrajectories();
    }

    private void getTrajectories() {
        Trajectory sampleTrajectory = Utilities.convertPathToTrajectory(Utilities.getPath("TestPath.path"), 30.0, 60.0);
        robotState.trajectories = new Trajectory[]{
            sampleTrajectory
        };
    }

    private void configurePilotButtonBindings() {
        primaryController.getBackButton().whenPressed(
                () -> robotState.drivetrain.resetGyroAngle(Rotation2.ZERO)
        );
        primaryController.getStartButton().whenPressed(
                drivetrainSubsystem::resetWheelAngles
        );
        primaryController.getAButton().whenPressed(() -> robotState.drivetrain.setTargetTurningAngle(ControllerConstants.CRITICAL_ANGLE_A));
        primaryController.getBButton().whenPressed(() -> robotState.drivetrain.setTargetTurningAngle(ControllerConstants.CRITICAL_ANGLE_B));
        primaryController.getXButton().whenPressed(() -> robotState.drivetrain.setTargetTurningAngle(ControllerConstants.CRITICAL_ANGLE_X));
        primaryController.getYButton().whenPressed(() -> robotState.drivetrain.setTargetTurningAngle(ControllerConstants.CRITICAL_ANGLE_Y));
        primaryController.getLeftBumperButton().whenPressed(() -> robotState.drivetrain.setManeuver("barrelroll"));
        primaryController.getRightBumperButton().whenPressed(() -> robotState.drivetrain.setManeuver("reversebarrelroll"));
        primaryController.getRightJoystickButton().whenPressed(() -> robotState.drivetrain.setManeuver("spin"));
    }

    private Axis getDriveForwardAxis() {
        return primaryController.getLeftYAxis();
    }

    private Axis getDriveStrafeAxis() {
        return primaryController.getLeftXAxis();
    }

    private Axis getDriveRotationAxis() {
        return primaryController.getRightXAxis();
    }

    private Axis getLeftTriggerAxis() {
        return primaryController.getLeftTriggerAxis();
    }

    private Axis getRightTriggerAxis() {
        return primaryController.getRightTriggerAxis();
    }

    private void configureCoPilotButtonBindings() {
    }

    /**
     * Get copilot axis inputs here
     */

    public SwerveDrivetrain getDrivetrainSubsystem() {
        return drivetrainSubsystem;
    }

    public XboxController getPrimaryController() {
        return primaryController;
    }
}
