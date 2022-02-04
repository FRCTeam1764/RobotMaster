package org.frcteam1764.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frcteam1764.robot.subsystems.Shooter;

public class ShooterCommand extends CommandBase {
  
  Shooter shooter;

  public enum ShooterControlMode{
    PID,
    STANDARD,
    TIMED
  }

  public ShooterCommand(double shooterMotorSpeed, ShooterControlMode controlMode) {
    shooter = new Shooter(shooterMotorSpeed, controlMode);

    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      shooter.shoot();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stopShooter();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}