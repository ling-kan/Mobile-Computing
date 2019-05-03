package com.example.lingkan.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;


public class cameraActivity extends AppCompatActivity {

    //https://stackoverflow.com/questions/43615697/capture-image-and-save-it-on-the-internal-storage
    //https://stackoverflow.com/questions/18024935/how-to-have-the-camera-intent-put-a-photo-into-internal-storage
    Button takeButton, shareButton, openButton;
    ImageView imageView;
    public static final int CAPTUREIMAGE = 1;
    // Uri for image path
    private static Uri photoUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        takeButton = (Button) this.findViewById(R.id.takeButton);
        shareButton = (Button) this.findViewById(R.id.shareButton);
        openButton = (Button) this.findViewById(R.id.openButton);
        imageView = (ImageView) this.findViewById(R.id.imageView);

        takeImageFromCamera();

    }

    //http://www.viralandroid.com/2015/12/how-to-capture-image-from-android-camera-and-display-it-programmatically.html
    //https://developer.android.com/training/camera/photobasics.html#TaskScalePhoto
    //https://stuff.mit.edu/afs/sipb/project/android/docs/training/camera/photobasics.html
    public void takeImageFromCamera() {
        ActivityCompat.requestPermissions(cameraActivity.this,
                new String[]{Manifest.permission.CAMERA}, 1);
    }

    //Handles camera request permission result
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Once permission is granted a message will appear
                    Toasty.success(cameraActivity.this, "Permission granted for camera!", Toast.LENGTH_SHORT).show();

                } else {
                    // permission denied,
                    AlertDialog.Builder alert = new AlertDialog.Builder(cameraActivity.this);
                    alert.setTitle("Error");
                    alert.setMessage("You must enable camera permissions, for this tool to work. Would you like to try again?");
                   //Have the option yes and no
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Once 'Yes' is clicked the dialog box is closed, and reopen camera permissions request
                            dialog.cancel();
                            takeImageFromCamera();
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Once 'No' is clicked, the current activity will close
                            cameraActivity.this.finish();
                        }
                    });
                    alert.show();
                }
            }
            return;
        }
    }

    //Take a picture , then display in ImageView and save to Gallery
    public void takePictureClick(View view) {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent, CAPTUREIMAGE);
        }
    }

    //Display image on ImageView and save to gallery
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTUREIMAGE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //Place the image in ImageView
            imageView.setImageBitmap(photo);
            //Upload  a message which shows the photo is added in the gallery
            Toasty.success(cameraActivity.this, "Your photo is added to Gallery!", Toast.LENGTH_SHORT).show();
        }
    }

    //https://developer.android.com/reference/android/provider/MediaStore.Images.Media.html
    //Open gallery applciation
    public void openClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivity(intent);
    }

    // /http://www.androhub.com/android-share-text-and-images-using-share-intent/
    public void sharePictureClick(View view) {
       //Share image
        pictureShare(photoUri);
    }
    private void pictureShare(Uri imagePath) {
        //Creating the share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //the file type to share must be an image
        shareIntent.setType("image/*");
        //The image is passed into the intent
        shareIntent.putExtra(Intent.EXTRA_STREAM, imagePath);
        //View the different options to share
        startActivity(Intent.createChooser(shareIntent, "Share Image Using"));
    }
}