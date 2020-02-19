/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.constants.FieldDimensionConstants;
import frc.robot.constants.RobotDimensionConstants;

/**
 * Add your docs here.
 */
public class Limelight {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  public static double xDeg;
  public static double yDeg;
  public static double area;
  public static double skew;
  public static boolean hasTarget;
  public static boolean ledOn;

  public static NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");

  public static double getXDeg(){
    // SmartDashboard.putNumber("LimelightXDeg", limelightTable.getEntry("tx").getDouble(0));
      return limelightTable.getEntry("tx").getDouble(0);
  }

  public static double getYDeg(){
      return limelightTable.getEntry("ty").getDouble(0);
  }

  public static double getSkew(){
    return limelightTable.getEntry("ts").getDouble(0);
  }

  public static double getArea(){
    return limelightTable.getEntry("ta").getDouble(0);
  }

  public static boolean hasTarget(){
    return Boolean.parseBoolean(limelightTable.getEntry("tv").getString("0"));
  }

  public static void turnLEDOn(){
    limelightTable.getEntry("ledMode").setNumber(3);
  }

  public static void turnLEDOff(){
    limelightTable.getEntry("ledMode").setNumber(1);
  }

  public static boolean isLEDOn(){
    return limelightTable.getEntry("ledMode").getValue().toString().equals("3")? true : false;
  }

  public static boolean isLEDOff(){
    return limelightTable.getEntry("ledMode").getValue().toString().equals("1")? true : false;
  }
 
  private static double distance = 0;
  
  final static int delay = 400; //in milliseconds


  /** 
   *  Finds the distance from the target to the front of the robot in inches, assuming the target is in a fixed position.
   */

  public static double getDistanceFixed() throws InterruptedException { // Finds the distance from the front of the robot to
                                                                 // target in inches, assuming fixed position
      turnLEDOn();
      Thread.sleep(delay);
      distance = FieldDimensionConstants.POWER_PORT_OUTER_PORT_CENTER_HEIGHT;
      distance -= RobotDimensionConstants.LIMELIGHT_HEIGHT_FROM_GROUND; 
      distance /= Math.tan((RobotDimensionConstants.LIMELIGHT_CAMERA_ANGLE + getYDeg()) * (Math.PI/180));
      distance -= RobotDimensionConstants.LIMELIGHT_DISTANCE_FROM_FRONT;
      turnLEDOff();
      return distance;
  }

  static double angle = 0;

  public static double getAngle() throws InterruptedException {
    turnLEDOn();
    Thread.sleep(delay);
    angle = getXDeg();
      turnLEDOff();
      return angle;
  }


  // only used within testing
  public static void victoryFlash() throws InterruptedException {
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
