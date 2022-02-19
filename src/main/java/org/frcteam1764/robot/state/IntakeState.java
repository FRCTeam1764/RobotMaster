package org.frcteam1764.robot.state;

public class IntakeState  {
    private boolean intakeDeployed;
    private int count;
	
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

    public void resetCount( ) {
        count=0;
    }
    public void incrementCount() {
        count++;
    }
    public int getCount() {
        return count;
    }

}
