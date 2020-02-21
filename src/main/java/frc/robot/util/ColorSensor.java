/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */
public class ColorSensor {
    static ColorSensorV3 sensor = new ColorSensorV3(I2C.Port.kOnboard);
  
    static ColorMatch match = new ColorMatch();
  
    static ColorMatchResult result;
    
    static Color blueTarget = ColorMatch.makeColor(0.239, 0.477, 0.278);
    static Color greenTarget = ColorMatch.makeColor(0.25, 0.497 , 0.25);
    static Color redTarget = ColorMatch.makeColor(0.304, 0.46, 0.227);
    static Color yellowTarget = ColorMatch.makeColor(0.29, 0.5, 0.2);


    public static ColorMatchResult getDetectedColor(){
        Color detectedColor = getRawColor();

        result = match.matchClosestColor(detectedColor);
        return result;
    }


    public static enum ColorType{
        BLUE,
        RED,
        GREEN,
        YELLOW,
        UNKNOWN

    }

    public static void changeColorMatch(ColorType color, double r, double g, double b){
        if(color == ColorType.BLUE){
            blueTarget = ColorMatch.makeColor(r, g, b);
        }
        else if(color == ColorType.RED){
            redTarget = ColorMatch.makeColor(r, g, b);
        }
        else if(color == ColorType.GREEN){
            greenTarget = ColorMatch.makeColor(r, g, b);
        }
        else if(color == ColorType.YELLOW){
            yellowTarget = ColorMatch.makeColor(r, g, b);
        }
    }

    public static String getColorString(){
        String colorString;

        Color detectedColor = getRawColor();

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

        return colorString;
    }

    public static ColorType getColorType(){
        ColorType colorType;

        Color detectedColor = getRawColor();

        result = match.matchClosestColor(detectedColor);

        if ( result.color == blueTarget) {
          colorType = ColorType.BLUE;
        } else if (result.color == redTarget) {
          colorType = ColorType.RED;
        } else if (result.color == greenTarget) {
          colorType = ColorType.GREEN;
        } else if (result.color == yellowTarget) {
          colorType = ColorType.YELLOW;
        } else {
          colorType = ColorType.UNKNOWN;
        }

        return colorType;
    }

    public static ColorType getControlPanelRequiredColor(){
      String gameData;
      gameData = DriverStation.getInstance().getGameSpecificMessage();
      if(gameData.length() > 0)
      {
        switch (gameData.charAt(0))
        {
          case 'B' :
            return ColorType.BLUE;
          case 'G' :
            return ColorType.GREEN;
          case 'R' :
            return ColorType.RED;
          case 'Y' :
            return ColorType.YELLOW;
          default :
            return ColorType.UNKNOWN; 
        }
      } else {
          return ColorType.UNKNOWN; 
      }
    }

    public static Color getRawColor(){
        return sensor.getColor();
    }

    public static void displayOnSmartDashboard(){
        Color detectedColor = getRawColor();

        SmartDashboard.putNumber("Red", detectedColor.red);
        SmartDashboard.putNumber("Green", detectedColor.green);
        SmartDashboard.putNumber("Blue", detectedColor.blue);
        SmartDashboard.putNumber("IR", sensor.getIR());
        SmartDashboard.putNumber("Proximity", sensor.getProximity());

        SmartDashboard.putNumber("Confidence", result.confidence);
        SmartDashboard.putString("Detected Color", getColorString());
    }
}
