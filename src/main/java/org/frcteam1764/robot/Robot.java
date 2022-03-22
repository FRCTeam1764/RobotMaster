package org.frcteam1764.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;

// import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.cameraserver.CameraServer;
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
    private ShuffleBoardInfo sbiInstance;
    private RobotState state;
    private RobotSubsystems subsystems;
    private LimelightUtil limelightUtil;
    private int initialShotCount;
    private boolean ballIsPresent;
   

    @Override
    public void robotInit() {
        robotContainer = new SwerveRobotContainer();
        sbiInstance = ShuffleBoardInfo.getInstance();
        state = robotContainer.getRobotState();
        subsystems = robotContainer.getRobotSubsystems();
        
        limelightUtil = new LimelightUtil(state, subsystems);
        updateManager = new UpdateManager(
                robotContainer.getRobotSubsystems().drivetrain
        );
        updateManager.startLoop(5.0e-3);
        Dashboard.configSmartDashboard(robotContainer.getRobotState());
        // state.limelight.setLedMode(LedMode.OFF);
        subsystems.intake.intakeOff();
        subsystems.climber.pneumaticsWithdraw();
        this.initialShotCount = 0;
        this.ballIsPresent = false;
       // CameraServer.startAutomaticCapture();
    }


    @Override
    public void robotPeriodic() {

        CommandScheduler.getInstance().run();
        Dashboard.updateSmartDashboard(robotContainer.getRobotState());
    }

    @Override
    public void autonomousInit() {
        Shooter shooter = subsystems.shooter;
        shooter.shoot();
        subsystems.shooterTopRoller.shoot();
        subsystems.setMotorModes(NeutralMode.Coast);
        state.drivetrain.resetGyroAngle(Rotation2.ZERO);
        CommandScheduler.getInstance().schedule(

        //
        //
        // 2 ball auto
        //
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
            // new SequentialCommandGroup(
            //     new ParallelRaceGroup(
            //         new AutoShooterCommand(shooter, subsystems.shooterTopRoller, 4100, state.shooter, 1),
            //         new FeederCommand(subsystems.conveyor, 1, subsystems.elevator, -0.9, state.shooter)
            //     ),
            //     new ParallelRaceGroup(
            //         new FollowPathCommand(subsystems.drivetrain, state.trajectories[0])
            //         ,new IntakeBallCommand(subsystems.intake, 0.8, subsystems.conveyor, 1, subsystems.elevator, -0.6, state.intake, false)
            //     ),
            //     new AutoShooterCommand(shooter, subsystems.shooterTopRoller, 4100, state.shooter, 0),
            //     new ParallelRaceGroup(
            //         new AutoShooterCommand(shooter, subsystems.shooterTopRoller, 4100, state.shooter, 1),
            //         new FeederCommand(subsystems.conveyor, 1, subsystems.elevator, -0.9, state.shooter)
            //     )
            // )

        //
        //
        // 5 ball auto
        //
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
            new SequentialCommandGroup(
                new ParallelRaceGroup(
                    new AutoShooterCommand(shooter, subsystems.shooterTopRoller, 4100, state.shooter, 1),
                    new FeederCommand(subsystems.conveyor, 1, subsystems.elevator, -0.9, state.shooter)
                ),
                new ParallelRaceGroup(
                    new FollowPathCommand(subsystems.drivetrain, state.trajectories[0])
                    ,new IntakeBallCommand(subsystems.intake, 0.8, subsystems.conveyor, 1, subsystems.elevator, -0.6, state.intake, false)
                ),
                new ParallelRaceGroup(
                    new AutoShooterCommand(shooter, subsystems.shooterTopRoller, 4100, state.shooter, 0),
                    new FeederCommand(subsystems.conveyor, 1, subsystems.elevator, -0.9, state.shooter)
                ),
                new ParallelRaceGroup(
                    new FollowPathCommand(subsystems.drivetrain, state.trajectories[1])
                    ,new IntakeBallCommand(subsystems.intake, 1, subsystems.conveyor, 1, subsystems.elevator, -0.6, state.intake, false)
                ),
                new AutoShooterCommand(shooter, subsystems.shooterTopRoller, 4100, state.shooter, 0),
                new ParallelRaceGroup(
                    new AutoShooterCommand(shooter, subsystems.shooterTopRoller, 4100, state.shooter, 0),
                    new FeederCommand(subsystems.conveyor, 1, subsystems.elevator, -0.9, state.shooter)
                )
            )
        );
        
    }

    @Override
    public void autonomousPeriodic() {
        // // TODO Auto-generated method stub
        super.autonomousPeriodic();
        
        if(ballIsPresent && !subsystems.shooter.ballIsPresent()){
            state.shooter.addShotCount();
        }
        ballIsPresent = subsystems.shooter.ballIsPresent();
        state.shooter.addToTimer();
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

        Limelight limelight = state.limelight;
        double yOffset = limelight.getTargetYOffset();
        double xOffset = limelight.getTargetXOffset();
        double limelightUpperYTolerance = 3.0;
        double limelightLowerYTolerance = -17.5;//-17.5
        
        double targetOffset = 0.2;
        double xScale = 4;
        double targetOffsetScale = 0.2;
        double targetDeltaScale = Math.abs(limelightLowerYTolerance - yOffset)*targetOffsetScale/Math.abs(limelightLowerYTolerance - limelightUpperYTolerance);
        double xDeltaScale = Math.abs(limelightLowerYTolerance - yOffset)*xScale/Math.abs(limelightLowerYTolerance - limelightUpperYTolerance); // plus or minus 4 close and plus or minus 2 far = 2. Y delta is between 0 and 6.
        double limelightUpperXTolerance =  1.5 + xDeltaScale + targetOffset + targetDeltaScale;
        double limelightLowerXTolerance = -1.5 - xDeltaScale + targetOffset + targetDeltaScale;
        double turningToleranceRate = 6; // radians per second
        boolean robotRotationReady = xOffset > limelightLowerXTolerance && xOffset < limelightUpperXTolerance;
        boolean robotDistanceReady = yOffset > limelightLowerYTolerance && yOffset < limelightUpperYTolerance;
        boolean isNotTurning = Math.abs(state.drivetrain.getGyro().getRate()) > Math.abs(turningToleranceRate);

        // SmartDashboard.putBoolean("Target Acquired", limelight.hasTarget());
        // SmartDashboard.putNumber("X Offset", xOffset);
        SmartDashboard.putNumber("Y Offset", yOffset);
        // SmartDashboard.putNumber("Bottom Shooter RPM", state.shooter.getActualVelocity());
        // SmartDashboard.putBoolean("Bottom Shooter Ready", state.shooter.getActualVelocity() > ((state.shooter.getAssignedVelocity() - 850)/60*2048*0.1));
        // SmartDashboard.putNumber("Top Shooter Ready", state.climber.getOffset());
        SmartDashboard.putNumber("Top Shooter RPM", state.shooter.getTopRollerActualVelocity());
        SmartDashboard.putNumber("assigned velo", state.shooter.getTopRollerAssignedVelocity());
        SmartDashboard.putBoolean("Shooter Ready", state.shooter.isReady());
  //  SmartDashboard.putNumber("Shooter Velocity", state.shooter.getActualVelocity());

        if(robotContainer.getCopilotRightTriggerAxis().get(true) < 0.5 && limelight.hasTarget() && robotDistanceReady && robotRotationReady && state.shooter.isReady()){
            state.isShooting = true;
            subsystems.conveyor.conveyorOn(1, true);
            subsystems.elevator.elevatorOn(-0.9, true);
        }
        else if(robotContainer.getCopilotRightTriggerAxis().get(true) < 0.5 && state.isShooting){
            subsystems.conveyor.conveyorOff();
            subsystems.elevator.elevatorOff();
            state.isShooting = false;

        }
    }
}
