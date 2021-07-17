package org.frcteam1764.robot;

import edu.wpi.first.wpilibj2.command.*;
import org.frcteam1764.robot.commands.DriveCommand;
import org.frcteam1764.robot.subsystems.*;
import org.frcteam2910.common.math.Rotation2;
import org.frcteam2910.common.robot.input.Axis;
import org.frcteam2910.common.robot.input.XboxController;
import org.frcteam1764.robot.state.DrivetrainState;
import org.frcteam1764.robot.state.RobotState;

public class RobotContainer {
    private final XboxController primaryController = new XboxController(Constants.PRIMARY_CONTROLLER_PORT);
    private final DrivetrainSubsystem drivetrainSubsystem;

    public RobotState robotState = new RobotState();

    public RobotContainer() {
        primaryController.getLeftXAxis().setInverted(true);
        primaryController.getRightXAxis().setInverted(true);
        robotState.drivetrain = new DrivetrainState(getLeftTriggerAxis(), getRightTriggerAxis());
        drivetrainSubsystem = new DrivetrainSubsystem(robotState.drivetrain);

        CommandScheduler.getInstance().setDefaultCommand(drivetrainSubsystem, new DriveCommand(drivetrainSubsystem, getDriveForwardAxis(), getDriveStrafeAxis(), getDriveRotationAxis(), this.robotState.drivetrain));

        configureButtonBindings();
    }

    private void configureButtonBindings() {
        primaryController.getBackButton().whenPressed(
                () -> robotState.drivetrain.resetGyroAngle(Rotation2.ZERO)
        );
        primaryController.getStartButton().whenPressed(
                drivetrainSubsystem::resetWheelAngles
        );
        primaryController.getXButton().whenPressed(() -> robotState.drivetrain.setTargetTurningAngle(90.0));
        primaryController.getAButton().whenPressed(() -> robotState.drivetrain.setTargetTurningAngle(180.0));
        primaryController.getBButton().whenPressed(() -> robotState.drivetrain.setTargetTurningAngle(270.0));
        primaryController.getYButton().whenPressed(() -> robotState.drivetrain.setTargetTurningAngle(360.0));
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

    public DrivetrainSubsystem getDrivetrainSubsystem() {
        return drivetrainSubsystem;
    }

    public XboxController getPrimaryController() {
        return primaryController;
    }
}
