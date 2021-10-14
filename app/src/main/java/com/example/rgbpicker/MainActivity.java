package com.example.rgbpicker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView imgView;
    TextView mColorValues;
    View mColorViews;
    Bitmap bitmap;
    Spinner mainSpinner;
    public static String serverIP = "192.168.1.113";

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

    protected void populateModeList()
    {
        mainSpinner = (Spinner)findViewById(R.id.plansList);
        List<String> modes = new ArrayList<String>();
        modes.add("SINGLE COLOR");
        modes.add("PIXEL BY PIXEL");
        modes.add("RAINBOW");
        modes.add("NO LIGHTS");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, modes);
        mainSpinner.setAdapter(dataAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = findViewById(R.id.color);
        mColorValues = findViewById(R.id.displayValues);
        mColorViews = findViewById(R.id.displayColours);
        imgView.setDrawingCacheEnabled(true);
        imgView.buildDrawingCache(true);

        populateModeList();

        imgView.setOnTouchListener(new View.OnTouchListener() {
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
                    String newText = "SETCOLOR:" + r + "," + g + "," + b + "";
                    mColorValues.setText(newText);
                    Intent intent = new Intent(getApplicationContext(), MessageSender.class);
                }
                return true;
            }
        });
    }

    public void onClickSend(View v)
    {
        MessageSender msgSender = new MessageSender();
        if(!msgSender.hasConnection())
        {
            Toast.makeText(getApplicationContext(), "Connection failed!", Toast.LENGTH_SHORT).show();
            return;
        }
        String msg = (mColorValues.getText().toString()+","+ mainSpinner.getSelectedItem().toString());
        msgSender.sendMessage(msg);
    }

    public void onClickConfig(View view)
    {
        Intent i = new Intent(MainActivity.this, ConfigActivity.class);
        startActivity(i);
    }

    public void onBackPressed()
    {
        finishAffinity();
    }
}