package se.ingoproduction.example.application_v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Method which opens a class that can access camera
    // Creates a intent which opens CameraLayout.class
    public void openTheCamera(View view) {
        Intent openTheCameraIntent = new Intent
                (this, CameraLayout.class);
        startActivity(openTheCameraIntent);
    }
    // Method which open a class that analyzes pictures
    // Creates a intent which opens ScanThePic.class
    public void getDataFromPic(View view) {
        Intent getInfoFromPicIntent= new Intent
                (this, ScanThePic.class);
        startActivity(getInfoFromPicIntent);
    }
    // Method which open up the sear
    // Creates a intent which opens Search.class
    public void startTheSearch(View view) {
        Intent startTheSearchintent = new Intent
                (this, Search.class);
        startActivity(startTheSearchintent);
    }
}
