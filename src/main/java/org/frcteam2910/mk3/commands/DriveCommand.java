package org.frcteam2910.mk3.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frcteam2910.mk3.subsystems.DrivetrainSubsystem;
import org.frcteam2910.common.math.Vector2;
import org.frcteam2910.common.robot.input.Axis;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class DriveCommand extends CommandBase {
    private DrivetrainSubsystem drivetrainSubsystem;
    // private Axis forward;
    // private Axis strafe;
    // private Axis rotation;
    // private Axis targetLock;
    // public static NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

    // public DriveCommand(DrivetrainSubsystem drivetrain, Axis forward, Axis strafe, Axis rotation, Axis targetLock) {
    //     this.forward = forward;
    //     this.strafe = strafe;
    //     this.rotation = rotation;
    //     this.targetLock = targetLock;

    //     drivetrainSubsystem = drivetrain;

    //     addRequirements(drivetrain);
    // }

    // @Override
    // public void execute() {
    //     double rotation2;
    //     double limelightXOffset = -1 * limelightTable.getEntry("tx").getDouble(0);
    //     System.out.println(limelightXOffset);

    //     if (targetLock.get(true) > 0.5) {
    //         double pConstant = 0.035;
    //         rotation2 = limelightXOffset * pConstant;
    //     }
    //     else {
    //         rotation2 = rotation.get(true);
    //     }
    //     drivetrainSubsystem.drive(new Vector2(forward.get(true), strafe.get(true)), rotation2, true);
    // }

    private double forward;
    private double strafe;
    private double rotation;
    private boolean isFieldOriented;

    public DriveCommand(DrivetrainSubsystem drivetrain, double forward, double strafe, double rotation, boolean isFieldOriented) {
        this.forward = forward;
        this.strafe = strafe;
        this.rotation = rotation;
        this.isFieldOriented = isFieldOriented;

        drivetrainSubsystem = drivetrain;

        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        drivetrainSubsystem.drive(new Vector2(forward, strafe), rotation, isFieldOriented);
    }

    @Override
    public void end(boolean interrupted) {
        drivetrainSubsystem.drive(Vector2.ZERO, 0, false);
    }
}
