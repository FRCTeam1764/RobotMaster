/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.AnalogInput;

/**
 * Add your docs here.
 */
public class SharpIRSensor {

    AnalogInput irSensor;

    public SharpIRSensor(int port){
        irSensor = new AnalogInput(port);
    }

    public double getVoltage(){
        return irSensor.getVoltage();
    }

    public int getValue(){
        return irSensor.getValue();
    }

}
