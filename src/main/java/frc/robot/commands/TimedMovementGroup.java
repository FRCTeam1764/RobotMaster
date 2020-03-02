/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Commands.ShooterCommand.ShooterControlMode;
import frc.robot.Commands.TimedDrive.DriveType;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class TimedMovementGroup extends SequentialCommandGroup {
  
  

  public TimedMovementGroup() {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(
          new StartShooter(3075), new TimedDrive(.5, 5, DriveType.STRAIGHT),
          new TimedDrive(-.75, .2, DriveType.STRAIGHT),
          new FeederCommand(0, .6, 1.0, 3075, 5),
          new StartShooter(0), new TimedDrive(-.6, 3, DriveType.STRAIGHT));
   //super(new TimedDrive(.5, 2.3, DriveType.STRAIGHT));
  }
}
