/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.constants;

/**
 * Add your docs here.
 */
public class RobotDimensionConstants {
    public static final double WHEEL_CIRCUMFERENCE = (6 * Math.PI);
    public static final double ROBOT_WIDTH_CENTER_WHEELS = 26.5;//Width of robot; measured from center wheel to center wheel
	public static final double ROBOT_ROTATION_CIRCUMFERENCE = ROBOT_WIDTH_CENTER_WHEELS * Math.PI;
    public static final double GEAR_BOX_RATIO = (52.0/12)*(52.0/22);
    
    public static final double LIMELIGHT_CAMERA_ANGLE = 20; //45, 60 degrees are also an option
    public static final double LIMELIGHT_HEIGHT_FROM_GROUND = 69.420;
    public static final double LIMELIGHT_DISTANCE_FROM_FRONT = 420.69;
}
