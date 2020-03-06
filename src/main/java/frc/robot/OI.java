/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Commands.ClimberCommand;
import frc.robot.Commands.ExtendControlPanelSolenoidCommand;
import frc.robot.Commands.FeederCommand;
import frc.robot.Commands.IntakeCommand;
import frc.robot.Commands.ShooterCommand;
import frc.robot.Commands.ShooterCommand.ShooterControlMode;
import frc.robot.Subsystems.TeleopSubsystems.Climber.ClimberControlType;
import frc.robot.constants.ControlsConstants;
import frc.robot.constants.PortConstants;

/**
 * Add your docs here.
 */
public class OI {


/* ---- For Joystick Controls ---- */
public Joystick driverJoystick = new Joystick(PortConstants.DRIVER_CONTROLLER_USB_PORT);
public Joystick coDriverJoystick = new Joystick(PortConstants.CO_DRIVER_CONTROLLER_USB_PORT);

// Driver Joystick Config

public JoystickButton intakeButton = new JoystickButton(driverJoystick,ControlsConstants.INTAKE_BUTTON);
public JoystickButton reverseIntake = new JoystickButton(driverJoystick, 10);
public JoystickButton feederButton = new JoystickButton(driverJoystick,ControlsConstants.FEEDER_BUTTON);

public JoystickButton shooterButton = new JoystickButton(driverJoystick, ControlsConstants.SHOOTER_BUTTON);
public JoystickButton shooterAutoAdjustButton = new JoystickButton(driverJoystick, ControlsConstants.SHOOTER_AUTO_ADJUST_BUTTON);

public JoystickButton controlPanelSolenoidButton = new JoystickButton(driverJoystick, ControlsConstants.CONTROL_PANEL_SOLENOID_BUTTON);

public JoystickButton controlPanelButton = new JoystickButton(driverJoystick, ControlsConstants.CONTROL_PANEL_BUTTON);

/* ---- For XBox Controls ---- */

public XboxController driverXbox = new XboxController(PortConstants.DRIVER_CONTROLLER_USB_PORT);
public XboxController coDriverXbox = new XboxController(PortConstants.CO_DRIVER_CONTROLLER_USB_PORT);

//Xbox Controller Config

public JoystickButton shooterButtonXbox = new JoystickButton(driverXbox, ControlsConstants.A_BUTTON);
public JoystickButton intakeButtonXbox = new JoystickButton(driverXbox, ControlsConstants.LEFT_SHOULDER_BUTTON);
public JoystickButton climberWinchButtonXbox = new JoystickButton(driverXbox,ControlsConstants.Y_BUTTON);
public JoystickButton climberPneumaticsButtonXbox = new JoystickButton(driverXbox,ControlsConstants.START_BUTTON);
public JoystickButton climberPneumaticsButtonOffXbox = new JoystickButton(driverXbox,ControlsConstants.BACK_BUTTON);

JoystickButton controlPanelExtendButtonXbox = new JoystickButton(driverXbox, ControlsConstants.X_BUTTON);

JoystickButton leftOuttakeTrigger = new JoystickButton(driverXbox, ControlsConstants.B_BUTTON);//new Trigger(() -> driverXbox.getTriggerAxis(Hand.kLeft)>.3);
JoystickButton rightFeederTrigger = new JoystickButton(driverXbox, ControlsConstants.RIGHT_SHOULDER_BUTTON);// new Trigger(() -> driverXbox.getTriggerAxis(Hand.kRight)>.3);

Trigger upDPadClimbUp = new Trigger(() -> driverXbox.getPOV()==0);
Trigger downDPadClimbDown = new Trigger(() -> driverXbox.getPOV()==180);

public JoystickButton coShooterButtonXbox = new JoystickButton(coDriverJoystick, 5);
public JoystickButton cointakeButtonXbox = new JoystickButton(coDriverJoystick, 1);
public JoystickButton coClimberWinchButtonXbox = new JoystickButton(coDriverJoystick,12);
public JoystickButton coClimberPneumaticsButtonXbox = new JoystickButton(coDriverJoystick,11);
public JoystickButton coSlowIntake = new JoystickButton(coDriverJoystick, 7);

JoystickButton coleftOuttakeTrigger = new JoystickButton(coDriverJoystick, 6);//new Trigger(() -> driverXbox.getTriggerAxis(Hand.kLeft)>.3);
JoystickButton coRightFeederTrigger = new JoystickButton(coDriverJoystick, 3);// new Trigger(() -> driverXbox.getTriggerAxis(Hand.kRight)>.3);

Trigger coUpDPadClimbUp = new Trigger(() -> coDriverJoystick.getPOV()==0);
Trigger coDownDPadClimbDown = new Trigger(() -> coDriverJoystick.getPOV()==180);


