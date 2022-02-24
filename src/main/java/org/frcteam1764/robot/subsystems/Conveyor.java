// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.ControlMode;
import org.frcteam2910.common.robot.drivers.LazyTalonFX;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import org.frcteam1764.robot.constants.RobotConstants;

/** Add your docs here*/
public class Conveyor extends Subsystem {
  private LazyTalonFX conveyorMotor;
  private DigitalInput conveyorBreakBeam;
  private int count;
  public Conveyor(DigitalInput conveyorBreakBeam){
    this.conveyorBreakBeam = conveyorBreakBeam;
    count = 0;
    this.conveyorMotor = new LazyTalonFX(RobotConstants.CONVEYOR_MOTOR);
		this.conveyorMotor.configFactoryDefault();
    this.conveyorMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_7_CommStatus, 51000);
  }

    public void conveyorOn(double conveyorSpeed, boolean override) {
      if(conveyorBreakBeam.get() || override){
        count =0;
        conveyorMotor.set(ControlMode.PercentOutput, conveyorSpeed);
      }
      else if(!conveyorBreakBeam.get() && count<25){
        count++;
        conveyorMotor.set(ControlMode.PercentOutput, conveyorSpeed);
      }
      else{
        conveyorMotor.set(ControlMode.PercentOutput, 0);
      }
    }
    public void conveyorOff() {
      conveyorMotor.set(ControlMode.PercentOutput, 0);
    }

    @Override
    protected void initDefaultCommand() {
      // TODO Auto-generated method stub
    }
  }
