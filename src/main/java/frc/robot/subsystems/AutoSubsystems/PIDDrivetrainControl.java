/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems.AutoSubsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.constants.PIDConstants;
import frc.robot.constants.RobotDimensionConstants;

/**
 * Add your docs here.
 */
public class PIDDrivetrainControl extends Subsystem {

  public enum DrivetrainSide {
    RIGHT, LEFT
  }

  final int kUnitsPerRevolution = 2048;
  final double kUnitsPerRobotRevolution = RobotDimensionConstants.ROBOT_WIDTH_CENTER_WHEELS * Math.PI / RobotDimensionConstants.WHEEL_CIRCUMFERENCE * RobotDimensionConstants.GEAR_BOX_RATIO* kUnitsPerRevolution;

  public PIDDrivetrainControl(){
  }

  public WPI_TalonFX setDrivetraingPIDConfig(WPI_TalonFX talon, DrivetrainSide side, boolean isMaster, WPI_TalonFX leftMaster){
    WPI_TalonFX configuredTalon = talon;
    TalonFXConfiguration config = new TalonFXConfiguration();
    if(isMaster){
      if(side == DrivetrainSide.LEFT){
        config.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor; // using the left side encoder
        configuredTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 5); // 5ms is how often its checked
      }
      else{ // all of this is to make sure that the right side follows the left side encoder
        config.remoteFilter0.remoteSensorDeviceID = leftMaster.getDeviceID(); // setting RemoteSensor0
        config.diff1Term = FeedbackDevice.RemoteSensor0;
        config.diff0Term = FeedbackDevice.SensorDifference;
        config.auxiliaryPID.selectedFeedbackSensor = FeedbackDevice.SensorDifference; //finding the difference between encoders to correct turning
        config.auxiliaryPID.selectedFeedbackCoefficient = kUnitsPerRevolution/kUnitsPerRobotRevolution;

        configuredTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_12_Feedback1, 20); // 20ms is how often its checked
        configuredTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 20); // 20ms is how often its checked
        configuredTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_14_Turn_PIDF1, 20); // 20ms is how often its checked

        int closedLoopTimeMs = 1;
        configuredTalon.configClosedLoopPeriod(0, closedLoopTimeMs, PIDConstants.kTimeoutMs);
        configuredTalon.configClosedLoopPeriod(1, closedLoopTimeMs, PIDConstants.kTimeoutMs);

        /* FPID Gains for distance servo */
        configuredTalon.config_kP(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kP, PIDConstants.kTimeoutMs);
        configuredTalon.config_kI(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kI, PIDConstants.kTimeoutMs);
        configuredTalon.config_kD(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kD, PIDConstants.kTimeoutMs);
        configuredTalon.config_kF(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kF, PIDConstants.kTimeoutMs);
        configuredTalon.config_IntegralZone(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kIzone, PIDConstants.kTimeoutMs);
        configuredTalon.configClosedLoopPeakOutput(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kPeakOutput, PIDConstants.kTimeoutMs);

        /* FPID Gains for turn servo */
        configuredTalon.config_kP(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kP, PIDConstants.kTimeoutMs);
        configuredTalon.config_kI(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kI, PIDConstants.kTimeoutMs);
        configuredTalon.config_kD(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kD, PIDConstants.kTimeoutMs);
        configuredTalon.config_kF(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kF, PIDConstants.kTimeoutMs);
        configuredTalon.config_IntegralZone(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kIzone, PIDConstants.kTimeoutMs);
        configuredTalon.configClosedLoopPeakOutput(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kPeakOutput, PIDConstants.kTimeoutMs);
        
        configuredTalon.configAuxPIDPolarity(false, PIDConstants.kTimeoutMs);
        
				/* Determine which slot affects which PID */
				configuredTalon.selectProfileSlot(PIDConstants.kSlot_Distanc, PIDConstants.PID_PRIMARY);
				configuredTalon.selectProfileSlot(PIDConstants.kSlot_Turning, PIDConstants.PID_TURN);
      }
      config.neutralDeadband = PIDConstants.kNeutralDeadband;
      config.peakOutputForward = 1.0;
      config.peakOutputReverse = -1.0;
    }

    //Apparently with integrated encoders we dont have to set sensor phase
    configuredTalon.configAllSettings(config);
    configuredTalon.getSensorCollection().setIntegratedSensorPosition(0, PIDConstants.kTimeoutMs);

    return configuredTalon;
  }

  public void setDrivePID(double distance, double angle){
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
