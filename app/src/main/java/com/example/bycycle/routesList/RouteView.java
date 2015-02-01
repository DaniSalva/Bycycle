package com.example.bycycle.routesList;


/*
    Elements structure of the list
 */
public class RouteView {

    public String name;
    public String zone;
    public String lengthtime;
    public String date;
    public int reward;

    public RouteView(String title, String zone,String lengthtime,String date,int reward){
        this.name = title;
        this.zone = zone;
        this.lengthtime = lengthtime;
        this.date = date;
        this.reward = reward;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getLengthtime() {
        return lengthtime;
    }

    public void setLengthtime(String lengthtime) {
        this.lengthtime = lengthtime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }


}