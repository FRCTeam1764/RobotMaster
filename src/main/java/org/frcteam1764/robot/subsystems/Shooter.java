// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.command.Subsystem;

/** Add your docs here. */
public class Shooter extends Subsystem {
  public WPI_TalonFX shooterMaster;
  WPI_TalonFX shooterFollower;

  double shooterVelocity;
  double shooter;

  public Shooter(double shooterVelocity){
    this.shooterVelocity = shooterVelocity;

    shooterFollower.follow(shooterMaster);
  }

  double velocity;
  

  @Override
  public void periodic() {
    
  }
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  public void shooterOn(){
    shooterMaster.set(velocity);
  }
  public void shooterStop(){
    shooterMaster.set(0);
  }
}
