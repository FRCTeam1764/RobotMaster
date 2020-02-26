/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems.TeleopSubsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.PortConstants;

public class Feeder extends SubsystemBase {
  WPI_TalonSRX leftFeeder = new WPI_TalonSRX(PortConstants.LEFT_FEEDER_MOTOR_PORT);
  WPI_TalonSRX rightFeeder = new WPI_TalonSRX(PortConstants.RIGHT_FEEDER_MOTOR_PORT);
  CANSparkMax conveyer = new CANSparkMax(PortConstants.CONVEYER_MOTOR_PORT, MotorType.kBrushless);
  int count=0;


  double conveyerSpeed;
  double feederSpeed;

  public Feeder(double conveyerSpeed, double feederSpeed) {
    this.conveyerSpeed = conveyerSpeed;
    this.feederSpeed = feederSpeed;

    rightFeeder.follow(leftFeeder);

    leftFeeder.setInverted(true);
    rightFeeder.setInverted(false);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void conveyerOn(){
    if(count<4 && conveyerSpeed > 0.0){
      conveyer.set(-conveyerSpeed);
      count++;
    }
    else{
      conveyer.set(conveyerSpeed);
    }
  }

  public void feederOn(){
    leftFeeder.set(ControlMode.PercentOutput, feederSpeed);
  }

  public void timedFeeder(double timeDuration){
    Timer timer = new Timer();
    timer.start();

    while(timer.get()<timeDuration){
      conveyerOn();
      feederOn();
    }

    timer.stop();
    conveyerStop();
    feederStop();
  }
  


  public void conveyerStop() {
    conveyer.set(0);
  }

  public void feederStop() {
    count=0;
    leftFeeder.set(ControlMode.PercentOutput, 0);
  }
}
