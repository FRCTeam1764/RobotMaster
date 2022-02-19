// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.ControlMode;
import org.frcteam2910.common.robot.drivers.LazyTalonFX;
import org.frcteam1764.robot.constants.RobotConstants;
import org.frcteam1764.robot.state.IntakeState;

/** Add your docs here*/
public class Intake extends Subsystem {
  private LazyTalonFX intakeMotor;
  private DoubleSolenoid intakeSolenoid;
  private IntakeState intakeState; 

  public Intake(IntakeState intakeState){
      this.intakeState = intakeState;
      this.intakeMotor = new LazyTalonFX(RobotConstants.INTAKE_MOTOR);
      this.intakeMotor.configFactoryDefault();
      this.intakeMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_7_CommStatus, 200);
      this.intakeSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, RobotConstants.INTAKE_SOLENOID_FORWARD, RobotConstants.INTAKE_SOLENOID_REVERSE);
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
