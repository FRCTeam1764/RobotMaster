/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands.DriveCommands;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.constants.PIDConstants;

public class XBoxDrive extends CommandBase {

  double throttle;

  public XBoxDrive() {
    // Use requires() here to declare subsystem dependencies
    addRequirements(Robot.drivetrain);
    throttle=.75;

  }

  TalonFX _rightMaster = Robot.drivetrain.rightTalons[0];
  TalonFX _leftMaster = Robot.drivetrain.leftTalons[0];

  DifferentialDrive diffDrive = Robot.drivetrain.diffDrive;

  XboxController controller = Robot.oi.driverXbox;
  
  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    double forward = -controller.getY(Hand.kLeft);
    double turn = controller.getX(Hand.kRight);
    forward = ForwardDeadband(forward);
    turn = TurningDeadband(turn);

    throttle = SmartDashboard.getBoolean("Slow Mode Active", false) ?
               .45 : .75;

    //throttle = controller.getStickButtonPressed(Hand.kLeft) ? 1 : throttle;

   /* if (controller.getAButton()) {

      Limelight.turnLEDOn();
    }
    if (!controller.getAButton()) {

      Limelight.turnLEDOff();
    }*/

    // System.out.println("This is Acade Drive.\n");

    // Apply throttle to everything just to make sure nothing is overpowered
    diffDrive.arcadeDrive(forward * throttle, turn*Math.abs(turn)* throttle);

    // DriverStation.reportError("" +turn , true);

    SmartDashboard.putNumber("throttle", throttle);

  }
//zach likes hannah confirmed

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
    if (value >= +0.15)
      return value;

    /* Lower deadband */
    if (value <= -0.15)
      return value;

    /* Outside deadband */
    return 0;
  }

  double getThrottle(double axisValue){
    //return controller.getTriggerAxis(Hand.kRight);
    return .5*(axisValue) +.5;

    /*Uses the equation .5(axisvalue) +.5 to give an equation
      with the domain [-1,1] and the range [0,1]*/
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
