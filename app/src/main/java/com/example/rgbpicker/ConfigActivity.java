package com.example.rgbpicker;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class ConfigActivity extends AppCompatActivity
{
    void changeStatus()
    {
        MessageSender msgSender = new MessageSender();
        View viewStat = (View)findViewById(R.id.viewStatus);
        int colorCode = msgSender.hasConnection() ? 0xFF00FF3D : 0xFFFF0000;
        viewStat.setBackgroundColor(colorCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        changeStatus();

        EditText textField = (EditText)findViewById(R.id.ipBox);
        textField.setText(MainActivity.serverIP);
        textField.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0)
                    MainActivity.serverIP = s.toString();
            }
        });
    }

    public void onClickTest(View v)
    {
        changeStatus();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ConfigActivity.this, MainActivity.class);
        startActivity(i);
    }
}