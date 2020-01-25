/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class FieldPositioning extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  public static double[] robotPos = new double[3]; // robotPos[0] = x-pos, robotPos[1] = y-pos, robotPos[2] = Angle orientation

  public FieldPositioning(int startingPos){ // 0=left, 1=middle, 2=right, driverstation's perspective
    switch(startingPos){
      case 0: //left
        robotPos[0] = 0;
        robotPos[1] = 0;
        robotPos[2] = 0;
        break;
      case 1: //middle
        robotPos[0] = 1;
        robotPos[1] = 1;
        robotPos[2] = 1;
        break;
      default: //right
        robotPos[0] = 2;
        robotPos[1] = 2;
        robotPos[2] = 2;
        break;

    }

  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  double degToRad(double num){
    return num * (Math.PI/180);
  }

  double radToDeg(double num){
    return num / (Math.PI/180);
  }

  void updateX(double deltaPos){
    robotPos[0] += deltaPos*Math.cos(degToRad(robotPos[2]));
  }
  void updateY(double deltaPos){
    robotPos[0] += deltaPos*Math.sin(degToRad(robotPos[2]));
  }

  //Always change angle first before x and y
  void updateAngle(double deltaAngle){
    robotPos[2] += deltaAngle;

    while(robotPos[2]<0){ //Makes it not negative
      robotPos[2] += 360;
    }

    robotPos[2] = robotPos[2]%360;
  }
}
