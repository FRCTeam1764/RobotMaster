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
import org.frcteam1764.robot.subsystems.Intake;
import org.frcteam1764.robot.subsystems.RobotSubsystems;
import org.frcteam2910.common.robot.drivers.Limelight.LedMode;
import org.frcteam2910.common.math.Rotation2;
import org.frcteam2910.common.robot.UpdateManager;

public class Robot extends TimedRobot {
    private SwerveRobotContainer robotContainer;
    private UpdateManager updateManager;
    private ShuffleBoardInfo sbiInstance = ShuffleBoardInfo.getInstance();
   

    @Override
    public void robotInit() {
        robotContainer = new SwerveRobotContainer();
        updateManager = new UpdateManager(
                robotContainer.getRobotSubsystems().drivetrain
        );
        updateManager.startLoop(5.0e-3);
        Dashboard.configSmartDashboard(robotContainer.getRobotState());
        robotContainer.getRobotState().limelight.setLedMode(LedMode.OFF);
        robotContainer.getRobotSubsystems().intake.intakeOff();
        robotContainer.getRobotSubsystems().climber.pneumaticsWithdraw();;
    }


    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        Dashboard.updateSmartDashboard(robotContainer.getRobotState());
       // SmartDashboard.putBoolean("Break Beam Elevator", robotContainer.breakBeamElevator.get());
       // SmartDashboard.putBoolean("Break beam Conveyor", robotContainer.breakBeamConveyor.get());
    }

    @Override
    public void autonomousInit() {
        robotContainer.getRobotSubsystems().setMotorModes(NeutralMode.Coast);
        RobotSubsystems subsystems = robotContainer.getRobotSubsystems();
        RobotState state = robotContainer.getRobotState();
        state.drivetrain.resetGyroAngle(Rotation2.ZERO);
        // CommandScheduler.getInstance().schedule(new AutoGroupCommand(robotContainer.getRobotState(), robotContainer.getRobotSubsystems()));
        CommandScheduler.getInstance().schedule(
            new SequentialCommandGroup(
                new ParallelRaceGroup(
                    new FollowPathCommand(subsystems.drivetrain, state.trajectories[0]),
                    new IntakeBallCommand(subsystems.intake, 1, subsystems.conveyor, 0, subsystems.elevator, 0, state.intake, false)
                ),
                new ParallelRaceGroup(
                    new FollowPathCommand(subsystems.drivetrain, state.trajectories[1]),
                    new IntakeBallCommand(subsystems.intake, 1, subsystems.conveyor, 0, subsystems.elevator, 0, state.intake, false)
                ),
                new ParallelRaceGroup(
                    new FollowPathCommand(subsystems.drivetrain, state.trajectories[2]),
                    new IntakeBallCommand(subsystems.intake, 1, subsystems.conveyor, 0, subsystems.elevator, 0, state.intake, false)
                ),
                new ParallelRaceGroup(
                    new FollowPathCommand(subsystems.drivetrain, state.trajectories[3]),
                    new IntakeBallCommand(subsystems.intake, 0, subsystems.conveyor, 0, subsystems.elevator, 0, state.intake, false)
                )
            )
        );
        
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
        robotContainer.getRobotSubsystems().setMotorModes(NeutralMode.Coast);
    }

    @Override
    public void disabledInit() {
        robotContainer.getRobotSubsystems().setMotorModes(NeutralMode.Coast);
    }
}
