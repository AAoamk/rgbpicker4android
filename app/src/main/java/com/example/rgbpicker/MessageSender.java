package com.example.rgbpicker;
import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class MessageSender
{
    public void sendMessage(final String msg)
    {
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Socket s = new Socket(MainActivity.serverIP, 9001);

                    OutputStream out = s.getOutputStream();

                    PrintWriter output = new PrintWriter(out);

                    output.println(msg);
                    output.flush();
                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    final String st = input.readLine();

                    handler.post(new Runnable() {
                        @Override
                        public void run()
                        {
                            if (st.trim().length() != 0)
                                Log.i(null, "Received message: " + st);
                        }
                    });

                    output.close();
                    out.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public boolean hasConnection()
    {
        Runtime runtime = Runtime.getRuntime();
        try
        {
            Process pingProcess = runtime.exec("/system/bin/ping -c 1 " + MainActivity.serverIP);
            pingProcess.waitFor(1, TimeUnit.SECONDS);
            int exitVal = pingProcess.exitValue();
            pingProcess.destroy();
            return (exitVal == 0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e(null, "Exception: " + e);
        }
        return false;
    }
}