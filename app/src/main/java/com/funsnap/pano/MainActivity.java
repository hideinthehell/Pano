package com.funsnap.pano;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.funsnap.stability.Stabilizer;
import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        testStitching();

        testStability();
    }

    private void testStability() {
        Stabilizer.stability(Environment.getExternalStorageDirectory() + "/test.mp4",
                Environment.getExternalStorageDirectory() + "/aa.mp4");
    }

    private void testStitching() {

        final LargeImageView largeImageView = findViewById(R.id.large_image);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] paths = new String[8];
                paths[0] = "/storage/emulated/0/Capture/Camera/CAPTURE_RECORD/PHOTO_20190727185319.jpg";
                paths[1] = "/storage/emulated/0/Capture/Camera/CAPTURE_RECORD/PHOTO_20190727185322.jpg";
                paths[2] = "/storage/emulated/0/Capture/Camera/CAPTURE_RECORD/PHOTO_20190727185325.jpg";
                paths[3] = "/storage/emulated/0/Capture/Camera/CAPTURE_RECORD/PHOTO_20190727185329.jpg";
                paths[4] = "/storage/emulated/0/Capture/Camera/CAPTURE_RECORD/PHOTO_20190727185332.jpg";
                paths[5] = "/storage/emulated/0/Capture/Camera/CAPTURE_RECORD/PHOTO_20190727185335.jpg";
                paths[6] = "/storage/emulated/0/Capture/Camera/CAPTURE_RECORD/PHOTO_20190727185339.jpg";
                paths[7] = "/storage/emulated/0/Capture/Camera/CAPTURE_RECORD/PHOTO_20190727185342.jpg";
//                paths[8] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190723_093938/CAP_20190723_095008.jpg";

//                String[] paths = new String[5];
//                paths[0] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190722_181806/CAP_20190722_181810.jpg";
//                paths[1] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190722_181806/CAP_20190722_181813.jpg";
//                paths[2] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190722_181806/CAP_20190722_181816.jpg";
//                paths[3] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190722_181806/CAP_20190722_181819.jpg";
//                paths[4] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190722_181806/CAP_20190722_181823.jpg";


                final String mResultPath = Environment.getExternalStorageDirectory() + "/Capture/" + "test.jpg";
                ImagesStitch.StitchImages(paths, mResultPath, new ImagesStitch.onStitchResultListener() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                largeImageView.setImage(new FileBitmapDecoderFactory(mResultPath));
                            }
                        });
                    }

                    @Override
                    public void onError(String errorMsg) {
                        Log.d("liuping", "错误:" + errorMsg);
                    }
                });
            }
        }).start();
    }


}
