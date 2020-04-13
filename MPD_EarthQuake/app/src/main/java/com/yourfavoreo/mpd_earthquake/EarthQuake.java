// Student ID: S1803434
// Student Name: Abdulrahman Salum Diwani

package com.yourfavoreo.mpd_earthquake;

import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class EarthQuake implements java.io.Serializable {


    private String location;
    private int depth;
    private Calendar originDate;
    private double magnitude;
    private double latitude;
    private double longitude;

    public String getLocation() {
        return location;
    }

    public int getDepth() {
        return depth;
    }

    public Calendar getOriginDate() {
        return originDate;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public EarthQuake(String[] earthQuake)  {
        for (int i = 0;i<earthQuake.length;i++){
            earthQuake[i] = earthQuake[i].split(": ")[1];
        }

        earthQuake[0]=earthQuake[0].split(", ")[1].split("\\s[0-9]+:[0-9]+:[0-9]+\\s$")[0];
        earthQuake[0] = convertStringDateToInt(earthQuake[0]);

        this.originDate = Calendar.getInstance();
        this.originDate.set(Integer.parseInt(earthQuake[0].substring(6,10)),Integer.parseInt(earthQuake[0].substring(3,5))-1,Integer.parseInt(earthQuake[0].substring(0,2)));
        this.location = earthQuake[1].substring(0, 1).toUpperCase() + earthQuake[1].substring(1).toLowerCase();
        this.latitude= Double.parseDouble(earthQuake[2].split(",")[0]);
        this.longitude= Double.parseDouble(earthQuake[2].split(",")[1]);
        this.depth = Integer.parseInt(earthQuake[3].split(" ")[0]);
        this.magnitude = Double.parseDouble(earthQuake[4]);
    }

    @Override
    public String toString() {
        return "Date: "+this.originDate.toString()+" ; Location: "+this.location+" ; Lat/long: "+
                this.latitude+"/"+this.longitude+" ; Depth: "+this.depth+"; Magnitude : "+this.magnitude;
    }

    private String convertStringDateToInt(String date){
        String[] monthStrings = new String[]{"Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
        String replacement = "";
        for (int i = 0;i<monthStrings.length;i++){
            if (i<9){
                replacement = "0"+(i+1);
            }
            else {
                replacement = Integer.toString(i+1);
            }
            date = date.replace(monthStrings[i],replacement);
        }
        return date;
    }
}
