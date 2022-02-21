package org.frcteam1764.robot.state;

public class ClimberState  {
    private int zeroOffset;
	
	public ClimberState() {
        this.zeroOffset = 0;
    }
    public int getOffset(){
        return zeroOffset;
    }

    public void setOffset(int offsetValue){
        zeroOffset = offsetValue;
    }
    

}
