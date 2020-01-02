/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.constants.Constants;
import frc.robot.constants.JoystickConstants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANPIDController;

/**
 * Add your docs here.
 */
public class Drivetrain extends Subsystem implements PIDOutput{
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private int[] leftCanIDs = new int[]{11, 12, 13};
  private int[] rightCanIDs = new int[]{14, 15, 16};
  private CANSparkMax[] leftMotors = new CANSparkMax[leftCanIDs.length];
  private CANSparkMax[] rightMotors = new CANSparkMax[rightCanIDs.length];
  PIDController turnController;
  AHRS ahrs;
  double rotateToAngleRate;
  DifferentialDrive drive;
  double angle;
  CANPIDController leftController;
  CANPIDController rightController;

  //PID
  static final double kP = 0.03;
  static final double kI = 0.00;
  static final double kD = 0.00;
  static final double kF = 0.00;

  static final double kToleranceDegrees = 2.0f;

  public Drivetrain(){
    for (int i= 0; i < leftCanIDs.length; i++){
      leftMotors[i] = new CANSparkMax(leftCanIDs[i], MotorType.kBrushless);
      if (i != 0){
        leftMotors[i].follow(leftMotors[0]);
      }
      ConfigureMotor(leftMotors[i], true);
    }
    for (int i= 0; i < rightCanIDs.length; i++){
      rightMotors[i] = new CANSparkMax(rightCanIDs[i], MotorType.kBrushless);
      if (i != 0){
        rightMotors[i].follow(rightMotors[0]);
      }
      ConfigureMotor(rightMotors[i], true);
    }

    ahrs = new AHRS(SPI.Port.kMXP);
    turnController = new PIDController(kP, kI, kD, ahrs, this);
    
    turnController.setInputRange(-180f, 180f);
    turnController.setOutputRange(-1f, 1f);
    turnController.setAbsoluteTolerance(kToleranceDegrees);
    turnController.setContinuous(true);

    drive = new DifferentialDrive(leftMotors[0], rightMotors[0]);

    leftController = new CANPIDController(leftMotors[0]);
    rightController = new CANPIDController(rightMotors[0]);
  } 

  public void turnToAngle(double angle){
    turnController.setSetpoint(90);
    turnController.enable();
    //drive.arcadeDrive(0, angle);
  }

  public void drive(double rightSpeed, double leftSpeed){
    double newRightSpeed = isWithinDeadzone(rightSpeed) ? rightSpeed : 0;
    double newLeftSpeed = isWithinDeadzone(leftSpeed) ? leftSpeed : 0;

    drive.tankDrive(newLeftSpeed, newRightSpeed);
  }

  private boolean isWithinDeadzone(double speed){
    return Math.abs(speed) > JoystickConstants.deadzone;
  }

  private void ConfigureMotor(CANSparkMax sparkMax, boolean inverted){
    sparkMax.setInverted(inverted);
    sparkMax.enableVoltageCompensation(12.0);
    sparkMax.setClosedLoopRampRate(Constants.closedDriveVoltageRampRate);
    sparkMax.setOpenLoopRampRate(Constants.openDriveVoltageRampRate);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  @Override
  public void pidWrite(double output){
    rotateToAngleRate = output;
  }
}
