package org.frcteam1764.robot;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoMode.PixelFormat;
import edu.wpi.first.networktables.NetworkTableEntry;

import org.frcteam1764.robot.state.RobotState;

// https://docs.wpilib.org/en/stable/docs/software/driverstation/manually-setting-the-driver-station-to-start-custom-dashboard.html
public class Dashboard {
	RobotState robotState;
	public static void configSmartDashboard(RobotState robotState) {
		initUsbCamera();
		updateSmartDashboard(robotState);
	}
	public static void updateSmartDashboard(RobotState robotState) {
	}
	private static void initUsbCamera() {
		
	}
}
