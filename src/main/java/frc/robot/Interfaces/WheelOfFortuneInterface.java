/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Interfaces;

import edu.wpi.first.wpilibj.buttons.Button;

/**
 * Add your docs here.
 */
public interface WheelOfFortuneInterface {
    public void brake(); //Motor comes to a complete stop
    public void moveWheel(); //Moves motor at a slow yet steady speed
    public void setSelectedColor(Button btn); //Sets the wanted color for the wheel 
    public String getSelectedColor(); //Gets selected color in the form of a string



}
