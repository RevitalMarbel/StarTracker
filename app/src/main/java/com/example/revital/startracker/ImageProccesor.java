package com.example.revital.startracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Revital on 09/10/2016.
 */


public class ImageProccesor {
    Bitmap pic;
    int width, heigth;
    Bitmap result;
    public ArrayList<Double> dists;
    ArrayList<Point3D> _stars;
    private static Context context;

    public ImageProccesor(Bitmap pic ,Context c) {
        this.pic = Bitmap.createBitmap(pic);
        this.pic = this.pic.copy(Bitmap.Config.ARGB_8888, true);
        this.width= pic.getWidth();
        this.heigth=pic.getHeight();
        context = c;
    }

    public double getIntens(int x, int y) {
        double ans = 0;
        int c=pic.getPixel(x, y);
        double red = (Color.red(c)* 0.299);
        double green = (Color.green(c)* 0.587);
        double blue = (Color.blue(c)*0.114);
        ans = red+green+blue;
        return ans;
    }
    public void setIntens( int x, int y, int rgb) {
        pic.setPixel(x, y, rgb);
    }
    public Bitmap Test2() throws FileNotFoundException {
            Log.w("Good", "begin Test2");
            //String name = "/home/boaz/Desktop/tmp_video/20160804_222016.jpg";
            // name =   "/home/boaz/Desktop/tmp_video/20160804_222214.jpg";
            //String name =   "/home/boaz/Desktop/tmp_video/20160804_222123.jpg";
            //File input = new File(name);
            //image = ImageIO.read(input);
            //image = image.getSubimage(2200, 1200, 1200, 700);
            //width = image.getWidth();
            //height = image.getHeight();
            pic=   Bitmap.createBitmap(pic,100, 100, 700, 700);

            heigth= pic.getHeight();
            width=pic.getWidth();
            int[] ggrr = new int[256];
            double t=0,TH_up=13, TH_down= 10;
            Color nc = new Color();
             _stars= new  ArrayList<Point3D>();
            Log.w("Good", "begin For");
            for(int i=0; i<heigth; i++){
                for(int j=0; j<width; j++){
                    double rgb_d = (int)getIntens(j,i);
                    int rgb = (int)rgb_d;
                    if(rgb_d>=TH_up) {
                        Log.w("Treshhold acomplished","Hey!!");
                        double[] s = computeStar(j,i,TH_down);
                        Point3D p1 = new Point3D(s[0],s[1], s[3]);
                        _stars.add(p1);
                        System.out.println((int)t+","+s[0]+","+s[1]+","+s[2]+","+s[3]);
                        t++;

                    }
                }
            }
            //calcDist(_stars);
            setInverse();
            //double d = 100;
          //  File ouptut = new File(name+"_small_"+TH_up+"_"+TH_down+".jpg");
          //  ImageIO.write(image, "jpg", ouptut);
            result=pic;
        return result;
    }


    void calcDist() throws FileNotFoundException {
        dists = new ArrayList<Double>();
        GeometricHash.createMap();
        Toast.makeText(context,String.valueOf (_stars.size()) , Toast.LENGTH_LONG).show();
        for (int a = 0; a < _stars.size() - 1; a++) {
            for (int b = a + 1; b < _stars.size(); b++) {
                for(int c=b+1;c<_stars.size();c++) {
                    double d1 = _stars.get(a).distance2D(_stars.get(b));
                    double d2 = _stars.get(c).distance2D(_stars.get(b));
                    double d3 = _stars.get(a).distance2D(_stars.get(c));
                    if(d1>20 && d2>20 && d3>20)
                    {
                    dists.add(d1);
                    String key=  GeometricHash.createKey(d1, d2, d3);
                    String value= String.valueOf(a)+"_"+String.valueOf(b)+"_"+String.valueOf(c);
          //              Toast.makeText(context, key , Toast.LENGTH_LONG).show();
                    ArrayList<String> Avalue;
                    if(GeometricHash.exist(key)) {
                        Avalue=GeometricHash.get(key);
                    }
                    else{
                        Avalue=new ArrayList<String>();}
                    Avalue.add(value);
                    GeometricHash.addToHashMap(key, Avalue);
                }
                }
            }
        }
        Toast.makeText(context, String.valueOf(GeometricHash.getSize()) , Toast.LENGTH_LONG).show();
        GeometricHash.createFile();
        Collections.sort(dists);
        for (int i = 0; i < dists.size(); i++) {
            System.out.println(dists.get(i));
          //  Toast.makeText( getActivity(), i +" :"+ dists.get(i), Toast.LENGTH_LONG).show();
        }
    }
    void setInverse()
        {
            //width = image.getWidth();
            //height = image.getHeight();
            int[] ggrr = new int[256];
            double t=0,TH_up=33, TH_down=28;
            //    Color nc = new Color(255,0,0);
            for(int i=0; i<heigth; i++){
                for(int j=0; j<width; j++){

                    double rgb_d = (int)getIntens(j,i);
                    if(rgb_d==0) {
                        //image.setRGB(j, i, 255*256);
                        pic.setPixel(j,i,255*256);
                    }
                }
            }
        }
    public double[] computeStar(int j,int i, double TH) {
        double[] ans = new double[4]; // <x,y,intes, area>
        PixelSet front = new PixelSet();
        front.add(j, i);
        ArrayList<Point3D>  star = computeStar(front, TH);
        ans = computeStar(star);
        return ans;
    }

    public double[] computeStar(ArrayList<Point3D> star) {
        double[] ans = new double[4];
        double in = 0, x=0,y=0;
        for(int i=0;i<star.size();i++) {
            Point3D c = star.get(i);
            in += c.z();
        }
        for(int i=0;i<star.size();i++) {
            Point3D c = star.get(i);
            double n  = c.z()/in;
            x+=c.x()*n;y+=c.y()*n;
        }
    ans[0] = x; ans[1]=y; ans[2] = in; ans[3] = star.size();
    return ans;
    }
    public ArrayList<Point3D> computeStar(PixelSet front, double TH) {
        ArrayList<Point3D> star = new ArrayList<Point3D>();
        while(!front.isEmpty()) {
            int[] pix = front.removeFirst();
            int x = pix[0];
            int y = pix[1];
            double in = getIntens(x,y);
            Point3D p = new Point3D(x,y,in);
            star.add(p);
            //image.setRGB(x, y, 0);
            pic.setPixel(x,y,0);
            if(x-1>=0) {
                in = getIntens(x-1,y);
                if(in>TH) {front.add(x-1, y);}
            }
            if(y-1>=0) {
                in = getIntens(x,y-1);
                if(in>TH) {front.add(x, y-1);}
            }
            if(x+1<width) {
                in = getIntens(x+1,y);
                if(in>TH) {front.add(x+1, y);}
            }
            if(y+1<heigth) {
                in = getIntens(x,y+1);
                if(in>TH) {front.add(x, y+1);}
            }
        }
        return star;
    }


}
