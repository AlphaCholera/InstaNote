package com.example.asus.instanote;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileHandling {

    public static boolean createFile(String filename, String res, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(res.getBytes());
            fos.close();
            return true;
        }
        catch (Exception e){
                return false;
            }
        }

    static public boolean deleteFile(String filename, Context context) {

        return context.deleteFile(filename);
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

    static public boolean renameFile(String oldname, String newname, Context context) {
        String res = contentsOfFile(oldname, context);
        return deleteFile(oldname, context) && createFile(newname, res, context);
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
