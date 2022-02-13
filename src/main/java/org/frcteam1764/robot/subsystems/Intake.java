// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import org.frcteam1764.robot.constants.RobotConstants;
import org.frcteam1764.robot.state.IntakeState;

/** Add your docs here*/
public class Intake extends Subsystem {
  private WPI_TalonFX intakeMotor;
  private DoubleSolenoid intakeSolenoid;
  private IntakeState intakeState; 

  public Intake(IntakeState intakeState){
      this.intakeState = intakeState;
      this.intakeMotor = new WPI_TalonFX(RobotConstants.INTAKE_MOTOR);
      this.intakeSolenoid = new DoubleSolenoid(RobotConstants.INTAKE_SOLENOID_FORWARD, RobotConstants.INTAKE_SOLENOID_REVERSE);
  }

  public void intakeOn(double intakeSpeed) {
    intakeMotor.set(ControlMode.PercentOutput, intakeSpeed);
    intakeSolenoid.set(Value.kForward);
    intakeState.deployIntake();
  }
  public void intakeOff() {
    intakeMotor.set(ControlMode.PercentOutput, 0);
    intakeSolenoid.set(Value.kReverse);
    intakeState.withdrawIntake();
  }

  @Override
  protected void initDefaultCommand() {
    // TODO Auto-generated method stub
  }
}
