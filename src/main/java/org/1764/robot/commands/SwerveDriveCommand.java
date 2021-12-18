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
    private SwerveDrivetrain drivetrainSubsystem;
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

        drivetrainSubsystem = drivetrain;

        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        drivetrainSubsystem.drive(new Vector2(applyDeadzone(getForward(), 0.15), applyDeadzone(getStrafe(), 0.15)), applyDeadzone(getRotation(), 0.15), drivetrainState.getIsFieldOriented());
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
        double limelightXOffset = limelight.getTargetXOffset();
        boolean limelightHasTarget = limelight.hasTarget();
        boolean robotIsLocked = drivetrainState.isRotationLocked() || drivetrainState.isStrafeLocked();
        limelight.setLedMode(robotIsLocked ? LedMode.ON : LedMode.OFF);
        limelight.setCamMode(robotIsLocked ? CamMode.VISION : CamMode.DRIVER);
        double targetAngle = drivetrainState.getTargetTurningAngle();

        if (limelightHasTarget && robotIsLocked) {
            drivetrainState.setManeuver("");
            drivetrainState.setTargetTurningAngle(0.0);
            double cameraRotationConstant = -0.025;
            double minRotationSignal = limelightXOffset * cameraRotationConstant > 0 ? 0.4 : -0.4;
            return minRotationSignal;
        }
        else if (Math.abs(rotation.get(true)) > 0.05) { // override of critical angles
            drivetrainState.setManeuver("");
            drivetrainState.setTargetTurningAngle(0.0);
            return rotation.get(true);
        }
        else if (targetAngle > 0.0 && !drivetrainState.getManeuver().equals("")) { // barrel roll code
            double currentAngle = drivetrainState.getGyro().getAngle().toDegrees();
            double angleDiff = targetAngle - currentAngle;
            String maneuver = drivetrainState.getManeuver();

            if (maneuver.equals("barrelroll") && Math.abs(angleDiff) > 4.0) {
                return -1;
            }
            else if (maneuver.equals("reversebarrelroll") && Math.abs(angleDiff) > 4.0) {
                return 1;
            }
            else {
                System.out.println(maneuver);
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
                drivetrainState.setManeuver("");
                return rotation.get(true);
            }
        }
        else if (robotIsLocked) {
            drivetrainState.setManeuver("");
            drivetrainState.setTargetTurningAngle(0.0);
            return rotation.get(true)/2; //intent to go slower when Lt or RT is held down
        }
        
        return 0.0;
    }
}