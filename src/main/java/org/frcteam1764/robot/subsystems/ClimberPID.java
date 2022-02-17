// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package org.frcteam1764.robot.subsystems;

// import org.frcteam1764.robot.constants.RobotConstants;
// import com.ctre.phoenix.motorcontrol.NeutralMode;
// import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
// import com.ctre.phoenix.motorcontrol.StatusFrame;
// import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
// import com.ctre.phoenix.motorcontrol.FollowerType;
// import com.ctre.phoenix.motorcontrol.DemandType;
// import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
// import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
// import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
// import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

// public class ClimberPID extends TimedRobot {
// 	/** Hardware */
// 	WPI_TalonFX leftMotor = new WPI_TalonFX(2, "rio");
// 	WPI_TalonFX rightMotor = new WPI_TalonFX(1, "rio");
// 	Joystick _gamepad = new Joystick(0);
	
	
	

// 	/** Config Objects for motor controllers */
// 	TalonFXConfiguration leftConfig = new TalonFXConfiguration();
// 	TalonFXConfiguration rightConfig = new TalonFXConfiguration();
    
//     public void teleopInit(){
//         /* Disable all motor controllers */
// 		rightMotor.set(TalonFXControlMode.PercentOutput, 0);
// 		leftMotor.set(TalonFXControlMode.PercentOutput, 0);

//         /* Set Neutral Mode */
// 		leftMotor.setNeutralMode(NeutralMode.Brake);
// 		rightMotor.setNeutralMode(NeutralMode.Brake);

// 		/* Configure output */
// 		rightMotor.setInverted(true);
		
//         leftConfig.primaryPID.selectedFeedbackSensor = TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice();

//         rightConfig.remoteFilter1.remoteSensorDeviceID = leftMotor.getDeviceID(); //Device ID of Remote Source
// 		rightConfig.remoteFilter1.remoteSensorSource = RemoteSensorSource.TalonFX_SelectedSensor; //Remote Source Type

//         setRobotTurnConfigs(rightConfig);

// 		leftConfig.neutralDeadband = Constants.kNeutralDeadband;
// 		rightConfig.neutralDeadband = Constants.kNeutralDeadband;

//         leftConfig.peakOutputForward = +1.0;
// 		leftConfig.peakOutputReverse = -1.0;
// 		rightConfig.peakOutputForward = +1.0;
// 		rightConfig.peakOutputReverse = -1.0;

//         rightConfig.slot0.kF = Constants.kGains_Distanc.kF;
// 		rightConfig.slot0.kP = Constants.kGains_Distanc.kP;
// 		rightConfig.slot0.kI = Constants.kGains_Distanc.kI;
// 		rightConfig.slot0.kD = Constants.kGains_Distanc.kD;
// 		rightConfig.slot0.integralZone = Constants.kGains_Distanc.kIzone;
// 		rightConfig.slot0.closedLoopPeakOutput = Constants.kGains_Distanc.kPeakOutput;

//         rightConfig.slot1.kF = Constants.kGains_Turning.kF;
// 		rightConfig.slot1.kP = Constants.kGains_Turning.kP;
// 		rightConfig.slot1.kI = Constants.kGains_Turning.kI;
// 		rightConfig.slot1.kD = Constants.kGains_Turning.kD;
// 		rightConfig.slot1.integralZone = Constants.kGains_Turning.kIzone;
// 		rightConfig.slot1.closedLoopPeakOutput = Constants.kGains_Turning.kPeakOutput;

//         int closedLoopTimeMs = 1;
// 		rightConfig.slot0.closedLoopPeriod = closedLoopTimeMs;
// 		rightConfig.slot1.closedLoopPeriod = closedLoopTimeMs;
// 		rightConfig.slot2.closedLoopPeriod = closedLoopTimeMs;
// 		rightConfig.slot3.closedLoopPeriod = closedLoopTimeMs;
		
// 		/* APPLY the config settings */
// 		leftMaster.configAllSettings(_leftConfig);
// 		rightMaster.configAllSettings(_rightConfig);
		
// 		/* Set status frame periods to ensure we don't have stale data */
// 		rightMaster.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20, Constants.kTimeoutMs);
// 		rightMaster.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20, Constants.kTimeoutMs);
// 		rightMaster.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 20, Constants.kTimeoutMs);
// 		leftMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, Constants.kTimeoutMs);
//     }

//         void setRobotTurnConfigs(TalonFXConfiguration masterConfig){
//             masterConfig.sum0Term = TalonFXFeedbackDevice.IntegratedSensor.toFeedbackDevice(); //Local Integrated Sensor
//             masterConfig.sum1Term = TalonFXFeedbackDevice.RemoteSensor1.toFeedbackDevice();   //Aux Selected Sensor
//             masterConfig.auxiliaryPID.selectedFeedbackSensor = TalonFXFeedbackDevice.SensorSum.toFeedbackDevice(); //Sum0 + Sum1
//             masterConfig.auxPIDPolarity = true;
//             masterConfig.auxiliaryPID.selectedFeedbackCoefficient = Constants.kTurnTravelUnitsPerRotation / Constants.kEncoderUnitsPerRotation;
//         }
// }