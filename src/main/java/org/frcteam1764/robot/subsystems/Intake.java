// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonFX;
import org.frcteam1764.robot.constants.RobotConstants;
import org.frcteam1764.robot.state.IntakeState;

/** Add your docs here*/
public class Intake extends SubsystemBase {
  private edu.wpi.first.wpilibj.PWMTalonFX intakeMotor;
  private DoubleSolenoid intakeSolenoid;
  private IntakeState intakeState; 
  private DigitalInput conveyorBreakBeam;
  private DigitalInput elevatorBreakBeam;

  public Intake(IntakeState intakeState, DigitalInput conveyorBreakBeam, DigitalInput elevatorBreakBeam){
      this.intakeState = intakeState;
      this.conveyorBreakBeam = conveyorBreakBeam;
      this.elevatorBreakBeam = elevatorBreakBeam;
      this.intakeMotor = new PWMTalonFX(RobotConstants.INTAKE_MOTOR);
      intakeMotor.setInverted(true);
      this.intakeSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, RobotConstants.INTAKE_SOLENOID_FORWARD, RobotConstants.INTAKE_SOLENOID_REVERSE);
  }

  public void intakeOn(double intakeSpeed, boolean override) {
    if(override){
      intakeMotor.set(intakeSpeed);
      if(!intakeState.isIntakeDeployed()){
        intakeSolenoid.set(Value.kForward);
        intakeState.deployIntake();
      }
    }
    else if(!elevatorBreakBeam.get() && !conveyorBreakBeam.get()){
      intakeMotor.set(intakeSpeed);
      if(intakeState.isIntakeDeployed()){
        intakeSolenoid.set(Value.kReverse);
        intakeState.withdrawIntake();
      }
    }
    else{
      intakeMotor.set(intakeSpeed);
      if(!intakeState.isIntakeDeployed()){
        intakeSolenoid.set(Value.kForward);
        intakeState.deployIntake();
      }
    }
  }
  public void intakeOff() {
    intakeMotor.set(0);
    if(intakeState.isIntakeDeployed()){
      intakeSolenoid.set(Value.kReverse);
      intakeState.withdrawIntake();
    }
  }
}
