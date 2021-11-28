package org.frcteam1764.robot.constants;

public class DrivetrainConstants {
    public static final int DRIVETRAIN_FRONT_LEFT_DRIVE_MOTOR = 22;
    public static final int DRIVETRAIN_FRONT_RIGHT_DRIVE_MOTOR = 27;
    public static final int DRIVETRAIN_BACK_LEFT_DRIVE_MOTOR = 32;
    public static final int DRIVETRAIN_BACK_RIGHT_DRIVE_MOTOR = 37;

    public static final int DRIVETRAIN_FRONT_LEFT_ANGLE_MOTOR = 21;
    public static final int DRIVETRAIN_FRONT_RIGHT_ANGLE_MOTOR = 26;
    public static final int DRIVETRAIN_BACK_LEFT_ANGLE_MOTOR = 31;
    public static final int DRIVETRAIN_BACK_RIGHT_ANGLE_MOTOR = 36;

    public static final int DRIVETRAIN_FRONT_LEFT_ENCODER_PORT = 23;
    public static final int DRIVETRAIN_FRONT_RIGHT_ENCODER_PORT = 28;
    public static final int DRIVETRAIN_BACK_LEFT_ENCODER_PORT = 33;
    public static final int DRIVETRAIN_BACK_RIGHT_ENCODER_PORT = 38;

    public static final int PIGEON_PORT = 51;

    // In degrees
    public static final double DRIVETRAIN_FRONT_LEFT_ENCODER_OFFSET = -Math.toRadians(31);
    public static final double DRIVETRAIN_FRONT_RIGHT_ENCODER_OFFSET = -Math.toRadians(81); // increasing turns clockwise
    public static final double DRIVETRAIN_BACK_LEFT_ENCODER_OFFSET = -Math.toRadians(58);
    public static final double DRIVETRAIN_BACK_RIGHT_ENCODER_OFFSET = -Math.toRadians(255);
}
