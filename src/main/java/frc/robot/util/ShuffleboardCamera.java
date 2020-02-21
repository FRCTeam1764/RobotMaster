/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/**
 * Add your docs here.
 */
public class ShuffleboardCamera {
    public static ShuffleboardTab cameraTab = Shuffleboard.getTab("Camera Feed");

    public static int cameraPort = 0;
    public static UsbCamera camera;

    public static NetworkTableEntry usbCameraFeed = Shuffleboard.getTab("Robot")
    .add("My Number", 0)
    .withWidget(BuiltInWidgets.kCameraStream)
    .getEntry();
}
