/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * Add your docs here.
 */
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