    public OI(){
    /* ---- Binds commands to button presses ---- */
 
    //On Joysticks
     /*intakeButton.whileHeld(new IntakeCommand(1));
     reverseIntake.whileHeld(new FeederCommand(-.6,0));
     feederButton.whileHeld(new FeederCommand(0.2, 1));*/

     //shooterButton.toggleWhenPressed(new ShooterCommand(0.55, ShooterControlMode.STANDARD));
     //shooterAutoAdjustButton.whenPressed(new PIDDrive(-24, MovementType.STRAIGHT));

     //controlPanelSolenoidButton.toggleWhenPressed(new WheelOfFortuneSolenoidCommand());

     //controlPanelButton.whileHeld(new WheelOfFortuneCommand(.8));

     //On XBox Controller
     //3050, 3100
    // shooterButtonXbox.toggleWhenPressed(new ShooterCommand(3075, ShooterControlMode.PID));
     leftOuttakeTrigger.whenHeld(new IntakeCommand(1, .2, .4)); //This is now intake
     rightFeederTrigger.whenHeld(new IntakeCommand(1, .2, .4)); //This is also intake

     controlPanelExtendButtonXbox.toggleWhenPressed(new ExtendControlPanelSolenoidCommand());

     climberWinchButtonXbox.whenHeld(new ClimberCommand(true, ClimberControlType.WINCH));
     //climberWinchButtonXbox.negate().toggleWhenActive(new ClimberCommand(false, ClimberControlType.WINCH));
     climberPneumaticsButtonXbox.whenPressed(new ClimberCommand(true, ClimberControlType.PNEUMATICS));
     climberPneumaticsButtonOffXbox.whenPressed(new ClimberCommand(false, ClimberControlType.PNEUMATICS));
     //climberPneumaticsButtonXbox.negate().toggleWhenActive(new ClimberCommand(false, ClimberControlType.PNEUMATICS));

   /// intakeButtonXbox.whenHeld(new IntakeCommand(1, .2, .4));

     coShooterButtonXbox.toggleWhenPressed(new ShooterCommand(3200, ShooterControlMode.PID));
     coleftOuttakeTrigger.whenHeld(new IntakeCommand(-1,-1, -1.0));
     coRightFeederTrigger.whenHeld(new FeederCommand(1, .8, 1.0));

     coSlowIntake.whenHeld(new IntakeCommand(.8, .2, .4));

     //coClimberWinchButtonXbox.toggleWhenActive(new ClimberCommand(true, ClimberControlType.WINCH));
     //coClimberWinchButtonXbox.negate().toggleWhenActive(new ClimberCommand(false, ClimberControlType.WINCH));
     //coClimberPneumaticsButtonXbox.whenActive(new ClimberCommand(true, ClimberControlType.PNEUMATICS));
     //climberPneumaticsButtonXbox.negate().toggleWhenActive(new ClimberCommand(false, ClimberControlType.PNEUMATICS));

     cointakeButtonXbox.whenHeld(new IntakeCommand(1, .2, .4));
    }

    //Used for xbox triggers
    public double deadBand(double input, double deadRange){
        if(input>deadRange || input<-deadRange){
            return input;
        }
        return 0;
    }
}

