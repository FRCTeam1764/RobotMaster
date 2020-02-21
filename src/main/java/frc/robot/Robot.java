package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Commands.DriveCommands.FarmingSimulatorDrive;
import frc.robot.Commands.DriveCommands.JoystickDrive;
import frc.robot.Commands.DriveCommands.XBoxDrive;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.AutoSubsystems.PIDMovement;
import frc.robot.util.ShuffleboardCamera;

public class Robot extends TimedRobot {

  public static Drivetrain drivetrain = new Drivetrain();

  public static OI oi = new OI();

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
    
  }

  @Override
  public void autonomousPeriodic() {
    CommandScheduler.getInstance().run();

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
}