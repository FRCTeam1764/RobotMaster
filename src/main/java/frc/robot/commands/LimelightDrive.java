/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.Commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Constants;
import frc.robot.LimeLightValues;
import frc.robot.Robot;
import frc.robot.Subsystems.Limelight;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

import edu.wpi.first.hal.sim.DriverStationSim;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LimelightDrive extends Command {
  NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
  AHRS navx;

  public LimelightDrive() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.drivetrain);
    requires(Robot.limelight);

    try {
      navx = new AHRS(SPI.Port.kMXP);
    } catch (RuntimeException ex) {
      DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
    }
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {

  }

  final double kP = 1.2 / 29.8; // (kP/MaxDegreesOutputted)
  final double kI = 0.01;
  double turningSpeed = 0;
  double xDegrees = 0;
  double navxDegrees = 0;

  //DifferentialDrive _diffDrive = Robot.drivetrain._diffD;

  double distanceTalonFX = 0; // Distance needed to be travelled, in Talon FX's arbitary units

  boolean firstCallGetAngle = true;
  boolean firstCallGetDistance = true;

  TalonFX _rightMaster = Robot.drivetrain._rightMaster;
  TalonFX _leftMaster = Robot.drivetrain._leftMaster;

  final double distanceFromTarget = 120; // The distance the robot needs to stop at in front of the target
                                         // In inches

  @Override
  protected void execute() {
    // Aligns robot to target; turning the robot

    // Gets angle from limelight and relates the angle to the navx

   /* if (firstCallGetAngle) {
      navx.zeroYaw();

      try {
        xDegrees = Robot.limelight.getAngle();
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      navx.setAngleAdjustment(xDegrees);
      DriverStation.reportError("Angle = " + navx.getAngle() + ", xDegrees = " + xDegrees, true);

      navxDegrees = navx.getAngle();
      firstCallGetAngle = false;
    }

    while (navxDegrees > 1 || navxDegrees < -1) {
      turningSpeed = kP * navxDegrees + 0.15; //(turningSpeed < .1? (kI) : 0);

      DriverStation.reportError("" + navxDegrees + "," + turningSpeed, true);
      // xDegrees = limelightTable.getEntry("tx").getDouble(0);

      navxDegrees = navx.getAngle();
      	_leftMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, +turningSpeed);
			_rightMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, -turningSpeed);
    }*/

    // Move forward
    if (firstCallGetDistance) {
      try {
        distanceTalonFX = convertInchesToUnits(Robot.limelight.getDistanceFixed() - distanceFromTarget);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      DriverStation.reportError("Distance = " + distanceTalonFX, true);

      if(distanceTalonFX<0){
        distanceTalonFX = 0;
      }
    }

    /* !--------------------------------------!
       Same as motion magic mode in Drive class
       !--------------------------------------! */

    _rightMaster.selectProfileSlot(Constants.kSlot_Distanc, Constants.PID_PRIMARY);
    _rightMaster.selectProfileSlot(Constants.kSlot_Turning, Constants.PID_TURN);
    final double target_turn = _rightMaster.getSelectedSensorPosition(1);

       /*
       * Configured for MotionMagic on Quad Encoders' Sum and Auxiliary PID on Quad
       * Encoders' Difference
       */
    _rightMaster.set(ControlMode.MotionMagic, distanceTalonFX, DemandType.AuxPID, target_turn);
    _leftMaster.follow(_rightMaster, FollowerType.AuxOutput1);

    firstCallGetDistance = false;

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    _leftMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, 0);
    _rightMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, 0);
  }

  //Converts the Talon FX's arbitary units to inches
  public double convertUnitToInches(double units){
    return units * (6*Math.PI)/2048; //2048 units per rotation, Circumference of wheels = 6PI In
  }

  // Opposite of convertUnitToInches()
  public double convertInchesToUnits(double feet){
    return feet/((6*Math.PI)/2048);
  }
}
