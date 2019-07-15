package com.funsnap.pano;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
    }

    private void initData() {

        final LargeImageView largeImageView = findViewById(R.id.large_image);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] paths = new String[9];
                paths[0] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190715_112203/CAP_20190715_112206.jpg";
                paths[1] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190715_112203/CAP_20190715_112209.jpg";
                paths[2] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190715_112203/CAP_20190715_112213.jpg";
                paths[3] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190715_112203/CAP_20190715_112216.jpg";
                paths[4] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190715_112203/CAP_20190715_112219.jpg";
                paths[5] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190715_112203/CAP_20190715_112222.jpg";
                paths[6] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190715_112203/CAP_20190715_112226.jpg";
                paths[7] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190715_112203/CAP_20190715_112229.jpg";
                paths[8] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190715_112203/CAP_20190715_112232.jpg";

//                String[] paths = new String[5];
//                paths[0] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_101021/CAP_20190712_101024.jpg";
//                paths[1] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_101021/CAP_20190712_101028.jpg";
//                paths[2] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_101021/CAP_20190712_101031.jpg";
//                paths[3] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_101021/CAP_20190712_101034.jpg";
//                paths[4] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_101021/CAP_20190712_101038.jpg";


                final String mResultPath = Environment.getExternalStorageDirectory() + "/Capture/" + "test.jpg";
                ImagesStitch.StitchImages(paths, mResultPath,new ImagesStitch.onStitchResultListener() {
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
