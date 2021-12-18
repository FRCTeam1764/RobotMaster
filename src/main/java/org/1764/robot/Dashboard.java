package org.frcteam1764.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import org.frcteam1764.robot.state.RobotState;

// https://docs.wpilib.org/en/stable/docs/software/driverstation/manually-setting-the-driver-station-to-start-custom-dashboard.html
public class Dashboard {
	public static void configSmartDashboard(RobotState robotState) {
		initUsbCamera();
		updateSmartDashboard(robotState);
	}
	public static void updateSmartDashboard(RobotState robotState) {
	}

	private static void initUsbCamera() {
		// CameraServer.getInstance().startAutomaticCapture();
		// CvSink cvSink = CameraServer.getInstance().getVideo();
		// CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
	}
}
