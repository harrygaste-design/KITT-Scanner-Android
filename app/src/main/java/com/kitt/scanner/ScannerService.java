
package com.kitt.scanner;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class ScannerService extends Service {

    private WindowManager windowManager;
    private LightView lightView;
    private final Handler handler = new Handler();
    private float position = 0f;

    private final Runnable animation = new Runnable() {
        @Override
        public void run() {
            if (lightView == null) return;
            position += 0.012f;
            if (position >= 1f) position = 0f;
            lightView.invalidate();
            handler.postDelayed(this, 16);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager)
            getSystemService(WINDOW_SERVICE);

        lightView = new LightView();

        WindowManager.LayoutParams params =
            new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            );

        params.gravity = Gravity.TOP | Gravity.START;
        windowManager.addView(lightView, params);
        handler.post(animation);
    }

    private class LightView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        LightView() {
            super(ScannerService.this);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            float w = getWidth();
            float h = getHeight();
            float inset = 10f;

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(7f);
            paint.setColor(Color.RED);
            paint.setShadowLayer(18f, 0, 0, Color.RED);
            canvas.drawRect(inset, inset, w - inset, h - inset, paint);

            paint.clearShadowLayer();
            paint.setStyle(Paint.Style.FILL);

            float perimeter = 2f * (w + h);
            float distance = position * perimeter;
            float x;
            float y;

            if (distance < w) {
                x = distance;
                y = 0;
            } else if (distance < w + h) {
                x = w;
                y = distance - w;
            } else if (distance < 2 * w + h) {
                x = w - (distance - w - h);
                y = h;
            } else {
                x = 0;
                y = h - (distance - 2 * w - h);
            }

            paint.setColor(Color.WHITE);
            paint.setShadowLayer(20f, 0, 0, Color.WHITE);
            canvas.drawCircle(x, y, 9f, paint);

            paint.setColor(Color.RED);
            paint.setShadowLayer(16f, 0, 0, Color.RED);
            canvas.drawCircle(x, y, 17f, paint);
        }
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(animation);
        if (windowManager != null && lightView != null) {
            windowManager.removeView(lightView);
        }
        lightView = null;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
                       }
