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
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.SPI;

import com.kauailabs.navx.frc.AHRS;

/**
 * Add your docs here.
 */
public class Drivetrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private int[] leftCanIDs = new int[]{11, 12, 13};
  private int[] rightCanIDs = new int[]{14, 15, 16};
  private CANSparkMax[] leftMotors = new CANSparkMax[leftCanIDs.length];
  private CANSparkMax[] rightMotors = new CANSparkMax[rightCanIDs.length];
  public AHRS ahrs;
  double rotateToAngleRate;
  DifferentialDrive drive;
  double angle;
  double rotation = 0;

  //PID
  static final double kP = 0.004;

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

    drive = new DifferentialDrive(leftMotors[0], rightMotors[0]);
  } 

  public void turnToAngle(double angle) {
    double error = angle - ahrs.getAngle();
    SmartDashboard.putNumber("Error", error);
    SmartDashboard.putNumber("GyroAngle", ahrs.getAngle());
    SmartDashboard.putBoolean("GryoIsCalibrating", ahrs.isCalibrating());

    if (Math.abs(error) > kToleranceDegrees && !ahrs.isCalibrating()){
      this.rotation = error;
      move();
     //this.rotation = 0;
     //ahrs.resetDisplacement();
      return;
    } else {
      this.rotation = 0;
      move();
      //ahrs.reset();
      return;
    }
  }

  public void move() {
    //Math.pow((.17*i), 2);
    //drive.tankDrive(0 + Math.pow((kP * this.rotation), 2), 0 - Math.pow((kP * this.rotation), 2));
    SmartDashboard.putBoolean("HitMove", true);
    SmartDashboard.putNumber("Rotation", this.rotation);
    double rotation =  kP * this.rotation;

    double newRotation2 = rotation + getVoltageCompensation(rotation);
    //SmartDashboard.putNumber("newRotation2", newRotation2);

    double volatageLimit =  newRotation2 > 1 ? 1 : newRotation2; 
    SmartDashboard.putNumber("newRotation", volatageLimit);

    SmartDashboard.putNumber("Left", 0 - volatageLimit);
    SmartDashboard.putNumber("Right", 0 + volatageLimit);

    drive.tankDrive(0 - volatageLimit, 0 + volatageLimit);
  }

  public double getVoltageCompensation(double voltage){
    if (voltage > 0 && voltage <= 20)
      return .23;

    if (voltage < 0 && voltage > -20)
      return -.23;

    return 0;
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
    sparkMax.setIdleMode(IdleMode.kBrake);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
