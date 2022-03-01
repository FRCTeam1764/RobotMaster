package org.frcteam1764.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import org.frcteam2910.common.robot.drivers.LazyTalonFX;

import org.frcteam1764.robot.constants.RobotConstants;
import org.frcteam1764.robot.constants.PIDConstants;
import org.frcteam1764.robot.constants.VoltageConstants;
import org.frcteam1764.robot.state.ShooterState;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.frcteam1764.robot.ShuffleBoardInfo;

public class Shooter extends SubsystemBase {
  
  public LazyTalonFX shooterMaster;
  public LazyTalonFX shooterFollower;

  public double shooterVelocity;
  public double timeDuration = -1;
  public double shooter;
  private ShooterState shooterState;
  private DigitalInput shooterBreakBeam;

  public Shooter(ShooterState shooterState, DigitalInput shooterBreakBeam) {
    this.shooterVelocity = 0;
    this.shooterState = shooterState;
    this.shooterBreakBeam = shooterBreakBeam;

    shooterMaster = configShooterMotors(RobotConstants.SHOOTER_MASTER_MOTOR, true, true);
    shooterFollower = configShooterMotors(RobotConstants.SHOOTER_FOLLOWER_MOTOR, false, false);

    shooterFollower.follow(shooterMaster);
    this.shooterFollower.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 150);
  }

  @Override
  public void periodic() {
    double velocity = shooterMaster.getSelectedSensorVelocity(0);
    shooterState.setActualVelocity(velocity);
    
  }

  public void shoot() {
    SimpleMotorFeedforward simpleMotorFeedforward = new SimpleMotorFeedforward(0.0, 0.0);
    double kF = simpleMotorFeedforward.calculate(shooterVelocity);
    shooterMaster.set(ControlMode.Velocity, shooterVelocity/60*2048*0.1);

  }

  public void stopShooter(){
    shooterMaster.set(ControlMode.PercentOutput,0);
  
  }

  public static LazyTalonFX configTalons(int _canId, boolean isMaster, boolean isInverted){
    LazyTalonFX talon = new LazyTalonFX(_canId);
    talon.configFactoryDefault();
    talon.setInverted(isInverted);
    talon.enableVoltageCompensation(true);
    talon.configVoltageCompSaturation(12.0, PIDConstants.kTimeoutMs);

    if(isMaster){
      talon.configOpenloopRamp(VoltageConstants.openDriveVoltageRampRate);
    }

    return talon;
  }

  public static LazyTalonFX configShooterMotors(int portNum, boolean isMaster, boolean isInverted) {
    LazyTalonFX talon = configTalons(portNum, isMaster, isInverted);
    talon.setNeutralMode(NeutralMode.Coast);

    if(isMaster){
      TalonFXConfiguration config = new TalonFXConfiguration();
      config.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
      talon.configAllSettings(config);

      talon.config_kP(PIDConstants.kSlot_Shooter_Velocity, 0.060, PIDConstants.kTimeoutMs);
      talon.config_kI(PIDConstants.kSlot_Shooter_Velocity, 0.0, PIDConstants.kTimeoutMs);
      talon.config_kD(PIDConstants.kSlot_Shooter_Velocity, 0.0009, PIDConstants.kTimeoutMs);
      talon.config_kF(PIDConstants.kSlot_Shooter_Velocity, 0.049, PIDConstants.kTimeoutMs);
      talon.config_IntegralZone(PIDConstants.kSlot_Shooter_Velocity, (int)PIDConstants.kGains_Velocity_Shooter.kIzone, PIDConstants.kTimeoutMs);
      talon.configClosedLoopPeakOutput(PIDConstants.kSlot_Shooter_Velocity, PIDConstants.kGains_Velocity_Shooter.kPeakOutput, PIDConstants.kTimeoutMs);
      talon.configAllowableClosedloopError(PIDConstants.kSlot_Shooter_Velocity, 0, PIDConstants.kTimeoutMs);
      talon.selectProfileSlot(PIDConstants.kSlot_Shooter_Velocity, PIDConstants.PID_PRIMARY);
    }

    talon.enableVoltageCompensation(true);
    talon.configVoltageCompSaturation(12.0);

    return talon;
  }

  public void setShooterVelocity(double velocity) {
    this.shooterVelocity = velocity;
  }

  public boolean ballIsPresent() {
    return shooterBreakBeam.get();
  }
}
