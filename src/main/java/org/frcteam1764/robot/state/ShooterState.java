// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.state;

public class ShooterState {
    private int timer;
    private int shotCount;
    private double assignedVelocity;
    private double actualVelocity;
    private double assignedTopRollerVelocity;
    private double actualTopRollerVelocity;
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

    public boolean isReady() {
        // double shooterTolerance = 75;
        // double topRollerTolerance = 600;
        boolean shooterAboveTolerance = actualVelocity > ((assignedVelocity - 850)/60*2048*0.1);
        boolean shooterBelowTolerance = actualVelocity < ((assignedVelocity - 850)/60*2048*0.1);
        boolean topRollerAboveTolerance = actualTopRollerVelocity > ((assignedTopRollerVelocity + 1700)/60*2048*0.1);
        boolean topRollerBelowTolerance = actualTopRollerVelocity < ((assignedTopRollerVelocity + 1700)/60*2048*0.1);

        return shooterAboveTolerance && topRollerAboveTolerance;
    }
}
