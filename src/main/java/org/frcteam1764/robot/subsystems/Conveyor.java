// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

/** Add your docs here. I wont*/
public class Conveyor extends Subsystem {
 WPI_TalonFX CVmotor = new WPI_TalonFX(69420);
 
 double conveyorSpeed;

 public Conveyor(double conveyorSpeed){
  this.conveyorSpeed = conveyorSpeed;
 }

  public void conveyorOn() {
    CVmotor.set(ControlMode.PercentOutput, conveyorSpeed);
  }
  public void conveyerOff() {
    CVmotor.set(ControlMode.PercentOutput, 0);
  }

  @Override
  protected void initDefaultCommand() {
    // TODO Auto-generated method stub

  }
  
}
