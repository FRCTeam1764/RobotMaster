package org.frcteam1764.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import org.frcteam1764.robot.constants.RobotConstants;
import org.frcteam1764.robot.state.IntakeState;
import org.frcteam1764.robot.state.RobotState;
import org.frcteam1764.robot.subsystems.SwerveDrivetrain;

import edu.wpi.first.wpilibj.DigitalInput;

public class RobotSubsystems  {
    /**
     * This is using the swerve drivetrain by default
     * in the case that swerve is not viable it will need to be changed
     */
    public SwerveDrivetrain drivetrain; 
    public Conveyor conveyor;
    public Intake intake;
    public Elevator elevator;
    public Climber climber;
    public Shooter shooter;
    public DigitalInput conveyorBreakBeam;
    public DigitalInput elevatorBreakBeam;
    public DigitalInput shooterBreakBeam;
	
    public RobotSubsystems(RobotState robotState) {
        this.conveyorBreakBeam = new DigitalInput(RobotConstants.CONVEYOR_BREAK_BEAM);
        this.elevatorBreakBeam = new DigitalInput(RobotConstants.ELEVATOR_BREAK_BEAM);
        this.shooterBreakBeam = new DigitalInput(RobotConstants.SHOOTER_BREAK_BEAM);
        this.drivetrain = new SwerveDrivetrain(robotState.drivetrain);
        this.conveyor = new Conveyor(conveyorBreakBeam);
        this.elevator = new Elevator(elevatorBreakBeam);
        this.climber = new Climber(robotState.climber);
        this.shooter = new Shooter();
        this.intake = new Intake(robotState.intake, conveyorBreakBeam, elevatorBreakBeam);
    }
    
    public void setMotorModes(NeutralMode mode){
        drivetrain.setMotorNeutralModes(mode);
    }
}