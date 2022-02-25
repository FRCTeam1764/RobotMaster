package org.frcteam1764.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
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

public class ShooterTopRoller extends SubsystemBase {
  
  public LazyTalonFX shooterTopRoller;

  public double shooterVelocity;
  public double timeDuration = -1;
  public double shooter;
  private ShooterState shooterState;

  public ShooterTopRoller(ShooterState shooterState) {
    this.shooterVelocity = 0;
    this.shooterState = shooterState;
    this.shooterTopRoller = configShooterMotors(RobotConstants.SHOOTER_TOP_ROLLER_MOTOR);
  }

  @Override
  public void periodic() {
    double velocity = shooterTopRoller.getSelectedSensorVelocity(0);
    shooterState.setTopRollerActualVelocity(velocity);
    
  }

  public void shoot() {
    SimpleMotorFeedforward simpleMotorFeedforward = new SimpleMotorFeedforward(0.0, 0.0);
    double kF = simpleMotorFeedforward.calculate(shooterVelocity);
    shooterTopRoller.set(ControlMode.Velocity, shooterVelocity/60*2048*0.1);

  }

  public void shoot(double time){
    Timer timer = new Timer();
    timer.start();

    while(timer.get()<=time){
      shoot();
    }

    timer.stop();
    stopShooter();
  }

  public void stopShooter(){
    shooterTopRoller.set(ControlMode.PercentOutput,0);
  
  }

  public static LazyTalonFX configTalons(int _canId){
    LazyTalonFX talon = new LazyTalonFX(_canId);
    talon.configFactoryDefault();
    talon.setInverted(false);
    talon.enableVoltageCompensation(true);
    talon.configVoltageCompSaturation(12.0, PIDConstants.kTimeoutMs);
    talon.configOpenloopRamp(VoltageConstants.openDriveVoltageRampRate);

    return talon;
  }

  public static LazyTalonFX configShooterMotors(int portNum) {
    LazyTalonFX talon = configTalons(portNum);
    talon.setNeutralMode(NeutralMode.Coast);

    TalonFXConfiguration config = new TalonFXConfiguration();
    config.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
    talon.configAllSettings(config);

    talon.config_kP(PIDConstants.kSlot_Shooter_Velocity, 0.066, PIDConstants.kTimeoutMs);
    talon.config_kI(PIDConstants.kSlot_Shooter_Velocity, 0.0, PIDConstants.kTimeoutMs);
    talon.config_kD(PIDConstants.kSlot_Shooter_Velocity, 0.0015, PIDConstants.kTimeoutMs);
    talon.config_kF(PIDConstants.kSlot_Shooter_Velocity, 0.049, PIDConstants.kTimeoutMs);
    talon.config_IntegralZone(PIDConstants.kSlot_Shooter_Velocity, (int)PIDConstants.kGains_Velocity_Shooter.kIzone, PIDConstants.kTimeoutMs);
    talon.configClosedLoopPeakOutput(PIDConstants.kSlot_Shooter_Velocity, PIDConstants.kGains_Velocity_Shooter.kPeakOutput, PIDConstants.kTimeoutMs);
    talon.configAllowableClosedloopError(PIDConstants.kSlot_Shooter_Velocity, 0, PIDConstants.kTimeoutMs);
    talon.selectProfileSlot(PIDConstants.kSlot_Shooter_Velocity, PIDConstants.PID_PRIMARY);

    talon.enableVoltageCompensation(true);
    talon.configVoltageCompSaturation(12.0);

    return talon;
  }

  public void setShooterTopRollerVelocity(double velocity) {
    this.shooterVelocity = velocity;
  }
}
