/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems.TeleopSubsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.EncoderType;
import com.revrobotics.SparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.PortConstants;

public class Climber extends SubsystemBase {
  /**
   * Creates a new Climber.
   */

   Solenoid leftSolenoid = new Solenoid(PortConstants.LEFT_CLIMBER_SOLENOID_PORT);
   Solenoid rightSolenoid = new Solenoid(PortConstants.RIGHT_CLIMBER_SOLENOID_PORT);

   CANSparkMax leftWinchMotor = new CANSparkMax(PortConstants.LEFT_CLIMBER_WINCH_MOTOR, MotorType.kBrushless);
   CANSparkMax rightWinchMotor = new CANSparkMax(PortConstants.RIGHT_CLIMBER_WINCH_MOTOR, MotorType.kBrushless);

   CANEncoder winchEncoder = leftWinchMotor.getEncoder(EncoderType.kHallSensor, 42);

   boolean mechanismOn;

   public enum ClimberControlType{
     PNEUMATICS, WINCH
   }

   ClimberControlType climberControlType;

  public Climber(boolean mechanismOn, ClimberControlType climberControlType){
    this.mechanismOn = mechanismOn;
    this.climberControlType = climberControlType;

    rightWinchMotor.follow(leftWinchMotor);

    leftWinchMotor.getEncoder().setPosition(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void extendPneumatics(){
    leftSolenoid.set(mechanismOn);
    rightSolenoid.set(mechanismOn);
  }

  public void spinWinchMotors(boolean mechanismOn){
    leftWinchMotor.set(mechanismOn ? .5 : -.5);
  }

  public void stopWinchMotors(){
    leftWinchMotor.set(0);
  }
}
