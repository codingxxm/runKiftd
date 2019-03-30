package com.xxm;

import javax.sound.midi.Soundbank;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainWorker {

    public final static String startKiftd = "";

    /**
     *
     * @param args args[0]:kiftd  args[1]:originFilePath args[2]:targetFilePath
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
//        if(args==null || args.length <3){
//            throw new RuntimeException("arguments error");
//        }
        Process process = Runtime.getRuntime().exec("java -jar I:\\kiftd-master\\kiftd-1.0.16-RELEASE.jar -console");
        PrintWriter writer = new PrintWriter(process.getOutputStream());
        writer.println("-start");
        writer.flush();

        writer.println("-files");
        writer.flush();

        writer.println("import I:\\Download\\a.txt");
        writer.flush();
    }


}
