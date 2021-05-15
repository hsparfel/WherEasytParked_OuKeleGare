package com.pouillcorp.oukelegare.utils;

import com.pouillcorp.oukelegare.App;
import com.pouillcorp.oukelegare.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {

    public static String ecrireDateHeureLettre(Date date) {
        Map<Integer, String> mapJours = new HashMap<>();
        mapJours.put(1, App.getRes().getString(R.string.sunday));
        mapJours.put(2,App.getRes().getString(R.string.monday));
        mapJours.put(3,App.getRes().getString(R.string.tuesday));
        mapJours.put(4,App.getRes().getString(R.string.wednesday));
        mapJours.put(5,App.getRes().getString(R.string.thursday));
        mapJours.put(6,App.getRes().getString(R.string.friday));
        mapJours.put(7,App.getRes().getString(R.string.saturday));

        Map<Integer, String> mapMois = new HashMap<>();
        mapMois.put(0,App.getRes().getString(R.string.january));
        mapMois.put(1,App.getRes().getString(R.string.february));
        mapMois.put(2,App.getRes().getString(R.string.march));
        mapMois.put(3,App.getRes().getString(R.string.april));
        mapMois.put(4,App.getRes().getString(R.string.may));
        mapMois.put(5,App.getRes().getString(R.string.june));
        mapMois.put(6,App.getRes().getString(R.string.july));
        mapMois.put(7,App.getRes().getString(R.string.august));
        mapMois.put(8,App.getRes().getString(R.string.september));
        mapMois.put(9,App.getRes().getString(R.string.october));
        mapMois.put(10,App.getRes().getString(R.string.november));
        mapMois.put(11,App.getRes().getString(R.string.december));

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);


        String dateMaj = "";
        dateMaj += mapJours.get(dayOfWeek)+" ";
        dateMaj += day+" ";
        dateMaj += mapMois.get(month)+" ";

        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        dateMaj += simpleDateFormat.format(date);

        return dateMaj;
    }

    public static String ecrireDateHeure(Date date) {
        String pattern = "dd/MM/yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String dateMaj = simpleDateFormat.format(date);
        return dateMaj;
    }
}
