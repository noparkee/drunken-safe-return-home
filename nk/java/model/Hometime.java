package com.orangeline.foregroundstudy;

public class Hometime {
    public String hour;
    public String minute;

    public Hometime(){}

    public Hometime(String hour, String minute){
        this.hour = hour;
        this.minute = minute;
    }

    public String getHour(){
        return hour;
    }

    public String getMinute(){
        return minute;
    }

    public void setHour(String hour){
        this.hour = hour;
    }

    public void setMinute(String minute){
        this.minute = minute;
    }

}
