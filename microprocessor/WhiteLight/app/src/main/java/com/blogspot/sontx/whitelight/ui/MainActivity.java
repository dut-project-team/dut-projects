package com.blogspot.sontx.whitelight.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blogspot.sontx.libex.net.MixSocket;
import com.blogspot.sontx.libex.util.Convert;
import com.blogspot.sontx.whitelight.R;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText txtAddr;
    private EditText txtPort;
    private EditText txtLog;
    private Button btnConnect;
    private Button btnDisconnect;
    private MixSocket client;
    private byte[] sample = {0, 1, 2, 3, 4, 5, 6, 10, 13};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, LightActivity.class));

        txtAddr = (EditText) findViewById(R.id.txt_addr);
        txtPort = (EditText) findViewById(R.id.txt_port);
        txtLog = (EditText) findViewById(R.id.txt_log);

        btnConnect = (Button) findViewById(R.id.btn_connect);
        btnDisconnect = (Button) findViewById(R.id.btn_disconnect);
        Button btnClear = (Button) findViewById(R.id.btn_clear);

        btnClear.setOnClickListener(this);
        btnConnect.setOnClickListener(this);
        btnDisconnect.setOnClickListener(this);

        Button btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log("Sending: " + Convert.bytesToString(sample, ","));
                String base64 = Convert.base64Encode(sample);
                log("Base64: " + base64);
                boolean ret = client.send(base64);
                log("Sent is " + (ret ? "OK" : "FAIL"));

                log("Waiting for server...");
                base64 = client.receiveLine();
                if (base64 != null) {
                    log("Received: " + base64);
                } else {
                    log("Something wrong, received nothing from server!");
                }
            }
        });
    }

    @Override
    public void onClick(final View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (view.getId()) {
                    case R.id.btn_connect:
                        connect();
                        break;
                    case R.id.btn_disconnect:
                        disconnect();
                        break;
                    case R.id.btn_clear:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtLog.setText("");
                            }
                        });

                        break;
                }
            }
        }).start();
    }

    private void log(final Object obj) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtLog.append(obj.toString() + "\n");
            }
        });
    }

    private void connect() {
        int port = Integer.parseInt(txtPort.getText().toString());
        log(String.format("Connect to %s:%d...", txtAddr.getText(), port));

        if (client != null)
            disconnect();

        try {
            client = new MixSocket(txtAddr.getText().toString(), port);
        } catch (IOException e) {
            log(e.getMessage());
            return;
        }

        log("Connected!");



        /*
        log("Waiting for server...");
        base64 = client.receiveString();
        if(base64 != null) {
            log("Received base64: " + base64);
            log("Decode base64: " + Convert.base64Decode(base64));
        } else {
            log("Something wrong, received nothing from server!");
        }

        client.send("x");
        log("Completed!");
        disconnect();
        */
    }

    private void disconnect() {
        if (client == null)
            return;
        log("Disconnecting...");
        try {
            client.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        log("Disconnected!");
    }
}
