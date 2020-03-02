///*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems.TeleopSubsystems;

import java.util.Map;//

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.constants.PortConstants;
import frc.robot.util.ColorSensor;
import frc.robot.util.ColorSensor.ColorType;

/**
 * Add your docs here.
 */
public class WheelOfFortune extends SubsystemBase {

  double wheelSpeed;
  //Spark wheelMotor = new Spark(PortConstants.CONTROL_PANEL_WHEEL_MOTOR_PORT);

  //Used for position control; a color's complement is the color 90 degrees from it.
  public static Map<ColorType, ColorType> colorComplements = Map.of(
  ColorType.BLUE, ColorType.RED,
  ColorType.RED, ColorType.BLUE,
  ColorType.GREEN, ColorType.YELLOW,
  ColorType.YELLOW, ColorType.GREEN,
  ColorType.UNKNOWN, ColorType.UNKNOWN
  );

  static boolean rotationControlComplete = false;
  static boolean positionControlComplete = false;

  public WheelOfFortune(double wheelSpeed){
    this.wheelSpeed=wheelSpeed;
  }

  /*public void stopWheel() {
    wheelMotor.set(0);

  }

  public void moveWheel() {
    wheelMotor.set(wheelSpeed);
  }*/

  public void extendWheel(Value value){
    Robot.controlPanelWheelExtender.set(value);
  }

  int count=0;
  boolean recentlyDetected = false;

  /**
  * Rotates the control panel enough times to count the first detected color 9 times.
  *
  * @param color The color the sensor will use to count up. Recommended to use the first detected color
  */

  public void rotationControl(ColorType color){
    //moveWheel();
    if(ColorSensor.getColorType() == color && !recentlyDetected){
      rotationControlCounter();
      recentlyDetected = true;
    }
    else if(ColorSensor.getColorType() != color){
      recentlyDetected = false;
    }

  }

  public void rotationControlCounter(){
    count++;

    System.out.println("Times Counted Color: " + count);

    if(count>6){
      rotationControlComplete = true;
    }
  }

  public boolean isRotationControlComplete(){
    return rotationControlComplete;
  }

  public void positionControl(ColorType selectedColor){
    //Stops at the color's complement to stop the wanted color underneath the sensor
    if(ColorSensor.getColorType() != colorComplements.get(selectedColor)){
     // moveWheel();
    }
    else{
    //  stopWheel();
      positionControlComplete = true;
    }
  }

  public boolean isPositionControlComplete(){
    return positionControlComplete;
  }
}
