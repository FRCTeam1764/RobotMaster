package frc.robot.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RLogger {
    private static ConfigurationManager config = ConfigurationManager.get();
    
    private static String getSignature(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    public static void debug(String s){
        if(config.debug){
            System.out.println("DEBUG | " + getSignature() + " | " + s);
        }
    }

    public static void info(String s){
        System.out.println("INFO | " + getSignature() + " | " + s);
    }

    public static void warn(String s){
        System.out.println("WARN | " + getSignature() + " | " + s);
    }

    public static void errr(String s){
        System.err.println("ERROR | " + getSignature() + " | " + s);
    }

    public static void errr(String s, Exception e){
        System.err.println("ERROR | " + getSignature() + " | " + s + "\n" + e.getMessage() + "\n" + e.getStackTrace());
    }

    public static void errr(Exception e){
        System.err.println("ERROR | " + getSignature() + " | " + e.getMessage() + "\n" + e.getStackTrace());
    }
}