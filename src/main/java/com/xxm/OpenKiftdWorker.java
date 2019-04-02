package com.xxm;

import java.io.PrintWriter;

public class OpenKiftdWorker {

    public static PrintWriter writer;

    public OpenKiftdWorker() {
    }

    public static void open(String initCommand) {
        try {
            Process process = Runtime.getRuntime().exec(initCommand);
            writer = new PrintWriter(process.getOutputStream());
            execCommand("-start");
            execCommand("-files");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized static void execCommand(String command){
        if(writer!=null && command!=null){
            writer.println(command);
            writer.flush();
        }
    }


}
