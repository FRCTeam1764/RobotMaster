// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.frcteam1764.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/** Add your docs here. */
public class ShuffleBoardInfo {
    private ShuffleboardTab tab;
    private NetworkTableEntry PValue, DValue, IValue;
    private static ShuffleBoardInfo instance = null;
  
    public void ShuffleBoardTabs() {
      tab = Shuffleboard.getTab("Shooter");
      PValue = tab.add("P Value", 0).getEntry();
      DValue = tab.add("D Value", 0).getEntry();
      IValue = tab.add("I Value", 0).getEntry();
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
