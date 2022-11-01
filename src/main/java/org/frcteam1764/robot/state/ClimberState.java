// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot.state;

/** Add your docs here. */
public class ClimberState {
    private boolean climberPistonsDeployed;
    private int zeroOffset;
	
	public ClimberState() {
        this.climberPistonsDeployed = false;
        this.zeroOffset = 0;
    }
    public boolean isClimberPistonsDeployed(){
        return climberPistonsDeployed;
    }
    public void withdrawClimberPistons(){
        climberPistonsDeployed = false;

    }
    public void deployClimberPistons(){
        climberPistonsDeployed = true;
    }
    public int getOffset(){
        return zeroOffset;
    }

    public void setOffset(int offsetValue){
        zeroOffset = offsetValue;
    }
    

}