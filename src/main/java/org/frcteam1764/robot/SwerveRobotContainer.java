package org.frcteam1764.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.WaitCommand;
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
        primaryController.getLeftTriggerAxis().getButton(.5).whileHeld(new IntakeBallCommand(robotSubsystems.intake, 0.6, robotSubsystems.conveyor, 1, robotSubsystems.elevator , -0.6, robotState.intake, false));
        // primaryController.getLeftBumperButton().whenPressed(() -> {
        //     robotSubsystems.conveyor.conveyorOn(1, true);
        //     // robotSubsystems.elevator.elevatorOn(-1, true);
        //     // robotSubsystems.intake.intakeOn(1, true);
        // });
        // primaryController.getLeftBumperButton().whenReleased(() -> {
        //     robotSubsystems.conveyor.conveyorOff();
        //     // robotSubsystems.elevator.elevatorOff();
        //     // robotSubsystems.intake.intakeOff();
        // });
        primaryController.getRightTriggerAxis().getButton(.5).whileHeld(new DistanceShooterCommand(robotSubsystems.shooter, robotSubsystems.shooterTopRoller, getRobotState().limelight, robotState.shooter));
    }

    private void configureCoPilotButtonBindings() {
        secondaryController.getRightBumperButton().toggleWhenPressed(new ShooterCommand(robotSubsystems.shooter, robotSubsystems.shooterTopRoller, 5000, robotState.shooter));
        secondaryController.getRightTriggerAxis().getButton(.5).whileHeld(new FeederCommand(robotSubsystems.conveyor, 1, robotSubsystems.elevator, -0.8, robotState.shooter));

        secondaryController.getLeftBumperButton().whileHeld(new NonOverrideFeederCommand(robotSubsystems.conveyor, 1, robotSubsystems.elevator, -0.6, robotState.shooter));//Indexing
        secondaryController.getLeftTriggerAxis().getButton(.5).whileHeld(new IntakeBallCommand(robotSubsystems.intake, 0.6, robotSubsystems.conveyor, 1, robotSubsystems.elevator , -0.6, robotState.intake, false));//intake
        
        // secondaryController.getYButton().whileHeld(new IntakeBallCommand(robotSubsystems.intake, 0.8, robotSubsystems.conveyor, 1,robotSubsystems.elevator, -0.5, robotState.intake, true));//Intake Override
        // secondaryController.getBButton().whileHeld(new IntakeBallCommand(robotSubsystems.intake, 0, robotSubsystems.conveyor, -0.5,robotSubsystems.elevator, 0.5, robotState.intake, true));//unjam

        secondaryController.getDPadButton(Direction.UP).whileHeld(new ClimberCommand(robotSubsystems.climber, 1));
        secondaryController.getDPadButton(Direction.DOWN).whileHeld(new ClimberCommand(robotSubsystems.climber, -1.0));
        secondaryController.getBButton().toggleWhenPressed(new ClimberPneumaticsTestCommand(robotSubsystems.climber));
        secondaryController.getRightJoystickButton().toggleWhenPressed(new IntakePneumaticsTestCommand(robotSubsystems.intake));
        // secondaryController.getYButton().whenPressed(new GoUpCommand(robotSubsystems.climber, 300000)); // testing buttons
        // secondaryController.getXButton().whenPressed(new GoUpCommand(robotSubsystems.climber, 125000)); // testing buttons

        ////////////////////////////////////////////////////////////////////////////////////////////////
        //
        //
        // current extension flow
        //
        //
        /////////////////////////////////////////////////////////////////////////////////////////////////
        
        secondaryController.getAButton().whenPressed(new SequentialCommandGroup(
            new PullDownCommand(robotSubsystems.climber, -0.6),
            new GoUpCommand(robotSubsystems.climber, 90000),
            new GoUpCommand(robotSubsystems.climber, 250000)
        ));
        secondaryController.getXButton().whenPressed(new SequentialCommandGroup(
            new ClimberPneumaticsCommand(robotSubsystems.climber, robotState.climber, true),
            new IntakePneumaticsCommand(robotSubsystems.intake, robotState.intake, true),
            new GoUpCommand(robotSubsystems.climber, 250000)
        ));
        secondaryController.getYButton().whenPressed(new SequentialCommandGroup(
            new GoUpCommand(robotSubsystems.climber, 350000),
            new SimpleWaitCommand(1000),
            new IntakePneumaticsCommand(robotSubsystems.intake, robotState.intake, false),
            new ClimberPneumaticsCommand(robotSubsystems.climber, robotState.climber, false)
        ));

        ////////////////////////////////////////////////////////////////////////////////////////////////
        //
        //
        // next bar swing brace
        //
        //
        /////////////////////////////////////////////////////////////////////////////////////////////////
        
        // secondaryController.getAButton().whenPressed(new SequentialCommandGroup(
        //     new PullDownCommand(robotSubsystems.climber, -0.6),
        //     new GoUpCommand(robotSubsystems.climber, 90000),
        //     new GoUpCommand(robotSubsystems.climber, 300000)
        // ));
        // secondaryController.getXButton().whenPressed(new ParallelCommandGroup(
        //     new ClimberPneumaticsCommand(robotSubsystems.climber, robotState.climber, true),
        //     new GoUpCommand(robotSubsystems.climber, 350000)
        // ));
        // secondaryController.getYButton().whenPressed(new SequentialCommandGroup(
        //     // new IntakePneumaticsCommand(robotSubsystems.intake, robotState.intake, true),
        //     new GoUpCommand(robotSubsystems.climber, 250000),
        //     new GoUpCommand(robotSubsystems.climber, 350000),
        //     new SimpleWaitCommand(1000),
        //     // new IntakePneumaticsCommand(robotSubsystems.intake, robotState.intake, false),
        //     new ClimberPneumaticsCommand(robotSubsystems.climber, robotState.climber, false)
        // ));

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

    public Axis getCopilotRightTriggerAxis() {
        return secondaryController.getRightTriggerAxis();
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
