/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.LimeLightValues;

/**
 * Add your docs here.
 */
public class Limelight extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private LimeLightValues values = new LimeLightValues();

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public void turnLEDOn(){
    
  }
 
  private final double targetHeight = 0.9166666666667; // ll in //Height to center of target in feet
  private final double cameraHeight = 1.0416666666667; // 12 1/2 in //Height to center of camera lense in feet
  private double distance;

  public double findDistanceFixed(){ //Finds the distance from the front of the robot to target in feet, assuming fixed position
      distance = (targetHeight - cameraHeight) / Math.tan( LimeLightValues.yDeg * Math.PI/180);
      return distance;

  }
}
