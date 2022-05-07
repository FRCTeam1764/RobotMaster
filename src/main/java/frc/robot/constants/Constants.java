package frc.robot.constants;

import com.swervedrivespecialties.swervelib.CanPort;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    /**
     * The left-to-right distance between the drivetrain wheels
     *
     * Should be measured from center to center.
     */
    public static final double DRIVETRAIN_TRACKWIDTH_METERS = 1.0; // FIXME Measure and set trackwidth
    /**
     * The front-to-back distance between the drivetrain wheels.
     *
     * Should be measured from center to center.
     */
    public static final double DRIVETRAIN_WHEELBASE_METERS = 1.0; // FIXME Measure and set wheelbase

    // public static final int DRIVETRAIN_PIGEON_ID = 0; // FIXME Set Pigeon ID

    public static final CanPort FRONT_LEFT_MODULE_DRIVE_MOTOR = new CanPort(6);
    public static final CanPort FRONT_RIGHT_MODULE_DRIVE_MOTOR = new CanPort(7);
    public static final CanPort BACK_LEFT_MODULE_DRIVE_MOTOR = new CanPort(9);
    public static final CanPort BACK_RIGHT_MODULE_DRIVE_MOTOR = new CanPort(8);

    public static final CanPort FRONT_LEFT_MODULE_STEER_MOTOR = new CanPort(10);
    public static final CanPort FRONT_RIGHT_MODULE_STEER_MOTOR = new CanPort(11);
    public static final CanPort BACK_LEFT_MODULE_STEER_MOTOR = new CanPort(13);
    public static final CanPort BACK_RIGHT_MODULE_STEER_MOTOR = new CanPort(12);

    public static final CanPort FRONT_LEFT_MODULE_STEER_ENCODER = new CanPort(15);
    public static final CanPort FRONT_RIGHT_MODULE_STEER_ENCODER = new CanPort(16);
    public static final CanPort BACK_LEFT_MODULE_STEER_ENCODER = new CanPort(18);
    public static final CanPort BACK_RIGHT_MODULE_STEER_ENCODER = new CanPort(17);

    // In degrees
    // increasing turns clockwise
    public static final double FRONT_LEFT_MODULE_STEER_OFFSET = Math.toRadians(-11.46);
    public static final double FRONT_RIGHT_MODULE_STEER_OFFSET = Math.toRadians(-300.42);
    public static final double BACK_LEFT_MODULE_STEER_OFFSET = Math.toRadians(-194.77);
    public static final double BACK_RIGHT_MODULE_STEER_OFFSET = Math.toRadians(-332.06);
    
    public static final int PRESSURE_SENSOR_PORT = 0;
}
