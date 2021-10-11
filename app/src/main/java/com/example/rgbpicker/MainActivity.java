package com.example.rgbpicker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView imgView;
    TextView mColorValues;
    View mColorViews;
    TextView modeStatus;
    Switch modeswitch;
    Bitmap bitmap;
    Spinner mainSpinner;

    private int getColor(float x, float y, View v)
    {
        if (x < 0 || y < 0 || x > (float) v.getWidth() || y > (float) v.getHeight())
        {
            return 0; //Invalid, return 0
        } else {
            //Convert touched x, y on View to on Bitmap
            int xBm = (int) (x * (bitmap.getWidth() / (double) v.getWidth()));
            int yBm = (int) (y * (bitmap.getHeight() / (double) v.getHeight()));

            return bitmap.getPixel(xBm, yBm);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = findViewById(R.id.color);
        mColorValues = findViewById(R.id.displayValues);
        mColorViews = findViewById(R.id.displayColours);
        modeStatus = findViewById(R.id.modeText);
        imgView.setDrawingCacheEnabled(true);
        imgView.buildDrawingCache(true);
        //modeswitch = findViewById(R.id.modeswitch);
        populatePlansList();
        Handler handler = new Handler();
        Runnable r=new Runnable() {
            public void run() {
                String spinnerText = (mainSpinner.getSelectedItem().toString());
                modeStatus.setText(spinnerText);
                handler.postDelayed(this, 200);
            }
        };

        handler.postDelayed(r, 500);
        imgView.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    bitmap = imgView.getDrawingCache();
                    int pixels = getColor(event.getX(), event.getY(), v);
                    int r = Color.red(pixels);
                    int g = Color.green(pixels);
                    int b = Color.blue(pixels);
                    String hex = "#" + Integer.toHexString(pixels);
                    mColorViews.setBackgroundColor(Color.rgb(r, g, b));
                    mColorValues.setText("SETCOLOR:" + r + "," + g + "," + b + "");
                    Intent intent = new Intent(getApplicationContext(), msgSender.class);
                }
                return true;
            }
        });
    }
    public void send(View v) {
        msgSender messageSent = new msgSender();
        String msg = (mColorValues.getText().toString()+","+ modeStatus.getText().toString());
        msgSender.sendMessage(msg);
    }
    protected void populatePlansList()
    {
        mainSpinner = (Spinner)findViewById(R.id.plansList);

        List<String> categories = new ArrayList<String>();
        categories.add("DEFAULT MODE");
        categories.add("WAVE MODE");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        mainSpinner.setAdapter(dataAdapter);
    }
}