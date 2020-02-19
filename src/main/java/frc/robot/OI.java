/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.button.*;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.constants.ControlsConstants;
import frc.robot.constants.PortConstants;
import frc.robot.util.ColorSensor.ColorType;
import frc.robot.Commands.FeederCommand;
import frc.robot.Commands.IntakeCommand;
import frc.robot.Commands.ShooterCommand;
import frc.robot.Commands.WheelOfFortuneCommand;
import frc.robot.Commands.WheelOfFortuneSolenoidCommand;
import frc.robot.Commands.PIDMovementCommands.PIDDrive.MovementType;

/**
 * Add your docs here.
 */
public class OI {


/* ---- For Joystick Controls ---- */
public Joystick driverJoystick = new Joystick(PortConstants.DRIVER_CONTROLLER_USB_PORT);
public Joystick coDriverJoystick = new Joystick(PortConstants.CO_DRIVER_CONTROLLER_USB_PORT);

// Driver Joystick Config

public JoystickButton intakeButton = new JoystickButton(driverJoystick,ControlsConstants.INTAKE_BUTTON);
public JoystickButton feederButton = new JoystickButton(driverJoystick,ControlsConstants.FEEDER_BUTTON);

public JoystickButton shooterButton = new JoystickButton(driverJoystick, ControlsConstants.SHOOTER_BUTTON);
public JoystickButton shooterAutoAdjustButton = new JoystickButton(driverJoystick, ControlsConstants.SHOOTER_AUTO_ADJUST_BUTTON);

public JoystickButton controlPanelSolenoidButton = new JoystickButton(driverJoystick, ControlsConstants.CONTROL_PANEL_SOLENOID_BUTTON);

public JoystickButton redControlPanelButton = new JoystickButton(driverJoystick, ControlsConstants.CONTROL_PANEL_RED_SELECTED);
public JoystickButton blueControlPanelButton = new JoystickButton(driverJoystick, ControlsConstants.CONTROL_PANEL_BLUE_SELECTED);
public JoystickButton yellowControlPanelButton = new JoystickButton(driverJoystick, ControlsConstants.CONTROL_PANEL_YELLOW_SELECTED);
public JoystickButton greenControlPanelButton = new JoystickButton(driverJoystick, ControlsConstants.CONTROL_PANEL_GREEN_SELECTED);

/* ---- For XBox Controls ---- */

public XboxController driverXbox = new XboxController(PortConstants.DRIVER_CONTROLLER_USB_PORT);
public XboxController coDriverXbox = new XboxController(PortConstants.CO_DRIVER_CONTROLLER_USB_PORT);

//Xbox Controller Config

public JoystickButton intakeButtonXbox = new JoystickButton(driverXbox, ControlsConstants.RIGHT_SHOULDER_BUTTON);
public JoystickButton feederButtonXbox = new JoystickButton(driverXbox, ControlsConstants.LEFT_SHOULDER_BUTTON);
public JoystickButton shooterButtonXbox = new JoystickButton(driverXbox,ControlsConstants.A_BUTTON);


    public OI(){
    /* ---- Binds commands to button presses ---- */

    //On Joysticks
     intakeButton.whileHeld(new IntakeCommand(0.8));
     feederButton.whileHeld(new FeederCommand(0.3, 1));

     shooterButton.toggleWhenPressed(new ShooterCommand(0.8));
     shooterButton.whenPressed(new PIDDrive(-24, MovementType.STRAIGHT));

     controlPanelSolenoidButton.toggleWhenPressed(new WheelOfFortuneSolenoidCommand());

     redControlPanelButton.whileHeld(new WheelOfFortuneCommand(.8,ColorType.RED));
     blueControlPanelButton.whileHeld(new WheelOfFortuneCommand(.8,ColorType.BLUE));
     yellowControlPanelButton.whileHeld(new WheelOfFortuneCommand(.8,ColorType.YELLOW));
     greenControlPanelButton.whileHeld(new WheelOfFortuneCommand(.8,ColorType.GREEN));

     //On XBox Controller
     intakeButtonXbox.whileHeld(new IntakeCommand(0.8));
     feederButtonXbox.whileHeld(new FeederCommand(0.5, 0.5));
     shooterButtonXbox.toggleWhenPressed(new ShooterCommand(0.9));
    }

    //Used for xbox triggers
    public double deadBand(double input, double deadRange){
        if(input>deadRange || input<-deadRange){
            return input;
        }
        return 0;
    }
}

