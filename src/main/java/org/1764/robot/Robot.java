package org.frcteam1764.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.frcteam1764.robot.commands.AutoGroupCommand;
import org.frcteam2910.common.robot.UpdateManager;

public class Robot extends TimedRobot {
    private SwerveRobotContainer robotContainer;
    private UpdateManager updateManager;

    @Override
    public void robotInit() {
        robotContainer = new SwerveRobotContainer();
        updateManager = new UpdateManager(
                robotContainer.getRobotSubsystems().drivetrain
        );
        updateManager.startLoop(5.0e-3);
        Dashboard.configSmartDashboard(robotContainer.getRobotState());
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        Dashboard.updateSmartDashboard(robotContainer.getRobotState());
    }

    @Override
    public void autonomousInit() {
        new AutoGroupCommand(robotContainer.getRobotState(), robotContainer.getRobotSubsystems());
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
