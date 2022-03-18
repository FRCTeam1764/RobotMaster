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
    double ty = limelight.getTargetYOffset();
  
    // if(ty <= -13.5) {
    // shooterTopRoller.setShooterTopRollerVelocity(5000);
    // shooter.setShooterVelocity(5000 / 4.8);
    // shooterState.setAssignedVelocity(5000 / 4.8);
    // shooterState.setTopRollerAssignedVelocity(5000);
    // shooter.shoot();
    // shooterTopRoller.shoot();
    // }
     if(ty <= -11 && ty > -13.5) {
      shooterTopRoller.setShooterTopRollerVelocity(4600);
      shooter.setShooterVelocity(4600 / 4.8);
      shooterState.setAssignedVelocity(4600 / 4.8);
      shooterState.setTopRollerAssignedVelocity(4600);
      shooter.shoot();
      shooterTopRoller.shoot();
    }
    else if(ty <= -8.5 && ty > -11) {
      shooterTopRoller.setShooterTopRollerVelocity(4300);
      shooter.setShooterVelocity(4300 / 4.5);
      shooterState.setAssignedVelocity(4300 / 4.5);
      shooterState.setTopRollerAssignedVelocity(4300);
      shooter.shoot();
      shooterTopRoller.shoot();
    }
    else if(ty <= -6 && ty > -8.5) {
      shooterTopRoller.setShooterTopRollerVelocity(3800);
      shooter.setShooterVelocity(3800 / 3.6);
      shooterState.setAssignedVelocity(3800 / 3.6);
      shooterState.setTopRollerAssignedVelocity(3800);
      shooter.shoot();
      shooterTopRoller.shoot();
    }
    else if(ty <= -3 && ty > -6) {
      shooterTopRoller.setShooterTopRollerVelocity(3000);
      shooter.setShooterVelocity(3000 / 2);
      shooterState.setAssignedVelocity(3000 / 2);
      shooterState.setTopRollerAssignedVelocity(3000 );
      shooter.shoot();
      shooterTopRoller.shoot();
    }
    else if(ty <= 3 && ty > -3) {
      shooterTopRoller.setShooterTopRollerVelocity(2400);
      shooter.setShooterVelocity(2400 / 1.5);
      shooterState.setAssignedVelocity(2400 / 1.5);
      shooterState.setTopRollerAssignedVelocity(2400);
      shooter.shoot();
      shooterTopRoller.shoot();
    }
    else {
      shooter.stopShooter();
      shooterTopRoller.stopShooter();
      shooterState.setAssignedVelocity(0);
      shooterState.setTopRollerAssignedVelocity(0);
    }
    System.out.println(ty);
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