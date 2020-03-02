/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.TeleopSubsystems.WheelOfFortune;

public class ExtendControlPanelSolenoidCommand extends CommandBase {
  /**
   * Creates a new ExtendControlPanelSolenoidCommand.
   */

  static boolean extended = false;
  WheelOfFortune wheel = new WheelOfFortune(0);
  public ExtendControlPanelSolenoidCommand() {
    addRequirements(wheel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    //extended = !extended;
    wheel.extendWheel(Value.kForward);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    wheel.extendWheel(Value.kReverse);
    System.out.println("reached here");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
