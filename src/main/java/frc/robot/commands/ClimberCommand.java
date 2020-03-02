/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.TeleopSubsystems.Climber;
import frc.robot.Subsystems.TeleopSubsystems.Climber.ClimberControlType;

public class ClimberCommand extends CommandBase {
  /**
   * Creates a new ClimberCommand.
   */
  Climber climber;
  boolean mechanismOn;
  ClimberControlType climberControlType;

  Timer timer = new Timer();

  public ClimberCommand(boolean mechanismOn, ClimberControlType climberControlType){
    this.mechanismOn = mechanismOn;
    this.climberControlType = climberControlType;
    climber = new Climber(mechanismOn, climberControlType);
    
    addRequirements(climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(climberControlType == ClimberControlType.PNEUMATICS){
      climber.extendPneumatics();
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(climberControlType == ClimberControlType.WINCH){
      climber.spinWinchMotors(mechanismOn);
      //System.out.println("reached here");
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climber.stopWinchMotors();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return climberControlType == ClimberControlType.PNEUMATICS;
  }

  //zach is a simp and likes hannah
}
