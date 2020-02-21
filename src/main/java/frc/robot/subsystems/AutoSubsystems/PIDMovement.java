/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems.AutoSubsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.PIDConstants;

public class PIDMovement extends SubsystemBase {
  /**
   * Creates a new PIDMovement.
   */
  public PIDMovement() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

    public void setPIDConfig(WPI_TalonFX talon, boolean setSensorPhase){
    
    //Ensure sensor is positive when output is positive
    talon.setSensorPhase(setSensorPhase);
    
    //Config the peak and nominal Outputs, 12V means full
    talon.configNominalOutputForward(0, PIDConstants.kTimeoutMs);
    talon.configNominalOutputReverse(0, PIDConstants.kTimeoutMs);

    talon.configNeutralDeadband(PIDConstants.kNeutralDeadband, PIDConstants.kTimeoutMs);

    int closedLoopTimeMs = 1;
    talon.configClosedLoopPeriod(0, closedLoopTimeMs, PIDConstants.kTimeoutMs);
    talon.configClosedLoopPeriod(1, closedLoopTimeMs, PIDConstants.kTimeoutMs);

    /* Initialize */
    talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_Targets, 10);
    resetEncoders(talon);
  }

  public void setDistancePIDConfig(WPI_TalonFX talon){
    talon.configSelectedFeedbackSensor(	FeedbackDevice.IntegratedSensor, 
    PIDConstants.PID_PRIMARY,
    PIDConstants.kTimeoutMs);

    talon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, PIDConstants.kTimeoutMs);
    talon.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20, PIDConstants.kTimeoutMs);

    //Config the allowable closed-loop error, closed-loop output will be neutral within this range.
    talon.configAllowableClosedloopError(PIDConstants.kSlot_Distanc, PIDConstants.TICKS_ERROR, PIDConstants.kTimeoutMs);

    /**
    * Max out the peak output (for all modes).  
    * However you can limit the output of a given PID object with configClosedLoopPeakOutput().
    */
    talon.configPeakOutputForward(+1.0, PIDConstants.kTimeoutMs);
    talon.configPeakOutputReverse(-1.0, PIDConstants.kTimeoutMs);

    /* FPID Gains for distance servo */
    talon.config_kP(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kP, PIDConstants.kTimeoutMs);
    talon.config_kI(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kI, PIDConstants.kTimeoutMs);
    talon.config_kD(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kD, PIDConstants.kTimeoutMs);
    talon.config_kF(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kF, PIDConstants.kTimeoutMs);
    talon.config_IntegralZone(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kIzone, PIDConstants.kTimeoutMs);
    talon.configClosedLoopPeakOutput(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kPeakOutput, PIDConstants.kTimeoutMs);
    talon.configAllowableClosedloopError(PIDConstants.kSlot_Distanc, 0, PIDConstants.kTimeoutMs);

    talon.selectProfileSlot(PIDConstants.kSlot_Distanc, PIDConstants.PID_PRIMARY);

    resetEncoders(talon);
  }

  public void setTurningPIDConfig(WPI_TalonFX talon){
    talon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, PIDConstants.kTimeoutMs);
    talon.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20, PIDConstants.kTimeoutMs);

    //Config the allowable closed-loop error, closed-loop output will be neutral within this range.
    talon.configAllowableClosedloopError(PIDConstants.kSlot_Distanc, PIDConstants.TICKS_ERROR, PIDConstants.kTimeoutMs);
    
    talon.configSelectedFeedbackSensor(	FeedbackDevice.IntegratedSensor, 
    PIDConstants.PID_PRIMARY,
    PIDConstants.kTimeoutMs);

    /**
    * Max out the peak output (for all modes).  
    * However you can limit the output of a given PID object with configClosedLoopPeakOutput().
    */
    talon.configPeakOutputForward(+1.0, PIDConstants.kTimeoutMs);
    talon.configPeakOutputReverse(-1.0, PIDConstants.kTimeoutMs);

    /* FPID Gains for turn servo */
    talon.config_kP(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kP, PIDConstants.kTimeoutMs);
    talon.config_kI(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kI, PIDConstants.kTimeoutMs);
    talon.config_kD(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kD, PIDConstants.kTimeoutMs);
    talon.config_kF(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kF, PIDConstants.kTimeoutMs);
    talon.config_IntegralZone(PIDConstants.kSlot_Turning, (int)PIDConstants.kGains_Turning.kIzone, PIDConstants.kTimeoutMs);
    talon.configClosedLoopPeakOutput(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kPeakOutput, PIDConstants.kTimeoutMs);
    talon.configAllowableClosedloopError(PIDConstants.kSlot_Turning, 0, PIDConstants.kTimeoutMs);

    talon.selectProfileSlot(PIDConstants.kSlot_Turning, PIDConstants.PID_PRIMARY);

    resetEncoders(talon);
  }

   /**
   * Resets the drive encoders to currently read a position of 0.
   */
  public void resetEncoders(WPI_TalonFX talon) {
    talon.getSensorCollection().setIntegratedSensorPosition(0, PIDConstants.kTimeoutMs);
  }
}
