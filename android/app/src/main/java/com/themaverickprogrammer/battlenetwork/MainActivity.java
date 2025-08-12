package com.themaverickprogrammer.battlenetwork;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends Activity {
    private View decorView;
    private SurfaceView surfaceView;

    static {
        System.loadLibrary("fluidsynth");
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up immersive fullscreen UI
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(visibility ->
                new Handler().postDelayed(this::hideSystemUI, 5000)
        );
        hideSystemUI();

        // Create and display a SurfaceView for SFML to render into
        surfaceView = new SurfaceView(this);
        setContentView(surfaceView);

        // Wait for the surface to become available
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Surface surface = holder.getSurface();
                if (surface != null && surface.isValid()) {
                    setNativeWindow(surface);   // 👈 pass surface to SFML
                    startGame();                // 👈 now call C++ entry point
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {}
        });
    }

    @Override
    protected void onResume() {
        hideSystemUI();
        super.onResume();
    }

    private void hideSystemUI() {
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }

    // 👇 Native methods implemented in C++
    private native void setNativeWindow(Surface surface);
    private native void startGame();
}

