package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Commands.TimedMovementGroup;
import frc.robot.Commands.DriveCommands.FarmingSimulatorDrive;
import frc.robot.Commands.DriveCommands.JoystickDrive;
import frc.robot.Commands.DriveCommands.XBoxDrive;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.AutoSubsystems.PIDMovement;
import frc.robot.constants.PortConstants;
import frc.robot.util.ShuffleboardCamera;

public class Robot extends TimedRobot {

  public static Drivetrain drivetrain = new Drivetrain();

  public static int ballCount = 0;
  public static OI oi = new OI();
  public static SharpIRSensor feederIRSensor = new SharpIRSensor(PortConstants.SHARP_IR_SENSOR_FEEDER_ANALOG_PORT);
  public static SharpIRSensor intakeIRSensor = new SharpIRSensor(PortConstants.SHARP_IR_SENSOR_INTAKE_ANALOG_PORT);
  public static DoubleSolenoid climberSolenoid = new DoubleSolenoid(PortConstants.LEFT_CLIMBER_SOLENOID_PORT, PortConstants.RIGHT_CLIMBER_SOLENOID_PORT);
  public static DoubleSolenoid controlPanelWheelExtender = new DoubleSolenoid(PortConstants.CONTROL_PANEL_FORWARD_PORT, PortConstants.CONTROL_PANEL_REVERSE_PORT);
  //public static Solenoid controlPanelWheelExtenderOn = new Solenoid(PortConstants.CONTROL_PANEL_FORWARD_PORT);


  JoystickDrive joystickdrive = new JoystickDrive();
  XBoxDrive xboxdrive = new XBoxDrive();
  FarmingSimulatorDrive jaxonDumbDrive = new FarmingSimulatorDrive();

  public static PIDMovement pidMovement = new PIDMovement();

  @Override
  public void robotInit() {
    ShuffleboardCamera.camera = CameraServer.getInstance().startAutomaticCapture(ShuffleboardCamera.cameraPort);
    ShuffleboardCamera.camera.setConnectVerbose(0);
  }

  
  @Override
  public void autonomousInit() {
    drivetrain.setDrivetrainNeturalMode(NeutralMode.Brake);
    climberSolenoid.set(Value.kReverse);
    controlPanelWheelExtender.set(Value.kReverse);
    pidMovement.setDistancePIDConfig(drivetrain.leftTalons[0], drivetrain.rightTalons[0]);
    CommandScheduler.getInstance().schedule(new TimedMovementGroup());
    
  }
  

  @Override
  public void autonomousPeriodic() {
    CommandScheduler.getInstance().run();
    
  }

  @Override
  public void teleopInit() {
    CommandScheduler.getInstance().cancelAll();
    drivetrain.setDrivetrainNeturalMode(NeutralMode.Brake);
    climberSolenoid.set(Value.kReverse);
    drivetrain.setDrivetrainInverted(false, false);

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
    //SmartDashboard.putNumber("IR Sensor Voltage", intakeIRSensor.getVoltage());
    
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
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void disabledPeriodic() {
    
    super.disabledPeriodic();
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
