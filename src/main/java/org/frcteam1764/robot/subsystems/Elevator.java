// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import org.frcteam2910.common.robot.drivers.LazyTalonFX;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;

import org.frcteam1764.robot.constants.RobotConstants;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem {
  /** Creates a new Elevator. */
  private LazyTalonFX elevatorMotor;

  public Elevator(){
    this.elevatorMotor = new LazyTalonFX(RobotConstants.ELEVATOR_MOTOR);
		this.elevatorMotor.configFactoryDefault();
    this.elevatorMotor.setInverted(true);
    this.elevatorMotor.setNeutralMode(NeutralMode.Brake);
    this.elevatorMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_7_CommStatus, 200);
  }

    public void elevatorOn(double elevatorSpeed) {
      elevatorMotor.set(ControlMode.PercentOutput, elevatorSpeed);
    }
    public void elevatorOff() {
      elevatorMotor.set(ControlMode.PercentOutput, 0);
    }

    @Override
    protected void initDefaultCommand() {
      // TODO Auto-generated method stub
    }
  }
