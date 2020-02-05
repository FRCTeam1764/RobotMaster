/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands.LimelightMovement;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import java.util.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.PIDConstants;
import frc.robot.Robot;

public class LimelightDrive extends Command {

  NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
  AHRS navx;

  public LimelightDrive() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.drivetrain);
    requires(Robot.limelight);

    try {
      navx = new AHRS(SPI.Port.kMXP);
    } catch (final RuntimeException ex) {
      DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
    }
  }


  final double kP = 0.11 / 29.8; // (kP/MaxDegreesOutputted)
  final double constantSpeed = 0.03; // Constant added during turning
  final double deadband = 0.45;
  double turningSpeed = 0;
  double xDegrees = 0;
  double navxDegrees = 0;

  // DifferentialDrive _diffDrive = Robot.drivetrain._diffD;

  double distanceTalonFX = 0; // Distance needed to be travelled, in Talon FX's arbitary units

  boolean firstCallGetDistance = true;
  boolean firstCallStartTimer = true;
  public static boolean travelledToTarget = false;

  double intAngle;

  Timer timer = new Timer();

  TalonFX _rightMaster = Robot.drivetrain._rightMaster;
  TalonFX _leftMaster = Robot.drivetrain._leftMaster;

  final double distanceFromTarget = 120; // The distance the robot needs to stop at in front of the target
                                         // In inches

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    getDistanceToTravel();

  }

  @Override
  protected void execute() {

    /*getXDeg();
    turnRobotToTarget();*/
    moveRobotForward();

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
public boolean isFinished() {
  return travelledToTarget;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    _leftMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, 0);
    _rightMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, 0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    _leftMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, 0);
    _rightMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, 0);
  }

  private double unitToInchFactor = (6 * Math.PI) / (2048*9.5); // 2048 units per rotation of motor, ~9.5 gear box ratio, Circumference of wheels = 6PI in

  // Converts the Talon FX's arbitary units to inches
  public double convertUnitToInches(final double units) {
    return units * unitToInchFactor;
  }

  // Opposite of convertUnitToInches()
  public double convertInchesToUnits(final double inches) {
    return inches / unitToInchFactor;
  }


  public void getDistanceToTravel() {
    if (firstCallGetDistance) {
      // Stops motors
      _leftMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, 0);
      _rightMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, 0);

      try {
        distanceTalonFX = convertInchesToUnits(Robot.limelight.getDistanceFixed() - distanceFromTarget);
      } catch (final InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      DriverStation.reportError("Distance = " + distanceTalonFX, true);

      if (distanceTalonFX < 0) {
        distanceTalonFX = 0;
      }

      final double intDistance = distanceTalonFX;

      SmartDashboard.putNumber("intDistance", intDistance);
    }

    firstCallGetDistance = false;

    _rightMaster.selectProfileSlot(PIDConstants.kSlot_Distanc, PIDConstants.PID_PRIMARY);
    _rightMaster.selectProfileSlot(PIDConstants.kSlot_Turning, PIDConstants.PID_TURN);

    zeroSensors();
  }

 private double error;

  public void moveRobotForward(){

    /* !--------------------------------------!
       Same as motion magic mode in Drive class
       !--------------------------------------! */


      final double target_turn = _rightMaster.getSelectedSensorPosition(1);

       /*
       * Configured for MotionMagic on Quad Encoders' Sum and Auxiliary PID on Quad
       * Encoders' Difference
       */
     _rightMaster.set(ControlMode.MotionMagic, distanceTalonFX, DemandType.AuxPID, target_turn);
     _leftMaster.follow(_rightMaster, FollowerType.AuxOutput1);

     error = _rightMaster.getSensorCollection().getIntegratedSensorPosition();

     //DriverStation.reportError(""+_rightMaster.getSensorCollection().getIntegratedSensorVelocity()+","+error, true);

     if(error>(distanceTalonFX-convertInchesToUnits(3)) && error<(distanceTalonFX+convertInchesToUnits(3)) && firstCallStartTimer){ // allow +/-3 inches of error
      timer.schedule(new StopRobot(), 2000);
      

      firstCallStartTimer = false;
     }
  
  }

  void zeroSensors() {
    _leftMaster.getSensorCollection().setIntegratedSensorPosition(0.0, PIDConstants.kTimeoutMs);
    _rightMaster.getSensorCollection().setIntegratedSensorPosition(0.0, PIDConstants.kTimeoutMs);
    System.out.println("All sensors are zeroed.\n" );
  }
}
