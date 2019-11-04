package com.funsnap.pano;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImagesStitch {
    public final static int OK = 0;
    public final static int ERR_NEED_MORE_IMGS = 1;
    public final static int ERR_HOMOGRAPHY_EST_FAIL = 2;
    public final static int ERR_CAMERA_PARAMS_ADJUST_FAIL = 3;

    static {
        System.loadLibrary("funsnap_stitch");
    }

    public static void StitchImages(String paths[], String outPath, @NonNull onStitchResultListener listener) {
        for (String path : paths) {
            if (!new File(path).exists()) {
                listener.onError("无法读取文件或文件不存在:" + path);
                return;
            }
        }

        float scale = 1;
        if (!isCPU64()) scale = 0.5f;
        long l = System.currentTimeMillis();
        int wh[] = stitchImages(paths, outPath, 0.1f, 0.2f, 500,0.5f);
        Log.d("liuping","时间:" + (System.currentTimeMillis() - l));

        switch (wh[0]) {
            case OK: {
                listener.onSuccess();
            }
            break;
            case ERR_NEED_MORE_IMGS: {
                listener.onError("需要更多图片");
                return;
            }
            case ERR_HOMOGRAPHY_EST_FAIL: {
                listener.onError("图片对应不上");
                return;
            }
            case ERR_CAMERA_PARAMS_ADJUST_FAIL: {
                listener.onError("图片参数处理失败");
                return;
            }
        }
    }

    /**
     * @param path        输入图片路径
     * @param outPath     输出图片路径
     * @param widthRatio  宽度最多裁掉的比例
     * @param heightRatio 高度最多裁掉的比例
     * @param length      //裁剪参数，值越大裁的比例越小，默认500
     * @return 【1】 拼接后宽度  【2】拼接后的高度
     */
    private native static int[] stitchImages(String path[], String outPath,
                                             float widthRatio, float heightRatio, int length,float scale);


    public interface onStitchResultListener {

        void onSuccess();

        void onError(String errorMsg);
    }

    public static boolean isCPU64(){
        boolean result = false;
        String mProcessor = null;
        List<String > list = null;
        try {
            mProcessor = getFieldFromCpuinfo("Processor");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mProcessor != null) {
            if (mProcessor.contains("aarch64")) {
                result = true;
            }
        }

        return result;
    }


    public static String getFieldFromCpuinfo(String field) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("/proc/cpuinfo"));
        Pattern p = Pattern.compile(field + "\\s*:\\s*(.*)");

        try {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher m = p.matcher(line);
                if (m.matches()) {
                    return m.group(1);
                }
            }
        } finally {
            br.close();
        }

        return null;
    }

}
