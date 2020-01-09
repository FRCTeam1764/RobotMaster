/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.Constants;
import frc.robot.constants.JoystickConstants;

import com.ctre.phoenix.motorcontrol.Faults;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

/**
 * Add your docs here.
 */
public class Drivetrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
 
  public WPI_TalonFX _rghtFront = new WPI_TalonFX(11);
  public WPI_TalonFX _rghtFollower = new WPI_TalonFX(12);
  public WPI_TalonFX _rghtFollower2 = new WPI_TalonFX(13);
  public WPI_TalonFX _leftFront = new WPI_TalonFX(14);
  public WPI_TalonFX _leftFollower = new WPI_TalonFX(15);
  public WPI_TalonFX _leftFollower2 = new WPI_TalonFX(16);
  
  public DifferentialDrive _diffD = new DifferentialDrive(_leftFront, _rghtFront);

  public Drivetrain(){
     /* factory default values */
     _rghtFront.configFactoryDefault();
     _rghtFollower.configFactoryDefault();
     _leftFront.configFactoryDefault();
     _leftFollower.configFactoryDefault();
     _rghtFollower2.configFactoryDefault();
     _leftFollower2.configFactoryDefault();

     _leftFront.configOpenloopRamp(0.5);
     _rghtFront.configOpenloopRamp(0.5);

     _leftFront.setNeutralMode(NeutralMode.Brake);
     _rghtFront.setNeutralMode(NeutralMode.Brake);

     /* set up followers */
     _rghtFollower.follow(_rghtFront);
     _leftFollower.follow(_leftFront);

     _rghtFollower2.follow(_rghtFront);
     _leftFollower2.follow(_leftFront);

     /* [3] flip values so robot moves forward when stick-forward/LEDs-green */
     _rghtFront.setInverted(true); // !< Update this
     _leftFront.setInverted(false); // !< Update this

     /*
      * set the invert of the followers to match their respective master controllers
      */
     _rghtFollower.setInverted(true);
     _leftFollower.setInverted(false);
    _rghtFollower2.setInverted(true);
     _leftFollower2.setInverted(false);        


     /*
      * [4] adjust sensor phase so sensor moves positive when Talon LEDs are green
      */
     _rghtFront.setSensorPhase(true);
     _leftFront.setSensorPhase(true);

     /*
      * WPI drivetrain classes defaultly assume left and right are opposite. call
      * this so we can apply + to both sides when moving forward. DO NOT CHANGE
      */
     _diffD.setRightSideInverted(false);
 }

  private void ConfigureMotor(final WPI_TalonFX falcon, final boolean inverted){
    falcon.setInverted(inverted);
    falcon.configClosedloopRamp(Constants.closedDriveVoltageRampRate);
    falcon.configOpenloopRamp(Constants.openDriveVoltageRampRate);
    falcon.setNeutralMode(NeutralMode.Brake);
    falcon.configNeutralDeadband(JoystickConstants.deadzone);
  }

  /*public void drive(final double leftSpeed, final double rightSpeed){
    leftMotors[0].set(ControlMode.PercentOutput, leftSpeed);
    rightMotors[0].set(ControlMode.PercentOutput, rightSpeed);
    SmartDashboard.putString("Percent Output", ControlMode.PercentOutput.toString());
  }*/

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
