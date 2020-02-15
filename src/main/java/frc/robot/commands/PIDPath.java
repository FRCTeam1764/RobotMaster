/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

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
  public PIDPath() {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(new PIDDrive(12, MovementType.STRAIGHT), new PIDDrive(90, MovementType.TURN), new PIDDrive(5, MovementType.SHOOT, 5));
  }
}
