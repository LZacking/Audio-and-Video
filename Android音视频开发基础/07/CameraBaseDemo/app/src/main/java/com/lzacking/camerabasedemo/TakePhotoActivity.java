package com.lzacking.camerabasedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePhotoActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO_BY_INTENT = 0;
    public static final int TAKE_PHOTO_TO_CACHE = 1;
    public static final int TAKE_PHOTO_TO_CUSTOMPATH = 2;
    public static final int TAKE_PHOTO_TO_ALBUM = 3;
    public static final int TAKE_PHOTO_TO_SCALE = 4;
    public static final int CHOOSE_PHOTO = 5;

    private ImageView mPicture;
    private ImageView mPictureScale;
    private Uri mImageUri;
    private String mCurrentPhotoPath;

    private Button mTakePhotoByIntent;
    private Button mTakePhotoToCache;
    private Button mTakePhotoToCustomPath;
    private Button mTakePhotoToAlbum;
    private Button mTakePhotoToScale;
    private Button mChooseFromAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        // 请求权限
        requestPermissions();
        // 使用默认Intent拍照
        mTakePhotoByIntent = findViewById(R.id.take_photo_by_intent);
        // 拍照之后，将图片保存在关联缓存目录
        mTakePhotoToCache = findViewById(R.id.take_photo_to_cache);
        // 拍照之后，将图片保存在自定义路径
        mTakePhotoToCustomPath = findViewById(R.id.take_photo_to_custompath);
        // 拍照之后，图片保存至自定义路径并且添加到相册
        mTakePhotoToAlbum = findViewById(R.id.take_photo_to_album);
        // 拍照之后，图片进行缩放处理
        mTakePhotoToScale = findViewById(R.id.take_photo_to_scale);
        // 从相册中选择图片，并且显示
        mChooseFromAlbum = findViewById(R.id.choose_from_album);

        mPicture = findViewById(R.id.picture);
        mPictureScale = findViewById(R.id.picture_scale);

        mTakePhotoByIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                // 确保有一个相机活动来处理意图
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, TAKE_PHOTO_BY_INTENT);
                }
            }
        });

        mTakePhotoToCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建File对象，用于存储拍照后的图片
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (outputImage != null) {
                    if (Build.VERSION.SDK_INT < 24) {
                        mImageUri = Uri.fromFile(outputImage);
                    } else {
                        mImageUri = FileProvider.getUriForFile(TakePhotoActivity.this,
                                "com.lzacking.camerabasedemo.fileprovider",
                                outputImage);
                    }
                    // 启动相机程序
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    // 确保有一个相机活动来处理意图
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);// 将拍取的照片保存到指定URI
                        startActivityForResult(intent, TAKE_PHOTO_TO_CACHE);
                    }
                }
            }
        });

        mTakePhotoToCustomPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outputImage = null;
                try {
                    outputImage = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (outputImage != null) {
                    if (Build.VERSION.SDK_INT < 24) {
                        mImageUri = Uri.fromFile(outputImage);
                    } else {
                        mImageUri = FileProvider.getUriForFile(TakePhotoActivity.this,
                                "com.lzacking.camerabasedemo.fileprovider",
                                outputImage);
                    }
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 确保有一个相机活动来处理意图
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                        startActivityForResult(intent, TAKE_PHOTO_TO_CUSTOMPATH);
                    }
                }
            }
        });

        mTakePhotoToAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outputImage = null;
                try {
                    outputImage = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (outputImage != null) {
                    if (Build.VERSION.SDK_INT < 24) {
                        mImageUri = Uri.fromFile(outputImage);
                    } else {
                        mImageUri = FileProvider.getUriForFile(TakePhotoActivity.this,
                                "com.lzacking.camerabasedemo.fileprovider",
                                outputImage);
                    }
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                        startActivityForResult(intent, TAKE_PHOTO_TO_ALBUM);
                    }
                }
            }
        });

        mTakePhotoToScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outputImage = null;
                try {
                    outputImage = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (outputImage != null) {
                    if (Build.VERSION.SDK_INT < 24) {
                        mImageUri = Uri.fromFile(outputImage);
                    } else {
                        mImageUri = FileProvider.getUriForFile(TakePhotoActivity.this,
                                "com.lzacking.camerabasedemo.fileprovider",
                                outputImage);
                    }
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                        startActivityForResult(intent, TAKE_PHOTO_TO_SCALE);
                    }
                }
            }
        });

        mChooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlbum();
            }
        });
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStorageDirectory();// 获取SDCard目录
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO_BY_INTENT:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    mPicture.setImageBitmap(imageBitmap);
                }
                break;

            case TAKE_PHOTO_TO_CACHE:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                        mPicture.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case TAKE_PHOTO_TO_CUSTOMPATH:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        // 这里的uri是根据不同的方式获得的，所以才裁剪的时候，无需再进行Android7.0判断.
                        mPicture.setImageURI(mImageUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case TAKE_PHOTO_TO_ALBUM:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        mPicture.setImageURI(mImageUri);
                        // 将照片存入相册，相册可以识别该程序拍的照片
                        addPictureToAlbum();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case TAKE_PHOTO_TO_SCALE:
                if (resultCode == RESULT_OK) {
                    scalePicture();
                }
                break;

            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void addPictureToAlbum() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mCurrentPhotoPath);
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "Add to Album success", Toast.LENGTH_SHORT).show();
    }

    private void scalePicture() {
        // 获取视图控件的尺寸
        int targetW = mPictureScale.getWidth();
        int targetH = mPictureScale.getHeight();
        // 获取图片的尺寸
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // 确定缩小图像的程度
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // 将图片进行缩放然后填充到视图控件中
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mPictureScale.setImageBitmap(bitmap);
    }


    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mPicture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }
}
