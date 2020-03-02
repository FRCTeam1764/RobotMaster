/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems.TeleopSubsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.constants.PortConstants;

public class Climber extends SubsystemBase {

   CANSparkMaxLowLevel leftWinchMotor = new CANSparkMax(PortConstants.LEFT_CLIMBER_WINCH_MOTOR, MotorType.kBrushless);
   CANSparkMaxLowLevel rightWinchMotor = new CANSparkMax(PortConstants.RIGHT_CLIMBER_WINCH_MOTOR, MotorType.kBrushless);

   boolean mechanismOn;

   public enum ClimberControlType{
     PNEUMATICS, WINCH
   }

   ClimberControlType climberControlType;

  public Climber(boolean mechanismOn, ClimberControlType climberControlType){
    this.mechanismOn = mechanismOn;
    this.climberControlType = climberControlType;

    leftWinchMotor.restoreFactoryDefaults();
    rightWinchMotor.restoreFactoryDefaults();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void extendPneumatics(){
    Value on = mechanismOn ? Value.kForward : Value.kReverse;
    Robot.climberSolenoid.set(on);
  }

  public void spinWinchMotors(boolean mechanismOn){
    double speed = mechanismOn ? .4 : -.4;
    leftWinchMotor.set(-speed);
    rightWinchMotor.set(speed);
  }

  public void stopWinchMotors(){
    leftWinchMotor.set(0);
    rightWinchMotor.set(0);
  }
}
