package frc.robot.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;

public class ConfigurationManager {
    private static ConfigurationManager cm;
    
    private Hashtable<String,String> content = new Hashtable<String,String>();

    public final boolean debug;

    public static ConfigurationManager get(){
        if(cm == null){
            cm = new ConfigurationManager();
        }

        return cm;
    }

    public String get(String key){
        if(content.containsKey(key)){
            return content.get(key);
        } else {
            return null;
        }
    }

    private ConfigurationManager() {
        System.out.println("Initializing Debugger!");
        Scanner sc = null; 
        try{
            sc = new Scanner(new File("global.cfg"));
        } catch(Exception e){}
        int lineno = 0;
        while(sc.hasNextLine()){
            // While there are lines left in the config file
            lineno++;
            String line = sc.nextLine();
            line = line.strip();

            if(line.length() == 0){
                // Line was entirely white space
                continue;
            }
            if(line.charAt(0) == '#'){
                // Line is a comment
                continue;
            }
            String[] parts = line.split("=");
            if(parts.length != 2){
                // Does not contain a key value pair
                System.err.println("Error parsing configuration file on line " + lineno);
                continue;
            }
            String key = parts[0].strip();
            String value = parts[1].strip();

            if(!content.containsKey(key)){
                content.put(key,value);
                System.out.println("Setting " + key + " to " + value);
            }
        }
        sc.close();
        if(content.containsKey("debug")){
            this.debug = content.get("debug").equals("true");
        } else {
            this.debug = false;
        }
    }
}