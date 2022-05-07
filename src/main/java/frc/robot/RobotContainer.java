package frc.robot;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
// import frc.robot.util.AutonomousChooser;
// import frc.robot.util.AutonomousTrajectories;
// import frc.robot.util.DriverReadout;
import frc.robot.libraries.external.control.Trajectory;
import frc.robot.libraries.external.math.Rotation2;
import frc.robot.libraries.external.robot.input.Axis;
import frc.robot.libraries.external.robot.input.XboxController;

import java.io.IOException;

public class RobotContainer {
    private final XboxController primaryController = new XboxController(0);

    private final Superstructure superstructure = new Superstructure();

    private final DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem();
    private final VisionSubsystem visionSubsystem = new VisionSubsystem(drivetrainSubsystem);
    private Trajectory[] trajectories;

    // private AutonomousTrajectories autonomousTrajectories;
    // private final AutonomousChooser autonomousChooser;

    // private final DriverReadout driverReadout;

    public RobotContainer() {
        // try {
        //     autonomousTrajectories = new AutonomousTrajectories(DrivetrainSubsystem.TRAJECTORY_CONSTRAINTS);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        // autonomousChooser = new AutonomousChooser(autonomousTrajectories);

        // primaryController.getLeftXAxis().setInverted(true);
        // primaryController.getRightXAxis().setInverted(true);

        CommandScheduler.getInstance().registerSubsystem(visionSubsystem);
        CommandScheduler.getInstance().registerSubsystem(drivetrainSubsystem);

        CommandScheduler.getInstance().setDefaultCommand(drivetrainSubsystem, new DriveCommand(drivetrainSubsystem, getDriveForwardAxis(), getDriveStrafeAxis(), getDriveRotationAxis()));

        // driverReadout = new DriverReadout(this);
        
        setTrajectories();
        configurePilotButtonBindings();
        configureCoPilotButtonBindings();
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

    // public Command getAutonomousCommand() {
    //     return autonomousChooser.getCommand(this);
    // }

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

    // public AutonomousChooser getAutonomousChooser() {
    //     return autonomousChooser;
    // }

    public Trajectory[] getTrajectories() {
        return trajectories;
    }
}