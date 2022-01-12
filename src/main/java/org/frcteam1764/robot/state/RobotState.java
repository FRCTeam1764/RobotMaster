package org.frcteam1764.robot.state;

import org.frcteam2910.common.control.Trajectory;
import org.frcteam2910.common.robot.drivers.Limelight;

public class RobotState  {
    /**
     * State of the robot drivetrain
     */
    public DrivetrainState drivetrain;

    /**
     * Limelight used for vision tracking
     */
    public Limelight limelight;

    /**
     * Trajectories generated on robot init on robot container
     */
    public Trajectory[] trajectories;
	
	public RobotState() {
        this.limelight = new Limelight();
    }
}