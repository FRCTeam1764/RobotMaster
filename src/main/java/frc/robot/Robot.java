package frc.robot;

import com.revrobotics.ColorMatch;
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
    public static FieldPositioning fieldpos = new FieldPositioning(0,0,0);

    Drive drive = new Drive();
    LimelightDrive lldrive = new LimelightDrive();
    PIDControls pidcontrols = new PIDControls();
    ColorSensor colorsensor = new ColorSensor();
    
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

    ColorSensorV3 sensor = new ColorSensorV3(I2C.Port.kOnboard);

  Color detectedColor;

  ColorMatch match = new ColorMatch();

  ColorMatchResult result;

  String colorString;
  
  final Color blueTarget = ColorMatch.makeColor(0.239, 0.477, 0.278);
  final Color greenTarget = ColorMatch.makeColor(0.25, 0.497 , 0.25);
  final Color redTarget = ColorMatch.makeColor(0.304, 0.46, 0.227);
  final Color yellowTarget = ColorMatch.makeColor(0.29, 0.5, 0.2);

    @Override
    public void testInit() {
        // TODO Auto-generated method stub
        //colorsensor.start();

        match.addColorMatch(blueTarget);
        match.addColorMatch(greenTarget);
        match.addColorMatch(redTarget);
        match.addColorMatch(yellowTarget);
        
    }
  
    @Override
    public void testPeriodic() {
        // TODO Auto-generated method stub

        Scheduler.getInstance().run();

        detectedColor = sensor.getColor();

        result = match.matchClosestColor(detectedColor);

        if ( result.color == blueTarget) {
          colorString = "Blue";
        } else if (result.color == redTarget) {
          colorString = "Red";
        } else if (result.color == greenTarget) {
          colorString = "Green";
        } else if (result.color == yellowTarget) {
          colorString = "Yellow";
        } else {
          colorString = "Unknown";
        }

        

    SmartDashboard.putNumber("Red", detectedColor.red);
    SmartDashboard.putNumber("Green", detectedColor.green);
    SmartDashboard.putNumber("Blue", detectedColor.blue);
    SmartDashboard.putNumber("IR", sensor.getIR());
    SmartDashboard.putNumber("Proximity", sensor.getProximity());

    SmartDashboard.putNumber("Confidence", result.confidence);
    SmartDashboard.putString("Detected Color", colorString);
       
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