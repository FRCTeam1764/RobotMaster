// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.math.controller.PIDController;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import org.frcteam1764.robot.constants.RobotConstants;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

/** Add your docs here*/
public class Climber extends Subsystem {
  private WPI_TalonFX climberMasterMotor;
  private WPI_TalonFX climberFollowerMotor;
  private DoubleSolenoid climberSolenoid;
  private PIDController pidController; 
  

  public Climber(){
    this.climberMasterMotor = new WPI_TalonFX(RobotConstants.CLIMBER_MASTER_MOTOR);
    this.climberFollowerMotor = new WPI_TalonFX(RobotConstants.CLIMBER_FOLLOWER_MOTOR);
    this.climberFollowerMotor.setInverted(true);
    this.climberFollowerMotor.follow(climberMasterMotor);
    this.climberSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, RobotConstants.CLIMBER_SOLENOID_FORWARD, RobotConstants.CLIMBER_SOLENOID_REVERSE);
    this.pidController = new PIDController(.0001, 0 , 0);
    configLimitSwitches();
  }

  private void configLimitSwitches(){
    climberMasterMotor.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 30);
    climberMasterMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 30);
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

  public double getMasterEncoder(){
    double selSenPos = climberMasterMotor.getSelectedSensorPosition(0);
    return selSenPos;
  }

  public void resetFalcon(){
    this.climberMasterMotor = new WPI_TalonFX(RobotConstants.CLIMBER_MASTER_MOTOR);
  }

    @Override
    protected void initDefaultCommand() {
      // TODO Auto-generated method stub
    }
  }
