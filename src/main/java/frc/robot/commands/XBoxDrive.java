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

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.util.Limelight;
import frc.robot.constants.PIDConstants;

public class XBoxDrive extends CommandBase {
  public XBoxDrive() {
    // Use requires() here to declare subsystem dependencies
    addRequirements(Robot.drivetrain);

  }

  TalonFX _rightMaster = Robot.drivetrain.rightTalons[0];
  TalonFX _leftMaster = Robot.drivetrain.leftTalons[0];

  boolean _firstCall = true;
	boolean _state = false;
	double _lockedDistance = 0;
  double _targetAngle = 0;
  int _smoothing;

  XboxController controller = OI.driverXbox;
  
  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    double forward = -controller.getY(Hand.kLeft);
    double turn = controller.getX(Hand.kRight);
    forward = ForwardDeadband(forward);
    turn = TurningDeadband(turn);

    /* Button processing for state toggle and sensor zeroing */
    // getButtons(btns, controller);
    /*
     * if(btns[2] && !_btns[2]){ _state = !_state; // Toggle state _firstCall =
     * true; // State change, do first call operation _targetAngle =
     * _rightMaster.getSelectedSensorPosition(1); _lockedDistance =
     * _rightMaster.getSelectedSensorPosition(0); }
     */
    if (controller.getAButton()) {

      Limelight.turnLEDOn();
    }
    if (!controller.getAButton()) {

      Limelight.turnLEDOff();
    }

    if (controller.getTriggerAxis(Hand.kRight) > .4) {
      forward = 0;
      turn = 0;
    }

    // System.out.println("This is Acade Drive.\n");

    // Apply throttle to everything just to make sure nothing is overpowered
    _leftMaster.set(ControlMode.PercentOutput, forward * getThrottle(), DemandType.ArbitraryFeedForward,
        +turn * getThrottle());
    _rightMaster.set(ControlMode.PercentOutput, forward * getThrottle(), DemandType.ArbitraryFeedForward,
        -turn * getThrottle());

    // DriverStation.reportError("" +turn , true);

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
    if (value >= +0.15)
      return value;

    /* Lower deadband */
    if (value <= -0.15)
      return value;

    /* Outside deadband */
    return 0;
  }

  double TurningDeadband(final double value) {
    /* Upper deadband */
    if (value >= +0.25)
      return value;

    /* Lower deadband */
    if (value <= -0.25)
      return value;

    /* Outside deadband */
    return 0;
  }

  double getThrottle(){
    //return controller.getTriggerAxis(Hand.kRight);
    return .75;

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
