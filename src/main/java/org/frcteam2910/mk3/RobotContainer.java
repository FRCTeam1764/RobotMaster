package org.frcteam2910.mk3;

import edu.wpi.first.wpilibj2.command.*;
import org.frcteam2910.mk3.commands.DriveCommand;
import org.frcteam2910.mk3.subsystems.*;
import org.frcteam2910.common.math.Rotation2;
import org.frcteam2910.common.robot.input.Axis;
import org.frcteam2910.common.robot.input.XboxController;
import org.frcteam2910.mk3.state.DrivetrainState;
import org.frcteam2910.mk3.state.RobotState;

public class RobotContainer {
    private final XboxController primaryController = new XboxController(Constants.PRIMARY_CONTROLLER_PORT);
    private final DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem();

    public RobotState robotState = new RobotState();

    public RobotContainer() {
        primaryController.getLeftXAxis().setInverted(true);
        primaryController.getRightXAxis().setInverted(true);
        robotState.drivetrainState = new DrivetrainState(getLeftTriggerAxis(), getRightTriggerAxis());

        CommandScheduler.getInstance().setDefaultCommand(drivetrainSubsystem, new DriveCommand(drivetrainSubsystem, getDriveForwardAxis(), getDriveStrafeAxis(), getDriveRotationAxis(), this.robotState));

        configureButtonBindings();
    }

    private void configureButtonBindings() {
        primaryController.getBackButton().whenPressed(
                () -> robotState.resetGyroAngle(Rotation2.ZERO)
        );
        primaryController.getStartButton().whenPressed(
                drivetrainSubsystem::resetWheelAngles
        );
        primaryController.getYButton().whenPressed(() -> robotState.drivetrainState.setTargetTurningAngle(0.0));
        primaryController.getBButton().whenPressed(() -> robotState.drivetrainState.setTargetTurningAngle(90.0));
        primaryController.getAButton().whenPressed(() -> robotState.drivetrainState.setTargetTurningAngle(180.0));
        primaryController.getXButton().whenPressed(() -> robotState.drivetrainState.setTargetTurningAngle(270.0));
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
