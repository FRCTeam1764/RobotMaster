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
import frc.robot.Commands.FeederCommand;
import frc.robot.Commands.IntakeCommand;
import frc.robot.Commands.ShooterCommand;

/**
 * Add your docs here.
 */
public class OI {


/* ---- For Joystick Controls ---- */
public static Joystick driverJoystick = new Joystick(PortConstants.DRIVER_CONTROLLER_USB_PORT);
public static Joystick coDriverJoystick = new Joystick(PortConstants.CO_DRIVER_CONTROLLER_USB_PORT);

// Driver Joystick Config

public static JoystickButton intakeButton = new JoystickButton(driverJoystick,ControlsConstants.INTAKE_BUTTON);
public static JoystickButton feederButton = new JoystickButton(driverJoystick,ControlsConstants.FEEDER_BUTTON);
public static JoystickButton shooterButton = new JoystickButton(driverJoystick, ControlsConstants.SHOOTER_BUTTON);

/* ---- For XBox Controls ---- */

public static XboxController driverXbox = new XboxController(PortConstants.DRIVER_CONTROLLER_USB_PORT);
public static XboxController coDriverXbox = new XboxController(PortConstants.CO_DRIVER_CONTROLLER_USB_PORT);

//Xbox Controller Config

public static JoystickButton intakeButtonXbox = new JoystickButton(driverXbox, ControlsConstants.RIGHT_SHOULDER_BUTTON);
public static JoystickButton feederButtonXbox = new JoystickButton(driverXbox, ControlsConstants.LEFT_SHOULDER_BUTTON);
public static JoystickButton shooterButtonXbox = new JoystickButton(driverXbox,ControlsConstants.A_BUTTON);


    public OI(){
    /* ---- Binds commands to button presses ---- */

    //On Joysticks
     intakeButton.whileHeld(new IntakeCommand(0.8));
     feederButton.whileHeld(new FeederCommand(0.5, 0.5));
     shooterButton.toggleWhenPressed(new ShooterCommand(0.9));

     //On XBox Controller
     intakeButtonXbox.whileHeld(new IntakeCommand(0.8));
     feederButtonXbox.whileHeld(new FeederCommand(0.5, 0.5));
     shooterButtonXbox.toggleWhenPressed(new ShooterCommand(0.9));
    }

    public double deadBand(double input, double deadRange){
        if(input>deadRange || input<-deadRange){
            return input;
        }
        return 0;
    }
}

