package org.frcteam1764.robot.state;

import com.google.errorprone.annotations.concurrent.GuardedBy;
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

    /**
     * Different Maneuvers
     */
    private String maneuver;
	
	public DrivetrainState(Axis leftTriggerAxis, Axis rightTriggerAxis) {
		this.leftTriggerAxis = leftTriggerAxis;
		this.rightTriggerAxis = rightTriggerAxis;
		this.targetTurningAngle = 0.0;
		this.gyro = new NavX(SPI.Port.kMXP);
        synchronized (sensorLock) {
            gyro.setInverted(true);
        }
		this.maneuver = "";
	}

	public boolean isRotationLocked() {
		return leftTriggerAxis.get(true) > 0.5;
	};

	public boolean isStrafeLocked() {
		return rightTriggerAxis.get(true) > 0.5;
	};

	public Gyroscope getGyro() {
        synchronized (this.sensorLock) {
			return gyro;
		}
	};

	public Rotation2 getGyroAngle () {
		Rotation2 angle;
        synchronized (this.sensorLock) {
            angle = gyro.getAngle();
		}
		return angle;
	}

	public double getGyroRate () {
        double rotationalVelocity;
        synchronized (this.sensorLock) {
            rotationalVelocity = gyro.getRate();
		}
		return rotationalVelocity;
	}

    public void resetGyroAngle(Rotation2 angle) {
        synchronized (sensorLock) {
			gyro.setAdjustmentAngle(
				gyro.getUnadjustedAngle().rotateBy(angle.inverse())
			);
		}
	}

	public double getTargetTurningAngle() {
		return targetTurningAngle;
	};

	public void setTargetTurningAngle(double angle) {
		targetTurningAngle = angle;
	};

	public String getManeuver() {
		return maneuver;
	};

	public void setManeuver(String maneuver) {
		double currentAngle;
        synchronized (this.sensorLock) {
            currentAngle = gyro.getAngle().toDegrees();
		}
		if (maneuver.equals("reversebarrelroll")) {
			double newAngle = currentAngle - 5.0;
			setTargetTurningAngle(newAngle < 0 ? newAngle + 360 : newAngle);
			this.maneuver = maneuver;
		}
		else if (maneuver.equals("barrelroll")) {
			double newAngle = currentAngle + 5.0;
			setTargetTurningAngle(newAngle > 360 ? newAngle - 360 : newAngle);
			this.maneuver = maneuver;
		}
		else if (maneuver.equals("spin")) {
			double newAngle = currentAngle + 180.0;
			setTargetTurningAngle(newAngle > 360 ? newAngle - 360 : newAngle);
		}
	};
}