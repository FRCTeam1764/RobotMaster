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
	private static Path autoPath1 = new SimplePathBuilder(Vector2.ZERO, Rotation2.ZERO)
	.lineTo(new Vector2(40.0, 0.0))
	.lineTo(new Vector2(0.0, -50.0), Rotation2.fromDegrees(-55.0))
	.lineTo(new Vector2(15.0, -100.0), Rotation2.fromDegrees(-55.0))
	.build();
	private static Path autoPath2 = new SimplePathBuilder(Vector2.ZERO, Rotation2.fromDegrees(-55.0))
	.lineTo(new Vector2(-5,100), Rotation2.fromDegrees(15.0))
	// .lineTo(Vector2.ZERO)
	.build();
	private static Path autoPath3 = new SimplePathBuilder(Vector2.ZERO, Rotation2.fromDegrees(15.0))
	.lineTo(new Vector2(120.0, -260.0), Rotation2.fromDegrees(-90.0))
	// .lineTo(Vector2.ZERO)
	.build();
	private static Path autoPath4 = new SimplePathBuilder(Vector2.ZERO, Rotation2.fromDegrees(-55.0))
	.lineTo(new Vector2(-115.0, 260.0), Rotation2.fromDegrees(-25.0))
	// .lineTo(Vector2.ZERO)
	.build();
	
	public static Trajectory[] getTrajectories() {
		Trajectory[] trajectories =  new Trajectory[]{
			Utilities.convertPathToTrajectory(autoPath1, 60.0, 70.0),
			Utilities.convertPathToTrajectory(autoPath2, 60.0, 70.0),
			Utilities.convertPathToTrajectory(autoPath3, 90.0, 170.0),
			Utilities.convertPathToTrajectory(autoPath4, 90.0, 170.0)
		};
		return trajectories;
	}
}
