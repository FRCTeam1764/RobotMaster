package frc.robot;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Commands.ColorSensor;
import frc.robot.Commands.Drive;
import frc.robot.Commands.PIDControls;
import frc.robot.Commands.PathFollowing;
import frc.robot.Commands.XBoxDrive;
import frc.robot.Commands.LimelightMovement.LimelightDrive;
import frc.robot.Commands.LimelightMovement.LimelightTurn;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.FieldPositioning;

public class Robot extends TimedRobot {

  public static Drivetrain drivetrain = new Drivetrain();
  public static FieldPositioning fieldpos = new FieldPositioning(1); // 0=left, 1=middle, 2=right, driverstation's
                                                                     // perspective
  public static PathFollowing pathfollowing = new PathFollowing();

  Drive drive = new Drive();
  XBoxDrive xboxdrive = new XBoxDrive();
  LimelightTurn llturn = new LimelightTurn();
  public static LimelightDrive lldrive = new LimelightDrive();
  PIDControls pidcontrols = new PIDControls();
  ColorSensor colorsensor = new ColorSensor();

  @Override
  public void autonomousInit() {
    pathfollowing.runCommand().schedule();

    // Scheduler.getInstance().add(llturn);
    // lldrive.isCanceled();

    // pidcontrols.start();
  }

  @Override
  public void autonomousPeriodic() {
    CommandScheduler.getInstance().run();

  }

  @Override
  public void teleopInit() {

    ((Command) drive).schedule();
   // Scheduler.getInstance().add(xboxdrive);
    }

	/**
	 * This function is called periodically during operator control
	 */

  public static NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
	public void teleopPeriodic() {
    CommandScheduler.getInstance().run();
    
    SmartDashboard.putNumber("LimelightSkew", limelightTable.getEntry("ts").getDouble(0));
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

        CommandScheduler.getInstance().run();

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