package com.funsnap.pano;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
                paths[0] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_103501/CAP_20190712_103504.jpg";
                paths[1] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_103501/CAP_20190712_103507.jpg";
                paths[2] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_103501/CAP_20190712_103511.jpg";
                paths[3] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_103501/CAP_20190712_103514.jpg";
                paths[4] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_103501/CAP_20190712_103517.jpg";
                paths[5] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_103501/CAP_20190712_103521.jpg";
                paths[6] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_103501/CAP_20190712_103524.jpg";
                paths[7] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_103501/CAP_20190712_103527.jpg";
                paths[8] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_103501/CAP_20190712_103530.jpg";

//                String[] paths = new String[5];
//                paths[0] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_101021/CAP_20190712_101024.jpg";
//                paths[1] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_101021/CAP_20190712_101028.jpg";
//                paths[2] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_101021/CAP_20190712_101031.jpg";
//                paths[3] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_101021/CAP_20190712_101034.jpg";
//                paths[4] = "/storage/emulated/0/Capture/Camera/NEW_PANO/PANO_20190712_101021/CAP_20190712_101038.jpg";

                ImagesStitch.StitchImages(paths, new ImagesStitch.onStitchResultListener() {
                    @Override
                    public void onSuccess(final Bitmap bitmap) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String mResultPath = Environment.getExternalStorageDirectory() + "/Capture/" + "test.jpg";
                                File f = new File(mResultPath);
                                if (f.exists()) {
                                    f.delete();
                                }
                                try {
                                    FileOutputStream out = new FileOutputStream(f);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                    out.flush();
                                    out.close();
                                } catch (FileNotFoundException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                largeImageView.setImage(new FileBitmapDecoderFactory(mResultPath));

                            }
                        });
                    }

                    @Override
                    public void onError(String errorMsg) {
                        Log.d("liuping", "111:" + errorMsg);
                    }
                });
            }
        }).start();
    }
}
