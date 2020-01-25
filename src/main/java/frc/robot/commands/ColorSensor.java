/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

public class ColorSensor extends Command {
  public ColorSensor() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }


  ColorSensorV3 sensor = new ColorSensorV3(I2C.Port.kOnboard);

  Color detectedColor;

  ColorMatch match = new ColorMatch();

  ColorMatchResult result;

  String colorString;
  
  final Color blueTarget = ColorMatch.makeColor(0.239, 0.477, 0.278);
  final Color greenTarget = ColorMatch.makeColor(0.25, 0.497 , 0.25);
  final Color redTarget = ColorMatch.makeColor(0.304, 0.46, 0.227);
  final Color yellowTarget = ColorMatch.makeColor(0.29, 0.5, 0.2);

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {

    match.addColorMatch(blueTarget);
    match.addColorMatch(greenTarget);
    match.addColorMatch(redTarget);
    match.addColorMatch(yellowTarget);
    
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

    detectedColor = sensor.getColor();

        result = match.matchClosestColor(detectedColor);

        if ( result.color == blueTarget) {
          colorString = "Blue";
        } else if (result.color == redTarget) {
          colorString = "Red";
        } else if (result.color == greenTarget) {
          colorString = "Green";
        } else if (result.color == yellowTarget) {
          colorString = "Yellow";
        } else {
          colorString = "Unknown";
        }

        

    SmartDashboard.putNumber("Red", detectedColor.red);
    SmartDashboard.putNumber("Green", detectedColor.green);
    SmartDashboard.putNumber("Blue", detectedColor.blue);
    SmartDashboard.putNumber("IR", sensor.getIR());
    SmartDashboard.putNumber("Proximity", sensor.getProximity());

    SmartDashboard.putNumber("Confidence", result.confidence);
    SmartDashboard.putString("Detected Color", colorString);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
