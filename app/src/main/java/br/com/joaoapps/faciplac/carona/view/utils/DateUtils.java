package br.com.joaoapps.faciplac.carona.view.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Eduardo on 14/01/2017.
 */

public class DateUtils {
    private static String[] months;

    public static Date toDate(String format, String dateToParse) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.parse(dateToParse);
    }

    public static Date toDateAndZeroSeconds(Long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date today() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static String toString(String format, Date dateToParse) {
        return toString(format, dateToParse, "");
    }

    public static String toString(String format, Date dateToParse, String defaultVale) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-03:00"));
            return simpleDateFormat.format(dateToParse);
        } catch (Exception e) {
        }
        return defaultVale;
    }

    public static Date toDate(String format, String dateToParse, TimeZone timeZone) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        if (timeZone != null) {
            simpleDateFormat.setTimeZone(timeZone);
        }
        return simpleDateFormat.parse(dateToParse);
    }

    public static String toString(String format, Date dateToParse, TimeZone timeZone) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            if (timeZone != null) {
                simpleDateFormat.setTimeZone(timeZone);
            }
            return simpleDateFormat.format(dateToParse);
        } catch (Exception e) {
        }
        return "";
    }

//    public static String getMonthName(Context context){
//        months = context.getResources().getStringArray(R.array.months);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        int mes = calendar.get(Calendar.MONTH);
//        return months[mes];
//    }

    public static boolean isThisMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisYear = calendar.get(Calendar.YEAR);
        calendar.setTime(date);
        int dateMonth = calendar.get(Calendar.MONTH);
        int dateYear = calendar.get(Calendar.YEAR);
        return (dateMonth == thisMonth) && (dateYear == thisYear);
    }

    public static boolean isThisDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        int thisMonth = calendar.get(Calendar.MONTH);
        int thisYear = calendar.get(Calendar.YEAR);
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(date);
        int dateMonth = calendar.get(Calendar.MONTH);
        int dateYear = calendar.get(Calendar.YEAR);
        int dateDay = calendar.get(Calendar.DAY_OF_MONTH);
        return (dateMonth == thisMonth) && (dateYear == thisYear) && (dateDay == thisDay);
    }

    public static long diferenceDays(Date maior, Date menor) {

        long DAY = 24L * 60L * 60L * 1000L;
        long diasPassados = ((maior.getTime() - menor.getTime()) / DAY);
        return diasPassados;
    }

    public static Date initDate(Date date) {
        Calendar firstDay = Calendar.getInstance();
        firstDay.setTime(date);
        firstDay.set(Calendar.DAY_OF_MONTH, 1);
        return firstDay.getTime();
    }

    public static Date finalDate(Date date) {
        Calendar lastDay = Calendar.getInstance();
        lastDay.setTime(date);
        lastDay.add(Calendar.MONTH, 1);
        lastDay.set(Calendar.DAY_OF_MONTH, 1);
        lastDay.add(Calendar.DAY_OF_MONTH, -1);
        return lastDay.getTime();
    }

    public static int getDayWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayWeek;
    }

    public static Date addMonth(Date date) {
        return addWithCalendar(date, Calendar.MONTH, 1);
    }

    public static Date subtractMonth(Date date) {
        return addWithCalendar(date, Calendar.MONTH, -1);
    }

    private static Date addWithCalendar(Date date, int field, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, value);
        return calendar.getTime();
    }

    public static int getDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return dayMonth;
    }

    public static int getDayMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayMonth = calendar.get(Calendar.DAY_OF_WEEK);
        return dayMonth;
    }

    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        return month;
    }

    public static int getWeekOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.WEEK_OF_YEAR);
        return month;
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    public static Date getPrimeiroDiaSemana(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    public static Date getUltimoDiaSemana(Date data) {
        Date primeiroDia = getPrimeiroDiaSemana(data);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(primeiroDia);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.add(Calendar.DATE, 7);
        return calendar.getTime();
    }

    public static Date getPrimeiroDiaMes(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Date getUltimoDiaMes(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        int ultimo = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, ultimo);
        return calendar.getTime();
    }

    public static int countMinutes(Date date) {
        Date dataAtual = new Date();
        long dataAtualLong = dataAtual.getTime();
        long dateLong = date.getTime();

        Date dataReturn = new Date(dataAtualLong - dateLong);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataReturn);

        return  calendar.get(Calendar.MINUTE);

    }
}
