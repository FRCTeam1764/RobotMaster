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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.constants.PIDConstants;
import frc.robot.util.Limelight;
import frc.robot.Robot;

public class LimelightDrive extends CommandBase {

  NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
  AHRS navx;

  public LimelightDrive() {
    // Use requires() here to declare subsystem dependencies
    addRequirements(Robot.drivetrain);

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
  public boolean travelledToTarget = false;

  double intAngle;

  TalonFX _rightMaster = Robot.drivetrain.rightTalons[0];
  TalonFX _leftMaster = Robot.drivetrain.leftTalons[0];

  final double distanceFromTarget = 120; // The distance the robot needs to stop at in front of the target
                                         // In inches

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    getDistanceToTravel();

  }

  @Override
  public void execute() {

    /*
     * getXDeg(); turnRobotToTarget();
     */
    moveRobotForward();

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
public boolean isFinished() {
  return travelledToTarget;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {
    _leftMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, 0);
    _rightMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, 0);
  }

  // Converts the Talon FX's arbitary units to inches
  public double convertUnitToInches(final double units) {
    return units / PIDConstants.CLICKS_PER_INCH;
  }

  // Opposite of convertUnitToInches()
  public double convertInchesToUnits(final double inches) {
    return inches * PIDConstants.CLICKS_PER_INCH;
  }


  public void getDistanceToTravel() {
    if (firstCallGetDistance) {
      // Stops motors
      _leftMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, 0);
      _rightMaster.set(ControlMode.PercentOutput, 0, DemandType.ArbitraryFeedForward, 0);

      try {
        distanceTalonFX = convertInchesToUnits(Limelight.getDistanceFixed() - distanceFromTarget);
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

     if(error>(distanceTalonFX-convertInchesToUnits(1)) && error<(distanceTalonFX+convertInchesToUnits(1))){ // allow +/-3 inches of error
      

      travelledToTarget =  true;
     }
  
  }

  void zeroSensors() {
    _leftMaster.getSensorCollection().setIntegratedSensorPosition(0.0, PIDConstants.kTimeoutMs);
    _rightMaster.getSensorCollection().setIntegratedSensorPosition(0.0, PIDConstants.kTimeoutMs);
    System.out.println("All sensors are zeroed.\n" );
  }
}
