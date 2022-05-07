package frc.robot;

import frc.robot.common.Utilities;
import frc.robot.libraries.external.control.Path;
import frc.robot.libraries.external.control.SimplePathBuilder;
import frc.robot.libraries.external.control.Trajectory;
import frc.robot.libraries.external.math.Rotation2;
import frc.robot.libraries.external.math.Vector2;

// It is probably preferred that paths are generated using Pathviewer
// Formula for creating a path in code is as follows:
// -new SimplePathBuilder(Vector2.ZERO, Rotation2.ZERO).lineTo().build() or
// -new SplinePathBuilder(Vector2.ZERO, Rotation2.ZERO, Rotation2.ZERO).hermite().build()
// lineTo() and hermite() can be chained for additional segments
// vectors for some reason read (y, x) instead of (x, y)

public class Trajectories {
	private static Path samplePath = new SimplePathBuilder(Vector2.ZERO, Rotation2.ZERO)
		.lineTo(new Vector2(50.0, 0.0))
		// .lineTo(new Vector2(100.0, -100.0))
		// .lineTo(new Vector2(0.0, -100.0))
		// .lineTo(Vector2.ZERO)
		.build();
	
	public static Trajectory[] getTrajectories() {
		Trajectory[] trajectories =  new Trajectory[]{
			Utilities.convertPathToTrajectory(samplePath, 30.0, 60.0)
		};
		return trajectories;
	}
}
