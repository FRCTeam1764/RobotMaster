package org.frcteam2910.mk3.commands;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frcteam2910.mk3.state.DrivetrainState;
import org.frcteam2910.mk3.subsystems.DrivetrainSubsystem;
import org.frcteam2910.common.math.Vector2;
import org.frcteam2910.common.robot.input.Axis;
import org.frcteam2910.common.drivers.Gyroscope;
import org.frcteam2910.common.robot.drivers.NavX;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class DriveCommand extends CommandBase {
    private DrivetrainSubsystem drivetrainSubsystem;
    private Axis forward;
    private Axis strafe;
    private Axis rotation;
    private DrivetrainState drivetrainState;
    public static NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

    public DriveCommand(DrivetrainSubsystem drivetrain, Axis forward, Axis strafe, Axis rotation, DrivetrainState drivetrainState) {
        this.forward = forward;
        this.strafe = strafe;
        this.rotation = rotation;
        this.drivetrainState = drivetrainState;

        drivetrainSubsystem = drivetrain;

        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        drivetrainSubsystem.drive(new Vector2(applyDeadzone(getForward(), 0.05), applyDeadzone(getStrafe(), 0.05)), applyDeadzone(getRotation(), 0.05), true);
    }

    private double getForward() {
        boolean robotIsLocked = drivetrainState.isRotationLocked() || drivetrainState.isStrafeLocked();
        if (robotIsLocked) {
            return forward.get(true)/3;
        }
        else {
            return forward.get(true);
        }
    }

    private double getStrafe() {
        boolean robotIsLocked = drivetrainState.isRotationLocked() || drivetrainState.isStrafeLocked();
        if (robotIsLocked) {
            return strafe.get(true)/3;
        }
        else {
            return strafe.get(true);
        }
    }

    private double getRotation() {
        double limelightXOffset = limelightTable.getEntry("tx").getDouble(0);
        double limelightHasTarget = limelightTable.getEntry("tv").getDouble(0);
        boolean robotIsLocked = drivetrainState.isRotationLocked() || drivetrainState.isStrafeLocked();
        // limelightTable.getEntry("ledMode").setNumber(robotIsLocked ? 3 : 1);
        limelightTable.getEntry("ledMode").setNumber(1);
    
        /*if (limelightHasTarget == 1.0 && robotIsLocked) {
            double cameraRotationConstant = -0.035;
            return limelightXOffset * cameraRotationConstant;
        }*/
        if(drivetrainState.getTargetTurningAngle() != 0){
            double p = 0.005;
            double unadjustedAngle = gyroscope.getAngle().toDegrees(); // current anlge
            double rotationAngle = unadjustedAngle - drivetrainState.getTargetTurningAngle();
            double adjustedAngle = rotationAngle > 0 ? rotationAngle : 360 + rotationAngle; // target angle
            boolean turningClockwise = rotationAngle < 0;
            double angleDiff = turningClockwise ? adjustedAngle - unadjustedAngle : unadjustedAngle - adjustedAngle;
            if(angleDiff < 2.0){
                drivetrainState.setTargetTurningAngle(0.0);
                return rotation.get(true)/2;
            }
            else{
                return (turningClockwise ? angleDiff : angleDiff * -1) * p;
            }
        }
        else if (robotIsLocked) {
            return rotation.get(true)/2;
        }
        else {
            return rotation.get(true);
        }
    }

    private  double applyDeadzone(double input, double deadzone) {
        return Math.abs(input) > deadzone ? input : 0;
    }
}








// private double getDriveForwardAxis() {
//     double leftTriggerAxis = primaryController.getLeftTriggerAxis().get(true);
//     double rightTriggerAxis = primaryController.getRightTriggerAxis().get(true);
//     double leftYAxis = primaryController.getLeftYAxis().get(true);
//     double driveForward;

//     if (leftTriggerAxis > 0.5 || rightTriggerAxis > 0.5) {
//         driveForward = Math.abs(leftYAxis) > 0.5 ? 0.5 : leftYAxis;
//     }
//     else {
//         driveForward =  leftXAxis;
//     }
//     return driveForward;
// }

// private double getDriveStrafeAxis() {
//     double leftTriggerAxis = primaryController.getLeftTriggerAxis().get(true);
//     double rightTriggerAxis = primaryController.getRightTriggerAxis().get(true);
//     double rightXAxis = primaryController.getRightXAxis().get(true);
//     double leftXAxis = primaryController.getLeftXAxis().get(true);
//     double limelightSkewOffset = limelightTable.getEntry("ts").getDouble(0);
//     boolean limelightHasTarget = limelightTable.getEntry("tv").getDouble(0) == 1;
//     double strafe;

//     if (limelightHasTarget && leftTriggerAxis > 0.5 && rightTriggerAxis > 0.5) {
//         double strafeConstant = .01;
//         double strafeVelocity = Math.abs(leftXAxis) * strafeRotationConstant;
//         strafe = strafeVelocity;
//     }
//     else if (leftTriggerAxis > 0.5 || rightTriggerAxis > 0.5) {
//         strafe = Math.abs(leftXAxis) > 0.5 ? 0.5 : leftXAxis;
//     }
//     else {
//         strafe =  leftXAxis;
//     }
//     return 0; //rotation;
// }

// private double getDriveRotationAxis() {
//     double leftTriggerAxis = primaryController.getLeftTriggerAxis().get(true);
//     double rightTriggerAxis = primaryController.getRightTriggerAxis().get(true);
//     double rightXAxis = primaryController.getRightXAxis().get(true);
//     double leftXAxis = primaryController.getLeftXAxis().get(true);
//     double limelightXOffset = limelightTable.getEntry("tx").getDouble(0);
//     boolean limelightHasTarget = limelightTable.getEntry("tv").getDouble(0) == 1;
//     double rotation;
    
//     limelightTable.getEntry("ledMode").setNumber(leftTriggerAxis > 0.5 ? 3 : 1);

//     if (limelightHasTarget && leftTriggerAxis > 0.5) {
//         double cameraRotationConstant = 0.035;
//         double strafeRotationConstant = 1;
//         double cameraRotation = -1 * limelightXOffset * pConstant;
//         double strafeRotation = Math.abs(leftXAxis) * strafeRotationConstant;
//         rotation = cameraRotation > strafeRotation ? cameraRotation : cameraRotation * strafeRotation;
//     }
//     else if (leftTriggerAxis > 0.5 || rightTriggerAxis > 0.5) {
//         rotation = Math.abs(rightXAxis) > 0.5 ? 0.5 : rightXAxis;
//     }
//     else {
//         rotation =  rightXAxis;
//     }
//     return 0; //rotation;
// }

// private double getIsFieldOriented() {
//     double leftTriggerAxis = primaryController.getLeftTriggerAxis().get(true);
//     double rightTriggerAxis = primaryController.getRightTriggerAxis().get(true);

//     return leftTriggerAxis > 0.5 && rightTriggerAxis > 0.5;
// }
