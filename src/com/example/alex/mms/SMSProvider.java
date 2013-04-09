package com.example.alex.mms;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Constant value of android SMS content provider.
 */
public class SMSProvider {
	public static final Uri URI_ALL = Uri.parse("content://sms");
	public static final Uri URI_SENT = Uri.parse("content://sms/sent");

	public static final Uri URI_INBOX = Uri.parse("content://sms/inbox");
	public static final Uri URI_OUTBOX = Uri.parse("content://sms/outbox");
	public static final Uri URI_DRAFT = Uri.parse("content://sms/draft");

	public class SMS implements BaseColumns {
		public static final String TABLE_SMS = "sms";

		// SMS type
		public static final int MESSAGE_TYPE_ALL = 0;
		public static final int MESSAGE_TYPE_INBOX = 1;
		public static final int MESSAGE_TYPE_SENT = 2;
		public static final int MESSAGE_TYPE_DRAFT = 3;
		public static final int MESSAGE_TYPE_OUTBOX = 4;
		// for failed outgoing messages
		public static final int MESSAGE_TYPE_FAILED = 5;
		// for messages to send later
		public static final int MESSAGE_TYPE_QUEUED = 6;

		// SMS columns
		public static final String DATE = "date";
		public static final String READ = "read";
		public static final String TYPE = "type";
		public static final String ADDRESS = "address";
		public static final String BODY = "body";

	}

	public class SMSThread {
		public static final String TABLE_SMS = "threads";
	}
}
