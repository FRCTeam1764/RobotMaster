package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

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
import frc.robot.Commands.PIDMovementCommands.PIDDrive;
import frc.robot.Commands.PIDMovementCommands.PIDDrive.PIDDriveControlType;
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
 
  private double distance; //inches
  private final double TICKS_PER_INCH = (2048*10.24)/(Math.PI*6);
  private final double kP = .07;
  private final double DISTANCE_TOLERANCE = .5; //inches

  JoystickDrive joystickdrive = new JoystickDrive();
  XBoxDrive xboxdrive = new XBoxDrive();
  FarmingSimulatorDrive jaxonDumbDrive = new FarmingSimulatorDrive();

  @Override
  public void robotInit() {
    ShuffleboardCamera.camera = CameraServer.getInstance().startAutomaticCapture(ShuffleboardCamera.cameraPort);
    ShuffleboardCamera.camera.setConnectVerbose(0);
  }

  
  @Override
  public void autonomousInit() {
    System.out.println("Start auto");
    CommandScheduler.getInstance().cancelAll();
    drivetrain.setDrivetrainNeturalMode(NeutralMode.Brake);
    climberSolenoid.set(Value.kReverse);
    //controlPanelWheelExtender.set(Value.kForward);
    controlPanelWheelExtender.set(Value.kReverse);
    //drivetrain.setDrivetrainInverted(false, true); //Because it is not performing arcade drive in auto, the right needs to be inverted in order to go forward
    //PIDMovement.setDistancePIDConfig(drivetrain.leftTalons[0], drivetrain.rightTalons[0]);
    CommandScheduler.getInstance().schedule(new TimedMovementGroup());
   //CommandScheduler.getInstance().schedule(new PIDDrive(24, PIDDriveControlType.STRAIGHT));
    
    /*distance = 12;
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
    drivetrain.leftTalons[0].configAllSettings(config);
    drivetrain.leftTalons[0].setSelectedSensorPosition(0);*/

  }
  

  @Override
  public void autonomousPeriodic() {
    CommandScheduler.getInstance().run();

   // PIDMovement.setMotionMagic(drivetrain.rightTalons[0], drivetrain.leftTalons[0], 50000, drivetrain.rightTalons[0].getSelectedSensorPosition(1));

    /*int leftDistanceTraveledTicks = drivetrain.leftTalons[0].getSelectedSensorPosition();
    double leftDistanceTraveledInches = leftDistanceTraveledTicks/TICKS_PER_INCH;
    double error = Math.abs(distance-leftDistanceTraveledInches);
    if (error > DISTANCE_TOLERANCE) {
      drivetrain.leftTalons[0].set(ControlMode.PercentOutput, error*kP);
      drivetrain.rightTalons[0].set(ControlMode.PercentOutput, error*kP);
    }
    else {
      drivetrain.rightTalons[0].follow(drivetrain.rightTalons[0]);
      drivetrain.leftTalons[0].set(TalonFXControlMode.PercentOutput, 0);
      drivetrain.rightTalons[0].set(TalonFXControlMode.PercentOutput, 0);
    }*/
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
