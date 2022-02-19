// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.state;

public class ShooterState {
    private int countTimer; //Timer loops from 0 to 24, creating a half-second timer for a 50 Hz loop

    public ShooterState() {
        this.countTimer = 0;
    }

    public int getTimer(){
        return countTimer;
    }

    /*
    / Adds one count to the timer
    */
    public void addToTimer(){
        countTimer++;

        if(countTimer>24){
            countTimer=0;
        }
    }
}
