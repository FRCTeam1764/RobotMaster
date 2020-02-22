/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.constants.PIDConstants;
import frc.robot.util.Limelight;

public class JoystickDrive extends CommandBase {
  public JoystickDrive() {
    // Use requires() here to declare subsystem dependencies
    addRequirements(Robot.drivetrain);

  }

  TalonFX _rightMaster = Robot.drivetrain.rightTalons[0];
  TalonFX _leftMaster = Robot.drivetrain.leftTalons[0];

  int _smoothing;

  Joystick joystick = Robot.oi.driverJoystick;
  
  boolean[] _btns = new boolean[PIDConstants.kNumButtonsPlusOne];
	boolean[] btns = new boolean[PIDConstants.kNumButtonsPlusOne];

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    double forward = -1 * joystick.getY();
		double turn = joystick.getZ();
		forward = ForwardDeadband(forward);
		turn = TurningDeadband(turn);
	
		/* Button processing for state toggle and sensor zeroing */
		getButtons(btns, joystick);
		
    if (btns[1] && !_btns[1]) {
			zeroSensors();			// Zero Sensors
		}
		if(btns[5] && !_btns[5]) {
      _smoothing--; // Decrement smoothing
      
      Limelight.turnLEDOn();

			if(_smoothing < 0) _smoothing = 0; // Cap smoothing
			_rightMaster.configMotionSCurveStrength(_smoothing);

			System.out.println("Smoothing value is: " + _smoothing);
		}
		if(btns[6] && !_btns[6]) {

      Limelight.turnLEDOff();

			_smoothing++; // Increment smoothing
			if(_smoothing > 8) _smoothing = 8; // Cap smoothing
			_rightMaster.configMotionSCurveStrength(_smoothing);
			
			System.out.println("Smoothing value is: " + _smoothing);
		}
		System.arraycopy(btns, 0, _btns, 0, PIDConstants.kNumButtonsPlusOne);

      //Apply throttle to everything just to make sure nothing is overpowered
			_leftMaster.set(ControlMode.PercentOutput, forward*getThrottle(), DemandType.ArbitraryFeedForward, +turn*getThrottle());
      _rightMaster.set(ControlMode.PercentOutput, forward*getThrottle(), DemandType.ArbitraryFeedForward, -turn*getThrottle());
      
      DriverStation.reportError("" +turn*getThrottle()  , true);
  
  }

  /** Zero quadrature encoders on Talon */
  void zeroSensors() {
    _leftMaster.getSensorCollection().setIntegratedSensorPosition(0.0, PIDConstants.kTimeoutMs);
    _rightMaster.getSensorCollection().setIntegratedSensorPosition(0.0, PIDConstants.kTimeoutMs);
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
    if (value >= +0.14)
      return value;

    /* Lower deadband */
    if (value <= -0.14)
      return value;

    /* Outside deadband */
    return 0;
  }

  double getThrottle(){
    return -0.5 *joystick.getRawAxis(3) + 0.5;

    /*Uses the equation .5(axisvalue) +.5 to give an equation
      with the domain [-1,1] and the range [0,1]*/
  }

  /** Gets all buttons from gamepad */
  void getButtons(final boolean[] btns, final Joystick gamepad) {
		for (int i = 1; i < PIDConstants.kNumButtonsPlusOne; ++i) {
			btns[i] = gamepad.getRawButton(i);
    }
  }
    
  @Override
  public boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {
  }
}
