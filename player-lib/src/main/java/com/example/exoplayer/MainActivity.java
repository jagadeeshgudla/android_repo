package com.example.exoplayer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
                Log.d("VIDEO", ((Cursor)localObject).getString(0));
                int j = ((Cursor)localObject).getColumnIndexOrThrow("_data");
                String str1 = ((Cursor)localObject).getString(((Cursor)localObject).getColumnIndexOrThrow("title"));
                int k = ((Cursor)localObject).getInt(((Cursor)localObject).getColumnIndex("_id"));
                String str2 = ((Cursor)localObject).getString(((Cursor)localObject).getColumnIndexOrThrow("mime_type"));
                Log.d("VIDEO", "Video is :" + str1);
                createView(str1, k, ((Cursor)localObject).getString(j), str2);
            }
            while (((Cursor)localObject).moveToNext());
            ((Cursor)localObject).close();
            ((Cursor) localObject).close();
        }

    }

    public void createView(String paramString1, int videoId, final String videoPath, final String paramString3)
    {
        ImageView iv = new ImageView(this);
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
        iv.setImageBitmap(bitmap);
        iv.setId(videoId);
        iv.setScaleType(ImageView.ScaleType.FIT_START);
        iv.setClickable(true);
        //iv.setFocusableInTouchMode(true);
        //iv.setDrawingCacheBackgroundColor(View.FOCUS_FORWARD);
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
        linearLayout = ((LinearLayout)findViewById(R.id.mainLL));
        if ((ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0) || (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE")));
        dumpVideos();

    }
}