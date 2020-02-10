/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands.LimelightMovement;

import java.util.TimerTask;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Robot;
import frc.robot.util.Limelight;

/**
 * Add your docs here.
 */


 //Only made for timer in LimelightDrive
public class StopRobot extends TimerTask{

    public void run() {
        Robot.lldrive.isFinished();
        DriverStation.reportError("reached timer", true);
        try {
           Limelight.victoryFlash();
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          Robot.lldrive.travelledToTarget = true;

    }
}

