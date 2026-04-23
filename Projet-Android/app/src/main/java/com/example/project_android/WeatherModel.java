package com.example.project_android;

public class WeatherModel {
    private String ville;
    private String description;
    private double temperature;
    private double tempMin;
    private double tempMax;
    private int humidity;
    private double windSpeed;
    private String icon;

    public WeatherModel(String ville, String description, double temperature,
                        double tempMin, double tempMax, int humidity,
                        double windSpeed, String icon) {
        this.ville       = ville;
        this.description = description;
        this.temperature = temperature;
        this.tempMin     = tempMin;
        this.tempMax     = tempMax;
        this.humidity    = humidity;
        this.windSpeed   = windSpeed;
        this.icon        = icon;
    }

    public String getVille()       { return ville; }
    public String getDescription() { return description; }
    public double getTemperature() { return temperature; }
    public double getTempMin()     { return tempMin; }
    public double getTempMax()     { return tempMax; }
    public int    getHumidity()    { return humidity; }
    public double getWindSpeed()   { return windSpeed; }
    public String getIcon()        { return icon; }
}