package org.frcteam1764.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frcteam1764.robot.state.RobotState;
import org.frcteam1764.robot.state.ShooterState;
import org.frcteam1764.robot.subsystems.Shooter;

public class ShooterCommand extends CommandBase {
  
  Shooter shooter;
  ShooterState robotState;

  public ShooterCommand(double shooterMotorSpeed, ShooterState robotState) {
    shooter = new Shooter(shooterMotorSpeed);
    this.robotState = robotState;
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
      robotState.addToTimer();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stopShooter();
    robotState.clearTimer();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (robotState.getBallCount() == 0 || robotState.getTimer() > 200);
  }
}