package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Commands.DriveCommands.FarmingSimulatorDrive;
import frc.robot.Commands.DriveCommands.JoystickDrive;
import frc.robot.Commands.DriveCommands.XBoxDrive;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.AutoSubsystems.PIDMovement;
import frc.robot.constants.PIDConstants;
import frc.robot.constants.PortConstants;
import frc.robot.util.ShuffleboardCamera;

public class Robot extends TimedRobot {

  public static Drivetrain drivetrain = new Drivetrain();

  public static int ballCount = 0;
  public static OI oi = new OI();
  public static SharpIRSensor feederIRSensor = new SharpIRSensor(PortConstants.SHARP_IR_SENSOR_FEEDER_ANALOG_PORT);
  public static SharpIRSensor intakeIRSensor = new SharpIRSensor(PortConstants.SHARP_IR_SENSOR_INTAKE_ANALOG_PORT);

  JoystickDrive joystickdrive = new JoystickDrive();
  XBoxDrive xboxdrive = new XBoxDrive();
  FarmingSimulatorDrive jaxonDumbDrive = new FarmingSimulatorDrive();

  public static PIDMovement pidMovement = new PIDMovement();

  @Override
  public void robotInit() {
    ShuffleboardCamera.camera = CameraServer.getInstance().startAutomaticCapture(ShuffleboardCamera.cameraPort);
    ShuffleboardCamera.camera.setConnectVerbose(0);
  }


  WPI_TalonFX _rightMaster = drivetrain.rightTalons[0];
  WPI_TalonFX _leftMaster = drivetrain.leftTalons[0];

	double _lockedDistance = 2048;
  double _targetAngle = 0;
	boolean _firstCall = false;
  
