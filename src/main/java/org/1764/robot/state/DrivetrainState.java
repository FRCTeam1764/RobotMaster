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
     * Angle used in the turning pid loop
     */
	private double currentAngle;

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

    /**
     * Drive speed defaulting to normal
     */
	private boolean driveSpeedFast;

    /**
     * Drive speed defaulting to normal
     */
	private boolean isFieldOriented;
	
	public DrivetrainState(Axis leftTriggerAxis, Axis rightTriggerAxis) {
		this.leftTriggerAxis = leftTriggerAxis;
		this.rightTriggerAxis = rightTriggerAxis;
		this.targetTurningAngle = 0.0;
		this.gyro = new NavX(SPI.Port.kMXP);
        synchronized (sensorLock) {
            gyro.setInverted(true);
        }
		this.maneuver = "";
		this.driveSpeedFast = false;
		this.isFieldOriented = true;
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
		return getGyro().getAngle();
	}

	public double getGyroAngleDegrees () {
    	return getGyro().getAngle().toDegrees();
	}

	public double getGyroRate () {
		return getGyro().getRate();
	}

    public void resetGyroAngle(Rotation2 angle) {
		getGyro().setAdjustmentAngle(
			getGyro().getUnadjustedAngle().rotateBy(angle.inverse())
		);
	}

	public double getTargetTurningAngle() {
		return targetTurningAngle;
	};

	public void setTargetTurningAngle(double angle) {
		targetTurningAngle = angle;
	};

	public double getCurrentAngle() {
		return currentAngle;
	};

	public void setCurrentAngle(double angle) {
		currentAngle = angle;
	};

	public String getManeuver() {
		return maneuver;
	};

	public void setManeuver(String maneuver) {
		double currentAngle = getGyroAngleDegrees();
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
		else if (maneuver.equals("")) {
			this.maneuver = "";
		}
	};

	public void toggleDriveSpeed() {
		driveSpeedFast = !driveSpeedFast;
	}

	public double getDriveSpeed() {
		return driveSpeedFast ? 1.0 : 0.75;
	}

	public void toggleIsFieldOriented() {
		isFieldOriented = !isFieldOriented;
	}

	public boolean getIsFieldOriented() {
		return isFieldOriented;
	}
}