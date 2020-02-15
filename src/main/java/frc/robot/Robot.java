package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Commands.FarmingSimulatorDrive;
import frc.robot.Commands.JoystickDrive;
import frc.robot.Commands.XBoxDrive;
import frc.robot.Commands.LimelightMovement.LimelightDrive;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.AutoSubsystems.PIDMovement;

public class Robot extends TimedRobot {

  public static Drivetrain drivetrain = new Drivetrain();

  JoystickDrive joystickdrive = new JoystickDrive();
  XBoxDrive xboxdrive = new XBoxDrive();
  FarmingSimulatorDrive jaxonDumbDrive = new FarmingSimulatorDrive();
  public static PIDMovement pidMovement = new PIDMovement();

  public static LimelightDrive lldrive = new LimelightDrive();

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

    CommandScheduler.getInstance().schedule(joystickdrive);
   // CommandScheduler.getInstance().schedule(xboxdrive);
   // CommandScheduler.getInstance().schedule(jaxonDumbDrive);
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