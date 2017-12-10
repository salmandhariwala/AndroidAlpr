package com.salmandhariwala.helloworld;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class UploadActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private String filePath = null;
    private ImageView imgPreview;
    private Button btnUpload;
    long totalSize = 0;
    private TextView resultTextView;
    private EditText vechNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        resultTextView = (TextView) findViewById(R.id.chargesText);
        vechNum = (EditText) findViewById(R.id.numberPlate);

        btnUpload.setVisibility(View.INVISIBLE);


        // Receiving the data from previous activity
        Intent i = getIntent();

        // image or video path that is captured in previous activity
        filePath = i.getStringExtra("filePath");

        if (filePath != null) {
            // Displaying the image or video on the screen
            previewMedia();
            new ExtractNumberPlate().execute(filePath);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }

        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String _vehicleNumber = vechNum.getText().toString();
                processVehicleNumber(_vehicleNumber);

            }
        });



    }

    private void processVehicleNumber(String vehicleNumber) {

        String tag = "processVehicleNumber";

        Log.e(tag, "Vehicle Number identified as  " + vehicleNumber);

        Context context = getApplicationContext();

        if (vehicleNumber == null) {
            resultTextView.setText("Unable to process this Vehicle");
        } else {
            boolean isAlreadyInParking = ParkingLotManager.isVehiclePresentInParking(context, vehicleNumber);

            Log.e(tag, "isAlreadyInParking  " + isAlreadyInParking);

            if (!isAlreadyInParking) {
                ParkingLotManager.processEntry(context, vehicleNumber);
                resultTextView.setText("Added this Vehicle in Parking Lot : " + vehicleNumber);
            } else {
                Double charges = ParkingLotManager.processExit(context, vehicleNumber);

                StringBuilder sb = new StringBuilder();
                sb.append("This Vehicle was already present in parking Lot");
                sb.append(" : ");
                sb.append(vehicleNumber);
                sb.append("\n");
                sb.append("Please collect cash of :");
                sb.append(charges);

                resultTextView.setText(sb.toString());

            }
        }

    }

    public class ExtractNumberPlate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            File f = new File(strings[0]);


            String vehicleNumber = null;

            try {
                vehicleNumber = AlprWrapper.recognize(f);


            } catch (Exception e) {
                Log.e("alpr fetch error", "error while fetching result from alpr ", e);
            }

            return vehicleNumber;
        }

        @Override
        protected void onPostExecute(String vehicleNumber) {


            if (vehicleNumber != null) {
                vechNum.setText(vehicleNumber);
            } else {
                vechNum.setText("Unable to recognize");
            }

            btnUpload.setVisibility(View.VISIBLE);

        }
    }

    private void previewMedia() {
        imgPreview.setVisibility(View.VISIBLE);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        imgPreview.setImageBitmap(bitmap);
    }


}
