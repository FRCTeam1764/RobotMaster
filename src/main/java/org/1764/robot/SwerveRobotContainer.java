package org.frcteam1764.robot;

import edu.wpi.first.wpilibj2.command.*;
import org.frcteam1764.robot.commands.SampleFollowPathCommand;
import org.frcteam1764.robot.commands.SwerveDriveCommand;
import org.frcteam1764.robot.subsystems.RobotSubsystems;
import org.frcteam1764.robot.subsystems.SwerveDrivetrain;
import org.frcteam2910.common.control.Path;
import org.frcteam2910.common.control.SimplePathBuilder;
import org.frcteam2910.common.control.Trajectory;
import org.frcteam2910.common.math.Rotation2;
import org.frcteam2910.common.math.Vector2;
import org.frcteam2910.common.robot.input.Axis;
import org.frcteam2910.common.robot.input.DPadButton;
import org.frcteam2910.common.robot.input.XboxController;
import org.frcteam1764.robot.common.Utilities;
import org.frcteam1764.robot.constants.ControllerConstants;
import org.frcteam1764.robot.state.DrivetrainState;
import org.frcteam1764.robot.state.RobotState;

public class SwerveRobotContainer {
    private final XboxController primaryController = new XboxController(ControllerConstants.PRIMARY_CONTROLLER_PORT);
    private final XboxController secondaryController = new XboxController(ControllerConstants.SECONDARY_CONTROLLER_PORT);
    private RobotState robotState = new RobotState();
    private RobotSubsystems robotSubsystems = new RobotSubsystems();

    public SwerveRobotContainer() {
        initRobotState();
        initRobotSubsystems();
        configureSmartDashboard();
        getTrajectories();
        configurePilotButtonBindings();
        configureCoPilotButtonBindings();

        CommandScheduler.getInstance().setDefaultCommand(robotSubsystems.drivetrain, new SwerveDriveCommand(robotSubsystems.drivetrain, getPilotDriveForwardAxis(), getPilotDriveStrafeAxis(), getPilotDriveRotationAxis(), this.robotState));
    }

    private void initRobotState() {
        robotState.drivetrain = new DrivetrainState(getPilotLeftTriggerAxis(), getPilotRightTriggerAxis());
    }
    private void initRobotSubsystems() {
        robotSubsystems.drivetrain = new SwerveDrivetrain(robotState.drivetrain);

    }

    private void configureSmartDashboard() {

    }

    private void getTrajectories() {
        Path samplePath = new SimplePathBuilder(Vector2.ZERO, Rotation2.ZERO)
            .lineTo(new Vector2(100.0, 0.0))
            .lineTo(new Vector2(100.0, -100.0))
            .lineTo(new Vector2(0.0, -100.0))
            .lineTo(Vector2.ZERO)
            .build();
        robotState.trajectories = new Trajectory[]{
            Utilities.convertPathToTrajectory(samplePath, 30.0, 60.0)
        };
    }

    private void configurePilotButtonBindings() {
        primaryController.getLeftXAxis().setInverted(true);
        primaryController.getRightXAxis().setInverted(true);
        primaryController.getBackButton().whenPressed(
                () -> robotState.drivetrain.resetGyroAngle(Rotation2.ZERO)
        );
        primaryController.getStartButton().whenPressed(
                robotSubsystems.drivetrain::resetWheelAngles
        );
        primaryController.getAButton().whenPressed(() -> robotState.drivetrain.setTargetTurningAngle(ControllerConstants.CRITICAL_ANGLE_A));
        primaryController.getBButton().whenPressed(() -> robotState.drivetrain.setTargetTurningAngle(ControllerConstants.CRITICAL_ANGLE_B));
        primaryController.getXButton().whenPressed(() -> robotState.drivetrain.setTargetTurningAngle(ControllerConstants.CRITICAL_ANGLE_X));
        primaryController.getYButton().whenPressed(() -> robotState.drivetrain.setTargetTurningAngle(ControllerConstants.CRITICAL_ANGLE_Y));
        primaryController.getLeftBumperButton().whenPressed(() -> robotState.drivetrain.setManeuver("barrelroll"));
        primaryController.getRightBumperButton().whenPressed(() -> robotState.drivetrain.setManeuver("reversebarrelroll"));
        primaryController.getRightJoystickButton().whenPressed(() -> robotState.drivetrain.setManeuver("spin"));
    }

    private void configureCoPilotButtonBindings() {
    }

    private Axis getPilotDriveForwardAxis() {
        return primaryController.getLeftYAxis();
    }

    private Axis getPilotDriveStrafeAxis() {
        return primaryController.getLeftXAxis();
    }

    private Axis getPilotDriveRotationAxis() {
        return primaryController.getRightXAxis();
    }

    private Axis getPilotLeftTriggerAxis() {
        return primaryController.getLeftTriggerAxis();
    }

    private Axis getPilotRightTriggerAxis() {
        return primaryController.getRightTriggerAxis();
    }

    public XboxController getPrimaryController() {
        return primaryController;
    }

    public XboxController getSecondaryController() {
        return secondaryController;
    }

    public RobotState getRobotState() {
        return robotState;
    }

    public RobotSubsystems getRobotSubsystems() {
        return robotSubsystems;
    }
}
