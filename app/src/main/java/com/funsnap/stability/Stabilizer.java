package com.funsnap.stability;

/**
 * TODO
 * version: V1.0 <描述当前版本功能>
 * fileName: com.funsnap.stability.Stabilizer
 * author: liuping
 * date: 2019/11/4 11:47
 */
public class Stabilizer {

    static {
        System.loadLibrary("stability");
    }

    public static native void stability(String inputPath, String outputPath);
}
