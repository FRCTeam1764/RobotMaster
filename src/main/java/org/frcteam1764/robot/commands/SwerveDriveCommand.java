package org.frcteam1764.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frcteam1764.robot.state.DrivetrainState;
import org.frcteam1764.robot.state.RobotState;
import org.frcteam1764.robot.subsystems.SwerveDrivetrain;
import org.frcteam2910.common.math.Vector2;
import org.frcteam2910.common.robot.drivers.Limelight;
import org.frcteam2910.common.robot.drivers.Limelight.CamMode;
import org.frcteam2910.common.robot.drivers.Limelight.LedMode;
import org.frcteam2910.common.robot.input.Axis;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class SwerveDriveCommand extends CommandBase {
    private SwerveDrivetrain drivetrain;
    private Axis forward;
    private Axis strafe;
    private Axis rotation;
    private DrivetrainState drivetrainState;
    private Limelight limelight;

    public SwerveDriveCommand(SwerveDrivetrain drivetrain, Axis forward, Axis strafe, Axis rotation, RobotState robotState) {
        this.forward = forward;
        this.strafe = strafe;
        this.rotation = rotation;
        this.drivetrainState = robotState.drivetrain;
        this.limelight = robotState.limelight;
        this.drivetrain = drivetrain;

        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        drivetrain.drive(new Vector2(applyDeadzone(getForward(), 0.15), applyDeadzone(getStrafe(), 0.15)), applyDeadzone(getRotation(), 0.15), drivetrainState.getIsFieldOriented());
    }

    private  double applyDeadzone(double input, double deadzone) {
        return Math.abs(input) > deadzone ? input : 0;
    }

    private double getForward() {
        boolean robotIsLocked = drivetrainState.isRotationLocked() || drivetrainState.isStrafeLocked();
        if (robotIsLocked) {
            return forward.get(true)/2; //intent to go slower when Lt or RT is held down
        }
        else {
            return forward.get(true);
        }
    }

    private double getStrafe() {
        boolean robotIsLocked = drivetrainState.isRotationLocked() || drivetrainState.isStrafeLocked();
        if (robotIsLocked) {
            return strafe.get(true)/2; //intent to go slower when Lt or RT is held down
        }
        else {
            return strafe.get(true);
        }
    }

    private double getRotation() {
        double targetAngle = drivetrainState.getTargetTurningAngle();
        boolean limelightHasTarget = limelight.hasTarget();
        boolean robotIsRotationLocked = drivetrainState.isRotationLocked();
        boolean robotIsCameraTracking = limelightHasTarget && robotIsRotationLocked;
        boolean controllerTurnSignalPresent = Math.abs(rotation.get(true)) > 0.15;
        boolean maneuverIsSet = targetAngle > 0.0 && !drivetrainState.getManeuver().equals("");
        boolean robotHasTargetTurningAngle = targetAngle > 0.0;
        limelight.setLedMode(robotIsRotationLocked ? LedMode.ON : LedMode.OFF);
        limelight.setCamMode(robotIsRotationLocked ? CamMode.VISION : CamMode.DRIVER);

        if (robotIsCameraTracking) {
            getCameraTrackingTurn();
        }
        else if (controllerTurnSignalPresent) { // override of critical angles
            drivetrainState.setManeuver("");
            drivetrainState.setTargetTurningAngle(0.0);
            return rotation.get(true);
        }
        else if (maneuverIsSet) {
            doABarrelRoll(targetAngle);
        }
        else if (robotHasTargetTurningAngle){
            getSetAngleTurn(targetAngle);
        }
        else if (robotIsRotationLocked) {
            drivetrainState.setManeuver("");
            drivetrainState.setTargetTurningAngle(0.0);
            return rotation.get(true)/2; //intent to go slower when Lt or RT is held down
        }
        return 0.0;
    }

    private double getCameraTrackingTurn() {
        double limelightXOffset = limelight.getTargetXOffset();
        double cameraRotationConstant = -0.025;
        double minRotationSignal = limelightXOffset * cameraRotationConstant > 0.0 ? 0.4 : -0.4;
        drivetrainState.setManeuver("");
        drivetrainState.setTargetTurningAngle(0.0);
        return minRotationSignal;
    }

    private double doABarrelRoll(double targetAngle) {
        double currentAngle = drivetrainState.getGyro().getAngle().toDegrees();
        double angleDiff = targetAngle - currentAngle;
        String maneuver = drivetrainState.getManeuver();

        if (maneuver.equals("barrelroll") && Math.abs(angleDiff) > 4.0) {
            return -1.0;
        }
        else if (maneuver.equals("reversebarrelroll") && Math.abs(angleDiff) > 4.0) {
            return 1.0;
        }

        drivetrainState.setManeuver("");
        drivetrainState.setTargetTurningAngle(0.0);
        return 0.0;
    }

    private double getSetAngleTurn(double targetAngle) {
        double p = 0.009;
        double currentAngle = drivetrainState.getGyro().getAngle().toDegrees();
        double angleDiff = targetAngle - currentAngle;
        drivetrainState.setManeuver("");

        angleDiff += angleDiff > 180.0 ? -360.0 : angleDiff < -180.0 ? 360 : 0.0 ;

        if (Math.abs(angleDiff) > 5.0) {
            double rotationSignal = angleDiff * p;
            double minRotationSignal = rotationSignal > 0 ? 0.4 : -0.4;
            return Math.abs(rotationSignal) > Math.abs(minRotationSignal) ? rotationSignal : minRotationSignal;
        }
        return 0.0;
    }
}