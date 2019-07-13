package com.funsnap.pano;

import android.support.annotation.NonNull;

import java.io.File;

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
        int wh[] = stitchImages(paths, outPath, 0.1f, 0.2f, 500);

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
     * @return
     */
    private native static int[] stitchImages(String path[], String outPath,
                                             float widthRatio, float heightRatio, int length);


    public interface onStitchResultListener {

        void onSuccess();

        void onError(String errorMsg);
    }

}
