package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Commands.DriveCommands.FarmingSimulatorDrive;
import frc.robot.Commands.DriveCommands.JoystickDrive;
import frc.robot.Commands.DriveCommands.XBoxDrive;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.AutoSubsystems.PIDMovement;

public class Robot extends TimedRobot {

  public static Drivetrain drivetrain = new Drivetrain();

  public static OI oi = new OI();

  JoystickDrive joystickdrive = new JoystickDrive();
  XBoxDrive xboxdrive = new XBoxDrive();
  FarmingSimulatorDrive jaxonDumbDrive = new FarmingSimulatorDrive();
  public static PIDMovement pidMovement = new PIDMovement();

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
        // TODO Auto-generated method stub
        CommandScheduler.getInstance().schedule(jaxonDumbDrive);
        
    }
  
    @Override
  public void testPeriodic() {
        // TODO Auto-generated method stub

        CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {
    drivetrain.setDrivetrainNeturalMode(NeutralMode.Coast);
  }

  @Override
  public void disabledPeriodic() {
    // TODO Auto-generated method stub
    super.disabledPeriodic();
  }
}