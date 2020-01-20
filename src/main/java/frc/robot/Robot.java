package frc.robot;

import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Commands.*;
import frc.robot.Subsystems.*;
import edu.wpi.first.wpilibj.I2C;

public class Robot extends TimedRobot {

	public static Drivetrain drivetrain =  new Drivetrain();
    public static Limelight limelight = new Limelight();
    Drive drive = new Drive();
    LimelightDrive lldrive = new LimelightDrive();
    PIDControls pidcontrols = new PIDControls();
    ColorSensor colorsensor = new ColorSensor();

    public static double[] robotPos = new double[3]; // robotPos[0] = x-pos, robotPos[1] = y-pos, robotPos[2] = Angle orientation
    
    @Override
    public void autonomousInit() {
        lldrive.start();
        //pidcontrols.start();
    }
    
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        
    }

    @Override
	public void teleopInit() {
		drive.start();
    }

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
    }

    @Override
    public void testInit() {
        // TODO Auto-generated method stub
        colorsensor.start();
    }

    ColorSensorV3 sensor = new ColorSensorV3(I2C.Port.kOnboard);

  Color detectedColor;

  ColorMatchResult match;
  
    @Override
    public void testPeriodic() {
        // TODO Auto-generated method stub
        Scheduler.getInstance().run();

        detectedColor = sensor.getColor();

    SmartDashboard.putNumber("Red", detectedColor.red);
    SmartDashboard.putNumber("Green", detectedColor.green);
    SmartDashboard.putNumber("Blue", detectedColor.blue);
    SmartDashboard.putNumber("IR", sensor.getIR());
    SmartDashboard.putNumber("Proximity", sensor.getProximity());
       
    }
}




/*
package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Commands.*;
import frc.robot.Subsystems.*;

public class Robot extends TimedRobot {

    
    public static Drivetrain drivetrain =  new Drivetrain();
    public static Limelight limelight = new Limelight();
    Drive drive = new Drive();
    LimelightDrive lldrive = new LimelightDrive();
    PIDControls pidcontrols = new PIDControls();

    @Override
    public void autonomousInit() {
        lldrive.start();
        //pidcontrols.start();
    }
    
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        
    }

    @Override
    public void teleopInit() {
        drive.start();
        
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
       
    }

    @Override
    public void robotInit() {
       
    }
}*/