  @Override
  public void autonomousInit() {
    TalonFXConfiguration config = new TalonFXConfiguration();/* Configure the Remote Talon's selected sensor as a remote sensor for the right Talon */
    /* Disable all motor controllers */
    _rightMaster.set(ControlMode.PercentOutput, 0);
		_leftMaster.set(ControlMode.PercentOutput, 0);

		/* Factory Default all hardware to prevent unexpected behaviour */
		_rightMaster.configFactoryDefault();
		_leftMaster.configFactoryDefault();
		
		/* Set Neutral Mode */
		_leftMaster.setNeutralMode(NeutralMode.Brake);
		_rightMaster.setNeutralMode(NeutralMode.Brake);
		
		/** Feedback Sensor Configuration */
		
		/* Configure the left Talon's selected sensor as local QuadEncoder */
		_leftMaster.configSelectedFeedbackSensor(	FeedbackDevice.IntegratedSensor,				// Local Feedback Source
													PIDConstants.PID_PRIMARY,					// PID Slot for Source [0, 1]
													PIDConstants.kTimeoutMs);					// Configuration Timeout
    config.remoteFilter1.remoteSensorDeviceID = _leftMaster.getDeviceID(); // setting RemoteSensor0
    _rightMaster.configAllSettings(config);

		
		
		/* Setup Sum signal to be used for Distance */
		_rightMaster.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor1, PIDConstants.kTimeoutMs);				// Feedback Device of Remote Talon
		_rightMaster.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.IntegratedSensor, PIDConstants.kTimeoutMs);	// Quadrature Encoder of current Talon
		
		/* Setup Difference signal to be used for Turn */
		_rightMaster.configSensorTerm(SensorTerm.Diff1, FeedbackDevice.RemoteSensor1, PIDConstants.kTimeoutMs);
		_rightMaster.configSensorTerm(SensorTerm.Diff0, FeedbackDevice.IntegratedSensor, PIDConstants.kTimeoutMs);
		
		/* Configure Sum [Sum of both QuadEncoders] to be used for Primary PID Index */
		_rightMaster.configSelectedFeedbackSensor(	FeedbackDevice.SensorSum, 
    PIDConstants.PID_PRIMARY,
    PIDConstants.kTimeoutMs);
		
		/* Scale Feedback by 0.5 to half the sum of Distance */
		_rightMaster.configSelectedFeedbackCoefficient(	0.5, 						// Coefficient
    PIDConstants.PID_PRIMARY,		// PID Slot of Source 
    PIDConstants.kTimeoutMs);		// Configuration Timeout
		
		/* Configure Difference [Difference between both QuadEncoders] to be used for Auxiliary PID Index */
		_rightMaster.configSelectedFeedbackSensor(	FeedbackDevice.SensorDifference, 
    PIDConstants.PID_TURN, 
    PIDConstants.kTimeoutMs);
		
		/* Scale the Feedback Sensor using a coefficient */
		_rightMaster.configSelectedFeedbackCoefficient(	1,
    PIDConstants.PID_TURN, 
    PIDConstants.kTimeoutMs);
		
    /* Configure output and sensor direction */
    _leftMaster.setSensorPhase(true);
    _rightMaster.setSensorPhase(true);
    _leftMaster.setInverted(false);
    _rightMaster.setInverted(true);
		
		/* Set status frame periods to ensure we don't have stale data */
		_rightMaster.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20, PIDConstants.kTimeoutMs);
		_rightMaster.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20, PIDConstants.kTimeoutMs);
		_rightMaster.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 20, PIDConstants.kTimeoutMs);
		_leftMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, PIDConstants.kTimeoutMs);

		/* Configure neutral deadband */
		_rightMaster.configNeutralDeadband(PIDConstants.kNeutralDeadband, PIDConstants.kTimeoutMs);
		_leftMaster.configNeutralDeadband(PIDConstants.kNeutralDeadband, PIDConstants.kTimeoutMs);

		/* Max out the peak output (for all modes).  
		 * However you can limit the output of a given PID object with configClosedLoopPeakOutput().
		 */
		_leftMaster.configPeakOutputForward(+1.0, PIDConstants.kTimeoutMs);
		_leftMaster.configPeakOutputReverse(-1.0, PIDConstants.kTimeoutMs);
		_rightMaster.configPeakOutputForward(+1.0, PIDConstants.kTimeoutMs);
		_rightMaster.configPeakOutputReverse(-1.0, PIDConstants.kTimeoutMs);

		/* FPID Gains for distance servo */
		_rightMaster.config_kP(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kP, PIDConstants.kTimeoutMs);
		_rightMaster.config_kI(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kI, PIDConstants.kTimeoutMs);
		_rightMaster.config_kD(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kD, PIDConstants.kTimeoutMs);
		_rightMaster.config_kF(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kF, PIDConstants.kTimeoutMs);
		_rightMaster.config_IntegralZone(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kIzone, PIDConstants.kTimeoutMs);
		_rightMaster.configClosedLoopPeakOutput(PIDConstants.kSlot_Distanc, PIDConstants.kGains_Distanc.kPeakOutput, PIDConstants.kTimeoutMs);

		/* FPID Gains for turn servo */
		_rightMaster.config_kP(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kP, PIDConstants.kTimeoutMs);
		_rightMaster.config_kI(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kI, PIDConstants.kTimeoutMs);
		_rightMaster.config_kD(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kD, PIDConstants.kTimeoutMs);
		_rightMaster.config_kF(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kF, PIDConstants.kTimeoutMs);
		_rightMaster.config_IntegralZone(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kIzone, PIDConstants.kTimeoutMs);
		_rightMaster.configClosedLoopPeakOutput(PIDConstants.kSlot_Turning, PIDConstants.kGains_Turning.kPeakOutput, PIDConstants.kTimeoutMs);
			
		/* 1ms per loop.  PID loop can be slowed down if need be.
		 * For example,
		 * - if sensor updates are too slow
		 * - sensor deltas are very small per update, so derivative error never gets large enough to be useful.
		 * - sensor movement is very slow causing the derivative error to be near zero.
		 */
        int closedLoopTimeMs = 1;
        _rightMaster.configClosedLoopPeriod(0, closedLoopTimeMs, PIDConstants.kTimeoutMs);
        _rightMaster.configClosedLoopPeriod(1, closedLoopTimeMs, PIDConstants.kTimeoutMs);

		/* configAuxPIDPolarity(boolean invert, int timeoutMs)
		 * false means talon's local output is PID0 + PID1, and other side Talon is PID0 - PID1
		 * true means talon's local output is PID0 - PID1, and other side Talon is PID0 + PID1
		 */
    _rightMaster.configAuxPIDPolarity(true, PIDConstants.kTimeoutMs);
    

		/* Initialize */
		zeroSensors();


   
		
		

    //CommandScheduler.getInstance().schedule(pidPath);
    
  }
  

  @Override
  public void autonomousPeriodic() {
    CommandScheduler.getInstance().run();
    if (_firstCall) {
      System.out.println("This is Drive Straight Distance with the Auxiliary PID using the difference between two encoders.");
      System.out.println("Servo [-6, 6] rotations while also maintaining a straight heading.\n");
      
      /* Determine which slot affects which PID */
      _rightMaster.selectProfileSlot(PIDConstants.kSlot_Distanc, PIDConstants.PID_PRIMARY);
      _rightMaster.selectProfileSlot(PIDConstants.kSlot_Turning, PIDConstants.PID_TURN);
    }
    
    /* Calculate targets from gamepad inputs */
    double target_sensorUnits = _lockedDistance;
    double target_turn = _targetAngle;
    
    /* Configured for Position Closed loop on Quad Encoders' Sum and Auxiliary PID on Quad Encoders' Difference */
    _rightMaster.set(ControlMode.Position, target_sensorUnits, DemandType.AuxPID, target_turn);
    _leftMaster.follow(_rightMaster, FollowerType.AuxOutput1);

  _firstCall = false;
  }

  @Override
  public void teleopInit() {
    drivetrain.setDrivetrainNeturalMode(NeutralMode.Brake);

    if(DriverStation.getInstance().getJoystickIsXbox(0)){
      CommandScheduler.getInstance().schedule(xboxdrive);
    }
    else{
      CommandScheduler.getInstance().schedule(joystickdrive);
    }
   
    }

	/**
	 * This function is called periodically during operator control
	 */

	public void teleopPeriodic() {
    CommandScheduler.getInstance().run();
    SmartDashboard.putNumber("Feeder IR", feederIRSensor.getVoltage());
    SmartDashboard.putNumber("IR Sensor Voltage", intakeIRSensor.getVoltage());
    
  }

    @Override
    public void testInit() {
        
        CommandScheduler.getInstance().schedule(jaxonDumbDrive);
        
    }
  
    @Override
  public void testPeriodic() {

        CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {
    drivetrain.setDrivetrainNeturalMode(NeutralMode.Coast);
  }

  @Override
  public void disabledPeriodic() {
    
    super.disabledPeriodic();
  }
	/* Zero quadrature encoders on Talons */
	void zeroSensors() {
		_leftMaster.getSensorCollection().setIntegratedSensorPosition(0, PIDConstants.kTimeoutMs);
		_rightMaster.getSensorCollection().setIntegratedSensorPosition(0, PIDConstants.kTimeoutMs);
		System.out.println("[Quadrature Encoders] All sensors are zeroed.\n");
	}
	
	/** Deadband 5 percent, used on the gamepad */
	double Deadband(double value) {
		/* Upper deadband */
		if (value >= +0.05) 
			return value;
		
		/* Lower deadband */
		if (value <= -0.05)
			return value;
		
		/* Outside deadband */
		return 0;
	}
}
