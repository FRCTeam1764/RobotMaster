package org.frcteam1764.robot;

import org.frcteam1764.robot.common.Utilities;
import org.frcteam2910.common.control.Path;
import org.frcteam2910.common.control.SimplePathBuilder;
import org.frcteam2910.common.control.Trajectory;
import org.frcteam2910.common.math.Rotation2;
import org.frcteam2910.common.math.Vector2;

// It is probably preferred that paths are generated using Pathviewer
// Formula for creating a path in code is as follows:
// -new SimplePathBuilder(Vector2.ZERO, Rotation2.ZERO).lineTo().build() or
// -new SplinePathBuilder(Vector2.ZERO, Rotation2.ZERO, Rotation2.ZERO).hermite().build()
// lineTo() and hermite() can be chained for additional segments
// vectors for some reason read (y, x) instead of (x, y)

public class Trajectories {
	private static Path firstPath = new SimplePathBuilder(Vector2.ZERO, Rotation2.ZERO)
	.lineTo(new Vector2(35.0, 0.0))
	.lineTo(new Vector2(0.0, -50.0), Rotation2.fromDegrees(-55.0))
	.lineTo(new Vector2(-15.0, -125.0), Rotation2.fromDegrees(-55.0))
	.lineTo(new Vector2(-55.0, -80.0), Rotation2.fromDegrees(-55.0))
	// .lineTo(Vector2.ZERO)
	.build();
	
	public static Trajectory[] getTrajectories() {
		Trajectory[] trajectories =  new Trajectory[]{
			Utilities.convertPathToTrajectory(firstPath, 30.0, 60.0)
		};
		return trajectories;
	}
}
