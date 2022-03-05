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
	.lineTo(new Vector2(35.0, -40.0), Rotation2.fromDegrees(-20.0))
	.lineTo(new Vector2(-30.0, -90.0), Rotation2.fromDegrees(-115.0))
	 .lineTo(new Vector2(-30.0, -140.0), Rotation2.fromDegrees(-115.0))
	 .lineTo(new Vector2(-30.0, -135.0), Rotation2.fromDegrees(-10.0))
	 .lineTo(new Vector2(-15.0, -105.0), Rotation2.fromDegrees(-10.0))
	.build();
	private static Path autoPath2 = new SimplePathBuilder(Vector2.ZERO, Rotation2.fromDegrees(15.0))
	.lineTo(new Vector2(120.0, -260.0), Rotation2.fromDegrees(-90.0))
	.build();
	
	public static Trajectory[] getTrajectories() {
		Trajectory[] trajectories =  new Trajectory[]{
			Utilities.convertPathToTrajectory(autoPath1, 40.0, 60.0),
			Utilities.convertPathToTrajectory(autoPath2, 90.0, 170.0)
		};
		return trajectories;
	}
}
