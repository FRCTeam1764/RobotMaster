package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LimeLightValues {
    public static double xDeg;
    public static double yDeg;
    public static double area;
    public static double skew;
    public static boolean hasTarget;


    public LimeLightValues(){
        NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
        xDeg = limelightTable.getEntry("tx").getDouble(0);
        yDeg = limelightTable.getEntry("ty").getDouble(0);
        area = limelightTable.getEntry("ta").getDouble(0);
        skew = limelightTable.getEntry("ts").getDouble(0);
        hasTarget = Boolean.parseBoolean(limelightTable.getEntry("tv").getString("0"));

        SmartDashboard.putNumber("LimelightXDeg", xDeg);
        SmartDashboard.putNumber("LimelightYDeg", yDeg);
        SmartDashboard.putNumber("LimelightArea", area);
        SmartDashboard.putNumber("LimelightSkew", skew);
        SmartDashboard.putBoolean("LimelightHasTarget", hasTarget);
    
    }
    
}