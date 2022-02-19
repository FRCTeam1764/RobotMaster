package org.frcteam1764.robot.state;

import org.frcteam2910.common.control.Trajectory;
import org.frcteam2910.common.robot.drivers.Limelight;
import org.frcteam2910.common.robot.input.Axis;

public class RobotState  {
    /**
     * State of the robot drivetrain
     */
    public DrivetrainState drivetrain;

    /**
     * State of the robot intake
     */
    public IntakeState intake;

    /**
     * State of the robot intake
     */
    public ShooterState shooter;

    /**
     * Limelight used for vision tracking
     */
    public Limelight limelight;

    

    /**
     * Trajectories generated on robot init on robot container
     */
    public Trajectory[] trajectories;
	
	public RobotState(Axis leftTriggerAxis, Axis rightTriggerAxis) {
        this.limelight = new Limelight();
        this.drivetrain = new DrivetrainState(leftTriggerAxis, rightTriggerAxis);
        this.intake = new IntakeState();
    }
}