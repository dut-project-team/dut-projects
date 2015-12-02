package com.blogspot.sontx.whitelight.net;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public final class ServerConnection {
    private static final String TAG = "NOEM";
    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static ServerConnection instance = null;
    private Activity context;
    private String mac;
    private Handler bluetoothIn;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private ConnectedThread mConnectedThread;

    public static ServerConnection getInstance() {
        if (instance == null)
            instance = new ServerConnection();
        return instance;
    }

    public void setHandler(Handler handler) {
        this.bluetoothIn = handler;
    }

    private void showToast(final String st) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, st, Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean connect() {
        disconnect();
        boolean ok = true;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = btAdapter.getRemoteDevice(mac);
        //Attempt to create a bluetooth socket for comms
        try {
            btSocket = device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        } catch (IOException e1) {
            showToast("ERROR - Could not create Bluetooth socket");
            ok = false;
        }

        // Establish the connection.
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();        //If IO exception occurs attempt to close socket
            } catch (IOException e2) {
                showToast("ERROR - Could not close Bluetooth socket");
            }
            ok = false;
        }
        if (ok) {
            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();
        }
        return ok;
    }

    public void disconnect() {
        if (mConnectedThread != null) {
            mConnectedThread.disconnect();
            mConnectedThread.interrupt();
            mConnectedThread = null;
        }
    }

    public void setMAC(String mac) {
        this.mac = mac;
    }

    public void setContext(Activity activity) {
        this.context = activity;
    }

    public synchronized void sendRequest(Handler inHandler, int command, byte[] request) {
        this.setHandler(inHandler);
        sendRequest(command, request);
    }

    public synchronized void sendRequest(int command, byte[] request) {
        byte[] buff = new byte[request.length + 1];
        buff[0] = (byte) command;
        System.arraycopy(request, 0, buff, 1, request.length);
        mConnectedThread.setCommand(command);
        mConnectedThread.write(buff);
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final BluetoothSocket socket;

        private int command;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            this.socket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void setCommand(int command) {
            this.command = command;
        }

        public void disconnect() {
            try {
                mmInStream.close();
                mmOutStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            byte[] buffer = new byte[256];

            // Keep looping to listen for received messages
            while (true) {
                try {

                    if (mmInStream.available() <= 0) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }

                    Log.d(TAG, "available! begin read response data...");

                    // get data length
                    int length = mmInStream.read();
                    Log.d(TAG, "data length is " + length);

                    // get data with length
                    for (int i = 0; i < length; i++) {
                        buffer[i] = (byte) mmInStream.read();
                        Log.d(TAG, "Received id[" + i + "] = " + buffer[i]);
                    }

                    Log.d(TAG, "RECEIVED OK");

                    try {
                        byte[] actual = new byte[length];
                        System.arraycopy(buffer, 0, actual, 0, length);
                        // Send the obtained bytes to the UI Activity via handler
                        bluetoothIn.obtainMessage(command, 0, command, actual).sendToTarget();
                    } catch (IllegalArgumentException ex) {
                        ex.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    showToast("Connection Failure");
                    break;
                }
            }
        }

        //write method
        public void write(byte[] input) {
            try {
                mmOutStream.write(input);
                mmOutStream.flush();
            } catch (IOException e) {
                //if you cannot write, close the application
                showToast("Connection Failure");
            }
        }
    }

    private class RefreshObject implements Runnable {
        @Override
        public void run() {

        }
    }
}
