package com.example.fmoyader.becomepainter;

import android.graphics.BlurMaskFilter;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.fmoyader.becomepainter.canvas.view.DrawingView;

public class CanvasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_canvas);

        DrawingView drawingView = new DrawingView(this);
        drawingView.setDrawingCacheEnabled(true);
        //mv.setBackgroundResource(R.drawable.afor);//set the back ground if you wish to
        setContentView(drawingView);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(0xFFFF0000);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(20);
        MaskFilter embossMaskFilter = new EmbossMaskFilter(new float[]{1, 1, 1},
                0.4f, 6, 3.5f);
        MaskFilter blurMaskFilter = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
    }
}
