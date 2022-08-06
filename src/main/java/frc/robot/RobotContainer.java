package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.libraries.external.util.AutonomousChooser;
import frc.robot.libraries.external.control.Trajectory;
import frc.robot.libraries.external.math.Rotation2;
import frc.robot.libraries.external.robot.input.Axis;
import frc.robot.libraries.external.robot.input.XboxController;

public class RobotContainer {
    private final XboxController primaryController = new XboxController(0);

    private final Superstructure superstructure = new Superstructure();

    private final DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem();
    private final VisionSubsystem visionSubsystem = new VisionSubsystem(drivetrainSubsystem);
    private Trajectory[] trajectories;
    private final AutonomousChooser autonomousChooser;

    public RobotContainer() {
        primaryController.getLeftXAxis().setInverted(true);
        primaryController.getRightXAxis().setInverted(true);

        CommandScheduler.getInstance().registerSubsystem(visionSubsystem);
        CommandScheduler.getInstance().registerSubsystem(drivetrainSubsystem);

        CommandScheduler.getInstance().setDefaultCommand(drivetrainSubsystem, new DriveCommand(drivetrainSubsystem, getDriveForwardAxis(), getDriveStrafeAxis(), getDriveRotationAxis()));
        
        setTrajectories();
        configurePilotButtonBindings();
        configureCoPilotButtonBindings();
        autonomousChooser = new AutonomousChooser(trajectories);
    }

    private void setTrajectories() {
        trajectories = Trajectories.getTrajectories();
    }

    private void configurePilotButtonBindings() {
        primaryController.getBackButton().whenPressed(
                () -> drivetrainSubsystem.resetGyroAngle(Rotation2.ZERO)
        );
    }

    private void configureCoPilotButtonBindings() {
    }

    public Command getAutonomousCommand() {
        return autonomousChooser.getCommand(this);
    }

    private Axis getDriveForwardAxis() {
        return primaryController.getLeftYAxis();
    }

    private Axis getDriveStrafeAxis() {
        return primaryController.getLeftXAxis();
    }

    private Axis getDriveRotationAxis() {
        return primaryController.getRightXAxis();
    }

    public DrivetrainSubsystem getDrivetrainSubsystem() {
        return drivetrainSubsystem;
    }

    public Superstructure getSuperstructure() {
        return superstructure;
    }

    public VisionSubsystem getVisionSubsystem() {
        return visionSubsystem;
    }

    public XboxController getPrimaryController() {
        return primaryController;
    }

    public AutonomousChooser getAutonomousChooser() {
        return autonomousChooser;
    }

    public Trajectory[] getTrajectories() {
        return trajectories;
    }
}