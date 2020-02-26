/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Commands.ShooterCommand.ShooterControlMode;
import frc.robot.Subsystems.TeleopSubsystems.Feeder;
import frc.robot.Subsystems.TeleopSubsystems.Intake;
import frc.robot.Subsystems.TeleopSubsystems.Shooter;

public class FeederCommand extends CommandBase {
  
  Feeder feeder;
  Intake intake;
  Shooter shooter;

  double time=-1;
  Timer timer = new Timer();

  public FeederCommand(double conveyerSpeed, double feederSpeed) {
    feeder = new Feeder(conveyerSpeed, feederSpeed);

    addRequirements(feeder);
  }

  public FeederCommand(double intakeSpeed, double conveyerSpeed, double feederSpeed) {
    feeder = new Feeder(conveyerSpeed, feederSpeed);
    intake = new Intake(intakeSpeed);

    addRequirements(feeder, intake);
  }

  public FeederCommand(double conveyerSpeed, double feederSpeed, float timeDuration) {
    feeder = new Feeder(conveyerSpeed, feederSpeed);
    time = timeDuration;

    addRequirements(feeder);
  }

  public FeederCommand(double intakeSpeed, double conveyerSpeed, double feederSpeed, double shooterSpeed) {
    intake = new Intake(intakeSpeed);
    feeder = new Feeder(conveyerSpeed, feederSpeed);
    shooter = new Shooter(shooterSpeed, ShooterControlMode.STANDARD);

    addRequirements(feeder);
  }

  @Override
  public void initialize() {
    if(time>0){
      timer.start();
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(time>0){
      feeder.timedFeeder(time);
      end(false);
    }
    feeder.conveyerOn();
    feeder.feederOn();
    intake.intake();
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    timer.stop();
    feeder.conveyerStop();
    feeder.feederStop();
    intake.stopIntake();
    shooter.stopShooter();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
