package com.example.alex.db.profilesprovider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ProfilesMetaData {
	public static final String DATABASE_NAME = "Profiles.db";
	public static final int DATABASE_VERSION = 1;

	public static final String AUTHORITY = "com.example.alex.db.profilesprovider";

	/**
	 * Profiles table
	 */
	public static final class Profiles implements BaseColumns {
		public static final String TABLE_NAME = "profiles";
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/profiles");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/profiles";
		public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/profiles";
		/**Order by _ID ASC*/
		public static final String SORT_ORDER_DEFAULT = _ID + "ASC";
		
		// items
		/** Profiles name. Type: String. */
		public static final String NAME = "profile_name";
		/** Profiles enabled or not. Type: Boolean.*/
		public static final String ENABLED = "enabled";
		
		//Telephone settings
		/** Ring tone. Type: String. */
		public static final String RINGTONE = "ringtone";
		/** Ring tone volume. Type: int. */
		public static final String RINGTONE_VOLUME = "ringtone_volume";
		/** Profiles name. Type: String. */
		public static final String RING_VIBRATION = "ring_vibration";
		
		// notification settings
		/** Notification volume. Type: int. */
		public static final String NOTIFICATION_VOLUME = "notification_volume";
		/** Profiles name. Type: String. */
		public static final String NOTIFICATION_TONE = "notification_tone";
		
		// other setting
		/** Media volume. Type: int. */
		public static final String MEDIA_VOLUME = "media_volume";
		/** Alarm voluem. Type: int. */
		public static final String AlARM_VOLUME = "alarm_volume";
		
		
	}
}
