package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

public class CheckMoodsOverTimePeriod extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_moods_over_time_period);
    }

    public class CheckIn{
        public String Diet;
        public Double Activity;
        public DateTime Date;
        protected CheckIn(String diet, Double activity, DateTime date){
            this.Diet = diet;
            this.Activity = activity;
            this.Date = date;
        }
    }
    public class MoodAndActivity{
        public String Mood;
        public String HasReason;
        public String Reason;
        public DateTime DateAndTime;
        public int Severity;
        public String Weather;
        public String Diet;
        public Double HoursOfActivity;
        public Double HoursOfSleep;
        protected MoodAndActivity(String mood, String hasReason, String reason, DateTime dateAndTime, int severity, String weather, String diet, Double hoursOfActivity, Double hoursOfSleep){
            this.Mood = mood;
            this.HasReason = hasReason;
            this.Reason = reason;
            this.DateAndTime = dateAndTime;
            this.Severity = severity;
            this.Weather = weather;
            this.Diet = diet;
            this.HoursOfActivity = hoursOfActivity;
            this.HoursOfSleep = hoursOfSleep;
        }
    }
    private static String MakeTimeStampUsable(String TimeStamp){
        String monthFromTimeStamp = TimeStamp.substring(5, 7);
        int month = Integer.parseInt(monthFromTimeStamp);
        month--;
        String string2 = String.format("%02d", month);
        String string1 = TimeStamp.substring(0, 5);
        String string3 = TimeStamp.substring(7, 19);
        String result = string1 + string2 + string3;
        return result;
    }
    private static DateTime parseDateTime(String input){
        String pattern = "yyyy-MM-dd HH:mm:ss";
        DateTime dateTime  = DateTime.parse(input, DateTimeFormat.forPattern(pattern));
        return dateTime;
    }

    public void queryDB(View view){
        DatePicker datePickerBegin = (DatePicker) findViewById(R.id.datePickerBegin);
        DatePicker datePickerEnd = (DatePicker) findViewById(R.id.datePickerEnd);
        int beginningDay = datePickerBegin.getDayOfMonth();
        int beginningMonth = datePickerBegin.getMonth();
        int beginningYear = datePickerBegin.getYear();
        beginningMonth++;
        String beginDateString = beginningYear + "-" + beginningMonth +"-" + beginningDay;
        DateTime beginDate = DateTime.parse(beginDateString);

        int endingDay = datePickerEnd.getDayOfMonth();
        int endingMonth = datePickerEnd.getMonth();
        int endingYear = datePickerEnd.getYear();
        endingMonth++;
        String endingDateString = endingYear + "-" + endingMonth + "-" + endingDay;
        DateTime endDate = DateTime.parse(endingDateString);

        SQLiteDatabase moodsDB = null;
        SQLiteDatabase checkInsDB = null;
        SQLiteDatabase averageDayDB = null;

        List<MoodAndActivity> moodDataList = new ArrayList<MoodAndActivity>();
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_check_moods_over_time_period);

        try{
            moodsDB = this.openOrCreateDatabase("moods", MODE_PRIVATE, null);
            Cursor moodsCursor = moodsDB.rawQuery("SELECT * FROM moods", null);
            int MoodsColumnMood = moodsCursor.getColumnIndex("mood");
            int MoodsColumnHasReason = moodsCursor.getColumnIndex("hasReason");
            int MoodsColumnReason = moodsCursor.getColumnIndex("reason");
            int MoodsColumnTime = moodsCursor.getColumnIndex("time");
            int MoodsColumnWeather = moodsCursor.getColumnIndex("weather");
            int MoodsColumnSeverity = moodsCursor.getColumnIndex("severity");

            checkInsDB = this.openOrCreateDatabase("checkIns", MODE_PRIVATE, null);
            Cursor checkInsCursor = checkInsDB.rawQuery("SELECT * FROM checkIns", null);
            int CheckInsColumnDiet = checkInsCursor.getColumnIndex("diet");
            int CheckInsColumnActivity = checkInsCursor.getColumnIndex("activity");
            int CheckInsColumnCheckInDate = checkInsCursor.getColumnIndex("checkInDate");

            averageDayDB = this.openOrCreateDatabase("averageDay", MODE_PRIVATE, null);
            Cursor averageDayCursor = averageDayDB.rawQuery("SELECT * FROM averageDay", null);
            int AverageDayColumnDiet = averageDayCursor.getColumnIndex("diet");
            int AverageDayColumnActivity = averageDayCursor.getColumnIndex("activity");
            int AverageDayColumnSleep = averageDayCursor.getColumnIndex("sleep");

            String AverageDiet = "ok";
            Double AverageActivity = 1.0;
            Double AverageSleep = 7.0;
            averageDayCursor.moveToFirst();
            if (averageDayCursor.moveToFirst()){
                do{
                    AverageDiet = averageDayCursor.getString(AverageDayColumnDiet);
                    AverageActivity = averageDayCursor.getDouble(AverageDayColumnActivity);
                    AverageSleep = averageDayCursor.getDouble(AverageDayColumnSleep);
                } while (averageDayCursor.moveToNext());
            }


            List<CheckIn> checkIns = new ArrayList<CheckIn>();
            checkInsCursor.moveToFirst();
            if (checkInsCursor.moveToFirst()) {
                do {
                    String Diet = checkInsCursor.getString(CheckInsColumnDiet);
                    Double Activity = checkInsCursor.getDouble(CheckInsColumnActivity);
                    String CheckInDateString = checkInsCursor.getString(CheckInsColumnCheckInDate);
                    DateTime CheckInDate = DateTime.parse(CheckInDateString);
                    CheckIn checkIn = new CheckIn(Diet,Activity,CheckInDate);
                    checkIns.add(checkIn);
                } while (checkInsCursor.moveToNext());
            }

            moodsCursor.moveToFirst();
            if (moodsCursor.moveToFirst()){
                do{
                    String Mood = moodsCursor.getString(MoodsColumnMood);
                    String HasReason = moodsCursor.getString(MoodsColumnHasReason);
                    String Reason = moodsCursor.getString(MoodsColumnReason);
                    int Severity = moodsCursor.getInt(MoodsColumnSeverity);
                    String Weather = moodsCursor.getString(MoodsColumnWeather);
                    String Time = moodsCursor.getString(MoodsColumnTime);
                    //String time = MakeTimeStampUsable(Time);
                    String dateFromTimeStamp = Time.substring(0, Math.min(Time.length(), 10));
                    DateTime date = DateTime.parse(dateFromTimeStamp);
                    DateTime dateAndTime = parseDateTime(Time);
                    Boolean isCheckedIn = false;
                    int checkInIndex = 0;
                    if (dateAndTime.isAfter(beginDate) && dateAndTime.isBefore(endDate)){
                        for (int index = 0; index < checkIns.size(); index++){
                            if (checkIns.get(index).Date == date){
                                isCheckedIn = true;
                                checkInIndex = index;
                            }
                        }
                        if (isCheckedIn){
                            String Diet = checkIns.get(checkInIndex).Diet;
                            Double HoursOfActivity = checkIns.get(checkInIndex).Activity;
                            Double HoursOfSleep = AverageSleep;
                            MoodAndActivity moodData = new MoodAndActivity(Mood, HasReason, Reason, dateAndTime, Severity, Weather, Diet, HoursOfActivity, HoursOfSleep);
                            moodDataList.add(moodData);
                        }
                        else{
                            MoodAndActivity moodData = new MoodAndActivity(Mood, HasReason, Reason, dateAndTime, Severity, Weather, AverageDiet, AverageActivity, AverageSleep);
                            moodDataList.add(moodData);
                        }
                    }
                } while (moodsCursor.moveToNext());
            }


        }
        catch(Throwable T){
            System.out.print(T);
        }
        finally {
            if (moodsDB != null) moodsDB.close();
            if (checkInsDB != null) checkInsDB.close();
            if (averageDayDB != null) averageDayDB.close();
        }
        String results = "Moods and Related info for query:";
        for (int index = 0; index < moodDataList.size(); index++){
            MoodAndActivity datum = moodDataList.get(index);
            String Mood = datum.Mood;
            String HasReason=datum.HasReason;
            String Reason=datum.Reason;
            DateTime DateAndTime= datum.DateAndTime;
            int Severity= datum.Severity;
            String Weather= datum.Weather;
            String Diet = datum.Diet;
            Double HoursOfActivity = datum.HoursOfActivity;
            Double HoursOfSleep = datum.HoursOfSleep;
            results = results + "\n\n DATETIME: "+DateAndTime+ " MOOD: " + Mood + " SEVERITY: " + Severity + "\n HASREASON: " + HasReason + " REASON: " + Reason
                    + " \n WEATHER: " + Weather + " DIET: " + Diet + " ACTIVITY: " + HoursOfActivity +" hours SLEEP: " + HoursOfSleep + "hours";
        }
        TextView moodList = new TextView(this);
        moodList.setTextSize(20);
        moodList.setText(results);
        layout.addView(moodList);


    }
}
