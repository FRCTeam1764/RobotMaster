package org.frcteam1764.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frcteam1764.robot.state.ShooterState;
import org.frcteam1764.robot.subsystems.Shooter;
import org.frcteam1764.robot.subsystems.ShooterTopRoller;
import org.frcteam2910.common.robot.drivers.Limelight;

public class DistanceShooterCommand extends CommandBase {
  
  Shooter shooter;
  ShooterTopRoller shooterTopRoller;
  ShooterState shooterState;
  Limelight limelight;
//14-17.5
//11-13
  public DistanceShooterCommand(Shooter shooter, ShooterTopRoller shooterTopRoller, Limelight limelight, ShooterState shooterState) {
    this.shooter = shooter;
    this.shooterTopRoller = shooterTopRoller;
    this.shooterState = shooterState;
    this.limelight = limelight;
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double ty = shooterState.getShooterDistance();
  
    if(shooterState.getShooterDistance() != 0){
      if(ty <= 3 && ty >= -17) {
        double shooterSpeed = shooterState.shooterInterpolator.getInterpolatedValue(ty);
        double shooterTopRollerSpeed = shooterState.shooterTopRollerInterpolator.getInterpolatedValue(ty);
        shooterTopRoller.setShooterTopRollerVelocity(shooterTopRollerSpeed);
        shooter.setShooterVelocity(shooterSpeed);
        shooterState.setAssignedVelocity(shooterSpeed);
        shooterState.setTopRollerAssignedVelocity(shooterTopRollerSpeed);
        shooter.shoot();
        shooterTopRoller.shoot();
      }
      else {
        shooter.stopShooter();
        shooterTopRoller.stopShooter();
        shooterState.setAssignedVelocity(0);
        shooterState.setTopRollerAssignedVelocity(0);
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stopShooter();
    shooterTopRoller.stopShooter();
    shooterState.setAssignedVelocity(0);
    shooterState.setTopRollerAssignedVelocity(0);
    shooterState.setShooterDistance(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}