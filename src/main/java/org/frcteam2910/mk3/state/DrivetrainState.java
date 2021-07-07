package org.frcteam2910.mk3.state;

import edu.wpi.first.wpilibj.SPI;
import org.frcteam2910.common.drivers.Gyroscope;
import org.frcteam2910.common.math.Rotation2;
import org.frcteam2910.common.robot.drivers.NavX;
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

    /**
     * This should be the only instance of the drivetrain gyro
     */
	private final Object sensorLock = new Object();
    @GuardedBy("sensorLock")
    private Gyroscope gyro;
	
	public DrivetrainState(Axis leftTriggerAxis, Axis rightTriggerAxis) {
		this.leftTriggerAxis = leftTriggerAxis;
		this.rightTriggerAxis = rightTriggerAxis;

		this.targetTurningAngle = 0;
		this.gyro = new NavX(SPI.Port.kMXP);
		this.gyro.setInverted(true);
	}

	public boolean isRotationLocked() {
		return this.leftTriggerAxis.get(true) > 0.5;
	};

	public boolean isStrafeLocked() {
		return this.rightTriggerAxis.get(true) > 0.5;
	};

	public Gyroscope getGyro() {
		return targetTurningAngle;
	};

    public void resetGyroAngle(Rotation2 angle) {
        synchronized (sensorLock) {
            gyroscope.setAdjustmentAngle(
                    gyroscope.getUnadjustedAngle().rotateBy(angle.inverse())
            );
        }
	}
	














	// public double getTargetTurningAngle() {
	// 	return targetTurningAngle;
	// };

	// public void setTargetTurningAngle(double angle) {
	// 	this.targetTurningAngle = angle;
	// };
}