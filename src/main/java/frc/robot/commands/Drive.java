/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import com.ctre.phoenix.motorcontrol.Faults;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Robot;
import frc.robot.OI;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class Drive extends Command {
  public Drive() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.drivetrain);
  }

  TalonFX _rightMaster = Robot.drivetrain._rightMaster;
  TalonFX _leftMaster = Robot.drivetrain._leftMaster;

  boolean _firstCall = true;
	boolean _state = false;
	double _lockedDistance = 0;
  double _targetAngle = 0;
  int _smoothing;

  Joystick _gamepad = OI.joystick;
  
  boolean[] _btns = new boolean[Constants.kNumButtonsPlusOne];
	boolean[] btns = new boolean[Constants.kNumButtonsPlusOne];

  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double forward = -1 * _gamepad.getY();
		double turn = _gamepad.getZ();
		forward = ForwardDeadband(forward);
		turn = TurningDeadband(turn);
	
		/* Button processing for state toggle and sensor zeroing */
		getButtons(btns, _gamepad);
		if(btns[2] && !_btns[2]){
			_state = !_state; 		// Toggle state
			_firstCall = true;		// State change, do first call operation
			_targetAngle = _rightMaster.getSelectedSensorPosition(1);
			_lockedDistance = _rightMaster.getSelectedSensorPosition(0);
		}else if (btns[1] && !_btns[1]) {
			zeroSensors();			// Zero Sensors
		}
		if(btns[5] && !_btns[5]) {
			_smoothing--; // Decrement smoothing
			if(_smoothing < 0) _smoothing = 0; // Cap smoothing
			_rightMaster.configMotionSCurveStrength(_smoothing);

			System.out.println("Smoothing value is: " + _smoothing);
		}
		if(btns[6] && !_btns[6]) {
			_smoothing++; // Increment smoothing
			if(_smoothing > 8) _smoothing = 8; // Cap smoothing
			_rightMaster.configMotionSCurveStrength(_smoothing);
			
			System.out.println("Smoothing value is: " + _smoothing);
		}
		System.arraycopy(btns, 0, _btns, 0, Constants.kNumButtonsPlusOne);
				
		if(!_state){
			if (_firstCall)
				System.out.println("This is Acade Drive.\n");
			
			_leftMaster.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, +turn);
			_rightMaster.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, -turn);
		}else{
			if (_firstCall) {
				System.out.println("This is Motion Magic with the Auxiliary PID using the difference between two encoders.");
				System.out.println("Servo [-6,6] rotations while also maintaining a straight heading.\n");

				/* Determine which slot affects which PID */
				_rightMaster.selectProfileSlot(Constants.kSlot_Distanc, Constants.PID_PRIMARY);
				_rightMaster.selectProfileSlot(Constants.kSlot_Turning, Constants.PID_TURN);
			}
			
			/* Calculate targets from gamepad inputs */
			//final double target_sensorUnits = forward * Constants.kSensorUnitsPerRotation * Constants.kRotationsToTravel
       //   + _lockedDistance;

       final double target_sensorUnits = 162116;
       final double target_turn = _targetAngle;

       /*
       * Configured for MotionMagic on Quad Encoders' Sum and Auxiliary PID on Quad
       * Encoders' Difference
       */
      _rightMaster.set(ControlMode.MotionMagic, target_sensorUnits, DemandType.AuxPID, target_turn);
      _leftMaster.follow(_rightMaster, FollowerType.AuxOutput1);
    }
    _firstCall = false;
  }

  /** Zero quadrature encoders on Talon */
  void zeroSensors() {
    _leftMaster.getSensorCollection().setIntegratedSensorPosition(0.0, Constants.kTimeoutMs);
    _rightMaster.getSensorCollection().setIntegratedSensorPosition(0.0, Constants.kTimeoutMs);
    System.out.println("All sensors are zeroed.\n");
  }

  /** Deadband 5 percent, used on the gamepad */
  double ForwardDeadband(final double value) {
    /* Upper deadband */
    if (value >= +0.1)
      return value;

    /* Lower deadband */
    if (value <= -0.1)
      return value;

    /* Outside deadband */
    return 0;
  }

  double TurningDeadband(final double value) {
    /* Upper deadband */
    if (value >= +0.23)
      return value;

    /* Lower deadband */
    if (value <= -0.23)
      return value;

    /* Outside deadband */
    return 0;
  }

  /** Gets all buttons from gamepad */
  void getButtons(final boolean[] btns, final Joystick gamepad) {
		for (int i = 1; i < Constants.kNumButtonsPlusOne; ++i) {
			btns[i] = gamepad.getRawButton(i);
    }
  }
    
    @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
	}


/*public class Drive extends Command {
  public Drive() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.drivetrain);
  }

  DifferentialDrive _diffDrive = Robot.drivetrain._diffD;

    Faults _faults_L = new Faults();
    Faults _faults_R = new Faults();

    private Joystick stick = OI.joystick;
  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
   // Robot.drivetrain.drive(OI.joystick.getY() + OI.joystick.getZ(), OI.joystick.getY() - OI.joystick.getZ());

   String work = "";

        /* get gamepad stick values */
       // double forw = -1 * stick.getRawAxis(1); /* positive is forward */
       // double turn = +1 * stick.getRawAxis(2); /* positive is right */
       // boolean btn1 = stick.getRawButton(1); /* is button is down, print joystick values */

        /* deadband gamepad 10% */
      /*  if (Math.abs(forw) < 0.10) {
            forw = 0;
        }
        if (Math.abs(turn) < 0.10) {
            turn = 0;
        }

        /* drive robot */
       // Robot.drivetrain._diffD.arcadeDrive(forw, turn);

        /*
         * [2] Make sure Gamepad Forward is positive for FORWARD, and GZ is positive for
         * RIGHT
         */
       // work += " GF:" + forw + " GT:" + turn;

        /* get sensor values */
     /*   // double leftPos = _leftFront.GetSelectedSensorPosition(0);
        // double rghtPos = _rghtFront.GetSelectedSensorPosition(0);
        double leftVelUnitsPer100ms = Robot.drivetrain._leftFront.getSelectedSensorVelocity(0);
        double rghtVelUnitsPer100ms = Robot.drivetrain._rghtFront.getSelectedSensorVelocity(0);

        work += " L:" + leftVelUnitsPer100ms + " R:" + rghtVelUnitsPer100ms;

        /*
         * drive motor at least 25%, Talons will auto-detect if sensor is out of phase
         */
       /* Robot.drivetrain._leftFront.getFaults(_faults_L);
        Robot.drivetrain._rghtFront.getFaults(_faults_R);

        if (_faults_L.SensorOutOfPhase) {
            work += " L sensor is out of phase";
        }
        if (_faults_R.SensorOutOfPhase) {
            work += " R sensor is out of phase";
        }

        /* print to console if btn1 is held down */
        /*if (btn1) {
            System.out.println(work);
        }
  }

  
  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}*/
