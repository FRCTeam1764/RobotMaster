package org.frcteam1764.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.frcteam2910.common.robot.UpdateManager;

public class Robot extends TimedRobot {
    private SwerveRobotContainer robotContainer;
    private UpdateManager updateManager;

    @Override
    public void robotInit() {
        robotContainer = new SwerveRobotContainer();
        updateManager = new UpdateManager(
                robotContainer.getDrivetrainSubsystem()
        );
        updateManager.startLoop(5.0e-3);
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void autonomousInit() {
        // this is a sample file read from a pathviewer generated path, not yet tested.
        // todo: need to implement a auto selector/chooser
        // try {
        //   Reader reader = new FileReader("TestPath.path");
        //   PathReader pathReader = new PathReader(reader);
        //   Path path = pathReader.read();
        //   pathReader.close();*/
        // }
        // catch(IOException e) {
        //   e.printStackTrace();
        // }
    }

    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void teleopInit() {
    }
}
