package org.frcteam1764.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frcteam1764.robot.state.ShooterState;
import org.frcteam1764.robot.subsystems.Shooter;

public class AutoShooterCommand extends CommandBase {
  
  Shooter shooter;
  ShooterState shooterState;
  double shooterSpeed;
  int initialShotCount;
  boolean ballIsPresent;

  public AutoShooterCommand(Shooter shooter, double shooterSpeed, ShooterState shooterState, int initialShotCount) {
    this.shooter = shooter;
    this.shooterState = shooterState;
    this.shooterSpeed = shooterSpeed;
    this.initialShotCount = initialShotCount;
    this.ballIsPresent = false;
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shooter.setShooterVelocity(shooterSpeed);
    shooterState.setShotCount(initialShotCount);
    shooterState.setAssignedVelocity(shooterSpeed);
    shooter.shoot();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(ballIsPresent && !shooter.ballIsPresent()){
      shooterState.addShotCount();
    }
    ballIsPresent = shooter.ballIsPresent();
    shooterState.addToTimer();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stopShooter();
    shooterState.clearTimer();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (shooterState.getShotCount() == 2 || shooterState.getTimer() > 50);
  }
}