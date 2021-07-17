package org.frcteam1764.robot.state;

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
	
	public RobotState() {
        this.limelight = new Limelight();
    }
}