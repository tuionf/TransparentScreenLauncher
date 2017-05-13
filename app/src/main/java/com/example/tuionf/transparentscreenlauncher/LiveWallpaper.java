package com.example.tuionf.transparentscreenlauncher;

import android.hardware.Camera;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by tuion on 2017/5/12.
 */

public class LiveWallpaper extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new CameraEngine();
    }

    class CameraEngine extends Engine implements Camera.PreviewCallback{

        private Camera camera;
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            //开启预览
            startPreview();
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            //停止预览
            stopPreview();
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            camera.addCallbackBuffer(data);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                startPreview();
            } else {
                stopPreview();
            }
        }

        private void startPreview() {


            int size = Camera.getNumberOfCameras();
            Log.e("hhp","----"+size);
            //关于相机的使用 API写的很清楚 https://developer.android.com/reference/android/hardware/Camera.html
            if (size == 1 || !Constant.isFontCamera) {
                camera = Camera.open();
            }else {
                camera = Camera.open(1);
            }


            camera.getParameters();
            camera.setDisplayOrientation(90);
            try {
                camera.setPreviewDisplay(getSurfaceHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
        }

        private void stopPreview() {
            if (camera != null) {
                camera.stopPreview();
                try {
                    camera.setPreviewDisplay(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                camera.release();
                camera = null;
            }
        }
    }

}
