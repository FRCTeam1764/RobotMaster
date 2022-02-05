// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/** Add your docs here. */
public class ShuffleBoardInfo {
    
    private NetworkTableEntry PValue, DValue, IValue;
    private static ShuffleBoardInfo instance = null;
  
    public void ShuffleBoardTabs() {
      ShuffleboardTab shooterTab = Shuffleboard.getTab("Shooter");
      Shuffleboard.selectTab("Shooter");
      PValue = shooterTab.add("P Value", 0.066).withPosition(0, 0).withSize(1, 1).getEntry();
      DValue = shooterTab.add("D Value", 0).withPosition(0, 1).withSize(1, 1).getEntry();
      IValue = shooterTab.add("I Value", 0.0015).withPosition(0, 2).withSize(1, 1).getEntry();
    }
  
    public static ShuffleBoardInfo getInstance() {
      if( instance == null ){
        instance = new ShuffleBoardInfo();
      }
   
      return instance;
    }

    public NetworkTableEntry getPValueEntry(){
      return PValue;
    }

    public NetworkTableEntry getDValueEntry(){
      return DValue;
    }

    public NetworkTableEntry getIValueEntry(){
      return IValue;
    }

}
