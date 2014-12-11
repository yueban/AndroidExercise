package com.bigfat.choosepictest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class MainActivity extends Activity implements View.OnClickListener {

    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;

    private Button takePhoto;
    private Button chooseFromAlbum;
    private ImageView picture;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePhoto = (Button) findViewById(R.id.take_photo);
        chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);
        picture = (ImageView) findViewById(R.id.picture);

        takePhoto.setOnClickListener(this);
        chooseFromAlbum.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
                File outputImageTake = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
                try {
                    if (outputImageTake.exists()) {
                        outputImageTake.delete();
                    }
                    outputImageTake.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImageTake);
                Intent intentTake = new Intent("android.media.action.IMAGE_CAPTURE");
                intentTake.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intentTake, TAKE_PHOTO);
                break;

            case R.id.choose_from_album:
                File outputImageChoose = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
                try {
                    if (outputImageChoose.exists()) {
                        outputImageChoose.delete();
                    }
                    outputImageChoose.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImageChoose);
                Intent intentChoose = new Intent("android.intent.action.GET_CONTENT");
                intentChoose.setType("image/*");
                intentChoose.putExtra("crop", "true");
                intentChoose.putExtra("scale", true);
                intentChoose.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intentChoose, CROP_PHOTO);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;

            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
}
