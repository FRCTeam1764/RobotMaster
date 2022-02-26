// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonFX;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import org.frcteam1764.robot.constants.RobotConstants;

/** Add your docs here*/
public class Conveyor extends SubsystemBase {
  private PWMTalonFX conveyorMotor;
  private DigitalInput conveyorBreakBeam;
  private int count;
  public Conveyor(DigitalInput conveyorBreakBeam){
    this.conveyorBreakBeam = conveyorBreakBeam;
    count = 0;
    this.conveyorMotor = new PWMTalonFX(RobotConstants.CONVEYOR_MOTOR);
    
  }

    public void conveyorOn(double conveyorSpeed, boolean override) {
      if(conveyorBreakBeam.get() || override){
        count =0;
        conveyorMotor.set(conveyorSpeed);
      }
      else if(!conveyorBreakBeam.get() && count < 25){
        count++;
        conveyorMotor.set(conveyorSpeed);
      }
      else{
        conveyorMotor.set(0);
      }
    }
    public void conveyorOff() {
      conveyorMotor.set(0);
    }
  }
