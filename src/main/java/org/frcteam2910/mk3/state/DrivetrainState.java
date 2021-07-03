package org.frcteam2910.mk3.state;
import org.frcteam2910.common.robot.input.Axis;

public class DrivetrainState  {
    /**
     * Robot is locked on target with right x axis disabled when left trigger is held
     */
	private Axis leftTriggerAxis;

    /**
     * Robot is locked on target with left x axis and right x axis disabled when right trigger is held
     */
    private Axis rightTriggerAxis;

    /**
     * Angle used in the turning pid loop
     */
	private double targetTurningAngle;
	
	public DrivetrainState(Axis leftTriggerAxis, Axis rightTriggerAxis) {
		this.leftTriggerAxis = leftTriggerAxis;
		this.rightTriggerAxis = rightTriggerAxis;

		this.targetTurningAngle = 0;
	}

	public boolean isRotationLocked() {
		return this.leftTriggerAxis.get(true) > 0.5;
	};

	public boolean isStrafeLocked() {
		return this.rightTriggerAxis.get(true) > 0.5;
	};

	public double getTargetTurningAngle() {
		return targetTurningAngle;
	};

	public void setTargetTurningAngle(double angle) {
		this.targetTurningAngle = angle;
	};
}