package org.frcteam1764.robot.state;

public class IntakeState  {
    private boolean intakeDeployed;
	
	public IntakeState() {
        this.intakeDeployed = false;
    }
    public boolean isIntakeDeployed(){
        return intakeDeployed;
    }
    public void withdrawIntake(){
        intakeDeployed = false;

    }
    public void deployIntake(){
        intakeDeployed = true;
    }
}
