/*
* File:	     DateUtil.java
* Creation date:
* Author:        Lênio
*/
package utils.electricaldata.ifsuldeminas.edu.br;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * The Class DateUtil.
 */
public final class DateUtil {

	private static final long MILISECONDS_DAY = 86400000;
	
	private static final String MASK_FULL = "dd/MM/yyyy HH:mm:ss";
	
	private static final String MASK_DATE = "dd/MM/yyyy";
	
	private static final String MASK_SQL_DATE = "yyyy-MM-dd";
	
	private static final String MASK_SQL_FULL_DATE = "yyyy-MM-dd HH:mm:ss"; 
	
	private static final String IMPORTED_MASK_DATE = "dd-MMM-yy HH:mm:ss";

	private DateUtil(){
		
	}
	
	/**
	 * Now time stamp.
	 *
	 * @return the timestamp
	 */
	public static Timestamp nowTimeStamp(){
		return new Timestamp(nowLong());
	}

	/**
	 * Date to time stamp.
	 *
	 * @param date the date
	 * @return the timestamp
	 */
	public static Timestamp dateToTimeStamp(Date date){
		return new Timestamp(date.getTime());
	}
	
	/**
	 * Date to time stamp end day.
	 *
	 * @param date the date
	 * @return the timestamp
	 */
	public static Timestamp dateToTimeStampEndDay(Date date){
			Calendar now = Calendar.getInstance();
			now.setTime(date);
			now.set(Calendar.HOUR_OF_DAY, 23);
			now.set(Calendar.MINUTE, 59);
			now.set(Calendar.SECOND, 59);
			now.set(Calendar.MILLISECOND, 999);
		return new Timestamp(now.getTime().getTime());
	}

	/**
	 * Date to time stamp start day.
	 *
	 * @param date the date
	 * @return the timestamp
	 */
	public static Timestamp dateToTimeStampStartDay(Date date){
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		return new Timestamp(now.getTime().getTime());
	}
	
	/**
	 * Date to time stamp.
	 *
	 * @param time the time
	 * @return the timestamp
	 */
	public static Date timeStampToDate(Timestamp time){
		return new Date(time.getTime());
	}

	/**
	 * Now long.
	 *
	 * @return the long
	 */
	public static long nowLong(){
		return System.currentTimeMillis();
	}

	/**
	 * Now plus days.
	 *
	 * @param days the days
	 * @return the timestamp
	 */
	public static Timestamp nowPlusDays(int days){
		Long n = nowLong();
		n = n+ (days*MILISECONDS_DAY);
		return new Timestamp(n);
	}
	
	/**
	 * Ts plus days.
	 *
	 * @param ts the ts
	 * @param days the days
	 * @return the timestamp
	 */
	public static Timestamp tsPlusDays(Timestamp ts, int days){
		Long n = ts.getTime();
		n = n+ (days*MILISECONDS_DAY);
		return new Timestamp(n);
	}
	
	/**
	 * Creates the date cal.
	 *
	 * @param year the year
	 * @param month the month
	 * @param day the day
	 * @param hour the hour
	 * @param minute the minute
	 * @param second the second
	 * @return the date
	 */
	public static Date createDateCal(String year, String month, String day,
			String hour, String minute, String second) {
		SimpleDateFormat sdf = new SimpleDateFormat(MASK_FULL);
		Date d = null;
		try {
			d = sdf.parse(year+month+day+hour+minute+second);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}	

	/**
	 * Creates the date time stamp.
	 *
	 * @param year the year
	 * @param month the month
	 * @param day the day
	 * @param hour the hour
	 * @param minute the minute
	 * @param second the second
	 * @return the timestamp
	 * @throws NullPointerException the null pointer exception
	 */
	public static Timestamp createDateTimeStamp(String year, String month, String day,
			String hour, String minute, String second) throws NullPointerException {
		Date d = createDateCal(year, month, day, hour, minute, second);
		Timestamp t = new Timestamp(d.getTime());
		return t;
	}

	/**
	 * Creates the date format full.
	 *
	 * @param date the date
	 * @return the string
	 */
	public static String createDateFormatFull(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(MASK_FULL);
		String ret = null;
		ret = sdf.format(date);
		return ret;
	}

	/**
	 * Creates the date format full.
	 *
	 * @param ts the ts
	 * @return the string
	 */
	public static String createDateFormatFull(Timestamp ts) {
		return createDateFormatFull(timeStampToDate(ts));
	}
	
	/**
	 * Creates the date formatyyyy m mdd.
	 *
	 * @param ts the ts
	 * @return the string
	 */
	public static String createDateFormatyyyyMMdd(Timestamp ts) {
		return createDateFormatyyyyMMdd(timeStampToDate(ts));
	}
	
	/**
	 * Creates the date formatyyyy m mdd.
	 *
	 * @param date the date
	 * @return the string
	 */
	public static String createDateFormatyyyyMMdd(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(MASK_DATE);
		String ret = null;
		ret = sdf.format(date);
		return ret;
	}
	
	/**
	 * Creates the date format my sql.
	 *
	 * @param date the date
	 * @return the string
	 */
	public static String createDateFormatMySql(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(MASK_SQL_DATE);
		String ret = null;
		ret = sdf.format(date);
		return ret;
	}
	
	/**
	 * Creates the date format my sql.
	 *
	 * @param dateStr the date
	 * @return the string
	 * @throws ParseException 
	 */
	public static String createDateFormatMySql(String dateStr) throws ParseException {
		SimpleDateFormat parser = new SimpleDateFormat(IMPORTED_MASK_DATE, new Locale("pt", "BR"));
		Date d = parser.parse(dateStr);		
		SimpleDateFormat sdf = new SimpleDateFormat(MASK_SQL_FULL_DATE);
		String ret = sdf.format(d);
		return ret;
	}	
	
	/**
	 * Gets the now utc.
	 *
	 * @return the now utc
	 * @throws ParseException the parse exception
	 */
	public static Timestamp getNowUtc() throws ParseException{
		DateFormat utcFormat = new SimpleDateFormat(MASK_FULL);
		DateFormat utcFormatRet = new SimpleDateFormat(MASK_FULL);
	    TimeZone utcTime = TimeZone.getTimeZone("UTC");
	    utcFormat.setTimeZone(utcTime);
	    String utcDateStr = utcFormat.format(new Date());
	    Timestamp ret = dateToTimeStamp(utcFormatRet.parse(utcDateStr));
	    return ret; 
	}
	
	/**
	 * Gets the now.
	 *
	 * @return the now
	 */
	public static Date getNow(){
		return Calendar.getInstance().getTime();
	}
}

