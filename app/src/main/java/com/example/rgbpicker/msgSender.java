package com.example.rgbpicker;
import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import android.os.Handler;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class msgSender
{
    public static void sendMessage(final String msg)
    {
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Socket s = new Socket("192.168.1.104", 9001);

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
}
/*
public class msgSender extends AsyncTask<String,Void,Void> {
    Socket s;
    DataOutputStream dos;
    PrintWriter pw;

    @Override
    protected Void doInBackground(String... voids) {
        //String msg = voids[0];
        String msg1 = "voids[0]";
        try {

            Socket s = new Socket("192.168.1.104", 9001);

            OutputStream out = s.getOutputStream();

            PrintWriter output = new PrintWriter(out);

            output.println(msg);
            output.flush();
            Socket socket = new Socket("192.168.56.1", 9000);

            OutputStream out = socket.getOutputStream();
            PrintWriter output = new PrintWriter(out);
            output.println(msg1);
            output.flush();
            output.close();
            socket.close();


        } catch (IOException e) {
            e.printStackTrace();

        }

        return null;
    }
}
*/