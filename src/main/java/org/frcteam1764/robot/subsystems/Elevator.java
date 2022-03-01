// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonFX;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;

import org.frcteam1764.robot.constants.RobotConstants;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase {
  /** Creates a new Elevator. */
  private PWMTalonFX elevatorMotor;
  private DigitalInput elevatorBreakBeam;
  public Elevator(DigitalInput elevatorBreakBeam){
    this.elevatorBreakBeam = elevatorBreakBeam;
    this.elevatorMotor = new PWMTalonFX(RobotConstants.ELEVATOR_MOTOR);
  }

    public void elevatorOn(double elevatorSpeed, boolean override) {
      if(elevatorBreakBeam.get() || override){
        elevatorMotor.set(elevatorSpeed);
      }
      else{
        elevatorMotor.set(0);
      }
    }
    public void elevatorOff() {
      elevatorMotor.set(0);
    }
  }
