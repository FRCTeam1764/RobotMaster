package org.frcteam1764.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frcteam1764.robot.state.ShooterState;
import org.frcteam1764.robot.subsystems.Shooter;

public class ShooterCommand extends CommandBase {
  
  Shooter shooter;
  ShooterState shooterState;
  double shooterSpeed;

  public ShooterCommand(Shooter shooter, double shooterSpeed, ShooterState shooterState) {
    this.shooter = shooter;
    this.shooterState = shooterState;
    this.shooterSpeed = shooterSpeed;
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shooter.setShooterVelocity(shooterSpeed);
    shooterState.setAssignedVelocity(shooterSpeed);
    shooter.shoot();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
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