package org.frcteam1764.robot.constants;

import org.frcteam1764.robot.common.Gains;

public class PIDConstants {
    /**
	 * Set to zero to skip waiting for confirmation.
	 * Set to nonzero to wait and report to DS if action fails.
	 */
	public final static int kTimeoutMs = 30;

	/**
	 * Motor neutral dead-band, set to the minimum 0.1%.
	 */
	public final static double kNeutralDeadband = 0.001;

	/**
	 * Variables for Motion Magic
	 */
	public final static double kCruiseVelocity = 2000;
	public final static double kAcceleration = 2000;
	
	/**
	 * PID Gains may have to be adjusted based on the responsiveness of control loop.
     * kF: 1023 represents output value to Talon at 100%, 6800 represents Velocity units at 100% output
     * Not all set of Gains are used in this project and may be removed as desired.
     * 
	 * 	                                    			  kP                 kI   kD   kF                  Iz    PeakOut */
	//public final static Gains kGains_Distanc = new Gains( 5*(.2*1023)/1163, 0.001,  0, 80*(.7*1023)/6000,  50,  1 );
	//public final static Gains kGains_Turning = new Gains( 5*(.2*1023)/1163, 0.001,  0, 80*(.7*1023)/6000,  50,  1 );
	//public final static Gains kGains_Turning = new Gains( 2.0, 0.0,  0, 0.0,            200,  1.00 );
	//public final static Gains kGains_Velocit = new Gains( 0.1, 0.0, 20.0, 1023.0/6800.0,  300,  0.50 );
	//public final static Gains kGains_MotProf = new Gains( 1.0, 0.0,  0.0, 1023.0/6800.0,  400,  1.00 );
	// (.5*1023)/19939, 80*(.7*1023)/6000
	//                                                    kP   kI    kD   kF   Iz    PeakOut
	public final static Gains kGains_Distanc = new Gains( 1, 0.0,  0.0, 0.0, 100,     1 );
	public final static Gains kGains_Turning = new Gains( 0.1, 0.0,  0.0, 0.0, 200,     1 );
	public final static Gains kGains_Velocity_Shooter = new Gains(1,0,0,1/1023,0,.8);
	
	/** ---- Flat constants, you should not need to change these ---- */
	/* We allow either a 0 or 1 when selecting an ordinal for remote devices [You can have up to 2 devices assigned remotely to a talon/victor] */
	public final static int REMOTE_0 = 0;
	public final static int REMOTE_1 = 1;
	/* We allow either a 0 or 1 when selecting a PID Index, where 0 is primary and 1 is auxiliary */
	public final static int PID_PRIMARY = 0;
	public final static int PID_TURN = 1;
	/* Firmware currently supports slots [0, 3] and can be used for either PID Set */
	public final static int SLOT_0 = 0;
	public final static int SLOT_1 = 1;
	public final static int SLOT_2 = 2;
	public final static int SLOT_3 = 3;
	/* ---- Named slots, used to clarify code ---- */
	public final static int kSlot_Distanc = SLOT_0;
	public final static int kSlot_Turning = SLOT_1;
	public final static int kSlot_Velocit = SLOT_2;
	public final static int kSlot_MotProf = SLOT_3;

	public final static int kSlot_Shooter_Velocity = SLOT_0;

	/* ---- Conversion Rates ---- */

	public static final double WHEEL_ROTATION_PER_ROBOT_ROTATION = RobotDimensionConstants.ROBOT_ROTATION_CIRCUMFERENCE/RobotDimensionConstants.WHEEL_CIRCUMFERENCE;
	public static final double CLICKS_PER_ROBOT_ROTATION = WHEEL_ROTATION_PER_ROBOT_ROTATION *2048 * RobotDimensionConstants.GEAR_BOX_RATIO;
	public static final double CLICKS_PER_INCH = (2048.0*RobotDimensionConstants.GEAR_BOX_RATIO) /RobotDimensionConstants.WHEEL_CIRCUMFERENCE ; 
	public static final double CLICKS_PER_DEGREES = CLICKS_PER_ROBOT_ROTATION/360;

	public static final double TALON_VELOCITY_PER_ROBOT_VELOCITY = (1/100)*1000/CLICKS_PER_INCH; //Ticks/100ms per inches/second
	public static final double TALON_VELOCITY_PER_RPM = (1/100)*1000*60/2048; //Ticks/100ms per Rev/min
	public static final double MAX_VELOCITY_INCH_PER_SECOND = 0;
	

	/* ---- Error Values ---- */

	public static final int TICKS_ERROR = 250;
}
