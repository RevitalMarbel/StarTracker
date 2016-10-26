package com.example.revital.startracker;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {
    Button btnTakePhoto;
    ImageView imTakenPhoto;
    private static final int CAM_REQUEST=1313;
    int picw, pich;
    //ImageView imPixel;
    private Canvas rez_canvas;
    Bitmap tumbnail;
    private static int RESULT_LOAD_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTakePhoto=(Button)findViewById(R.id.Button1);
        imTakenPhoto=(ImageView)findViewById(R.id.ImageView1);
//        imPixel=(ImageView)findViewById(R.id.ImageView2);
        btnTakePhoto.setOnClickListener(new btnTakePhotoClicker());
//////////////////select picture from gallery button////////////////////////
        Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
////////////if picture taken/////////////////////////////////////
        if (requestCode == CAM_REQUEST) {
            tumbnail = (Bitmap) data.getExtras().get("data");
           // ImageProccesor IP = new ImageProccesor(tumbnail, );
            //Image tumbnail=(Image) data.getExtras().get("data");
            //System.out.println(tumbnail.getPixel(5,5));
            //System.o ut.println(tumbnail.getPixel(5,10));
            ImageProccesor ip = new ImageProccesor(tumbnail, this.getApplication());
            Bitmap Pic_result = null;
            try {
                Pic_result = ip.Test2();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // imageView.setImageBitmap(IP.pic);
            imTakenPhoto.setImageBitmap(Pic_result);

//            imTakenPhoto.setImageBitmap(IP.pic);
        }
////////////////////if selected from galery///////////////////////////////
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            //   ImageView imageView = (ImageView) findViewById(R.id.ImageView1);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            // ImageProccesor IP = new ImageProccesor(BitmapFactory.decodeFile(picturePath));
            Bitmap tempBitmap = BitmapFactory.decodeFile(picturePath);
            ImageProccesor ip = new ImageProccesor(tempBitmap,this.getBaseContext());
            Bitmap Pic_result = null;
            try {
                Pic_result = ip.Test2();
                imTakenPhoto.setImageBitmap(Pic_result);
                ip.calcDist();
                Toast.makeText(this, " :"+"stars: "+GeometricHash.getSize() , Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Log.w("Good", "begin image proccesing");
            // imageView.setImageBitmap(IP.pic);
            Boolean isEmpty=true;
            try {
                ReadFRomMAp Stars =new ReadFRomMAp();
               isEmpty=Stars.isEmpty();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Toast.makeText(this,"is map:"+ isEmpty , Toast.LENGTH_LONG).show();
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class btnTakePhotoClicker implements View.OnClickListener {
        @Override
        public void onClick(View v){
            Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent ,CAM_REQUEST);
        }
    }
}
