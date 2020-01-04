package frc.robot.helpers;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LimeLightValues {
    public double xDeg;
    public double yDeg;
    public double area;
    public double skew;
    public boolean hasTarget;


    public LimeLightValues(){
        NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
        this.xDeg = limelightTable.getEntry("tx").getDouble(0);
        this.yDeg = limelightTable.getEntry("ty").getDouble(0);
        this.area = limelightTable.getEntry("ta").getDouble(0);
        this.skew = limelightTable.getEntry("ts").getDouble(0);
        this.hasTarget = Boolean.parseBoolean(limelightTable.getEntry("tv").getString("0"));

        SmartDashboard.putNumber("LimelightXDeg", this.xDeg);
        SmartDashboard.putNumber("LimelightYDeg", this.yDeg);
        SmartDashboard.putNumber("LimelightArea", this.area);
        SmartDashboard.putNumber("LimelightSkew", this.skew);
        SmartDashboard.putBoolean("LimelightHasTarget", this.hasTarget);
    
    }
    
}