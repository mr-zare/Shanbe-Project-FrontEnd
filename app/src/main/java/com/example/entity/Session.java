package com.example.entity;

public class Session {
    private String year;
    private String month;
    private String day;
    private String hour;
    private String min;
    private int limit;
    private int id;
    private String event_id;
    private String time;
    private String session_token;
    private int filled;

    public Session(String year, String month, String day, String hour, String min, String limit,String session_token) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.limit = Integer.parseInt(limit);
        this.session_token = session_token;
    }



    public Session(String year, String month, String day, String hour, String min, String limit) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.limit = Integer.parseInt(limit);
    }
    public Session(int id,String session_token ,String event_id,String time,int limit,int filled){
        this.id=id;
        this.event_id=event_id;
        this.time = time;
        this.limit = limit;
        this.session_token = session_token;
        this.filled = filled;
    }

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getTime() {
        return time;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public int getLimit() {
        return limit;
    }

}
