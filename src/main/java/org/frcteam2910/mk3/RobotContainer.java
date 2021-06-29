package org.frcteam2910.mk3;

import edu.wpi.first.wpilibj2.command.*;
import org.frcteam2910.mk3.commands.DriveCommand;
import org.frcteam2910.mk3.subsystems.*;
import org.frcteam2910.common.math.Rotation2;
import org.frcteam2910.common.robot.input.Axis;
import org.frcteam2910.common.robot.input.XboxController;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class RobotContainer {
    private final XboxController primaryController = new XboxController(Constants.PRIMARY_CONTROLLER_PORT);

    private final DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem();
    public static NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

    public RobotContainer() {
        primaryController.getLeftXAxis().setInverted(true);
        primaryController.getRightXAxis().setInverted(true);

        // CommandScheduler.getInstance().setDefaultCommand(drivetrainSubsystem, new DriveCommand(drivetrainSubsystem, getDriveForwardAxis(), getDriveStrafeAxis(), getDriveRotationAxis(), getLeftTriggerAxis()));
        CommandScheduler.getInstance().setDefaultCommand(drivetrainSubsystem, new DriveCommand(drivetrainSubsystem, getDriveForwardAxis(), getDriveStrafeAxis(), getDriveRotationAxis(), getIsFieldOriented()));

        configureButtonBindings();
    }

    private void configureButtonBindings() {
        primaryController.getBackButton().whenPressed(
                () -> drivetrainSubsystem.resetGyroAngle(Rotation2.ZERO)
        );
        primaryController.getStartButton().whenPressed(
                drivetrainSubsystem::resetWheelAngles
        );
    }

    // private Axis getDriveForwardAxis() {
    //     return primaryController.getLeftYAxis();
    // }

    // private Axis getDriveStrafeAxis() {
    //     return primaryController.getLeftXAxis();
    // }

    // private Axis getDriveRotationAxis() {
    //     return primaryController.getRightXAxis();
    // }

    // private Axis getLeftTriggerAxis() {
    //     return primaryController.getLeftTriggerAxis();
    // }

    private double getDriveForwardAxis() {
        double leftTriggerAxis = primaryController.getLeftTriggerAxis().get(true);
        double rightTriggerAxis = primaryController.getRightTriggerAxis().get(true);
        double leftYAxis = primaryController.getLeftYAxis().get(true);
        double driveForward;

        if (leftTriggerAxis > 0.5 || rightTriggerAxis > 0.5) {
            driveForward = Math.abs(leftYAxis) > 0.5 ? 0.5 : leftYAxis;
        }
        else {
            driveForward =  leftXAxis;
        }
        return driveForward;
    }

    private double getDriveStrafeAxis() {
        double leftTriggerAxis = primaryController.getLeftTriggerAxis().get(true);
        double rightTriggerAxis = primaryController.getRightTriggerAxis().get(true);
        double rightXAxis = primaryController.getRightXAxis().get(true);
        double leftXAxis = primaryController.getLeftXAxis().get(true);
        double limelightSkewOffset = limelightTable.getEntry("ts").getDouble(0);
        boolean limelightHasTarget = limelightTable.getEntry("tv").getDouble(0) == 1;
        double strafe;

        if (limelightHasTarget && leftTriggerAxis > 0.5 && rightTriggerAxis > 0.5) {
            double strafeConstant = .01;
            double strafeVelocity = Math.abs(leftXAxis) * strafeRotationConstant;
            strafe = strafeVelocity;
        }
        else if (leftTriggerAxis > 0.5 || rightTriggerAxis > 0.5) {
            strafe = Math.abs(leftXAxis) > 0.5 ? 0.5 : leftXAxis;
        }
        else {
            strafe =  leftXAxis;
        }
        return 0; //rotation;
    }

    private double getDriveRotationAxis() {
        double leftTriggerAxis = primaryController.getLeftTriggerAxis().get(true);
        double rightTriggerAxis = primaryController.getRightTriggerAxis().get(true);
        double rightXAxis = primaryController.getRightXAxis().get(true);
        double leftXAxis = primaryController.getLeftXAxis().get(true);
        double limelightXOffset = limelightTable.getEntry("tx").getDouble(0);
        boolean limelightHasTarget = limelightTable.getEntry("tv").getDouble(0) == 1;
        double rotation;
        
        limelightTable.getEntry("ledMode").setNumber(leftTriggerAxis > 0.5 ? 3 : 1);

        if (limelightHasTarget && leftTriggerAxis > 0.5) {
            double cameraRotationConstant = 0.035;
            double strafeRotationConstant = 1;
            double cameraRotation = -1 * limelightXOffset * pConstant;
            double strafeRotation = Math.abs(leftXAxis) * strafeRotationConstant;
            rotation = cameraRotation > strafeRotation ? cameraRotation : cameraRotation * strafeRotation;
        }
        else if (leftTriggerAxis > 0.5 || rightTriggerAxis > 0.5) {
            rotation = Math.abs(rightXAxis) > 0.5 ? 0.5 : rightXAxis;
        }
        else {
            rotation =  rightXAxis;
        }
        return 0; //rotation;
    }

    private double getIsFieldOriented() {
        double leftTriggerAxis = primaryController.getLeftTriggerAxis().get(true);
        double rightTriggerAxis = primaryController.getRightTriggerAxis().get(true);

        return leftTriggerAxis > 0.5 && rightTriggerAxis > 0.5;
    }

    public DrivetrainSubsystem getDrivetrainSubsystem() {
        return drivetrainSubsystem;
    }

    public XboxController getPrimaryController() {
        return primaryController;
    }
}
