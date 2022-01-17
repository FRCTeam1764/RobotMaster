package org.frcteam1764.robot.common;

import org.frcteam1764.robot.subsystems.SwerveDrivetrain;
import org.frcteam2910.common.control.MaxVelocityConstraint;
import org.frcteam2910.common.control.MaxAccelerationConstraint;
import org.frcteam2910.common.control.Path;
import org.frcteam2910.common.control.Trajectory;
import org.frcteam2910.common.control.TrajectoryConstraint;
import org.frcteam2910.common.io.PathReader;

import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;

public class Utilities {
	public static Trajectory convertPathToTrajectory(Path path, double maxAcceleration, double maxVelocity) {
		// With a path and our constraints we can create a trajectory.
		// To create a trajectory, pass in a path and the constraints set above.
		// A third parameter is required called sample distance. This sample distance
		// determines how often the trajectory makes sure that the velocity and acceleration are within
		// the limits determined by the constraints. Smaller values will create a smoother and more accurate path
		// but they will take much longer to generate.

		// Specify some constraints for our trajectory.
		TrajectoryConstraint[] constraints = {
				// Both of these constraints are in seemingly arbitrary units
				new MaxAccelerationConstraint(maxAcceleration), // 30 seems to be a good starting point
				new MaxVelocityConstraint(maxVelocity) // 60 seems to be a good starting point
		};
		if(!path.equals(null)) {
			return new Trajectory(path, constraints, 1e-2);
		}
		else {
			return null;
		}
	}

	public static Path getPath(String filePath) {
        try {
          Reader reader = new FileReader(filePath);
          PathReader pathReader = new PathReader(reader);
          Path path = pathReader.read();
		  pathReader.close();
		  return path;
        }
        catch(IOException e) {
          e.printStackTrace();
		}
		return null;
	}
}
