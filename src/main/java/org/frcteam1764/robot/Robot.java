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
import org.frcteam1764.robot.subsystems.Shooter;
import org.frcteam2910.common.robot.drivers.Limelight.LedMode;
import org.frcteam2910.common.math.Rotation2;
import org.frcteam2910.common.robot.UpdateManager;

public class Robot extends TimedRobot {
    private SwerveRobotContainer robotContainer;
    private UpdateManager updateManager;
    private ShuffleBoardInfo sbiInstance = ShuffleBoardInfo.getInstance();
    private RobotState state = robotContainer.getRobotState();
   

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
    }

    @Override
    public void autonomousInit() {
        RobotSubsystems subsystems = robotContainer.getRobotSubsystems();
        Shooter shooter = robotContainer.getRobotSubsystems().shooter;
        subsystems.setMotorModes(NeutralMode.Coast);
        state.drivetrain.resetGyroAngle(Rotation2.ZERO);
        CommandScheduler.getInstance().schedule(
            new SequentialCommandGroup(
                new ParallelRaceGroup(
                    new AutoShooterCommand(shooter, 3050, state.shooter),
                    new FeederCommand(subsystems.conveyor, .4, subsystems.elevator, .4)
                ),
                new ParallelRaceGroup(
                    new FollowPathCommand(subsystems.drivetrain, state.trajectories[0]),
                    new IntakeBallCommand(subsystems.intake, 1, subsystems.conveyor, 0, subsystems.elevator, 0, state.intake, false)
                ),
                new ParallelRaceGroup(
                    new AutoShooterCommand(shooter, 3050, state.shooter),
                    new FeederCommand(subsystems.conveyor, 1, subsystems.elevator, 1)
                ),
                new ParallelRaceGroup(
                    new FollowPathCommand(subsystems.drivetrain, state.trajectories[1]),
                    new IntakeBallCommand(subsystems.intake, 1, subsystems.conveyor, 0, subsystems.elevator, 0, state.intake, false)
                ),
                new ParallelRaceGroup(
                    new AutoShooterCommand(shooter, 3050, state.shooter),
                    new FeederCommand(subsystems.conveyor, 1, subsystems.elevator, 1)
                )
            )
        );
        
    }

    @Override
    public void autonomousPeriodic() {
        // TODO Auto-generated method stub
        super.autonomousPeriodic();
        if(!robotContainer.getRobotSubsystems().conveyorBreakBeam.get() && !robotContainer.getRobotSubsystems().elevatorBreakBeam.get()){
            robotContainer.getRobotSubsystems().shooter.shoot();
            state.shooter.setBallCount(2);
        }
        else if(!robotContainer.getRobotSubsystems().elevatorBreakBeam.get() && robotContainer.getRobotSubsystems().conveyorBreakBeam.get()){
            state.shooter.setBallCount(1);
        }
        if(!robotContainer.getRobotSubsystems().shooterBreakBeam.get()){
            state.shooter.subtractBallCount();
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
        robotContainer.getRobotSubsystems().setMotorModes(NeutralMode.Coast);
    }

    @Override
    public void disabledInit() {
        robotContainer.getRobotSubsystems().setMotorModes(NeutralMode.Coast);
    }

    @Override
    public void teleopPeriodic() {
        // TODO Auto-generated method stub
        super.teleopPeriodic();
        robotContainer.getRobotSubsystems().climber.climb();
        SmartDashboard.putNumber("Climber position", robotContainer.getRobotSubsystems().climber.getMasterEncoder());
    }
}
