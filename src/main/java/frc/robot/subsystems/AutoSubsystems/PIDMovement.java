/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems.AutoSubsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.constants.PIDConstants;
import frc.robot.constants.RobotDimensionConstants;

public class PIDMovement extends SubsystemBase {
  
  public PIDMovement() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  final static int kUnitsPerRevolution = 2048;
	final static double kUnitsPerRobotRevolution = RobotDimensionConstants.ROBOT_ROTATION_CIRCUMFERENCE / 360
			* RobotDimensionConstants.WHEEL_CIRCUMFERENCE * RobotDimensionConstants.GEAR_BOX_RATIO
			* kUnitsPerRevolution;

	public static void setPIDConfig(WPI_TalonFX talon, boolean setSensorPhase) {

		// Ensure sensor is positive when output is positive
		talon.setSensorPhase(setSensorPhase);

		// Config the peak and nominal Outputs, 12V means full
		talon.configNominalOutputForward(0, PIDConstants.kTimeoutMs);
		talon.configNominalOutputReverse(0, PIDConstants.kTimeoutMs);

		talon.configNeutralDeadband(PIDConstants.kNeutralDeadband, PIDConstants.kTimeoutMs);

		int closedLoopTimeMs = 1;
		talon.configClosedLoopPeriod(0, closedLoopTimeMs, PIDConstants.kTimeoutMs);
		talon.configClosedLoopPeriod(1, closedLoopTimeMs, PIDConstants.kTimeoutMs);

		/* Initialize */
		talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_Targets, 10);
		resetEncoders(talon);
	}

	public static void setDistancePIDConfig(WPI_TalonFX leftMaster, WPI_TalonFX rightMaster) {
		/** Feedback Sensor Configuration */
		
		/* Configure the left Talon's selected sensor as local QuadEncoder */
		leftMaster.configSelectedFeedbackSensor(	FeedbackDevice.IntegratedSensor,				// Local Feedback Source
													PIDConstants.PID_PRIMARY,					// PID Slot for Source [0, 1]
													PIDConstants.kTimeoutMs);					// Configuration Timeout

		/* Configure the Remote Talon's selected sensor as a remote sensor for the right Talon */
		rightMaster.configRemoteFeedbackFilter(leftMaster.getDeviceID(),					// Device ID of Source
												RemoteSensorSource.TalonSRX_SelectedSensor,	// Remote Feedback Source
												PIDConstants.REMOTE_0,							// Source number [0, 1]
												PIDConstants.kTimeoutMs);						// Configuration Timeout
		
		/* Setup Sum signal to be used for Distance */
		rightMaster.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.IntegratedSensor, PIDConstants.kTimeoutMs);				// Feedback Device of Remote Talon
		rightMaster.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.CTRE_MagEncoder_Relative, PIDConstants.kTimeoutMs);	// Quadrature Encoder of current Talon
		
		/* Setup Difference signal to be used for Turn */
		rightMaster.configSensorTerm(SensorTerm.Diff1, FeedbackDevice.RemoteSensor0, PIDConstants.kTimeoutMs);
		rightMaster.configSensorTerm(SensorTerm.Diff0, FeedbackDevice.CTRE_MagEncoder_Relative, PIDConstants.kTimeoutMs);
		
		/* Configure Sum [Sum of both QuadEncoders] to be used for Primary PID Index */
		rightMaster.configSelectedFeedbackSensor(	FeedbackDevice.IntegratedSensor, 
													PIDConstants.PID_PRIMARY,
													PIDConstants.kTimeoutMs);
		
		/* Scale Feedback by 0.5 to half the sum of Distance */
		rightMaster.configSelectedFeedbackCoefficient(	0.5, 						// Coefficient
														PIDConstants.PID_PRIMARY,		// PID Slot of Source 
														PIDConstants.kTimeoutMs);		// Configuration Timeout
		
		/* Configure Difference [Difference between both QuadEncoders] to be used for Auxiliary PID Index */
		rightMaster.configSelectedFeedbackSensor(	FeedbackDevice.SensorDifference, 
													PIDConstants.PID_TURN, 
													PIDConstants.kTimeoutMs);
		
		/* Scale the Feedback Sensor using a coefficient */
		rightMaster.configSelectedFeedbackCoefficient(	1,
														PIDConstants.PID_TURN, 
														PIDConstants.kTimeoutMs);
		/* Configure output */
		leftMaster.setSensorPhase(true);
		rightMaster.setSensorPhase(true);
		
		/* Set status frame periods to ensure we don't have stale data */
		rightMaster.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20, PIDConstants.kTimeoutMs);
		rightMaster.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20, PIDConstants.kTimeoutMs);
		rightMaster.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 20, PIDConstants.kTimeoutMs);
		rightMaster.setStatusFramePeriod(StatusFrame.Status_10_Targets, 20, PIDConstants.kTimeoutMs);
		leftMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, PIDConstants.kTimeoutMs);

		/* Configure neutral deadband */
		rightMaster.configNeutralDeadband(PIDConstants.kNeutralDeadband, PIDConstants.kTimeoutMs);
		leftMaster.configNeutralDeadband(PIDConstants.kNeutralDeadband, PIDConstants.kTimeoutMs);
		
		/* Motion Magic Configurations */
		rightMaster.configMotionAcceleration(2000, PIDConstants.kTimeoutMs);
		rightMaster.configMotionCruiseVelocity(2000, PIDConstants.kTimeoutMs);

		/**
		 * Max out the peak output (for all modes).  
		 * However you can limit the output of a given PID object with configClosedLoopPeakOutput().
		 */
		leftMaster.configPeakOutputForward(+1.0, PIDConstants.kTimeoutMs);
		leftMaster.configPeakOutputReverse(-1.0, PIDConstants.kTimeoutMs);
		rightMaster.configPeakOutputForward(+1.0, PIDConstants.kTimeoutMs);
		rightMaster.configPeakOutputReverse(-1.0, PIDConstants.kTimeoutMs);

		/* FPID Gains for distance servo */
		rightMaster.config_kP(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kP, PIDConstants.kTimeoutMs);
		rightMaster.config_kI(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kI, PIDConstants.kTimeoutMs);
		rightMaster.config_kD(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kD, PIDConstants.kTimeoutMs);
		rightMaster.config_kF(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kF, PIDConstants.kTimeoutMs);
		rightMaster.config_IntegralZone(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kIzone, PIDConstants.kTimeoutMs);
		rightMaster.configClosedLoopPeakOutput(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kPeakOutput, PIDConstants.kTimeoutMs);
		rightMaster.configAllowableClosedloopError(PIDConstants.kSlot_Distanc, 0, PIDConstants.kTimeoutMs);

		/* FPID Gains for turn servo */
		rightMaster.config_kP(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kP, PIDConstants.kTimeoutMs);
		rightMaster.config_kI(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kI, PIDConstants.kTimeoutMs);
		rightMaster.config_kD(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kD, PIDConstants.kTimeoutMs);
		rightMaster.config_kF(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kF, PIDConstants.kTimeoutMs);
		rightMaster.config_IntegralZone(PIDConstants.kSlot_Turning, (int)PIDConstants.kGains_Turning.kIzone, PIDConstants.kTimeoutMs);
		rightMaster.configClosedLoopPeakOutput(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kPeakOutput, PIDConstants.kTimeoutMs);
		rightMaster.configAllowableClosedloopError(PIDConstants.kSlot_Turning, 0, PIDConstants.kTimeoutMs);

		/**
		 * 1ms per loop.  PID loop can be slowed down if need be.
		 * For example,
		 * - if sensor updates are too slow
		 * - sensor deltas are very small per update, so derivative error never gets large enough to be useful.
		 * - sensor movement is very slow causing the derivative error to be near zero.
		 */
		int closedLoopTimeMs = 1;
		rightMaster.configClosedLoopPeriod(0, closedLoopTimeMs, PIDConstants.kTimeoutMs);
		rightMaster.configClosedLoopPeriod(1, closedLoopTimeMs, PIDConstants.kTimeoutMs);

		/**
		 * configAuxPIDPolarity(boolean invert, int timeoutMs)
		 * false means talon's local output is PID0 + PID1, and other side Talon is PID0 - PID1
		 * true means talon's local output is PID0 - PID1, and other side Talon is PID0 + PID1
		 */
		rightMaster.configAuxPIDPolarity(false, PIDConstants.kTimeoutMs);

		/* Initialize */
		rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_Targets, 10);
		Robot.drivetrain.resetEncoders();
	}

	/**
	 * Changes how the two sides of the drivetrain interact during the Aux PID loop.
	 * Use this everytime one side of the drivetrain changes their inversion
	 * setting.
	 * 
	 * @param masterTalon the talon with the PID values on it. Most likely the right
	 *                    one
	 */
	public static void changeAuxDiffSetting(WPI_TalonFX masterTalon) {
		TalonFXConfiguration config = new TalonFXConfiguration();

		setRobotDistanceConfigs(masterTalon.getInverted(), config);
		setRobotTurnConfigs(masterTalon.getInverted(), config);

		masterTalon.configAllSettings(config);
	}

	/**
	 * Resets the drive encoders to currently read a position of 0.
	 */
	public static void resetEncoders(WPI_TalonFX talon) {
		talon.getSensorCollection().setIntegratedSensorPosition(0, PIDConstants.kTimeoutMs);
	}

	/**
	 * Determines if SensorSum or SensorDiff should be used for combining left/right
	 * sensors into Robot Distance.
	 * 
	 * Assumes Aux Position is set as Remote Sensor 0.
	 * 
	 * configAllSettings must still be called on the master config after this
	 * function modifies the config values.
	 * 
	 * @param masterInvertType Invert of the Master Talon
	 * @param masterConfig     Configuration object to fill
	 */
	static void setRobotDistanceConfigs(Boolean masterInvertType, TalonFXConfiguration masterConfig) {
		/**
		 * Determine if we need a Sum or Difference.
		 * 
		 * The auxiliary Talon FX will always be positive in the forward direction
		 * because it's a selected sensor over the CAN bus.
		 * 
		 * The master's native integrated sensor may not always be positive when forward
		 * because sensor phase is only applied to *Selected Sensors*, not native sensor
		 * sources. And we need the native to be combined with the aux (other side's)
		 * distance into a single robot distance.
		 */

		/*
		 * THIS FUNCTION should not need to be modified. This setup will work regardless
		 * of whether the master is on the Right or Left side since it only deals with
		 * distance magnitude.
		 */

		/* Check if we're inverted */
		if (masterInvertType) {
			/*
			 * If master is inverted, that means the integrated sensor will be negative in
			 * the forward direction. If master is inverted, the final sum/diff result will
			 * also be inverted. This is how Talon FX corrects the sensor phase when
			 * inverting the motor direction. This inversion applies to the *Selected
			 * Sensor*, not the native value. Will a sensor sum or difference give us a
			 * positive total magnitude? Remember the Master is one side of your drivetrain
			 * distance and Auxiliary is the other side's distance. Phase | Term 0 | Term 1
			 * | Result Sum: -1 *((-)Master + (+)Aux )| NOT OK, will cancel each other out
			 * Diff: -1 *((-)Master - (+)Aux )| OK - This is what we want, magnitude will be
			 * correct and positive. Diff: -1 *((+)Aux - (-)Master)| NOT OK, magnitude will
			 * be correct but negative
			 */

			masterConfig.diff0Term = TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice(); // Local Integrated
																								// Sensor
			masterConfig.diff1Term = TalonFXFeedbackDevice.RemoteSensor0.toFeedbackDevice(); // Aux Selected Sensor
			masterConfig.primaryPID.selectedFeedbackSensor = TalonFXFeedbackDevice.SensorDifference.toFeedbackDevice(); // Diff0
																														// -
																														// Diff1
		} else {
			/* Master is not inverted, both sides are positive so we can sum them. */
			masterConfig.sum0Term = TalonFXFeedbackDevice.RemoteSensor0.toFeedbackDevice(); // Aux Selected Sensor
			masterConfig.sum1Term = TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice(); // Local IntegratedSensor
			masterConfig.primaryPID.selectedFeedbackSensor = TalonFXFeedbackDevice.SensorSum.toFeedbackDevice(); // Sum0
																													// +
																													// Sum1
		}

		/*
		 * Since the Distance is the sum of the two sides, divide by 2 so the total
		 * isn't double the real-world value
		 */
		masterConfig.primaryPID.selectedFeedbackCoefficient = 0.5;
	}

	/**
	 * Determines if SensorSum or SensorDiff should be used for combining left/right
	 * sensors into Robot Distance.
	 * 
	 * Assumes Aux Position is set as Remote Sensor 0.
	 * 
	 * configAllSettings must still be called on the master config after this
	 * function modifies the config values.
	 * 
	 * @param masterInvertType Invert of the Master Talon
	 * @param masterConfig     Configuration object to fill
	 */
	static void setRobotTurnConfigs(boolean masterInvertType, TalonFXConfiguration masterConfig) {
		/**
		 * Determine if we need a Sum or Difference.
		 * 
		 * The auxiliary Talon FX will always be positive in the forward direction
		 * because it's a selected sensor over the CAN bus.
		 * 
		 * The master's native integrated sensor may not always be positive when forward
		 * because sensor phase is only applied to *Selected Sensors*, not native sensor
		 * sources. And we need the native to be combined with the aux (other side's)
		 * distance into a single robot heading.
		 */

		/*
		 * THIS FUNCTION should not need to be modified. This setup will work regardless
		 * of whether the master is on the Right or Left side since it only deals with
		 * heading magnitude.
		 */

		/* Check if we're inverted */
		if (masterInvertType) {
			/*
			 * If master is inverted, that means the integrated sensor will be negative in
			 * the forward direction. If master is inverted, the final sum/diff result will
			 * also be inverted. This is how Talon FX corrects the sensor phase when
			 * inverting the motor direction. This inversion applies to the *Selected
			 * Sensor*, not the native value. Will a sensor sum or difference give us a
			 * positive heading? Remember the Master is one side of your drivetrain distance
			 * and Auxiliary is the other side's distance. Phase | Term 0 | Term 1 | Result
			 * Sum: -1 *((-)Master + (+)Aux )| OK - magnitude will cancel each other out
			 * Diff: -1 *((-)Master - (+)Aux )| NOT OK - magnitude increases with forward
			 * distance. Diff: -1 *((+)Aux - (-)Master)| NOT OK - magnitude decreases with
			 * forward distance
			 */

			masterConfig.sum0Term = TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice(); // Local Integrated
																								// Sensor
			masterConfig.sum1Term = TalonFXFeedbackDevice.RemoteSensor0.toFeedbackDevice(); // Aux Selected Sensor
			masterConfig.auxiliaryPID.selectedFeedbackSensor = TalonFXFeedbackDevice.SensorSum.toFeedbackDevice(); // Sum0
																													// +
																													// Sum1

			/*
			 * PID Polarity With the sensor phasing taken care of, we now need to determine
			 * if the PID polarity is in the correct direction This is important because if
			 * the PID polarity is incorrect, we will run away while trying to correct Will
			 * inverting the polarity give us a positive counterclockwise heading? If we're
			 * moving counterclockwise(+), and the master is on the right side and inverted,
			 * it will have a negative velocity and the auxiliary will have a negative
			 * velocity heading = right + left heading = (-) + (-) heading = (-) Let's
			 * assume a setpoint of 0 heading. This produces a positive error, in order to
			 * cancel the error, the right master needs to drive backwards. This means the
			 * PID polarity needs to be inverted to handle this
			 * 
			 * Conversely, if we're moving counterclwise(+), and the master is on the left
			 * side and inverted, it will have a positive velocity and the auxiliary will
			 * have a positive velocity. heading = right + left heading = (+) + (+) heading
			 * = (+) Let's assume a setpoint of 0 heading. This produces a negative error,
			 * in order to cancel the error, the left master needs to drive forwards. This
			 * means the PID polarity needs to be inverted to handle this
			 */
			masterConfig.auxPIDPolarity = true;
		} else {
			/* Master is not inverted, both sides are positive so we can diff them. */
			masterConfig.diff0Term = TalonFXFeedbackDevice.RemoteSensor1.toFeedbackDevice(); // Aux Selected Sensor
			masterConfig.diff1Term = TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice(); // Local
																								// IntegratedSensor
			masterConfig.auxiliaryPID.selectedFeedbackSensor = TalonFXFeedbackDevice.SensorDifference
					.toFeedbackDevice(); // Sum0 + Sum1
			/*
			 * With current diff terms, a counterclockwise rotation results in negative
			 * heading with a right master
			 */
			masterConfig.auxPIDPolarity = true;
		}

		/**
		 * Heading units should be scaled to ~4000 per 360 deg, due to the following
		 * limitations... - Target param for aux PID1 is 18bits with a range of
		 * [-131072,+131072] units. - Target for aux PID1 in motion profile is 14bits
		 * with a range of [-8192,+8192] units. ... so at 3600 units per 360', that
		 * ensures 0.1 degree precision in firmware closed-loop and motion profile
		 * trajectory points can range +-2 rotations.
		 */
		masterConfig.auxiliaryPID.selectedFeedbackCoefficient = kUnitsPerRevolution / kUnitsPerRobotRevolution;
		;
	 }


  /**
   * Have it so the PID slots for distance are selected and affecting the master talon
   * 
   * @param masterTalon the talon with all of the PID values on it. Most likely the right one.
   */
	public static void selectDistancePIDSlots(WPI_TalonFX masterTalon) {
    masterTalon.selectProfileSlot(PIDConstants.kSlot_Distanc, PIDConstants.PID_PRIMARY);
	masterTalon.selectProfileSlot(PIDConstants.kSlot_Turning, PIDConstants.PID_TURN);
  }

  public static void setMotionMagic(WPI_TalonFX masterTalon, WPI_TalonFX followerTalon, double units, double target_adjust){
    masterTalon.set(TalonFXControlMode.MotionMagic, units, DemandType.AuxPID, target_adjust);
    followerTalon.follow(masterTalon, FollowerType.AuxOutput1);
  }

  public static double getDistanceErrorValue(WPI_TalonFX masterTalon){
    return masterTalon.getClosedLoopError(0);
  }

  public double getTurningAdjustErrorValue(WPI_TalonFX masterTalon){
    return masterTalon.getClosedLoopError(1);
  }
}
