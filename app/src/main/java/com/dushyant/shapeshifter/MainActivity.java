package com.dushyant.shapeshifter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dushyant.library.PolygonView;

public class MainActivity extends AppCompatActivity {
    PolygonView polygonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        polygonView = (PolygonView) findViewById(R.id.polygon);
        polygonView.setBorder(true);
        polygonView.setBorderWidth(10);
        polygonView.setImageSource(R.drawable.dragon);
        polygonView.setRotateDegree(0);
        polygonView.setSides(7);
        polygonView.setScaleType(PolygonView.ScaleType.CENTRE_CROP);
    }
}
