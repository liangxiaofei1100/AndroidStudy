package com.example.alex.calendar;

import java.security.spec.InvalidParameterSpecException;
import java.util.Calendar;
import java.util.Date;

import android.text.format.Time;

public class LunarDateCalendar {

	final static long[] lunarInfo = new long[] { 0x04bd8, 0x04ae0, 0x0a570,
			0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
			0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0,
			0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50,
			0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566,
			0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0,
			0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4,
			0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550,
			0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950,
			0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260,
			0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0,
			0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
			0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40,
			0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3,
			0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960,
			0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0,
			0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9,
			0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0,
			0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65,
			0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0,
			0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2,
			0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };

	final static long[] STermInfo = new long[] { 0, 21208, 42467, 63836, 85337,
			107014, 128867, 150921, 173149, 195551, 218072, 240693, 263343,
			285989, 308563, 331033, 353350, 375494, 397447, 419210, 440795,
			462224, 483532, 504758 };

	private String[] mMonthStrings;
	private static final int MONTH_LENGTH = 12;
	// = {"正月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月",
	// "十二月"};
	private String[] m24SolarTerm;
	// = { "小寒", "大寒", "立春", "雨水",
	// "惊蛰", "春分", "清明", "谷雨", "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋",
	// "处暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"};
	private static final int SOLARTERM_LENGTH = 24;
	private String[] mHolidayVacations;
	private static final int HOLIDAY_LENGTH = 11;
	// = {
	// "春节","元宵节","端午节","中秋节","七夕节","中元节","重阳节","下元节","腊八节","小年","除夕"
	// };

	private String[] mGongVacations;
	private static final int GONG_LENGTH = 11;
	// = {
	// "元旦","情人节","妇女节","愚人节","劳动节","儿童节","建军节","教师节","国庆节","平安夜","圣诞节",
	// };

	private String[] mChineseTen;
	private static final int TEN_LENGTH = 4;
	// = {"初", "十", "廿", "卅"};
	private String[] mDayStrings;
	private static final int DAY_LENGTH = 10;
	// = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
	private String mLeapmonthPrefix;

	// = "闰";

	public LunarDateCalendar(String[] month, String[] solarTerm,
			String[] vacations, String[] chineseTen, String[] dayStrings,
			String leapmonthPrefix, String[] gongVacations)
			throws NullPointerException, InvalidParameterSpecException {
		if (month == null || solarTerm == null || vacations == null
				|| chineseTen == null || leapmonthPrefix == null
				|| dayStrings == null || gongVacations == null) {
			throw new NullPointerException();
		}
		if (month.length != MONTH_LENGTH
				|| solarTerm.length != SOLARTERM_LENGTH
				|| vacations.length != HOLIDAY_LENGTH
				|| chineseTen.length != TEN_LENGTH
				|| dayStrings.length != DAY_LENGTH
				|| gongVacations.length != GONG_LENGTH) {
			throw new InvalidParameterSpecException();
		}
		mMonthStrings = month;
		m24SolarTerm = solarTerm;
		mHolidayVacations = vacations;
		mChineseTen = chineseTen;
		mDayStrings = dayStrings;
		mLeapmonthPrefix = leapmonthPrefix;
		mGongVacations = gongVacations;
	}

	/**
	 * @param lunar
	 *            year
	 * @return all days num
	 * **/
	final private static int yearDays(int y) {
		int i, sum = 348;
		for (i = 0x8000; i > 0x8; i >>= 1) {
			if ((lunarInfo[y - 1900] & i) != 0)
				sum += 1;
		}
		return (sum + leapDays(y));
	}

	/**
	 * @param lunar
	 *            year
	 * @return days of leapMonth
	 * **/
	final private static int leapDays(int y) {
		if (leapMonth(y) != 0) {
			if ((lunarInfo[y - 1900] & 0x10000) != 0)
				return 30;
			else
				return 29;
		} else
			return 0;
	}

	/**
	 * @param lunar
	 *            year
	 * @return leapMonth(1-12),not-0
	 * **/
	final private static int leapMonth(int y) {
		return (int) (lunarInfo[y - 1900] & 0xf);
	}

