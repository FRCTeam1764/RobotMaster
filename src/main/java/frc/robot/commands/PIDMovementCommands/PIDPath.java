/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands.PIDMovementCommands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Commands.PIDMovementCommands.PIDDrive;
import frc.robot.Commands.PIDMovementCommands.PIDDrive.MovementType;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class PIDPath extends SequentialCommandGroup {
  /**
   * Creates a new PIDPath.
   */

  static int robotPos = DriverStation.getInstance().getLocation();

  static PIDDrive[] leftPath = new PIDDrive[] { 
    new PIDDrive(12, MovementType.STRAIGHT),
    new PIDDrive(90, MovementType.TURN), 
    new PIDDrive(5, MovementType.SHOOT, 5) };

  static PIDDrive[] middlePath = new PIDDrive[]{
    new PIDDrive(-24, MovementType.STRAIGHT)
  };

  static PIDDrive[] rightPath = new PIDDrive[]{
    new PIDDrive(90, MovementType.TURN),
    new PIDDrive(-90, MovementType.TURN),
    new PIDDrive(4, MovementType.SHOOT, 3)
  };

  public PIDPath() {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
   /* super(robotPos == 1 ? leftPath : 
          robotPos == 2 ? middlePath:
                          rightPath);*/

    super(new PIDDrive(60, MovementType.STRAIGHT));
  }
}
