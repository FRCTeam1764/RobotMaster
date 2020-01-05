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

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;;

/**
 * Add your docs here.
 */
public class Drivetrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private int[] rightCanIDs = new int[]{11, 12, 13};
  private int[] leftCanIDs = new int[]{ 14, 15, 16 };
  private WPI_TalonFX[] leftMotors = new WPI_TalonFX[leftCanIDs.length];
  private WPI_TalonFX[] rightMotors = new WPI_TalonFX[rightCanIDs.length];
  double rotateToAngleRate;
  double angle;
  double rotation = 0;
  public DifferentialDrive drive;

  public Drivetrain(){
    for (int i= 0; i < leftCanIDs.length; i++){
      leftMotors[i] = new WPI_TalonFX(leftCanIDs[i]);
      if (i != 0){
        leftMotors[i].follow(leftMotors[0]);
      }
        ConfigureMotor(leftMotors[i], true);
    }
    for (int i= 0; i < rightCanIDs.length; i++){
      rightMotors[i] = new WPI_TalonFX(rightCanIDs[i]);
      if (i != 0){
        rightMotors[i].follow(rightMotors[0]);
      }
      ConfigureMotor(rightMotors[i], true);
    }
  } 

  private void ConfigureMotor(WPI_TalonFX falcon, boolean inverted){
    falcon.setInverted(inverted);
    falcon.configClosedloopRamp(Constants.closedDriveVoltageRampRate);
    falcon.configOpenloopRamp(Constants.openDriveVoltageRampRate);
    falcon.setNeutralMode(NeutralMode.Brake);
    falcon.configNeutralDeadband(JoystickConstants.deadzone);
  }

  public void drive(double leftSpeed, double rightSpeed){
    leftMotors[0].set(ControlMode.PercentOutput, leftSpeed);
    rightMotors[0].set(ControlMode.PercentOutput, rightSpeed);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
