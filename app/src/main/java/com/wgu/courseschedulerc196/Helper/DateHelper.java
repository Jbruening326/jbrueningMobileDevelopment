package com.wgu.courseschedulerc196.Helper;

import android.app.DatePickerDialog;
import android.widget.Button;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper extends AppCompatActivity {

    private static DatePickerDialog datePickerDialog;



    public static String getTodayDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        month = month + 1;
        return makeDateString(day, month, year);

    }

    public static DatePickerDialog initDatePicker(Button button) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker datePicker, int year, int month , int day) {
                month = month +1;
                String date = makeDateString(day, month, year);
                button.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return datePickerDialog = new DatePickerDialog(button.getContext(), dateSetListener, year, month, day);
    }




    public static String makeDateString(int day, int month, int year) {

        return getMonthFormat(month) + " " + day + " " + year;
    }

    public static Date makeStringDate(String dateString){
        String myFormat = "MMM dd yyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date date = null;
        try{
            date =sdf.parse(dateString);
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return date;

    }

    public static String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        else if(month == 2)
            return "FEB";
        else if(month == 2)
            return "FEB";
        else if(month == 3)
            return "MAR";
        else if(month == 4)
            return "APR";
        else if(month == 5)
            return "MAY";
        else if(month == 6)
            return "JUN";
        else if(month == 7)
            return "JUL";
        else if(month == 8)
            return "AUG";
        else if(month == 9)
            return "SEP";
        else if(month == 10)
            return "OCT";
        else if(month == 11)
            return "NOV";
        else if(month == 12)
            return "DEC";
        else {
            return "JAN";
        }

    }

}
