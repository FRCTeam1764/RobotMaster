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
		UsbCamera usbCamera = new UsbCamera("USB Camera 0", 0);
		MjpegServer mjpegServer1 = new MjpegServer("serve_USB Camera 0", 1181);
		mjpegServer1.setSource(usbCamera);
	
		// Creates the CvSink and connects it to the UsbCamera
		CvSink cvSink = new CvSink("opencv_USB Camera 0");
		cvSink.setSource(usbCamera);
	
		// Creates the CvSource and MjpegServer [2] and connects them
		CvSource outputStream = new CvSource("Blur", PixelFormat.kMJPEG, 640, 480, 30);
		MjpegServer mjpegServer2 = new MjpegServer("serve_Blur", 1182);
		mjpegServer2.setSource(outputStream);
		// Creates UsbCamera and MjpegServer [1] and connects them
		ShuffleboardTab cameraTab = Shuffleboard.getTab("Camera Feed");
		NetworkTableEntry usbCameraFeed = Shuffleboard.getTab("Camera Feed")
		.add("My Number", 0)
		.withWidget(BuiltInWidgets.kCameraStream)
		.getEntry(); 
		// CameraServer.startAutomaticCapture();
		// CvSink cvSink = CameraServer.getVideo();
		// CvSource outputStream = CameraServer.putVideo("Blur", 640, 480);
	}
}
