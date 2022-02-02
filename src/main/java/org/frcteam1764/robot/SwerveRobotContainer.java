package org.frcteam1764.robot;

import edu.wpi.first.wpilibj2.command.*;

import org.frcteam1764.robot.commands.ConveyorCommand;
import org.frcteam1764.robot.commands.ElevatorCommand;
import org.frcteam1764.robot.commands.ShooterCommand;
import org.frcteam1764.robot.commands.SwerveDriveCommand;
import org.frcteam1764.robot.commands.ShooterCommand.ShooterControlMode;
import org.frcteam1764.robot.subsystems.Conveyor;
import org.frcteam1764.robot.subsystems.Elevator;
import org.frcteam1764.robot.subsystems.Intake;
import org.frcteam1764.robot.subsystems.RobotSubsystems;
import org.frcteam1764.robot.subsystems.SwerveDrivetrain;
import org.frcteam2910.common.math.Rotation2;
import org.frcteam2910.common.robot.input.Axis;
import org.frcteam2910.common.robot.input.DPadButton.Direction;
import org.frcteam2910.common.robot.input.XboxController;
import org.frcteam1764.robot.constants.ControllerConstants;
import org.frcteam1764.robot.state.DrivetrainState;
import org.frcteam1764.robot.state.RobotState;
import org.frcteam1764.robot.Trajectories;

public class SwerveRobotContainer {
    private final XboxController primaryController = new XboxController(ControllerConstants.PRIMARY_CONTROLLER_PORT);
    private final XboxController secondaryController = new XboxController(ControllerConstants.SECONDARY_CONTROLLER_PORT);
    private RobotState robotState = new RobotState(getPilotLeftTriggerAxis(), getPilotRightTriggerAxis());
    private RobotSubsystems robotSubsystems = new RobotSubsystems(robotState);
    private Elevator elevator = new Elevator();
    private Conveyor conveyor = new Conveyor();
   // private Intake intake = new Intake();

    public SwerveRobotContainer() {
        getTrajectories();
        configurePilotButtonBindings();
        configureCoPilotButtonBindings();

        CommandScheduler.getInstance().setDefaultCommand(robotSubsystems.drivetrain, new SwerveDriveCommand(robotSubsystems.drivetrain, getPilotDriveForwardAxis(), getPilotDriveStrafeAxis(), getPilotDriveRotationAxis(), this.robotState));
    }

    private void getTrajectories() {
        robotState.trajectories = Trajectories.getTrajectories();
    }

    private void configurePilotButtonBindings() {
        primaryController.getLeftXAxis().setInverted(true);
        primaryController.getRightXAxis().setInverted(true);
        primaryController.getBackButton().whenPressed(
            () -> robotState.drivetrain.resetGyroAngle(Rotation2.ZERO)
        );
        primaryController.getStartButton().whenPressed(
            () -> {
                robotState.drivetrain.toggleDriveSpeed();
                robotSubsystems.drivetrain.setDrivetrainMaxOutput(robotState.drivetrain.getDriveSpeed());
            }
        );
        primaryController.getAButton().whenPressed(() -> robotState.drivetrain.setTargetTurningAngle(ControllerConstants.CRITICAL_ANGLE_A));
        primaryController.getBButton().whenPressed(() -> robotState.drivetrain.setTargetTurningAngle(ControllerConstants.CRITICAL_ANGLE_B));
        primaryController.getXButton().whenPressed(() -> robotState.drivetrain.setTargetTurningAngle(ControllerConstants.CRITICAL_ANGLE_X));
        primaryController.getYButton().whenPressed(() -> robotState.drivetrain.setTargetTurningAngle(ControllerConstants.CRITICAL_ANGLE_Y));
        primaryController.getLeftBumperButton().whenPressed(() -> robotState.drivetrain.setManeuver("barrelroll"));
        primaryController.getRightBumperButton().whenPressed(() -> robotState.drivetrain.setManeuver("reversebarrelroll"));
        primaryController.getRightJoystickButton().whenPressed(() -> robotState.drivetrain.setManeuver("spin"));
        primaryController.getDPadButton(Direction.DOWN).whenPressed(() -> robotState.drivetrain.toggleIsFieldOriented());
    }

    private void configureCoPilotButtonBindings() {
        secondaryController.getAButton().whenHeld(new ElevatorCommand(elevator, .4));
        secondaryController.getYButton().whenHeld(new ConveyorCommand(conveyor, .4));
        secondaryController.getBButton().whenHeld(new ShooterCommand(.5, ShooterControlMode.PID));
      //  secondaryController.getBButton().whenHeld(new IntakeCommand(intake, .5));
    }

    private Axis getPilotDriveForwardAxis() {
        return primaryController.getLeftYAxis();
    }

    private Axis getPilotDriveStrafeAxis() {
        return primaryController.getLeftXAxis();
    }

    private Axis getPilotDriveRotationAxis() {
        return primaryController.getRightXAxis();
    }

    private Axis getPilotLeftTriggerAxis() {
        return primaryController.getLeftTriggerAxis();
    }

    private Axis getPilotRightTriggerAxis() {
        return primaryController.getRightTriggerAxis();
    }

    public XboxController getPrimaryController() {
        return primaryController;
    }

    public XboxController getSecondaryController() {
        return secondaryController;
    }

    public RobotState getRobotState() {
        return robotState;
    }

    public RobotSubsystems getRobotSubsystems() {
        return robotSubsystems;
    }
}
