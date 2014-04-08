package com.example.alex.wifi;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Looper;
import android.util.Log;

public class WiFiDirectService {
	private static final String TAG = "WiFiDirectService";
	private static final int SERVER_PORT = 55558;
	private WifiP2pManager mWifiP2pManager;
	private Channel channel;

	private Context mContext;

	public WiFiDirectService(Context context, Looper looper) {
		mContext = context;
		mWifiP2pManager = (WifiP2pManager) context
				.getSystemService(Context.WIFI_P2P_SERVICE);
		channel = mWifiP2pManager.initialize(context, looper,
				new ChannelListener() {

					@Override
					public void onChannelDisconnected() {
						Log.d(TAG, "onChannelDisconnected");
					}
				});
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
		Log.d(TAG, "channel = " + channel);
	}

	public void startRegistration() {
		Log.d(TAG, "startRegistration");
		// Create a string map containing information about your service.
		Map record = new HashMap();
		record.put("listenport", String.valueOf(SERVER_PORT));
		record.put("buddyname",
				"com.dreamlink.communication" + (int) (Math.random() * 1000));
		record.put("available", "visible");

		// Service information. Pass it an instance name, service type
		// _protocol._transportlayer, and the map containing
		// information other devices will want once the connect to this one.
		WifiP2pDnsSdServiceInfo serviceInfo = WifiP2pDnsSdServiceInfo
				.newInstance("_test", "_presence._tcp", record);

		// Add the local service, sending the service info, network channel,
		// and listener that will be used to indicate success or failure of
		// the request.
		mWifiP2pManager.addLocalService(channel, serviceInfo,
				new ActionListener() {

					@Override
					public void onSuccess() {
						// Command successful! Code isn't necessarily needed
						// here,Unless you want to update the UI or add logging
						// statement.
						Log.d(TAG, "addLocalService success.");

					}

					@Override
					public void onFailure(int reason) {
						// Command failed. Check for P2P_UNSUPPORTED, ERROR, OR
						// BUSY.
						Log.e(TAG, "addLocalService error: "
								+ getErrorCodeMeaning(reason));

					}
				});

	}

	private final HashMap<String, String> buddies = new HashMap<String, String>();
	private WifiP2pDnsSdServiceRequest mServiceRequest;

	public void discorerService() {
		Log.d(TAG, "discorerService");
		DnsSdTxtRecordListener txtRecordListener = new DnsSdTxtRecordListener() {

			@Override
			public void onDnsSdTxtRecordAvailable(String fullDomain,
					Map<String, String> record, WifiP2pDevice device) {
				Log.d(TAG, "onDnsSdTxtRecordAvailable: " + record.toString());
				buddies.put(device.deviceAddress, record.get("buddyname"));
			}
		};

		DnsSdServiceResponseListener serviceResponseListener = new DnsSdServiceResponseListener() {

			@Override
			public void onDnsSdServiceAvailable(String instanceName,
					String registrationType, WifiP2pDevice resourceType) {
				Log.d(TAG, "onDnsSdServiceAvailable: " + instanceName);
				// Update the device name with the human-friendly version from
				// the DnsTxtRecord, assuming one arrived.
				resourceType.deviceName = buddies
						.containsKey(resourceType.deviceAddress) ? buddies
						.get(resourceType.deviceAddress)
						: resourceType.deviceName;
				// Add to custom adapter defined specifically for showing wifi
				// devices.

			}
		};
		mWifiP2pManager.setDnsSdResponseListeners(channel,
				serviceResponseListener, txtRecordListener);
		mServiceRequest = WifiP2pDnsSdServiceRequest.newInstance();
		mWifiP2pManager.addServiceRequest(channel, mServiceRequest,
				new ActionListener() {

					@Override
					public void onSuccess() {
						// Success
						Log.d(TAG, "addServiceRequest sucess.");
					}

					@Override
					public void onFailure(int reason) {
						// Command failed. Check for P2P_UNSUPPORTED, ERROR, OR
						// BUSY.
						Log.e(TAG, "addServiceRequest error: "
								+ getErrorCodeMeaning(reason));

					}
				});

		mWifiP2pManager.discoverServices(channel, new ActionListener() {

			@Override
			public void onSuccess() {
				// Success
				Log.d(TAG, "discoverServices success.");
			}

			@Override
			public void onFailure(int reason) {
				// Command failed. Check for P2P_UNSUPPORTED, ERROR, OR
				// BUSY.
				Log.e(TAG, "discoverServices error: "
						+ getErrorCodeMeaning(reason));
			}
		});

	}

	private String getErrorCodeMeaning(int errorCode) {
		String error = null;
		switch (errorCode) {
		case WifiP2pManager.P2P_UNSUPPORTED:
			error = "Wi-Fi Direct isn't supported on the device running the app";
			break;
		case WifiP2pManager.BUSY:
			error = "The system is to busy to process the request";
		case WifiP2pManager.ERROR:
			error = "The operation failed due to an internal error";
		default:
			error = "Unkown error: " + errorCode;
			break;
		}
		return error;
	}

	public void searchPeer() {
		registerReceiver();
		mWifiP2pManager.discoverPeers(channel, new ActionListener() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "discoverPeers success:");
			}

			@Override
			public void onFailure(int reason) {
				Log.e(TAG, "discoverPeers fail: " + getErrorCodeMeaning(reason));
			}
		});
	}

	public void stopSearchPeer() {
		unregisterReceiver();
	}

	private final IntentFilter intentFilter = new IntentFilter();
	WiFiDirectBroadcastReceiver receiver;

	public void registerReceiver() {
		receiver = new WiFiDirectBroadcastReceiver(mWifiP2pManager, channel);
		mContext.registerReceiver(receiver, intentFilter);
	}

	public void unregisterReceiver() {
		mContext.unregisterReceiver(receiver);
	}
}
