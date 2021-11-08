package org.frcteam1764.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import org.frcteam1764.robot.subsystems.SwerveDrivetrain;

public class RobotSubsystems  {
    /**
     * This is using the swerve drivetrain by default
     * in the case that swerve is not viable it will need to be changed
     */
    public SwerveDrivetrain drivetrain; 
	
    public RobotSubsystems() {}
    
    public void setMotorModes(NeutralMode mode){
        drivetrain.setMotorNeutralModes(mode);
    }
}