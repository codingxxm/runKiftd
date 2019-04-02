package com.xxm;

public class CopyInstance {

    private static volatile CopyHelper helper = null;

    public static CopyHelper getHelper(){
        if(helper==null){
            synchronized (CopyInstance.class){
                if(helper==null){
                    helper = new CopyHelper();
                }
            }
        }
        return helper;
    }
}
