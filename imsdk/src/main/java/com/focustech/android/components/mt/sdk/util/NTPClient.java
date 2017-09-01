package com.focustech.android.components.mt.sdk.util;

import android.os.SystemClock;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Simple SNTP client class for retrieving network time.
 * <p/>
 * Sample usage:
 * <pre>SntpClient client = new SntpClient();
 * if (client.requestTime("time.foo.com")) {
 *     long now = client.getNtpTime() + SystemClock.elapsedRealtime() - client.getNtpTimeReference();
 * }
 * </pre>
 */
public class NTPClient {
    private static NTPClient instance = new NTPClient();

    //private static final int REFERENCE_TIME_OFFSET = 16;
    private static final int ORIGINATE_TIME_OFFSET = 24;
    private static final int RECEIVE_TIME_OFFSET = 32;
    private static final int TRANSMIT_TIME_OFFSET = 40;
    private static final int NTP_PACKET_SIZE = 48;

    private static final int NTP_MODE_CLIENT = 3;
    private static final int NTP_VERSION = 3;

    // system time computed from NTP server response
    private long ntpTime;

    // value of SystemClock.elapsedRealtime() corresponding to mNtpTime
    private long ntpTimeReference;

    // round trip time in milliseconds
    private long ntpRoundTripTime;

    private long clockOffset;

    public static NTPClient getInstance() {
        return instance;
    }

    /**
     * Sends an SNTP request to the given host and processes the response.
     *
     * @param host    host name of the server.
     * @param timeout network timeout in milliseconds.
     * @return true if the transaction was successful.
     */
    public boolean requestTime(String host, int port, int timeout) {
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(timeout);
            byte[] buffer = new byte[NTP_PACKET_SIZE];

            // set mode = 3 (client) and version = 3
            // mode is in low 3 bits of first byte
            // version is in bits 3-5 of first byte
            buffer[0] = NTP_MODE_CLIENT | (NTP_VERSION << 3);

            // get current time and write it to the request packet
            long requestTime = System.currentTimeMillis();
            long requestTicks = SystemClock.elapsedRealtime();
            writeTimeStamp(buffer, ORIGINATE_TIME_OFFSET, requestTime);

            OutputStream out = socket.getOutputStream();
            out.write(buffer);
            out.flush();

            InputStream in = socket.getInputStream();
            in.read(buffer);

            long responseTicks = SystemClock.elapsedRealtime();
            long responseTime = requestTime + (responseTicks - requestTicks);

            // extract the results
            long originateTime = readTimeStamp(buffer, ORIGINATE_TIME_OFFSET);
            long receiveTime = readTimeStamp(buffer, RECEIVE_TIME_OFFSET);
            long transmitTime = readTimeStamp(buffer, TRANSMIT_TIME_OFFSET);
            long roundTripTime = responseTicks - requestTicks - (transmitTime - receiveTime);
            // receiveTime = originateTime + transit + skew
            // responseTime = transmitTime + transit - skew
            // clockOffset = ((receiveTime - originateTime) + (transmitTime - responseTime))/2
            //             = ((originateTime + transit + skew - originateTime) +
            //                (transmitTime - (transmitTime + transit - skew)))/2
            //             = ((transit + skew) + (transmitTime - transmitTime - transit + skew))/2
            //             = (transit + skew - transit + skew)/2
            //             = (2 * skew)/2 = skew
            clockOffset = ((receiveTime - originateTime) + (transmitTime - responseTime)) / 2;
            // if (false) Log.d(TAG, "round trip: " + roundTripTime + " ms");
            // if (false) Log.d(TAG, "clock offset: " + clockOffset + " ms");

            // save our results - use the times on this side of the network latency
            // (response rather than request time)
            ntpTime = responseTime + clockOffset;
            ntpTimeReference = responseTicks;
            ntpRoundTripTime = roundTripTime;
        } catch (Exception e) {
            return false;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    public long getClockOffset() {
        return clockOffset;
    }

    /**
     * Returns the time computed from the NTP transaction.
     *
     * @return time value computed from NTP server response.
     */
    public long getNtpTime() {
        return ntpTime;
    }

    /**
     * Returns the reference clock value (value of SystemClock.elapsedRealtime())
     * corresponding to the NTP time.
     *
     * @return reference clock corresponding to the NTP time.
     */
    public long getNtpTimeReference() {
        return ntpTimeReference;
    }

    /**
     * Returns the round trip time of the NTP transaction
     *
     * @return round trip time in milliseconds.
     */
    public long getRoundTripTime() {
        return ntpRoundTripTime;
    }

    /**
     * Reads the NTP time stamp at the given offset in the buffer and returns
     * it as a system time (milliseconds since January 1, 1970).
     */
    private long readTimeStamp(byte[] buffer, int offset) {
        byte b0 = buffer[offset];
        byte b1 = buffer[offset + 1];
        byte b2 = buffer[offset + 2];
        byte b3 = buffer[offset + 3];
        byte b4 = buffer[offset + 4];
        byte b5 = buffer[offset + 5];
        byte b6 = buffer[offset + 6];
        byte b7 = buffer[offset + 7];


        // convert signed bytes to unsigned values
        int i0 = ((b0 & 0x80) == 0x80 ? (b0 & 0x7F) + 0x80 : b0);
        int i1 = ((b1 & 0x80) == 0x80 ? (b1 & 0x7F) + 0x80 : b1);
        int i2 = ((b2 & 0x80) == 0x80 ? (b2 & 0x7F) + 0x80 : b2);
        int i3 = ((b3 & 0x80) == 0x80 ? (b3 & 0x7F) + 0x80 : b3);
        int i4 = ((b4 & 0x80) == 0x80 ? (b4 & 0x7F) + 0x80 : b4);
        int i5 = ((b5 & 0x80) == 0x80 ? (b5 & 0x7F) + 0x80 : b5);
        int i6 = ((b6 & 0x80) == 0x80 ? (b6 & 0x7F) + 0x80 : b6);
        int i7 = ((b7 & 0x80) == 0x80 ? (b7 & 0x7F) + 0x80 : b7);

        return ((long) i0 << 56) + ((long) i1 << 48) + ((long) i2 << 40) + ((long) i3 << 32) + ((long) i4 << 24) + ((long) i5 << 16) + ((long) i6 << 8) + (long) i7;
    }

    /**
     * Writes system time (milliseconds since January 1, 1970) as an NTP time stamp
     * at the given offset in the buffer.
     */
    private void writeTimeStamp(byte[] buffer, int offset, long time) {
        buffer[offset++] = (byte) (time >> 56);
        buffer[offset++] = (byte) (time >> 48);
        buffer[offset++] = (byte) (time >> 40);
        buffer[offset++] = (byte) (time >> 32);
        buffer[offset++] = (byte) (time >> 24);
        buffer[offset++] = (byte) (time >> 16);
        buffer[offset++] = (byte) (time >> 8);
        buffer[offset++] = (byte) (time >> 0);
    }
}
