/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems.TeleopSubsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.PortConstants;

public class Feeder extends SubsystemBase {
  WPI_TalonSRX feeder = new WPI_TalonSRX(PortConstants.FEEDER_MOTOR_PORT);
  CANSparkMax conveyer = new CANSparkMax(PortConstants.CONVEYER_MOTOR_PORT, MotorType.kBrushless);


  double conveyerSpeed;
  double feederSpeed;

  public Feeder(double conveyerSpeed, double feederSpeed) {
    this.conveyerSpeed = conveyerSpeed;
    this.feederSpeed = feederSpeed;

    feeder.setInverted(true);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void conveyerOn(){
    conveyer.set(conveyerSpeed);
  }

  public void feederOn(){
    feeder.set(ControlMode.PercentOutput, feederSpeed);
  }

  public void timedFeeder(double timeDuration){
    Timer timer = new Timer();
    timer.start();

    while(timer.get()<timeDuration){
      conveyerOn();
      feederOn();
    }

    conveyerStop();
    feederStop();
  }
  


  public void conveyerStop() {
    conveyer.set(0);
  }

  public void feederStop() {
    feeder.set(ControlMode.PercentOutput, 0);
  }
}
