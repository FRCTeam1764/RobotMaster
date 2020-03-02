/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands.LimelightMovementCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.util.Limelight;

public class LimelightTurn extends CommandBase {
  
  final double kP = 0.11 / 29.8; // (kP/MaxDegreesOutputted)
  final double constantSpeed = 0.03; // Constant added during turning
  final double deadband = 0.3;
  double turningSpeed = 0;
  double xDegrees = 0;

  public LimelightTurn() {
    addRequirements(Robot.drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Limelight.turnLEDOn();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    turningSpeed = kP * Limelight.xDeg;
    turningSpeed += Limelight.xDeg>0 ? constantSpeed : -constantSpeed;

    Robot.drivetrain.diffDrive.arcadeDrive(0, turningSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.drivetrain.stopDrivetrain();
    Limelight.turnLEDOff();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Limelight.getXDeg()<deadband && Limelight.getXDeg()>-deadband;
  }
}
