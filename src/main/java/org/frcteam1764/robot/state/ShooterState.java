// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.state;

public class ShooterState {
    private int timer;
    private int ballCount;
    public ShooterState() {
        this.timer = 0;
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

    public int getBallCount(){
        return ballCount;
    }

    public void subtractBallCount(){
        ballCount --;
    }
    public void setBallCount(int ballCount){
        this.ballCount = ballCount;
    }
}
