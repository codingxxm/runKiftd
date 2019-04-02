package com.xxm;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CopyHelper {

    private OpenKiftdWorker worker = new OpenKiftdWorker();


    public synchronized void syncFile(Set<String> files,String originPath , String targetPath){
        System.out.println("开始同步文件");
        Iterator<String> iterator = files.iterator();
        String[] stepIn = targetPath.split("/");
        int stepLength = stepIn.length;
        String[] stepOut = new String[stepIn.length];
        for(int i = 0 ; i< stepLength ;i++){
            System.out.println("进入" + stepIn[i] + "目录");
            worker.execCommand("cd "  + stepIn[i]);
            stepOut[stepLength-i-1] = stepIn[i];
        }

        while (iterator.hasNext()) {
            try {
                String file = iterator.next();
                worker.execCommand("import " + originPath + File.separator + file);
                System.out.println("已经将" + originPath + File.separator + file + "复制到" + targetPath + "目录");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(String step:stepOut){
            worker.execCommand("cd ../");
        }
        System.out.println("返回根目录");
        System.out.println("同步完成");
    }
}
