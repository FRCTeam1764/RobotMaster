// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.ControlMode;
import org.frcteam2910.common.robot.drivers.LazyTalonFX;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import org.frcteam1764.robot.constants.RobotConstants;

/** Add your docs here*/
public class Conveyor extends Subsystem {
  private LazyTalonFX conveyorMotor;

  public Conveyor(){
    this.conveyorMotor = new LazyTalonFX(RobotConstants.CONVEYOR_MOTOR);
		this.conveyorMotor.configFactoryDefault();
    this.conveyorMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_7_CommStatus, 200);
  }

    public void conveyorOn(double conveyorSpeed) {
      conveyorMotor.set(ControlMode.PercentOutput, conveyorSpeed);
    }
    public void conveyerOff() {
      conveyorMotor.set(ControlMode.PercentOutput, 0);
    }

    @Override
    protected void initDefaultCommand() {
      // TODO Auto-generated method stub
    }
  }
