package org.frcteam1764.robot.common;

public class Gains {
    public final double kP;
    public final double kI;
    public final double kD;
    public final double kF;
    public final double kPeakOutput;

    public final int kIzone;
    
    public Gains(double p, double i, double d, double f, int izone, double peakoutput){
        kP = p;
        kI = i;
        kD = d;
        kF = f;
        kIzone = izone;
        kPeakOutput = peakoutput;

    }
}
