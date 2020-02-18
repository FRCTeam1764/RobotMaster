/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems.TeleopSubsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.PortConstants;
import frc.robot.util.ColorSensor;
import frc.robot.util.ColorSensor.ColorType;

/**
 * Add your docs here.
 */
public class WheelOfFortune extends SubsystemBase {

  double wheelSpeed;
  Spark wheelMotor = new Spark(PortConstants.CONTROL_PANEL_WHEEL_MOTOR_PORT);
  Solenoid controlPanelWheelExtender = new Solenoid(PortConstants.CONTROL_PANEL_WHEEL_SOLENOID_PORT);

  boolean rotationControlComplete = false;
  boolean positionControlComplete = false;

  public WheelOfFortune(double motorSpeed){
    wheelSpeed=motorSpeed;
  }

  public void stopWheel() {
    wheelMotor.set(0);

  }

  public void moveWheel() {
    wheelMotor.set(wheelSpeed);
  }

  public void extendWheel(boolean shouldExtend){
    controlPanelWheelExtender.set(shouldExtend);
  }

  int count;

  /**
  * Rotates the control panel enough times to count the first detected color 9 times.
  *
  * @param color The color the sensor will use to count up. Recommended to use the first detected color
  */

  public void rotationControl(ColorType color){
    moveWheel();
    if(ColorSensor.getColorType() == color){
      rotationControlCounter();
    }
  }

  public void rotationControlCounter(){
    count++;

    System.out.println("Times Counted Color: " + count);

    if(count>=9){
      rotationControlComplete = true;
    }
  }

  public boolean isRotationControlComplete(){
    return rotationControlComplete;
  }

  public void positionControl(ColorType selectedColor){
    if(ColorSensor.getColorType() != selectedColor){
      moveWheel();
    }
    else{
      stopWheel();
      positionControlComplete = true;
    }
  }

  public boolean isPositionControlComplete(){
    return positionControlComplete;
  }
}
