/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.PIDConstants;
import frc.robot.constants.PortConstants;
import frc.robot.constants.VoltageConstants;

/**
 * Add your docs here.
 */
public class Drivetrain extends SubsystemBase {

  public WPI_TalonFX[] leftTalons = new WPI_TalonFX[3];
  public WPI_TalonFX[] rightTalons = new WPI_TalonFX[3];

  public DifferentialDrive diffDrive;
  private TalonFXSensorCollection m_leftEncoder;
  private TalonFXSensorCollection m_rightEncoder;

  public Drivetrain(){

    for(int i=0; i<PortConstants.LEFT_MOTORS_IDS.length; i++){
      if(i==0){
        leftTalons[i] = configTalons(PortConstants.LEFT_MOTORS_IDS[i], true, false);
      }
      else{
        leftTalons[i] = configTalons(PortConstants.LEFT_MOTORS_IDS[i], false, false);
        leftTalons[i].follow(leftTalons[0]);
      }
    }

    for(int i=0; i<PortConstants.RIGHT_MOTORS_IDS.length; i++){
      if(i==0){
        rightTalons[i] = configTalons(PortConstants.RIGHT_MOTORS_IDS[i], true, false);
        
      }
      else{
        rightTalons[i] = configTalons(PortConstants.RIGHT_MOTORS_IDS[i], false, false);
        rightTalons[i].follow(rightTalons[0]);
      }
    }

    diffDrive = new DifferentialDrive(leftTalons[0], rightTalons[0]);

    m_leftEncoder = leftTalons[0].getSensorCollection();
    m_rightEncoder = rightTalons[0].getSensorCollection();
		
  }
  
  public WPI_TalonFX configTalons(int _canId, boolean isMaster, boolean isInverted){
    WPI_TalonFX talon = new WPI_TalonFX(_canId);
    talon.configFactoryDefault();
    talon.setInverted(isInverted);
    talon.enableVoltageCompensation(true);
    talon.configVoltageCompSaturation(12.0, PIDConstants.kTimeoutMs);

    if(isMaster){
      talon.configOpenloopRamp(VoltageConstants.openDriveVoltageRampRate);
    }

    return talon;
  }

  public void setDrivetrainNeturalMode(NeutralMode _mode){
    leftTalons[0].setNeutralMode(_mode); //the 0 index is the master motor
    rightTalons[0].setNeutralMode(_mode);
  }

  public void stopDrivetrain(){
    leftTalons[0].set(ControlMode.PercentOutput, 0); //the 0 index is the master motor
    rightTalons[0].set(ControlMode.PercentOutput, 0);
  }

	@Override
  public void periodic(){
   
  }

  /**
   * Resets the drive encoders to currently read a position of 0.
   */
  public void resetEncoders() {
    m_leftEncoder.setIntegratedSensorPosition(0, PIDConstants.kTimeoutMs);
	  m_rightEncoder.setIntegratedSensorPosition(0, PIDConstants.kTimeoutMs);
	  System.out.println("All sensors are zeroed.\n");
  }

  /**
   * Gets the left drive encoder.
   *
   * @return the left drive encoder
   */
  public TalonFXSensorCollection getLeftEncoder() {
    return m_leftEncoder;
  }

  /**
   * Gets the right drive encoder.
   *
   * @return the right drive encoder
   */
  public TalonFXSensorCollection getRightEncoder() {
    return m_rightEncoder;
  }

  /**
   * Sets the max output of the drive.  Useful for scaling the drive to drive more slowly.
   *
   * @param maxOutput the maximum output to which the drive will be constrained
   */
  public void setMaxOutput(double maxOutput) {
    diffDrive.setMaxOutput(maxOutput);
  }
  

  /** 
   * Sets the motors' inversion for the entire drivetrain
   * 
   * @param leftInverted what the entire left side is set to
   * @param rightInverted what the entire right side is set to
  */
  public void setDrivetrainInverted(boolean leftInverted, boolean rightInverted){
    for(int i=0; i<PortConstants.LEFT_MOTORS_IDS.length; i++){
      leftTalons[i].setInverted(leftInverted);
    }

    for(int i=0; i<PortConstants.RIGHT_MOTORS_IDS.length; i++){
      rightTalons[i].setInverted(rightInverted);
    }
  }
}
