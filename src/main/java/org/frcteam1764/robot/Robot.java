package org.frcteam1764.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import org.frcteam1764.robot.commands.*;
import org.frcteam1764.robot.state.IntakeState;
import org.frcteam1764.robot.state.RobotState;
import org.frcteam1764.robot.subsystems.*;
import org.frcteam2910.common.robot.drivers.Limelight;
import org.frcteam2910.common.robot.drivers.Limelight.LedMode;
import org.frcteam2910.common.math.Rotation2;
import org.frcteam2910.common.robot.UpdateManager;

public class Robot extends TimedRobot {
    private SwerveRobotContainer robotContainer;
    private UpdateManager updateManager;
    private ShuffleBoardInfo sbiInstance = ShuffleBoardInfo.getInstance();
    private RobotState state = robotContainer.getRobotState();
    private RobotSubsystems subsystems = robotContainer.getRobotSubsystems();
   

    @Override
    public void robotInit() {
        robotContainer = new SwerveRobotContainer();
        updateManager = new UpdateManager(
                robotContainer.getRobotSubsystems().drivetrain
        );
        updateManager.startLoop(5.0e-3);
        Dashboard.configSmartDashboard(robotContainer.getRobotState());
        state.limelight.setLedMode(LedMode.OFF);
        subsystems.intake.intakeOff();
        subsystems.climber.pneumaticsWithdraw();;
    }


    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        Dashboard.updateSmartDashboard(robotContainer.getRobotState());
    }

    @Override
    public void autonomousInit() {
        Shooter shooter = subsystems.shooter;
        subsystems.setMotorModes(NeutralMode.Coast);
        state.drivetrain.resetGyroAngle(Rotation2.ZERO);
        CommandScheduler.getInstance().schedule(
            new SequentialCommandGroup(
                new ParallelRaceGroup(
                    new AutoShooterCommand(shooter, subsystems.shooterTopRoller, 3050, state.shooter, 1),
                    new FeederCommand(subsystems.conveyor, 1, subsystems.elevator, 1, state.shooter)
                ),
                new ParallelRaceGroup(
                    new FollowPathCommand(subsystems.drivetrain, state.trajectories[0]),
                    new IntakeBallCommand(subsystems.intake, 1, subsystems.conveyor, 0, subsystems.elevator, 0, state.intake, false)
                ),
                new ParallelRaceGroup(
                    new AutoShooterCommand(shooter, subsystems.shooterTopRoller, 3050, state.shooter, 0),
                    new FeederCommand(subsystems.conveyor, 1, subsystems.elevator, 1, state.shooter)
                ),
                new ParallelRaceGroup(
                    new FollowPathCommand(subsystems.drivetrain, state.trajectories[1]),
                    new IntakeBallCommand(subsystems.intake, 1, subsystems.conveyor, 0, subsystems.elevator, 0, state.intake, false)
                ),
                new ParallelRaceGroup(
                    new AutoShooterCommand(shooter, subsystems.shooterTopRoller, 3050, state.shooter, 0),
                    new FeederCommand(subsystems.conveyor, 1, subsystems.elevator, 1, state.shooter)
                )
            )
        );
        
    }

    @Override
    public void autonomousPeriodic() {
        // TODO Auto-generated method stub
        super.autonomousPeriodic();
        if(!subsystems.conveyorBreakBeam.get() && !subsystems.elevatorBreakBeam.get()){
            subsystems.shooter.shoot();
            subsystems.shooterTopRoller.shoot();
        }
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
        subsystems.setMotorModes(NeutralMode.Coast);
    }

    @Override
    public void disabledInit() {
        subsystems.setMotorModes(NeutralMode.Coast);
    }

    @Override
    public void teleopPeriodic() {
        // TODO Auto-generated method stub
        super.teleopPeriodic();
        SmartDashboard.putNumber("Climber position", subsystems.climber.getMasterEncoder());

        Limelight limelight = state.limelight;
        double yOffset = limelight.getTargetYOffset();
        double xOffset = limelight.getTargetXOffset();
        double limelightUpperYTolerance = 15.0;
        double limelightLowerYTolerance = 10.0;
        double limelightUpperXTolerance = 2.0;
        double limelightLowerXTolerance = -2.0;
        boolean robotRotationReady = xOffset > limelightLowerXTolerance && xOffset < limelightUpperXTolerance;
        boolean robotDistanceReady = yOffset > limelightLowerYTolerance && yOffset < limelightUpperYTolerance;

        if(state.shooter.isReady() && robotRotationReady && robotRotationReady){
            state.drivetrain.disable();
            subsystems.conveyor.conveyorOn(1, true);
            subsystems.elevator.elevatorOn(1, true);
        }
        else {
            state.drivetrain.enable();
            subsystems.conveyor.conveyorOff();
            subsystems.elevator.elevatorOff();
        }
    }
}
