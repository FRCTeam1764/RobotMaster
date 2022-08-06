package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.libraries.external.robot.drivers.Limelight;

public class VisionSubsystem implements Subsystem {

    private static final Limelight LIMELIGHT = new Limelight("limelight");

    public VisionSubsystem(DrivetrainSubsystem drivetrainSubsystem) {
        // drivetrain = drivetrainSubsystem;
        // ShuffleboardTab tab = Shuffleboard.getTab("Vision");
        // distanceToTargetEntry = tab.add("distance to target", 0.0)
        //         .withPosition(0, 0)
        //         .withSize(1, 1)
        //         .getEntry();
        // dXOuterEntry = tab.add("dXOuter", 0.0)
        //         .withPosition(1, 0)
        //         .withSize(1, 1)
        //         .getEntry();
        // dYOuterEntry = tab.add("dYOuter", 0.0)
        //         .withPosition(2, 0)
        //         .withSize(1, 1)
        //         .getEntry();
        // tab.addNumber("target angle", () -> Math.toDegrees(getAngleToTarget().orElse(Double.NaN)))
        //         .withPosition(4, 0)
        //         .withSize(1, 1);
        // tab.addBoolean("Is on target", this::isOnTarget)
        //         .withPosition(5, 0)
        //         .withSize(1, 1);
        // tab.addNumber("target angle", () -> Math.toDegrees(getAngleToTarget().orElse(Double.NaN)))
        //         .withPosition(4, 0)
        //         .withSize(1, 1);
        // tab.addBoolean("Is on target", this::isOnTarget)
        //         .withPosition(5, 0)
        //         .withSize(1, 1);
        // tab.addNumber("Horizontal Target Error", () -> {
        //             double gyroAngle = drivetrain.getPose().rotation.toRadians();
        //             return getDistanceToTarget().orElse(0.0) *
        //                     (Math.sin(gyroAngle - getAngleToTarget().orElse(0.0)) / Math.sin(Math.PI / 2.0 - gyroAngle));
        //         })
        //         .withPosition(6, 0)
        //         .withSize(1, 1);
        // tab.addNumber("X-Coord Distance to Target", () -> xFromTarget)
        //         .withPosition(2, 1)
        //         .withSize(1, 1);
        // tab.addNumber("Y-Coord Distance To Target", () -> yFromTarget)
        //         .withPosition(3, 1)
        //         .withSize(1, 1);
    }

    public void setCamMode(Limelight.CamMode mode) {
        LIMELIGHT.setCamMode(mode);
    }

    @Override
    public void periodic() {
    }

    public void setLedMode(Limelight.LedMode mode) {
        LIMELIGHT.setLedMode(mode);
    }
}
