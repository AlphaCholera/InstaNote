package com.example.asus.instanote;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileHandling {
    private static DatabaseManagement databaseManagement;

    public static String getFileName(File filesDir, String filename) {
        File file = new File(filesDir, filename);
        int count = 1;
        while (file.exists()) {
            if (count > 1)
                filename = filename.substring(0, filename.length()-1);
            filename = filename + String.valueOf(count);
            file = new File(filesDir, filename);
            count++;
        }
        return filename;
    }

    public static boolean createFile(File filesDir, String filename, String res, int color, Context context) {
        databaseManagement = new DatabaseManagement(context);
        filename = getFileName(filesDir, filename);
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(res.getBytes());
            fos.close();
            return databaseManagement.addEntry(filename, color);
        }
        catch (Exception e){
                return false;
            }
        }

    static public boolean deleteFile(String filename, Context context) {
        databaseManagement = new DatabaseManagement(context);
        return context.deleteFile(filename) && databaseManagement.deleteEntry(filename);
    }

    static public String contentsOfFile(String filename, Context context) {
        try {
            FileInputStream file_to_be_opened = new FileInputStream(context.getFileStreamPath(filename));
            int x;
            String s = "";
            while ((x = file_to_be_opened.read()) != -1)
                // s=s+(char)x;
                s = String.format("%s%s", s, (char) x);
            return s;
        } catch (Exception e) {
            return null;
        }
    }

    static public boolean renameFile(File filesDir, String oldName, String newName, String res, int color, Context context) {
        return deleteFile(oldName, context) && createFile(filesDir, newName, res, color, context);
    }

    static public void shareFile(View v, Context context, String body) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, body);
        sendIntent.setType("text/plain");
        if (v==null)
            context.startActivity(sendIntent);
        else
            v.getContext().startActivity(sendIntent);

    }
}