	/**
	 * @param lunar
	 *            year
	 * @param lunar
	 *            month
	 * @return days
	 * **/
	final private static int monthDays(int y, int m) {
		if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0)
			return 29;
		else
			return 30;
	}

	/**
	 * @param lunar
	 *            day
	 * @return chinaDayString
	 * */
	public String getChinaDayString(int day) {
		int n = day % 10 == 0 ? 9 : day % 10 - 1;
		if (day > 30)
			return "";
		if (day == 10)
			day--;
		return mChineseTen[day / 10] + mDayStrings[n];
	}

	Calendar mLunarCalendar = Calendar.getInstance();

	/** check year */
	public boolean isContation(Time time) {
		if (time.year < 1901 || time.year > 2049) {
			return false;
		}
		return true;
	}

	public void creatLunarStrings(Time time, int offse, String[] nonDays) {
		final Calendar c = mLunarCalendar;
		c.set(Calendar.YEAR, time.year);
		c.set(Calendar.MONTH, time.month);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.add(Calendar.DAY_OF_YEAR, -offse);
		for (int row = 0; row < 6; row++) {
			for (int column = 0; column < 7; column++) {
				int year = c.get(Calendar.YEAR);
				int mon = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);
				long[] ls = LunarDateCalendar.calElement(year, mon + 1, day);
				nonDays[row * 7 + column] = getMonthVocation(year, mon + 1,
						day, ls);
				c.add(Calendar.DATE, 1);
			}
		}
	}

	/**
	 * @param Gregorian
	 *            year
	 * @param Gregorian
	 *            month(1-12)
	 * @param Gregorian
	 *            day
	 * @param lunar
	 *            calendar date
	 * @return Holiday and vacations and chinaDayString
	 * **/
	public String getMonthVocation(int gongyear, int gongmonth, int gongday,
			long[] ls) {
		String st = null;
		final int nongyear = (int) ls[0];
		final int nongmonth = (int) ls[1];
		final int iday = (int) ls[2];
		// try{
		// 移动要求农历节假日
		if (((nongmonth) == 1) && iday == 1)
			st = mHolidayVacations[0];// "春节";
		else if (((nongmonth) == 5) && iday == 5)
			st = mHolidayVacations[2];// "端午节";
		else if (((nongmonth) == 8) && iday == 15)
			st = mHolidayVacations[3];// "中秋节";

		// 移动要求主要阳历节假日
		if (st == null) {
			if (gongmonth == 1 && gongday == 1) {
				st = mGongVacations[0];// "元旦";
			} else if (gongmonth == 5 && gongday == 1) {
				st = mGongVacations[4];// "劳动节";
			} else if (gongmonth == 10 && gongday == 1) {
				st = mGongVacations[8];// "国庆节";
			}
		}
		// 移动要求24节气
		if (st == null) {
			st = getSoralTerm(gongyear, gongmonth, gongday);
		}

		// 农历节假日
		if (st == null) {
			if (((nongmonth) == 1) && iday == 15)
				st = mHolidayVacations[1];// "元宵节";
			else if (((nongmonth) == 7) && iday == 7)
				st = mHolidayVacations[4];// "七夕节";
			else if (((nongmonth) == 7) && iday == 15)
				st = mHolidayVacations[5];// "中元节";
			else if (((nongmonth) == 9) && iday == 9)
				st = mHolidayVacations[6];// "重阳节";
			else if (((nongmonth) == 10) && iday == 15)
				st = mHolidayVacations[7];// "下元节";
			else if (((nongmonth) == 12) && iday == 8)
				st = mHolidayVacations[8];// "腊八节";
			else if (((nongmonth) == 12) && iday == 23)
				st = mHolidayVacations[9];// "小年";
			else if (((nongmonth) == 12)
					&& iday == monthDays(nongyear, nongmonth))
				st = mHolidayVacations[10];// "除夕";
		}
		// 阳历节假日
		if (st == null) {
			if (gongmonth == 2 && gongday == 14) {
				st = mGongVacations[1];// "情人节";
			} else if (gongmonth == 3 && gongday == 8) {
				st = mGongVacations[2];// "妇女节";
			} else if (gongmonth == 4 && gongday == 1) {
				st = mGongVacations[3];// "愚人节";
			} else if (gongmonth == 6 && gongday == 1) {
				st = mGongVacations[5];// "儿童节";
			} else if (gongmonth == 8 && gongday == 1) {
				st = mGongVacations[6];// "建军节";
			} else if (gongmonth == 9 && gongday == 10) {
				st = mGongVacations[7];// "教师节";
			} else if (gongmonth == 12 && gongday == 24) {
				st = mGongVacations[9];// "平安夜";
			} else if (gongmonth == 12 && gongday == 25) {
				st = mGongVacations[10];// "圣诞节";
			}
		}

		if (st == null) {
			if (iday == 1) {
				if (ls[6] == 1) {
					st = mLeapmonthPrefix + mMonthStrings[nongmonth - 1];
				} else {
					st = mMonthStrings[nongmonth - 1];
				}
			} else {
				st = getChinaDayString(iday);
			}
		}
		return st;
	}

	/**
	 * From the Gregorian calendar to lunar calendar
	 * 
	 * @param Gregorian
	 *            year
	 * @param Gregorian
	 *            month(1-12)
	 * @param Gregorian
	 *            day
	 * @return lunar calendar:year .month .day .yearCyl3 .monCyl4 .dayCyl5
	 *         .isLeap6
	 */
	final public static long[] calElement(int y, int m, int d)

	{
		long[] nongDate = new long[7];
		int i = 0, temp = 0, leap = 0;
		Date baseDate = new Date(0, 0, 31);
		Date objDate = new Date(y - 1900, m - 1, d);
		long offset = (objDate.getTime() - baseDate.getTime()) / 86400000L;
		nongDate[5] = offset + 40;
		nongDate[4] = 14;
		for (i = 1900; i < 2050 && offset > 0; i++) {
			temp = yearDays(i);
			offset -= temp;
			nongDate[4] += 12;
		}
		if (offset < 0) {
			offset += temp;
			i--;
			nongDate[4] -= 12;
		}
		nongDate[0] = i;
		nongDate[3] = i - 1864;
		leap = leapMonth(i);
		nongDate[6] = 0;
		for (i = 1; i < 13 && offset > 0; i++) {
			// Leap month
			if (leap > 0 && i == (leap + 1) && nongDate[6] == 0) {
				--i;
				nongDate[6] = 1;
				temp = leapDays((int) nongDate[0]);
			} else {
				temp = monthDays((int) nongDate[0], i);
			}
			// Relieve Leap month
			if (nongDate[6] == 1 && i == (leap + 1))
				nongDate[6] = 0;
			offset -= temp;
			if (nongDate[6] == 0)
				nongDate[4]++;
		}
		if (offset == 0 && leap > 0 && i == leap + 1) {
			if (nongDate[6] == 1) {
				nongDate[6] = 0;
			} else {
				nongDate[6] = 1;
				--i;
				--nongDate[4];
			}
		}
		if (offset < 0) {
			offset += temp;
			--i;
			--nongDate[4];
		}
		nongDate[1] = i;
		nongDate[2] = offset + 1;
		return nongDate;
	}

	/**
	 * get 24 solar term
	 * 
	 * @param Gregorian
	 *            year
	 * @param Gregorian
	 *            month(1-12)
	 * @param Gregorian
	 *            day
	 * @return 24 solar term
	 */
	public String getSoralTerm(int y, int m, int d) {
		String solarTerms;
		if (d == sTerm(y, (m - 1) * 2))
			solarTerms = m24SolarTerm[(m - 1) * 2];
		else if (d == sTerm(y, (m - 1) * 2 + 1))
			solarTerms = m24SolarTerm[(m - 1) * 2 + 1];
		else {
			// not a solar term
			solarTerms = null;
		}
		return solarTerms;
	}

	/**
	 * get the solar term day which position is n(times) in the y(year),from
	 * slight_cold
	 * 
	 * @param Gregorian
	 *            year
	 * @param n
	 *            solar term(position)
	 * @return day of month
	 */
	private static int sTerm(int y, int n) {
		Calendar cal = Calendar.getInstance();
		cal.set(1900, 0, 6, 2, 5, 0);
		long temp = cal.getTime().getTime();
		cal.setTime(new Date(
				(long) ((31556925974.7 * (y - 1900) + STermInfo[n] * 60000L) + temp)));

		return cal.get(Calendar.DAY_OF_MONTH);
	}
}