package com.lzacking.imageviewdrawpictures;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

// ImageView加载几种来源：
// (1)drawable/mipmap中通过R.drawabe.xxx加载图片资源
// (2)assests
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView customImageView = findViewById(R.id.iv_picture);
        // 方式一：drawable/mipmap中通过R.drawabe.xxx加载图片资源
        // customImageView.setBackgroundResource(R.mipmap.wuqian);

        // 方式二：加载assests路径的资源
        customImageView.setImageBitmap(getImageFromAssetsFile(this, "lilangdi.jpg"));
    }

    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}
