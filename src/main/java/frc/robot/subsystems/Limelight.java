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

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public void turnLEDOn(){
    LimeLightValues.limelightTable.getEntry("ledMode").setNumber(3);
  }

  public void turnLEDOff(){
    LimeLightValues.limelightTable.getEntry("ledMode").setNumber(1);
  }

  public boolean isLEDOn(){
    return LimeLightValues.limelightTable.getEntry("ledMode").getValue().toString().equals("3")? true : false;
  }

  public boolean isLEDOff(){
    return LimeLightValues.limelightTable.getEntry("ledMode").getValue().toString().equals("1")? true : false;
  }
 
  private final double targetHeight = 78+2.5+4.75; //May be inaccurate //Height to center of target in inches (no skew) //Note: remeasure
  private final double cameraHeight = 13;//+21; //Height to center of camera lense in inches
  private final double cameraAngle = 19.05; //Angle camera is mounted at in degrees
  private final double frontToCamera = 8; //Distance from front of robot to camera, in inches
  private double distance = 0;
  
  final int delay = 500; //in milliseconds

  public double getDistanceFixed() throws InterruptedException { // Finds the distance from the front of the robot to
                                                                 // target in inches, assuming fixed position
      turnLEDOn();
      Thread.sleep(delay+300);
      distance = targetHeight - cameraHeight; 
      distance /= Math.tan((cameraAngle + LimeLightValues.getYDeg()) * (Math.PI/180));
      distance -= frontToCamera;
      turnLEDOff();
      return distance;
  }

  double angle = 0;

  public double getAngle() throws InterruptedException {
      turnLEDOn();
      Thread.sleep(delay+300);
      angle = LimeLightValues.getXDeg();
      turnLEDOff();
      return angle;
  }


  // only used within testing
  public void victoryFlash() throws InterruptedException {
    turnLEDOn();
    Thread.sleep(150);
    turnLEDOff();
    Thread.sleep(50);
    turnLEDOn();
    Thread.sleep(150);
    turnLEDOff();
    Thread.sleep(50);
    turnLEDOn();
    Thread.sleep(50);
    turnLEDOff();
    Thread.sleep(50);
    turnLEDOn();
    Thread.sleep(50);
    turnLEDOff();
    Thread.sleep(50);
    turnLEDOn();
    Thread.sleep(50);
    turnLEDOff();
  }
}
