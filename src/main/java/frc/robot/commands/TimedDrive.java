/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class TimedDrive extends CommandBase {
  /**
   * Creates a new TimedDrive.
   */

   double speed;
   double timeDuration;

   public enum DriveType{
     STRAIGHT, TURN
   }

   DriveType driveType;

   Timer timer = new Timer();

  public TimedDrive(double speed, double timeDuration, DriveType driveType) {
    this.speed = speed;
    this.timeDuration = timeDuration;
    this.driveType = driveType;

    addRequirements(Robot.drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(driveType == DriveType.STRAIGHT){
      Robot.drivetrain.diffDrive.arcadeDrive(speed, 0);
    }
    else if(driveType == DriveType.TURN){
      Robot.drivetrain.diffDrive.arcadeDrive(0, speed);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    timer.stop();
    Robot.drivetrain.diffDrive.arcadeDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.get()>timeDuration;
  }
}
