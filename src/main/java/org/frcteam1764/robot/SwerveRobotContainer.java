package org.frcteam1764.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.*;

import org.frcteam1764.robot.commands.*;
import org.frcteam1764.robot.subsystems.*;
import org.frcteam2910.common.math.Rotation2;
import org.frcteam2910.common.robot.input.Axis;
import org.frcteam2910.common.robot.input.DPadButton.Direction;
import org.frcteam2910.common.robot.input.XboxController;
import org.frcteam1764.robot.constants.ControllerConstants;
import org.frcteam1764.robot.constants.RobotConstants;
import org.frcteam1764.robot.state.DrivetrainState;
import org.frcteam1764.robot.state.IntakeState;
import org.frcteam1764.robot.state.RobotState;
import org.frcteam1764.robot.Trajectories;

public class SwerveRobotContainer {
    private final XboxController primaryController = new XboxController(ControllerConstants.PRIMARY_CONTROLLER_PORT);
    private final XboxController secondaryController = new XboxController(ControllerConstants.SECONDARY_CONTROLLER_PORT);
    private RobotState robotState = new RobotState(getPilotLeftTriggerAxis(), getPilotRightTriggerAxis());
    private RobotSubsystems robotSubsystems = new RobotSubsystems(robotState);
    private boolean startHeld = false;
    private boolean backHeld = false;
    // public DigitalInput breakBeamElevator = new DigitalInput(RobotConstants.ELEVATOR_BREAK_BEAM);
    // public DigitalInput breakBeamConveyor = new DigitalInput(RobotConstants.CONVEYOR_BREAK_BEAM);
   
    private int count = 0;

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
        secondaryController.getRightBumperButton().toggleWhenPressed(new ShooterCommand(3050, robotState.shooter));
        secondaryController.getLeftBumperButton().whileHeld(new IntakeBallCommand(robotSubsystems.intake, 1, robotSubsystems.conveyor, 1,robotSubsystems.elevator, 1, robotState.intake, true));//Intake Override
        secondaryController.getBButton().whileHeld(new IntakeBallCommand(robotSubsystems.intake, 0, robotSubsystems.conveyor, -.25,robotSubsystems.elevator, -.25, robotState.intake, true));//unjam
        secondaryController.getLeftTriggerAxis().getButton(.5).whileHeld(new IntakeBallCommand(robotSubsystems.intake, .9, robotSubsystems.conveyor, 1, robotSubsystems.elevator ,.81, robotState.intake, false));//intake
        secondaryController.getDPadButton(Direction.UP).whileHeld(new ClimberCommand(robotSubsystems.climber, .75));
        secondaryController.getDPadButton(Direction.DOWN).whileHeld(new ClimberCommand(robotSubsystems.climber, -.75));
        secondaryController.getBackButton().whenPressed(() -> {
            backHeld = true;
            if(startHeld){
               //new AutoClimb(climber, 1);
            }
        });
        secondaryController.getBackButton().whenReleased(() -> toggleBackButton());
        secondaryController.getStartButton().whileHeld(() -> {
            startHeld = true;
            if(backHeld){
                //new AutoClimb(climber, 1);
            }
            
        });
        secondaryController.getStartButton().whenReleased(() -> toggleStartButton());
        //secondaryController.getRightTriggerAxis().getButton(.5).whileHeld(new FeedCommand(robotSubsystems.conveyor, 1, robotSubsystems.elevator, 1));
        secondaryController.getXButton().toggleWhenPressed(new ClimberPneumaticsCommand(robotSubsystems.climber));

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

    private void toggleStartButton() {
        if(startHeld) {
            startHeld = false;
        }
        else {
            startHeld = true;
        }
    }

    private void toggleBackButton() {
        if(backHeld) {
            backHeld = false;
        }
        else {
            backHeld = true;
        }
    }

}
