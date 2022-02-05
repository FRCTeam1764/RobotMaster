package org.frcteam1764.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.frcteam1764.robot.commands.AutoGroupCommand;
import org.frcteam1764.robot.commands.ConveyorCommand;
import org.frcteam1764.robot.commands.ElevatorCommand;
import org.frcteam1764.robot.commands.IntakeCommand;
import org.frcteam1764.robot.commands.ShooterCommand;
import org.frcteam1764.robot.commands.ShooterCommand.ShooterControlMode;
import org.frcteam2910.common.robot.drivers.Limelight.LedMode;
import org.frcteam2910.common.robot.UpdateManager;
import org.frcteam1764.robot.subsystems.Shooter;

public class Robot extends TimedRobot {
    private SwerveRobotContainer robotContainer;
    private UpdateManager updateManager;
    private ShuffleBoardInfo sbiInstance = ShuffleBoardInfo.getInstance();
    private Shooter shooter;

    @Override
    public void robotInit() {
        robotContainer = new SwerveRobotContainer();
        updateManager = new UpdateManager(
                robotContainer.getRobotSubsystems().drivetrain
        );
        shooter = new Shooter(1500, ShooterControlMode.PID);
        updateManager.startLoop(5.0e-3);
        Dashboard.configSmartDashboard(robotContainer.getRobotState());
        robotContainer.getRobotState().limelight.setLedMode(LedMode.OFF);
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        Dashboard.updateSmartDashboard(robotContainer.getRobotState());
        new ShooterCommand(1500, ShooterControlMode.PID); // max rpm is 6380
        new ElevatorCommand(robotContainer.getRobotSubsystems().elevator, 1);
        new ConveyorCommand(robotContainer.getRobotSubsystems().conveyor, 1);
        new IntakeCommand(robotContainer.getRobotSubsystems().intake, 1);
         // max rpm is 6380
        robotContainer.getRobotSubsystems().elevator.elevatorOn(1);
        robotContainer.getRobotSubsystems().conveyor.conveyorOn(1);
    }

    @Override
    public void autonomousInit() {
        robotContainer.getRobotSubsystems().setMotorModes(NeutralMode.Brake);
        CommandScheduler.getInstance().schedule(new AutoGroupCommand(robotContainer.getRobotState(), robotContainer.getRobotSubsystems()));
        
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
        robotContainer.getRobotSubsystems().setMotorModes(NeutralMode.Brake);
        shooter.shoot();
         // max rpm is 6380
        robotContainer.getRobotSubsystems().elevator.elevatorOn(1);
        robotContainer.getRobotSubsystems().conveyor.conveyorOn(1);
    }

    @Override
    public void disabledInit() {
        robotContainer.getRobotSubsystems().setMotorModes(NeutralMode.Coast);
    }
}
