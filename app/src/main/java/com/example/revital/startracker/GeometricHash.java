package com.example.revital.startracker;

import android.annotation.TargetApi;
import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Revital on 11/10/2016.
 */
public class GeometricHash {

    private static HashMap<String,  ArrayList<String>> stasMap;
    public static void createMap()
    {
        stasMap=new HashMap<String, ArrayList<String>>();
    }
    public static String createKey(Double d1, Double d2,Double d3)
    {
        ArrayList<Double> TripDists =new ArrayList<Double>();
        TripDists.add(d1);
        TripDists.add(d2);
        TripDists.add(d3);
        Collections.sort(TripDists);
        String key= String.valueOf(TripDists.get(0))+"_"+String.valueOf(TripDists.get(1))+"_"+String.valueOf(TripDists.get(2));
        return key;
    }
    public static void addToHashMap(String key,ArrayList<String> value)
    {
        stasMap.put(key,value);
    }
    public static boolean exist(String key)
    {
        if (stasMap.get(key)!=null)
            return true;
        else
            return false;
    }
    public static ArrayList get(String key)
    {
        return stasMap.get(key);
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void createFile() throws FileNotFoundException {
//        FileOutputStream fileOutputStream = new FileOutputStream(StaticElements.HashfileName);
//        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
//            objectOutputStream.writeObject(stasMap);
//            objectOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        File file = new File(StaticElements.HashfileName);
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(file));
                outputStream.writeObject(stasMap);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
    }

    public static int getSize()
    {
        return stasMap.size();
    }

}
