// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import org.frcteam1764.robot.constants.RobotConstants;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem {
  /** Creates a new Elevator. */
  private WPI_TalonFX ElevatorMotor = new WPI_TalonFX(RobotConstants.ELEVATOR_MOTOR);

  public Elevator(){
    
  }

    public void elevatorOn(double elevatorSpeed) {
      ElevatorMotor.set(ControlMode.PercentOutput, elevatorSpeed);
    }
    public void elevatorOff() {
      ElevatorMotor.set(ControlMode.PercentOutput, 0);
    }

    @Override
    protected void initDefaultCommand() {
      // TODO Auto-generated method stub
    }
  }