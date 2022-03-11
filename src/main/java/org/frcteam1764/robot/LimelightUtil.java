// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot;

import org.frcteam1764.robot.state.RobotState;
import org.frcteam1764.robot.subsystems.RobotSubsystems;
import org.frcteam2910.common.robot.drivers.Limelight;

/** Add your docs here. */
public class LimelightUtil {

    
    private Limelight limelight;
    private double yOffset;
    private double xOffset;
    private double limelightUpperYTolerance;
    private double limelightLowerYTolerance;
    private double limelightUpperXTolerance;
    private double limelightLowerXTolerance;
    private boolean robotRotationReady;
    private boolean robotDistanceReady;
    private RobotSubsystems subsystems;
    private RobotState state;
    
    public LimelightUtil(RobotState state, RobotSubsystems subsystems){
        this.subsystems = subsystems;
        this.state = state;
        limelight = state.limelight;
        yOffset = limelight.getTargetYOffset();
        xOffset = limelight.getTargetXOffset();
        limelightUpperYTolerance = -2.0;
        limelightLowerYTolerance = -7.0;
        limelightUpperXTolerance = 2.0;
        limelightLowerXTolerance = -2.0;
        robotRotationReady = xOffset > limelightLowerXTolerance && xOffset < limelightUpperXTolerance;
        robotDistanceReady = yOffset > limelightLowerYTolerance && yOffset < limelightUpperYTolerance;
    }

    public void runShooter(){
        System.out.println(limelight.getTargetYOffset());
        if(limelight.hasTarget() && state.shooter.isReady() && robotRotationReady() && robotDistanceReady()){
            // state.drivetrain.disable();
            state.isShooting = true;
            subsystems.conveyor.conveyorOn(1, true);
            subsystems.elevator.elevatorOn(-1, true);
        }
        else if(state.isShooting){
            // state.drivetrain.enable();
            subsystems.conveyor.conveyorOff();
            subsystems.elevator.elevatorOff();
            state.isShooting = false;
            
        }
    }

    public boolean robotRotationReady(){
        return limelightUpperXTolerance > limelight.getTargetXOffset() && limelight.getTargetXOffset() > limelightLowerXTolerance;
    }
    public boolean robotDistanceReady(){
        return limelightUpperYTolerance > limelight.getTargetYOffset() && limelight.getTargetYOffset() > limelightLowerYTolerance;
    }
}
