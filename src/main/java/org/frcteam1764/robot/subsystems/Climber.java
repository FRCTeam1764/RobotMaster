// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.math.controller.PIDController;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import org.frcteam2910.common.robot.drivers.LazyTalonFX;
import org.frcteam1764.robot.constants.RobotConstants;
import org.frcteam1764.robot.state.ClimberState;

import edu.wpi.first.wpilibj.PneumaticsModuleType;

/** Add your docs here*/
public class Climber extends Subsystem {
  private LazyTalonFX climberMasterMotor;
  private LazyTalonFX climberFollowerMotor;
  private DoubleSolenoid climberSolenoid;
  private PIDController pidController; 
  private ClimberState climberState;
  private DigitalInput rightLimitSwitch;
  private DigitalInput leftLimitSwitch;

  public Climber(ClimberState climberState){
    this.climberMasterMotor = new LazyTalonFX(RobotConstants.CLIMBER_MASTER_MOTOR);
    this.climberFollowerMotor = new LazyTalonFX(RobotConstants.CLIMBER_FOLLOWER_MOTOR);
		this.climberMasterMotor.configFactoryDefault();
		this.climberFollowerMotor.configFactoryDefault();
    this.climberFollowerMotor.setInverted(true);
    this.climberMasterMotor.setNeutralMode(NeutralMode.Brake);
    this.climberFollowerMotor.setNeutralMode(NeutralMode.Brake);
    this.climberFollowerMotor.follow(climberMasterMotor);
    this.climberSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, RobotConstants.CLIMBER_SOLENOID_FORWARD, RobotConstants.CLIMBER_SOLENOID_REVERSE);
    this.pidController = new PIDController(.0001, 0 , 0);
    this.climberState = climberState; 
    this.rightLimitSwitch = new DigitalInput(RobotConstants.RIGHT_LIMIT_SWITCH);
    this.leftLimitSwitch = new DigitalInput(RobotConstants.LEFT_LIMIT_SWITCH);
  }


  public void pneumaticsWithdraw(){
    climberSolenoid.set(Value.kReverse);
  }
  public void pneumaticsDeploy(){
    climberSolenoid.set(Value.kForward);
  }

  public void climberOn(double climberSpeed) {
    climberMasterMotor.set(ControlMode.PercentOutput, climberSpeed);
  }
  public void climberOff() {
    climberMasterMotor.set(ControlMode.PercentOutput, 0);
  }

  public void climb(){
    climberMasterMotor.set(pidController.calculate(getMasterEncoder()));
  }

  public void climberMaxHeight(){
    pidController.setSetpoint(100);
  }

  public int getMasterEncoder(){
    int selSenPos = (int) climberMasterMotor.getSelectedSensorPosition(0);
    return selSenPos;
  }

  public void resetFalcon(){
    climberState.setOffset(getMasterEncoder());
  }

  public boolean checkLimitSwitches(){
    if(rightLimitSwitch.get() || leftLimitSwitch.get()){
      return true;
    }
    else
    {
      return false;
    }
  }

  @Override
  protected void initDefaultCommand() {
    // TODO Auto-generated method stub
  }
}
