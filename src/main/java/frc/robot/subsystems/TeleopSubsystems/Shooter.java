/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems.TeleopSubsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.PIDConstants;
import frc.robot.constants.PortConstants;
import frc.robot.Robot;
import frc.robot.Subsystems.AutoSubsystems.PIDMovement;

public class Shooter extends SubsystemBase {
  
  public static WPI_TalonFX shooterMaster = configShooterMotors(PortConstants.SHOOTER_MASTER_MOTOR_PORT, true, false);
  static WPI_TalonFX shooterFollower = configShooterMotors(PortConstants.SHOOTER_FOLLOWER_MOTOR_PORT, false, true);

  double shooterVelocity;
  double shooter;

  public Shooter(double targetVelocity) {
    shooterVelocity = targetVelocity;

    shooterFollower.follow(shooterMaster);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void shoot() {
    shooterMaster.set(ControlMode.Velocity, shooterVelocity);
  }

  public void shoot(double time){
    Timer timer = new Timer();
    timer.start();

    while(timer.get()<=time){
      shoot();
    }

    stopShooter();
  }

  public void stopShooter(){
    shooterMaster.set(ControlMode.PercentOutput,0);
  }

  public static WPI_TalonFX configShooterMotors(int portNum, boolean isMaster, boolean isInverted) {
    WPI_TalonFX talon = Robot.drivetrain.configTalons(PortConstants.SHOOTER_MASTER_MOTOR_PORT, isMaster, isInverted);
    talon.setNeutralMode(NeutralMode.Brake);

    if(isMaster){
      Robot.pidMovement.setPIDConfig(shooterMaster, false);
      setPIDShooterVelocityConfig(talon);
    }

    return talon;
  }

  public static void setPIDShooterVelocityConfig(WPI_TalonFX talon) {
    Robot.pidMovement.setPIDConfig(talon, false);

    talon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, PIDConstants.kTimeoutMs);
    talon.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20, PIDConstants.kTimeoutMs);
    
    talon.configSelectedFeedbackSensor(	FeedbackDevice.IntegratedSensor, 
    PIDConstants.PID_PRIMARY,
    PIDConstants.kTimeoutMs);

    /**
    * Max out the peak output (for all modes).  
    * However you can limit the output of a given PID object with configClosedLoopPeakOutput().
    */
    talon.configPeakOutputForward(+1.0, PIDConstants.kTimeoutMs);
    talon.configPeakOutputReverse(0, PIDConstants.kTimeoutMs); //Don't want to reverse


    talon.config_kP(PIDConstants.kSlot_Shooter_Velocity, PIDConstants.kGains_Velocity_Shooter.kP, PIDConstants.kTimeoutMs);
    talon.config_kI(PIDConstants.kSlot_Shooter_Velocity, PIDConstants.kGains_Velocity_Shooter.kI, PIDConstants.kTimeoutMs);
    talon.config_kD(PIDConstants.kSlot_Shooter_Velocity, PIDConstants.kGains_Velocity_Shooter.kD, PIDConstants.kTimeoutMs);
    talon.config_kF(PIDConstants.kSlot_Shooter_Velocity, PIDConstants.kGains_Velocity_Shooter.kF, PIDConstants.kTimeoutMs);
    talon.config_IntegralZone(PIDConstants.kSlot_Shooter_Velocity, (int)PIDConstants.kGains_Velocity_Shooter.kIzone, PIDConstants.kTimeoutMs);
    talon.configClosedLoopPeakOutput(PIDConstants.kSlot_Shooter_Velocity, PIDConstants.kGains_Velocity_Shooter.kPeakOutput, PIDConstants.kTimeoutMs);
    talon.configAllowableClosedloopError(PIDConstants.kSlot_Shooter_Velocity, 0, PIDConstants.kTimeoutMs);

    talon.selectProfileSlot(PIDConstants.kSlot_Shooter_Velocity, PIDConstants.PID_PRIMARY);
  }
}
