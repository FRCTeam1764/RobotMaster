/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.Robot;

public class FarmingSimulatorDrive extends CommandBase {
  /**
   * Creates a new FarmingSimulatorDrive.
   */
  public FarmingSimulatorDrive() {
    addRequirements(Robot.drivetrain);
  }

  XboxController xbox = Robot.oi.driverXbox;

  double leftfactor = 1;
  double rightfactor = 1;
  double speed = 0;
  double throttlefactor = .8;

  @Override
  public void execute() {
   /* Robot.drivetrain.setSpeed(stick.getY() * throttleFactor(), stick.getZ()*throttleFactor());
    DriverStation.reportError("its working", true);*/
    speed = -(deadband(xbox.getTriggerAxis(Hand.kRight)) - deadband(xbox.getTriggerAxis(Hand.kLeft)));

    if(xbox.getBumper(Hand.kRight)){
      throttlefactor = .95;
    }
    else{
      throttlefactor = .8;
    }

    if(deadband(xbox.getX(Hand.kLeft))>0){
      leftfactor = 1;
      rightfactor = getFactorValue(xbox.getX(Hand.kLeft));
    }
    else if(deadband(xbox.getX(Hand.kLeft))<0){
      rightfactor = 1;
      leftfactor = getFactorValue(xbox.getX(Hand.kLeft));
    }

    if(xbox.getAButton()){
      speed = 0;
      leftfactor=0;
      rightfactor=0;
    }
    

    Robot.drivetrain.diffDrive.tankDrive(throttlefactor*speed*leftfactor, throttlefactor*speed*rightfactor);
  }

  private double getFactorValue(double triggerAxis) {
    return (triggerAxis>0) ? -2*triggerAxis+1 : 2*triggerAxis+1;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  private double getThrottleXbox(){
    return throttlefactor;
  }

  double deadband(double dooble){
    if(dooble <.1 && dooble>-.1){
      return 0;
    }
    return dooble;
  }
}
