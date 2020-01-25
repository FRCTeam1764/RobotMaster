/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
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

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {

  }

  final double kP = 0.6 / 29.8; // (kP/MaxDegreesOutputted)
  final double constantSpeed = .12; // Constant added during turning
  double turningSpeed = 0;
  double xDegrees = 0;
  double navxDegrees = 0;

  // DifferentialDrive _diffDrive = Robot.drivetrain._diffD;

  double distanceTalonFX = 0; // Distance needed to be travelled, in Talon FX's arbitary units

  boolean firstCallGetAngle = true;
  boolean firstCallGetDistance = true;
  boolean firstCallStartTimer = true;

  double intAngle;

  TalonFX _rightMaster = Robot.drivetrain._rightMaster;
  TalonFX _leftMaster = Robot.drivetrain._leftMaster;

  final double distanceFromTarget = 120; // The distance the robot needs to stop at in front of the target
                                         // In inches

  @Override
  protected void execute() {

    getXDeg();
    turnRobotToTarget();
    getDistanceToTravel();
    moveRobotForward();

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    _leftMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, 0);
    _rightMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, 0);

    try {
      Robot.limelight.victoryFlash();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    _leftMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, 0);
    _rightMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, 0);
  }

  //Converts the Talon FX's arbitary units to inches
  public double convertUnitToInches(final double units){
    return units * (6*Math.PI)/2048; //2048 units per rotation, Circumference of wheels = 6PI In
  }

  // Opposite of convertUnitToInches()
  public double convertInchesToUnits(final double feet){
    return feet/((6*Math.PI)/2048);
  }

  public void getXDeg(){
    // Aligns robot to target; turning the robot

    // Gets angle from limelight and relates the angle to the navx

    if (firstCallGetAngle) {
      navx.zeroYaw();

      try {
        xDegrees = Robot.limelight.getAngle();
      } catch (final InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      navx.setAngleAdjustment(xDegrees);
      DriverStation.reportError("Angle = " + navx.getAngle() + ", xDegrees = " + xDegrees, true);

      navxDegrees = navx.getAngle();
      firstCallGetAngle = false;

      DriverStation.reportWarning("started", true);

      final double intSpeed = kP * navxDegrees + constantSpeed;
      intAngle = navxDegrees;

      SmartDashboard.putNumber("intSpeed", intSpeed);
      SmartDashboard.putNumber("intAngle", intAngle);

      navx.zeroYaw();
    }
  }


  double errorDegrees;
  public void turnRobotToTarget(){

    errorDegrees = navxDegrees;

  /* while (navxDegrees > 0.8 || navxDegrees < -0.8) {
      turningSpeed = kP * navxDegrees + constantSpeed; //(turningSpeed < .1? (kI) : 0);

      DriverStation.reportError(navxDegrees + "," + turningSpeed, true);
      // xDegrees = limelightTable.getEntry("tx").getDouble(0);

      navxDegrees = navx.getAngle();
      	_leftMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, +turningSpeed);
			_rightMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, -turningSpeed);
    }*/

    while (errorDegrees > 0.3 || errorDegrees < -0.3) {
      turningSpeed = kP * errorDegrees + constantSpeed; //(turningSpeed < .1? (kI) : 0);

      DriverStation.reportError(errorDegrees + "," + turningSpeed, true);
      // xDegrees = limelightTable.getEntry("tx").getDouble(0);

      errorDegrees = intAngle - navx.getYaw();
      _leftMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, +turningSpeed);
			_rightMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, -turningSpeed);
    }
  }

  public void getDistanceToTravel(){
    // Move forward
    if (firstCallGetDistance) {
      try {
        distanceTalonFX = convertInchesToUnits(Robot.limelight.getDistanceFixed() - distanceFromTarget);
      } catch (final InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      DriverStation.reportError("Distance = " + distanceTalonFX, true);

      if(distanceTalonFX<0){
        distanceTalonFX = 0;
      }

      final double intDistance = distanceTalonFX;

      SmartDashboard.putNumber("intDistance", intDistance);
    }

    firstCallGetDistance = false;

    zeroSensors();

    _rightMaster.selectProfileSlot(Constants.kSlot_Distanc, Constants.PID_PRIMARY);
    _rightMaster.selectProfileSlot(Constants.kSlot_Turning, Constants.PID_TURN);
  }

 private double error;
 Timer timer = new Timer();

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

     if(firstCallStartTimer && (error>distanceTalonFX-1 || error<distanceTalonFX+1)){
      timer.start();

      firstCallStartTimer = false;
     }

     if(timer.hasPeriodPassed(5)){ //5 seconds of PID positioning
      isFinished();
     }

  
  }

  void zeroSensors() {
    _leftMaster.getSensorCollection().setIntegratedSensorPosition(0.0, Constants.kTimeoutMs);
    _rightMaster.getSensorCollection().setIntegratedSensorPosition(0.0, Constants.kTimeoutMs);
    System.out.println("All sensors are zeroed.\n");
  }
}
