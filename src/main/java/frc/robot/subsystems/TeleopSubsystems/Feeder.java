/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems.TeleopSubsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.PortConstants;

public class Feeder extends SubsystemBase {
  WPI_TalonSRX feeder = new WPI_TalonSRX(PortConstants.FEEDER_MOTOR_PORT);
  WPI_TalonSRX conveyer = new WPI_TalonSRX(PortConstants.CONVEYER_MOTOR_PORT);


  double conveyerSpeed;
  double feederSpeed;

  public Feeder(double conveyerMotorSpeed, double feederMotorSpeed) {
    conveyerSpeed = conveyerMotorSpeed;
    feederSpeed = feederMotorSpeed;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void conveyerOn(){
    conveyer.set(ControlMode.PercentOutput, conveyerSpeed);
  }

  public void feederOn(){
    feeder.set(ControlMode.PercentOutput, feederSpeed);
  }

  public void timedFeeder(double timeDuration){
    Timer timer = new Timer();
    timer.start();

    while(timer.get()<timeDuration){
      conveyer.set(ControlMode.PercentOutput, conveyerSpeed);
      feeder.set(ControlMode.PercentOutput, conveyerSpeed);
    }

    conveyerStop();
  }
  


  public void conveyerStop() {
    conveyer.set(ControlMode.PercentOutput, 0);
  }

  public void feederStop() {
    feeder.set(ControlMode.PercentOutput, 0);
  }
}
