// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import org.frcteam1764.robot.constants.RobotConstants;

/** Add your docs here*/
public class Climber extends Subsystem {
  private WPI_TalonFX climberMotor;

  public Climber(){
    this.climberMotor = new WPI_TalonFX(RobotConstants.CLIMBER_MOTOR);
  }

    public void climberOn(double climberSpeed) {
      climberMotor.set(ControlMode.PercentOutput, climberSpeed);
    }
    public void climberOff() {
      climberMotor.set(ControlMode.PercentOutput, 0);
    }

    @Override
    protected void initDefaultCommand() {
      // TODO Auto-generated method stub
    }
  }
