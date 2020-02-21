/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Subsystems.TeleopSubsystems.WheelOfFortune;
import frc.robot.util.ColorSensor;
import frc.robot.util.ColorSensor.ColorType;

public class WheelOfFortuneCommand extends CommandBase {

  WheelOfFortune wheelOfFortune;
  ColorType colorSelected;

  ColorType firstColorDetected;

  public WheelOfFortuneCommand(double motorSpeed){
    wheelOfFortune = new WheelOfFortune(motorSpeed);
    colorSelected = ColorSensor.getControlPanelRequiredColor();

    addRequirements(wheelOfFortune);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    firstColorDetected = ColorSensor.getColorType();

    if( colorSelected == ColorType.UNKNOWN){
      end(false);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
   if(!wheelOfFortune.isRotationControlComplete()){
     wheelOfFortune.rotationControl(firstColorDetected);
   }
   else if(!wheelOfFortune.isPositionControlComplete()){
     wheelOfFortune.rotationControl(colorSelected);
   }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    wheelOfFortune.stopWheel();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return wheelOfFortune.isRotationControlComplete() && wheelOfFortune.isPositionControlComplete();
  }
}
