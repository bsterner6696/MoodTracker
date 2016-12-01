package com.wordpress.httpsbenjaminsterner.moodtracker;

import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import org.joda.time.DateTime;

public class CheckMoodsOverTimePeriod extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_moods_over_time_period);
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


    }
}
