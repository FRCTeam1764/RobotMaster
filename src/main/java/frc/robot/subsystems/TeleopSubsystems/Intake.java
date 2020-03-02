/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems.TeleopSubsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.PortConstants;

public class Intake extends SubsystemBase {
  /**
   * Creates a new Intake.
   */

  public static WPI_TalonSRX intakeMotor = new WPI_TalonSRX(PortConstants.INTAKE_MOTOR_PORT);
  public double intakeSpeed;

  public Intake(double motorIntakeSpeed) {
    intakeMotor.setInverted(true);
    intakeSpeed = motorIntakeSpeed;
  }

  @Override
  public void periodic() {
    
  }

  public void intake() {
    intakeMotor.set(ControlMode.PercentOutput, intakeSpeed);
    SmartDashboard.putBoolean("Intake Status",intakeSpeed>0.0);
  }

  public void timedIntake(double timeDuration){
    Timer timer = new Timer();
    timer.start();

    while(timer.get()<timeDuration){
      intake();
    }

    timer.stop();
    stopIntake();
  }

  public void stopIntake(){
    intakeMotor.set(ControlMode.PercentOutput, 0);
    SmartDashboard.putBoolean("Intake Status",false);
  }

}
