package org.frcteam1764.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frcteam1764.robot.state.ShooterState;
import org.frcteam1764.robot.subsystems.Shooter;
import org.frcteam1764.robot.subsystems.ShooterTopRoller;

public class ShooterCommand extends CommandBase {
  
  Shooter shooter;
  ShooterTopRoller shooterTopRoller;
  ShooterState shooterState;
  double shooterTopRollerSpeed;
  double shooterRatio;
//14-17.5
//11-13
  public ShooterCommand(Shooter shooter, ShooterTopRoller shooterTopRoller, double shooterTopRollerSpeed, ShooterState shooterState) {
    this.shooter = shooter;
    this.shooterTopRoller = shooterTopRoller;
    this.shooterState = shooterState;
    this.shooterTopRollerSpeed = shooterTopRollerSpeed;
    this.shooterRatio = 4.8; //3.2 //4.1 //4.8
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    shooterTopRoller.setShooterTopRollerVelocity(shooterTopRollerSpeed); //3700 4500
    shooter.setShooterVelocity(shooterTopRollerSpeed / shooterRatio); //1150 1000
    shooterState.setAssignedVelocity(shooterTopRollerSpeed);
    shooterState.setTopRollerAssignedVelocity(shooterTopRollerSpeed / shooterRatio);
    shooter.shoot();
    shooterTopRoller.shoot();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stopShooter();
    shooterTopRoller.stopShooter();
    shooterState.setAssignedVelocity(0);
    shooterState.setTopRollerAssignedVelocity(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}