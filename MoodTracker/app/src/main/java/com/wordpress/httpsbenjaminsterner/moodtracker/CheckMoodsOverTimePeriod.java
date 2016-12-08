package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ScrollView;
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
        public Double Sleep;
        protected CheckIn(String diet, Double activity, DateTime date, Double sleep){
            this.Diet = diet;
            this.Activity = activity;
            this.Date = date;
            this.Sleep = sleep;
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
            int CheckInsColumnSleep = checkInsCursor.getColumnIndex("sleep");

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
                    Double Sleep = checkInsCursor.getDouble(CheckInsColumnSleep);
                    CheckIn checkIn = new CheckIn(Diet,Activity,CheckInDate,Sleep);
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
                            Double HoursOfActivity = AverageActivity;
                            Double HoursOfSleep = AverageSleep;
                            if (checkIns.get(checkInIndex).Sleep != null) HoursOfSleep = checkIns.get(checkInIndex).Sleep;
                            if (checkIns.get(checkInIndex).Activity != null) HoursOfActivity = checkIns.get(checkInIndex).Activity;
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
        String results = "Moods and Related info for query:\n\n";
        results += weighMoodList(moodDataList);
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
            results = results + "\n\n DATETIME: "+DateAndTime+ "\nMOOD: " + Mood + "\nSEVERITY: " + Severity + "\nHASREASON: " + HasReason + "\nREASON: " + Reason
                    + "\nWEATHER: " + Weather + "\nDIET: " + Diet + "\nACTIVITY: " + HoursOfActivity +"hours\nSLEEP: " + HoursOfSleep + "hours";
        }
        TextView moodList = new TextView(this);
        moodList.setTextSize(20);
        moodList.setText(results);
        TextView homeButton = (TextView) findViewById(R.id.homeButton);

        layout.removeAllViews();
        layout.addView(homeButton);
        layout.addView(moodList);
        ScrollView scroller = (ScrollView) findViewById(R.id.scroller);
        scroller.scrollTo(0, scroller.getTop());



    }
    private String weighMoodList(List<MoodAndActivity> moodList){
        List<MoodAndActivity> contentList = new ArrayList<>();
        List contentWeighedScores = new ArrayList();
        double averageContentScore = 0;
        int contentCount = 0;
        List<MoodAndActivity> happyList = new ArrayList<>();
        List happyWeighedScores = new ArrayList();
        double averageHappyScore = 0;
        int happyCount = 0;
        List<MoodAndActivity> angryList = new ArrayList<>();
        List angryWeighedScores = new ArrayList();
        double averageAngryScore = 0;
        int angryCount = 0;
        List<MoodAndActivity> fearfulList = new ArrayList<>();
        List fearfulWeighedScores = new ArrayList();
        double averageFearfulScore = 0;
        int fearfulCount = 0;
        List<MoodAndActivity> sadList = new ArrayList<>();
        List sadWeighedScores = new ArrayList();
        double averageSadScore = 0;
        int sadCount = 0;
        List<MoodAndActivity> emptyList = new ArrayList<>();
        List emptyWeighedScores = new ArrayList();
        double averageEmptyScore = 0;
        int emptyCount = 0;
        List<MoodAndActivity> undefinedList = new ArrayList<>();
        int undefinedCount = 0;

        for (int index = 0; index < moodList.size(); index++){
            MoodAndActivity datum = moodList.get(index);
            String datumMood = datum.Mood;
            switch (datumMood){
                case "Content":
                    contentList.add(datum);
                    break;
                case "Happy":
                    happyList.add(datum);
                    break;
                case "Angry/Irritated":
                    angryList.add(datum);
                    break;
                case "Fearful":
                    fearfulList.add(datum);
                    break;
                case "Sad":
                    sadList.add(datum);
                    break;
                case "Empty":
                    emptyList.add(datum);
                    break;
                default:
                    undefinedList.add(datum);
                    break;
            }
        }
        contentCount = contentList.size();
        happyCount = happyList.size();
        angryCount = angryList.size();
        fearfulCount = fearfulList.size();
        sadCount = sadList.size();
        emptyCount = emptyList.size();
        undefinedCount = undefinedList.size();
        averageContentScore = weighScorePositive(contentList);
        averageHappyScore = weighScorePositive(happyList);
        averageAngryScore = weighScoreNegative(angryList);
        averageFearfulScore = weighScoreNegative(fearfulList);
        averageEmptyScore = weighScoreNeutral(emptyList);
        averageSadScore = weighScoreNegative(sadList);
        String weighedScores = "For this dataset, the average weighed scores are as follows: \n";
        if (happyCount > 0) weighedScores = weighedScores +"\nHappy:\nScore: " + averageHappyScore + "\n Count: " + happyCount;
        if (sadCount > 0) weighedScores = weighedScores + "\nSad:\nScore: " + averageSadScore + "\nCount: " + sadCount;
        if (contentCount > 0) weighedScores = weighedScores + "\nContent:\nScore: " + averageContentScore + "\nCount: " + contentCount;
        if (angryCount > 0) weighedScores = weighedScores + "\nAngry:\nScore: " + averageAngryScore + "\nCount: " + angryCount;
        if (fearfulCount > 0) weighedScores = weighedScores + "\nFearful:\nScore: " + averageFearfulScore + "\nCount: " + fearfulCount;
        if (emptyCount > 0) weighedScores = weighedScores + "\nEmpty:\nScore: " + averageEmptyScore + "\nCount: " + emptyCount;
        if (undefinedCount > 0) weighedScores =  weighedScores + "\n" + undefinedCount + " moods were registered that did not fall under recognized moods.\n\n";
        return weighedScores;


    }
    private double weighScorePositive(List<MoodAndActivity> moodList){
        double averageWeighedScore = 0;
        double totalScore = 0;
        for (int index = 0; index < moodList.size(); index++){
            MoodAndActivity datum = moodList.get(index);
            double score = datum.Severity;
            if (datum.HasReason == "true"){
                score = score * .75;
            }
            totalScore += score;
        }
        averageWeighedScore = totalScore/moodList.size();
        return averageWeighedScore;

    }
    private double weighScoreNeutral(List<MoodAndActivity> moodList){
        double totalScore = 0;
        double averageWeighedScore = 0;
        for (int index = 0; index < moodList.size(); index++){
            MoodAndActivity datum = moodList.get(index);
            double score = datum.Severity;
            if (datum.HasReason == "false"){
                score = score * .75;
            }
            totalScore += score;
        }
        averageWeighedScore = totalScore / moodList.size();
        return averageWeighedScore;
    }

    private double weighScoreNegative(List<MoodAndActivity> moodList){
        double averageWeighedScore = 0;
        double totalScore = 0;
        for (int index = 0; index < moodList.size(); index++){
            MoodAndActivity datum = moodList.get(index);
            double score = datum.Severity;
            if (datum.HasReason == "true"){
                score = score * .75;
            }
            if (datum.HoursOfSleep < 4){
                score = score * .4;
            } else if (datum.HoursOfSleep >= 4 && datum.HoursOfSleep < 6){
                score = score *.6;
            } else if (datum.HoursOfSleep >= 6 && datum.HoursOfSleep < 8){
                score = score*.8;
            }
            switch (datum.Weather){
                case "Thunderstorm":
                    score = score*.8;
                    break;
                case "Drizzle":
                    score = score*.8;
                    break;
                case "Rain":
                    score = score * .8;
                    break;
                case "Snow":
                    score = score * .8;
                    break;
                case "Atmosphere":
                    score = score * .9;
                    break;
                case "Extreme":
                    score = score * .7;
                    break;
                case "Clouds":
                    score = score * .9;
                    break;
                default:
                    score = score * .9;
                    break;
            }
            switch (datum.Diet){
                case "ok":
                    score = score * .9;
                    break;
                case "junk":
                    score = score * .8;
                    break;
                case "malnourished":
                    score = score * .7;
                    break;
            }
            if (datum.HoursOfActivity < 1){
                score = score * .9;
            }
            totalScore += score;
        }
        averageWeighedScore = totalScore/moodList.size();
        return averageWeighedScore;

    }
    public void GoHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
