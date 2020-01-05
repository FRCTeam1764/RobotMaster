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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

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
  private WPI_TalonSRX[] leftMotors = new WPI_TalonSRX[leftCanIDs.length];
  private WPI_TalonSRX[] rightMotors = new WPI_TalonSRX[rightCanIDs.length];
  public AHRS ahrs;
  double rotateToAngleRate;
  public DifferentialDrive drive;
  double angle;
  double rotation = 0;

  //PID
  static final double kP = 0.004;

  static final double kToleranceDegrees = 2.0f;

  public Drivetrain(){
    for (int i= 0; i < leftCanIDs.length; i++){
      leftMotors[i] = new WPI_TalonSRX(leftCanIDs[i]);
      if (i != 0){
        leftMotors[i].follow(leftMotors[0]);
      }
      ConfigureMotor(leftMotors[i], true);
    }
    for (int i= 0; i < rightCanIDs.length; i++){
      leftMotors[i] = new WPI_TalonSRX(rightCanIDs[i]);
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

  private void ConfigureMotor(WPI_TalonSRX falcon, boolean inverted){
    falcon.setInverted(inverted);
    falcon.enableVoltageCompensation(true);
    falcon.configClosedloopRamp(Constants.closedDriveVoltageRampRate);
    falcon.configOpenloopRamp(Constants.openDriveVoltageRampRate);
    falcon.setNeutralMode(NeutralMode.Brake);
    falcon.configNeutralDeadband(JoystickConstants.deadzone);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
