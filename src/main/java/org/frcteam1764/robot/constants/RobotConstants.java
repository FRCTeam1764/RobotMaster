package org.frcteam1764.robot.constants;

public class RobotConstants {
    public static final int RIGHT_LIMIT_SWITCH = 6;
    public static final int LEFT_LIMIT_SWITCH = 8;

    public static final int INTAKE_MOTOR = 60;
    public static final int INTAKE_SOLENOID_FORWARD = 0;
    public static final int INTAKE_SOLENOID_REVERSE = 1;

    public static final int SHOOTER_MASTER_MOTOR = 32;
    public static final int SHOOTER_FOLLOWER_MOTOR = 31;

    public static final int ELEVATOR_MOTOR = 20;

    public static final int CONVEYOR_MOTOR = 50;

    public static final int CLIMBER_MASTER_MOTOR = 40;
    public static final int CLIMBER_FOLLOWER_MOTOR = 41;
    public static final int CLIMBER_SOLENOID_FORWARD = 2;
    public static final int CLIMBER_SOLENOID_REVERSE = 3;

    public static final int DRIVETRAIN_FRONT_LEFT_DRIVE_MOTOR = 6;
    public static final int DRIVETRAIN_FRONT_RIGHT_DRIVE_MOTOR = 7;
    public static final int DRIVETRAIN_BACK_LEFT_DRIVE_MOTOR = 9;
    public static final int DRIVETRAIN_BACK_RIGHT_DRIVE_MOTOR = 8;

    public static final int DRIVETRAIN_FRONT_LEFT_ANGLE_MOTOR = 10;
    public static final int DRIVETRAIN_FRONT_RIGHT_ANGLE_MOTOR = 11;
    public static final int DRIVETRAIN_BACK_LEFT_ANGLE_MOTOR = 13;
    public static final int DRIVETRAIN_BACK_RIGHT_ANGLE_MOTOR = 12;

    public static final int DRIVETRAIN_FRONT_LEFT_ENCODER_PORT = 15;
    public static final int DRIVETRAIN_FRONT_RIGHT_ENCODER_PORT = 16;
    public static final int DRIVETRAIN_BACK_LEFT_ENCODER_PORT = 18;
    public static final int DRIVETRAIN_BACK_RIGHT_ENCODER_PORT = 17;

    // In degrees
    // increasing turns clockwise
    public static final double DRIVETRAIN_FRONT_LEFT_ENCODER_OFFSET = Math.toRadians(11.33);
    public static final double DRIVETRAIN_FRONT_RIGHT_ENCODER_OFFSET = Math.toRadians(302.18);
    public static final double DRIVETRAIN_BACK_LEFT_ENCODER_OFFSET = Math.toRadians(15.34);
    public static final double DRIVETRAIN_BACK_RIGHT_ENCODER_OFFSET = Math.toRadians(330.07);
}
