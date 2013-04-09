package com.example.alex.callnative;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CTAWiFiSetting {
	BufferedWriter bufferedWriter;

	public CTAWiFiSetting(File file) {
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write("1" + '\n');
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * (1)Command: 22 1 12 1 22: Initial transmit power in the antenna 1: Set
	 * the wanted channel (1~14) 12: Set the wanted power, typically G mode: 12
	 * dBm/ B mode: 15 dBm 1: Set the mode, 1_G mode & N mode , 0_ B mode
	 * 
	 * @param channel
	 * @param power
	 * @param mode
	 */
	public void setInitialPower(int channel, int power, int mode) {
		try {
			bufferedWriter.write("22 " + channel + ' ' + power + ' ' + mode
					+ '\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * (2)Command: 112 0 (only for N mode) 112: N mode HT20/40. 0: for HT20/40.
	 * 0_HT20, 1_HT40
	 */
	public void setNMode(int mode) {
		try {
			bufferedWriter.write("112 " + mode + '\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * (3)Command: 25 1 13 25: Set duty cycle, packet mode. 1: Data rate enable
	 * 13: Data rate set up B mode & G mode 1Mbps 5.5Mbps 11Mbps 6Mbps 9Mbps
	 * 12Mbps 18Mbps 24Mbps 1 3 4 6 7 8 9 10 36Mbps 48Mbps 54Mbps 11 12 13
	 * 
	 * 
	 * N mode MCS0 MCS1 MCS2 MCS3 MCS4 MCS5 MCS6 MCS7 MCS8 15 16 17 18 19 20 21
	 * 22 23
	 */
	public void setDutyCycleAndPacketMode(int dataRate) {
		try {
			bufferedWriter.write("25 1 " + dataRate + '\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Command: 25 0 Stop transmitting packet signal.
	 */
	public void stopTransmitting() {
		try {
			bufferedWriter.write("25 0" + '\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * quit labtool and close IO stream.
	 */
	public void setCompleted(){
		try {
			bufferedWriter.write("99\n99\n");
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
