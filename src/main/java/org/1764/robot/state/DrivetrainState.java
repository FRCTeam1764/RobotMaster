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
		this.gyro.setInverted(true);
		this.maneuver = "";
	}

	public boolean isRotationLocked() {
		return leftTriggerAxis.get(true) > 0.5;
	};

	public boolean isStrafeLocked() {
		return rightTriggerAxis.get(true) > 0.5;
	};

	public Gyroscope getGyro() {
		return gyro;
	};

    public void resetGyroAngle(Rotation2 angle) {
		gyro.setAdjustmentAngle(
				gyro.getUnadjustedAngle().rotateBy(angle.inverse())
		);
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
		double currentAngle = gyro.getAngle().toDegrees();
		if (maneuver == "reversebarrelroll") {
			double newAngle = currentAngle - 5.0;
			setTargetTurningAngle(newAngle < 0 ? newAngle + 360 : newAngle);
			this.maneuver = maneuver;
		}
		else if (maneuver == "barrelroll") {
			double newAngle = currentAngle + 5.0;
			setTargetTurningAngle(newAngle > 360 ? newAngle - 360 : newAngle);
			this.maneuver = maneuver;
		}
		else if (maneuver == "spin") {
			double newAngle = currentAngle + 180.0;
			setTargetTurningAngle(newAngle > 360 ? newAngle - 360 : newAngle);
		}
	};
}