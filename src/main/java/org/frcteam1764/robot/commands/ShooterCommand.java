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

  public ShooterCommand(double shooterMotorSpeed, ShooterControlMode controlMode, double timeDuration) {
    shooter = new Shooter(shooterMotorSpeed, controlMode, timeDuration);

    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Shooter.shooterOn = !Shooter.shooterOn;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(shooter.timeDuration >0){
      shooter.shoot(shooter.timeDuration);
      end(false);
    }
    else{
      shooter.shoot();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stopShooter();
    Shooter.shooterOn = !Shooter.shooterOn;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}