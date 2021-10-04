package com.example.rgbpicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageView imgView;
    TextView mColorValues;
    View mColorViews;
    TextView mColorStatus;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = findViewById(R.id.color);
        mColorValues = findViewById(R.id.displayValues);
        mColorViews = findViewById(R.id.displayColours);
        mColorStatus = findViewById(R.id.StatusText);
        imgView.setDrawingCacheEnabled(true);
        imgView.buildDrawingCache(true);

        imgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                    bitmap = imgView.getDrawingCache();
                    int pixels = bitmap.getPixel((int)event.getX(), (int)event.getY());
                    int r = Color.red(pixels);
                    int g = Color.green(pixels);
                    int b = Color.blue(pixels);
                    String hex = "#"+ Integer.toHexString(pixels);
                    mColorViews.setBackgroundColor(Color.rgb(r,g,b));
                    mColorValues.setText("rgb: "+r+", "+g+","+b+"\nhex: "+hex);
                    Intent intent = new Intent(getApplicationContext(), msgSender.class);
                }
                return true;
            }
        });
    }
    public void send(View v) {
        msgSender messageSent = new msgSender();
        msgSender.sendMessage("hello");
    }

}