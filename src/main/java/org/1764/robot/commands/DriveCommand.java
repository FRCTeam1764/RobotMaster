package org.frcteam1764.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frcteam1764.robot.state.DrivetrainState;
import org.frcteam1764.robot.state.RobotState;
import org.frcteam1764.robot.subsystems.DrivetrainSubsystem;
import org.frcteam2910.common.math.Vector2;
import org.frcteam2910.common.robot.drivers.Limelight;
import org.frcteam2910.common.robot.drivers.Limelight.LedMode;
import org.frcteam2910.common.robot.input.Axis;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class DriveCommand extends CommandBase {
    private DrivetrainSubsystem drivetrainSubsystem;
    private Axis forward;
    private Axis strafe;
    private Axis rotation;
    private DrivetrainState drivetrainState;
    private Limelight limelight;

    public DriveCommand(DrivetrainSubsystem drivetrain, Axis forward, Axis strafe, Axis rotation, RobotState robotState) {
        this.forward = forward;
        this.strafe = strafe;
        this.rotation = rotation;
        this.drivetrainState = robotState.drivetrain;
        this.limelight = robotState.limelight;

        drivetrainSubsystem = drivetrain;

        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        drivetrainSubsystem.drive(new Vector2(applyDeadzone(getForward(), 0.1), applyDeadzone(getStrafe(), 0.1)), applyDeadzone(getRotation(), 0.1), true);
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
        double limelightXOffset = limelight.getTargetXOffset();
        boolean limelightHasTarget = limelight.hasTarget();
        boolean robotIsLocked = drivetrainState.isRotationLocked() || drivetrainState.isStrafeLocked();
        limelight.setLedMode(robotIsLocked ? LedMode.ON : LedMode.OFF);
        double targetAngle = drivetrainState.getTargetTurningAngle();

        if (limelightHasTarget && robotIsLocked) {
            double cameraRotationConstant = -0.035;
            return limelightXOffset * cameraRotationConstant;
        }
        else if (Math.abs(rotation.get(true)) > 0.05) {
            drivetrainState.setTargetTurningAngle(0.0);
            return rotation.get(true);
        }
        else if (targetAngle > 0.0 && drivetrainState.getManeuver() != "") {
            double currentAngle = drivetrainState.getGyro().getAngle().toDegrees();
            double angleDiff = targetAngle - currentAngle;
            String maneuver = drivetrainState.getManeuver();

            if ((maneuver == "barrelroll") && Math.abs(angleDiff) > 4.0) {
                return 1;
            }
            else if (maneuver == "reversebarrelroll" && Math.abs(angleDiff) > 4.0) {
                return -1;
            }
            else {
                drivetrainState.setManeuver("");
                drivetrainState.setTargetTurningAngle(0.0);
                return rotation.get(true);
            }
            
        }
        else if (targetAngle > 0.0){
            double p = 0.009;
            double currentAngle = drivetrainState.getGyro().getAngle().toDegrees();
            double angleDiff = targetAngle - currentAngle;

            angleDiff += angleDiff > 180.0 ? -360.0 : angleDiff < -180.0 ? 360 : 0 ;

            if (Math.abs(angleDiff) > 5.0) {
                double rotationSignal = angleDiff * p;
                double minRotationSignal = rotationSignal > 0 ? 0.4 : -0.4;
                return Math.abs(rotationSignal) > Math.abs(minRotationSignal) ? rotationSignal : minRotationSignal;
            }
            else{
                return rotation.get(true);
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