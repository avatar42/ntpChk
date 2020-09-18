/**
 * 
 */
package com.dea42.ntpChk;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple quick check of ntp servers so see they are responding. Based on
 * https://www.javatips.net/api/truetime-android-master/library/src/main/java/com/instacart/library/truetime/SntpClient.java
 * 
 * @author avata
 *
 */
public class SntpTest {
	private static final String TAG = SntpTest.class.getSimpleName();
	private static final Logger LOGGER = LoggerFactory.getLogger(TAG);
	private static final String TIMEOUT_KEY = "timeoutInMillis";

	private static final int NTP_PORT = 123;
	private static final int NTP_MODE = 3;
	private static final int NTP_VERSION = 3;
	private static final int NTP_PACKET_SIZE = 48;

	private static final int INDEX_VERSION = 0;
	private static final int INDEX_ROOT_DELAY = 4;
	private static final int INDEX_ROOT_DISPERSION = 8;
	private static final int INDEX_ORIGINATE_TIME = 24;
	private static final int INDEX_RECEIVE_TIME = 32;
	private static final int INDEX_TRANSMIT_TIME = 40;

	// 70 years plus 17 leap days
	private static final long OFFSET_1900_TO_1970 = ((365L * 70L) + 17L) * 24L * 60L * 60L;

	/**
	 * Sends an NTP request to the given host and processes the response.
	 * 
	 * @param name            TODO
	 * @param ntpHost         host name of the server.
	 * @param timeoutInMillis network timeout in milliseconds.
	 *
	 * @throws Exception
	 */
	private ResponseInfo requestTime(String name, String ntpHost, int timeoutInMillis) {

		DatagramSocket socket = null;
		ResponseInfo ri = new ResponseInfo(ntpHost, name);

		try {

			byte[] buffer = new byte[NTP_PACKET_SIZE];
			InetAddress address = InetAddress.getByName(ntpHost);

			DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, NTP_PORT);

			writeVersion(buffer);

			// -----------------------------------------------------------------------------------
			// get current time and write it to the request packet

			long requestTime = System.currentTimeMillis();
			long requestTicks = System.currentTimeMillis(); // SystemClock.elapsedRealtime();

			writeTimeStamp(buffer, INDEX_TRANSMIT_TIME, requestTime);

			socket = new DatagramSocket();
			socket.setSoTimeout(timeoutInMillis);
			socket.send(request);

			// -----------------------------------------------------------------------------------
			// read the response

			DatagramPacket response = new DatagramPacket(buffer, buffer.length);
			socket.receive(response);

			long responseTicks = System.currentTimeMillis(); // SystemClock.elapsedRealtime();
			ri.setResponse_response_ticks(responseTicks);
//
//			// -----------------------------------------------------------------------------------
//			// extract the results
//			// See here for the algorithm used:
//			// https://en.wikipedia.org/wiki/Network_Time_Protocol#Clock_synchronization_algorithm
//
			long originateTime = readTimeStamp(buffer, INDEX_ORIGINATE_TIME); // T0
			long receiveTime = readTimeStamp(buffer, INDEX_RECEIVE_TIME); // T1
			long transmitTime = readTimeStamp(buffer, INDEX_TRANSMIT_TIME); // T2
			long responseTime = requestTime + (responseTicks - requestTicks); // T3

			ri.setResponse_originate_time(originateTime);
			ri.setResponse_receive_time(receiveTime);
			ri.setResponse_transmit_time(transmitTime);
			ri.setResponse_response_time(responseTime);

			// -----------------------------------------------------------------------------------
			// check validity of response

			ri.setResponse_root_delay(read(buffer, INDEX_ROOT_DELAY));
			double rootDelay = doubleMillis(ri.getResponse_root_delay());
			if (rootDelay > 100) {

				ri.addError("Invalid response from NTP server. Root delay violation " + rootDelay);
			}

			ri.setResponse_dispersion(read(buffer, INDEX_ROOT_DISPERSION));
			double rootDispersion = doubleMillis(ri.getResponse_dispersion());
			if (rootDispersion > 100) {
				ri.addError("Invalid response from NTP server. Root dispersion violation " + rootDispersion);
			}

			final byte mode = (byte) (buffer[0] & 0x7);
			if (mode != 4 && mode != 5) {
				ri.addError("untrusted mode value for TrueTime: " + mode);
			}

			final int stratum = buffer[1] & 0xff;
			ri.setResponse_stratum(stratum);
			if (stratum < 1 || stratum > 15) {
				ri.addError("untrusted stratum value for TrueTime: " + stratum);
			}

			final byte leap = (byte) ((buffer[0] >> 6) & 0x3);
			if (leap == 3) {
				ri.addError("unsynchronized server responded for TrueTime");
			}

			long delay = Math.abs((responseTime - originateTime) - (transmitTime - receiveTime));
			if (delay >= 100) {
				ri.addError("Server response delay too large for comfort " + delay);
			}

			long timeElapsedSinceRequest = Math.abs(originateTime - System.currentTimeMillis());
			if (timeElapsedSinceRequest >= 10000) {
				ri.addError("Request was sent more than 10 seconds back " + timeElapsedSinceRequest);
			}

			LOGGER.info("---- SNTP successful response from " + ntpHost);

		} catch (Exception e) {
			ri.addError("---- SNTP request failed for " + ntpHost);
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
		return ri;
	}

