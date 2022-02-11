package com.example.asus.instanote;

public class EntryItem {
    private String date;
    private String fileName;
    private int color;

    public EntryItem(String date, String fileName, int color) {
        this.date = date;
        this.fileName = fileName;
        this.color = color;
    }

    public String getDate() {
        return date;
    }

    public String getFileName() {
        return fileName;
    }

    public int getColor() {
        return color;
    }
}
