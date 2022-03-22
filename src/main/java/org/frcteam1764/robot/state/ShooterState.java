// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.state;

import org.frcteam1764.robot.common.LinearInterpolator;

public class ShooterState {
    private int timer;
    private int shotCount;
    private double assignedVelocity;
    private double actualVelocity;
    private double assignedTopRollerVelocity;
    private double actualTopRollerVelocity;
    private double shooterDistance;
    public final LinearInterpolator shooterInterpolator;
    public final LinearInterpolator shooterTopRollerInterpolator;

    public static final double[][] SHOOTER_SPEED_ARRAY = {
        {0, 5461},
        {-4.5, 5120},
        {-7.25, 3603},
        {-9.75, 3262},
        {-12.25, 3271},
        {-15.25, 3282}
    };
    public static final double[][] SHOOTER_TOP_SPEED_ARRAY = {
        {0, 8192},
        {-4.5, 10240},
        {-7.25, 12971},
        {-9.75, 14677},
        {-12.25, 15701},
        {-15.25, 17067}
    };


    public ShooterState() {
        timer = 0;
        shotCount = 0;
        assignedVelocity = 0;
        actualVelocity = 0;
        this.shooterDistance = 0;
		this.shooterInterpolator = new LinearInterpolator(SHOOTER_SPEED_ARRAY);
        this.shooterTopRollerInterpolator = new LinearInterpolator(SHOOTER_TOP_SPEED_ARRAY);
    }

    public int getTimer(){
        return timer;
    }

    /*
    / Adds one count to the timer
    */
    public void addToTimer(){
        timer++;
    }

    public void clearTimer(){
        timer = 0;
    }

    public int getShotCount(){
        return shotCount;
    }

    public void addShotCount(){
        shotCount++;
    }
    public void setShotCount(int shotCount){
        this.shotCount = shotCount;
    }

    public void setAssignedVelocity(double velocity) {
        this.assignedVelocity = velocity;
    }

    public double getAssignedVelocity() {
        return assignedVelocity;
    }

    public void setActualVelocity(double velocity) {
        this.actualVelocity = velocity;
    }

    public double getActualVelocity() {
        return actualVelocity;
    }

    public void setTopRollerAssignedVelocity(double velocity) {
        this.assignedTopRollerVelocity = velocity;
    }

    public double getTopRollerAssignedVelocity() {
        return assignedTopRollerVelocity;
    }

    public void setTopRollerActualVelocity(double velocity) {
        this.actualTopRollerVelocity = velocity;
    }

    public double getTopRollerActualVelocity() {
        return actualTopRollerVelocity;
    }

    public void setShooterDistance(double shooterDistance) {
        this.shooterDistance = shooterDistance;
    }

    public double getShooterDistance() {
        return shooterDistance;
    }

    public boolean isReady() {
        return actualTopRollerVelocity > assignedTopRollerVelocity && actualVelocity > assignedVelocity;
    }
}
