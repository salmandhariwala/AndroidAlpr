package com.salmandhariwala.helloworld;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    private Button btnCapturePicture,clearParkingLot;
    private TextView parkingLotStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
        parkingLotStatus = (TextView) findViewById(R.id.parkingLotStatus);
        clearParkingLot = (Button) findViewById(R.id.clearParkingLot);

        /**
         * Capture image button click event
         */
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });

        clearParkingLot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParkingLotManager.clearParkingLot(getApplicationContext());
                updateParkingLotStatus();
            }
        });

        updateParkingLotStatus();

    }

    private void updateParkingLotStatus(){

        Map<String, String> _parkingLotStatus = ParkingLotManager.getParkingLotStatuString(getApplicationContext());


        StringBuilder sb =new StringBuilder();

        for(String vehicle:_parkingLotStatus.keySet()){
            sb.append(rightpad(vehicle,15));
            sb.append(" --- ");
            sb.append(_parkingLotStatus.get(vehicle));
            sb.append("\n");
        }

        parkingLotStatus.setText(sb.toString());

    }

    private String rightpad(String text, int length) {
        return String.format("%-" + length + "." + length + "s", text);
    }


    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                launchUploadActivity();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }

    private void launchUploadActivity() {
        Intent i = new Intent(MainActivity.this, UploadActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        startActivity(i);
    }


    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private static File getOutputMediaFile(int type) {


        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/OpenALPR/");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateParkingLotStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateParkingLotStatus();
    }


}

