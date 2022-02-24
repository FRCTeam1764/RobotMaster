// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.state;

public class ShooterState {
    private int timer;
    private int shotCount;
    private double assignedVelocity;
    private double actualVelocity;
    public ShooterState() {
        this.timer = 0;
        shotCount = 0;
        assignedVelocity = 0;
        actualVelocity = 0;
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
}
