package frc.robot.libraries.external.util;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.*;
import frc.robot.libraries.external.control.Trajectory;
import frc.robot.RobotContainer;

public class AutonomousChooser {
    private final Trajectory[] trajectories;

    private SendableChooser<AutonomousMode> autonomousModeChooser = new SendableChooser<>();

    private enum AutonomousMode {
        DEFAULT,
        OPTION1
    }

    public AutonomousChooser(Trajectory[] trajectories) {
        this.trajectories = trajectories;

        autonomousModeChooser.setDefaultOption("Default", AutonomousMode.DEFAULT);
        autonomousModeChooser.addOption("Option1", AutonomousMode.OPTION1);
    }

    public SendableChooser<AutonomousMode> getAutonomousModeChooser() {
        return autonomousModeChooser;
    }

    private Command getDefaultAutoCommand(RobotContainer robotContainer) {
        return new FollowTrajectoryCommand(robotContainer.getDrivetrainSubsystem(), trajectories[0]);
    }

    private Command getOption1AutoCommand(RobotContainer robotContainer) {
        return new FollowTrajectoryCommand(robotContainer.getDrivetrainSubsystem(), trajectories[0]);
    }

    public Command getCommand(RobotContainer container) {
        switch (autonomousModeChooser.getSelected()) {
            case DEFAULT:
                return getDefaultAutoCommand(container);
            case OPTION1:
                return getOption1AutoCommand(container);
        }

        return getDefaultAutoCommand(container);
    }
}