	// -----------------------------------------------------------------------------------
	// private helpers

	/**
	 * Writes NTP version as defined in RFC-1305
	 */
	private void writeVersion(byte[] buffer) {
		// mode is in low 3 bits of first byte
		// version is in bits 3-5 of first byte
		buffer[INDEX_VERSION] = NTP_MODE | (NTP_VERSION << 3);
	}

	/**
	 * Writes system time (milliseconds since January 1, 1970) as an NTP time stamp
	 * as defined in RFC-1305 at the given offset in the buffer
	 */
	private void writeTimeStamp(byte[] buffer, int offset, long time) {

		long seconds = time / 1000L;
		long milliseconds = time - seconds * 1000L;

		// consider offset for number of seconds
		// between Jan 1, 1900 (NTP epoch) and Jan 1, 1970 (Java epoch)
		seconds += OFFSET_1900_TO_1970;

		// write seconds in big endian format
		buffer[offset++] = (byte) (seconds >> 24);
		buffer[offset++] = (byte) (seconds >> 16);
		buffer[offset++] = (byte) (seconds >> 8);
		buffer[offset++] = (byte) (seconds >> 0);

		long fraction = milliseconds * 0x100000000L / 1000L;

		// write fraction in big endian format
		buffer[offset++] = (byte) (fraction >> 24);
		buffer[offset++] = (byte) (fraction >> 16);
		buffer[offset++] = (byte) (fraction >> 8);

		// low order bits should be random data
		buffer[offset++] = (byte) (Math.random() * 255.0);
	}

	/**
	 * @param offset offset index in buffer to start reading from
	 * @return NTP timestamp in Java epoch
	 */
	private long readTimeStamp(byte[] buffer, int offset) {
		long seconds = read(buffer, offset);
		long fraction = read(buffer, offset + 4);

		return ((seconds - OFFSET_1900_TO_1970) * 1000) + ((fraction * 1000L) / 0x100000000L);
	}

	/**
	 * Reads an unsigned 32 bit big endian number from the given offset in the
	 * buffer
	 *
	 * @return 4 bytes as a 32-bit long (unsigned big endian)
	 */
	private long read(byte[] buffer, int offset) {
		byte b0 = buffer[offset];
		byte b1 = buffer[offset + 1];
		byte b2 = buffer[offset + 2];
		byte b3 = buffer[offset + 3];

		return ((long) ui(b0) << 24) + ((long) ui(b1) << 16) + ((long) ui(b2) << 8) + (long) ui(b3);
	}

	/***
	 * Convert (signed) byte to an unsigned int
	 *
	 * Java only has signed types so we have to do more work to get unsigned ops
	 *
	 * @param b input byte
	 * @return unsigned int value of byte
	 */
	private int ui(byte b) {
		return b & 0xFF;
	}

	/**
	 * Used for root delay and dispersion
	 *
	 * According to the NTP spec, they are in the NTP Short format viz. signed 16.16
	 * fixed point
	 *
	 * @param fix signed fixed point number
	 * @return as a double in milliseconds
	 */
	private double doubleMillis(long fix) {
		return fix / 65.536D;
	}

	public void chkServers() {

		ResourceBundle bundle = ResourceBundle.getBundle("servers");
		int timeoutInMillis = Integer.parseInt(bundle.getString(TIMEOUT_KEY));

		int goodCnt = 0;
		long totalOffsets = 0;
		List<ResponseInfo> goodHosts = new ArrayList<ResponseInfo>();
		for (String key : bundle.keySet()) {
			if (TIMEOUT_KEY.equals(key))
				continue;

			ResponseInfo ri = requestTime(key, bundle.getString(key), timeoutInMillis);
			LOGGER.info(ri.toString());
			if (ri.isGood()) {
				goodCnt++;
				totalOffsets += ri.getOffset();
				goodHosts.add(ri);
			} else {
				LOGGER.error(ri.getName() + ":" + ri.getHost() + ":" + ri.getErrors());
			}
		}
		long avg = totalOffsets / goodCnt;
		LOGGER.warn("---------------------------------------");
		LOGGER.warn("Average offset from " + goodCnt + " good responders:" + avg + " millisecs");
		LOGGER.warn("name:\thost:\toffset:\tDeviation");
		for (ResponseInfo ri : goodHosts) {
			LOGGER.warn(ri.getName() + ":\t" + ri.getHost() + ":\t" + ri.getOffset() + ":\t" + (ri.getOffset() - avg));
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SntpTest st = new SntpTest();
		st.chkServers();
	}

}
