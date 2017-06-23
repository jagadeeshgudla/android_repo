package com.example.exoplayer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    LinearLayout linearLayout;

    public void dumpVideos()
    {
        Object localObject = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        localObject = getApplicationContext().getContentResolver().query((Uri)localObject, new String[] { "_id", "_data", "title", "mime_type" }, null, null, null);
        if (localObject != null)
        {


            ((Cursor)localObject).moveToFirst();
            do
            {
                //((Cursor)localObject).getCount();
                Log.d("VIDEO", ((Cursor)localObject).getString(0));
                int j = ((Cursor)localObject).getColumnIndexOrThrow("_data");
                String str1 = ((Cursor)localObject).getString(((Cursor)localObject).getColumnIndexOrThrow("title"));
                int k = ((Cursor)localObject).getInt(((Cursor)localObject).getColumnIndex("_id"));
                String str2 = ((Cursor)localObject).getString(((Cursor)localObject).getColumnIndexOrThrow("mime_type"));
                Log.d("VIDEO", "Video is :" + str1);
                createView(str1, k, ((Cursor)localObject).getString(j), str2);
            }
            while (((Cursor)localObject).moveToNext());
            ((Cursor) localObject).close();
        }

    }

    public void createView(String paramString1, int videoId, final String videoPath, final String paramString3)
    {
        ImageView iv = new ImageView(this);
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
        int h = 200; // height in pixels
        int w = 200; // width in pixels
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, h, w, true);
        iv.setImageBitmap(scaled);
        iv.setId(videoId);
        iv.setClickable(true);
        //iv.setBackgroundColor(Color.BLUE);
        iv.setDrawingCacheBackgroundColor(View.FOCUS_FORWARD);
        iv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this.getBaseContext(), PlayerActivity.class);
                intent.setDataAndType(Uri.parse(videoPath), paramString3);
                 startActivity(intent);
            }
        });
        linearLayout.addView(iv);
    }

    @Override
    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);
        linearLayout = ((LinearLayout) findViewById(R.id.mainLL));
          if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            Toast.makeText(this, "Permission checking", Toast.LENGTH_SHORT).show();
            checkPermission();
        }

        dumpVideos();

    }



    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);

        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)     {
                    //Peform your task here if any
                } else {

                    checkPermission();
                }
                return;
            }
        }
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }


}