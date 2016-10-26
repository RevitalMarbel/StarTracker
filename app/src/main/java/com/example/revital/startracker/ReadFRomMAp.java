package com.example.revital.startracker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Revital on 11/10/2016.
 */

public class ReadFRomMAp {

    private HashMap<String, ArrayList<String>> stasMap=new HashMap<String, ArrayList<String>>();


    public ReadFRomMAp() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream  = new FileInputStream(StaticElements.HashfileName);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        stasMap = (HashMap) objectInputStream.readObject();
        objectInputStream.close();
    }
    public ArrayList<String> getValue(String key)
    {
        ArrayList<String> Hashans=stasMap.get(key);
        ArrayList ans= new ArrayList<String>();
        for(int i=0;i<Hashans.size();i++)
        {
            String[] tempS =  Hashans.get(i).split("_");
            for(int j=0;j<tempS.length;j++)
            {
                ans.add(tempS[j]);
            }
        }
        return ans;
    }
    public ArrayList getValue(Double d1, Double d2, Double d3)
    {
        String key= GeometricHash.createKey(d1,d2,d3);
        return getValue(key);
    }
    public boolean isEmpty()
    {
        return stasMap.isEmpty();
    }

}
