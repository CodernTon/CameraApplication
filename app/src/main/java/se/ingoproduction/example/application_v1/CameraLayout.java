package se.ingoproduction.example.application_v1;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraLayout extends Activity {
    Camera camera;
    FrameLayout frameLayout;
    OpenTheCamera openTheCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);
        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        openCam();
    }

    //Method that opens camera and adds it to the layout
    public void openCam(){
        camera=camera.open();
        openTheCamera = new OpenTheCamera(this, camera);
        frameLayout.addView(openTheCamera);
    }
    //Goes back to main
    public void closeTheCamera(View view) {
        Intent closeTheCamera = new Intent(this, MainActivity.class);
        startActivity(closeTheCamera);
    }

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback(){
        @Override
        public void onPictureTaken(byte[] data,Camera camera){
            File picture_file = getOutputMediaFile();    //Receive the picture taken and saves it in a File.

            if(picture_file == null){
                return;
            }
            else{
                try {
                    FileOutputStream fos = new FileOutputStream(picture_file); //If the picturefile contains
                    fos.write(data);                                           //something it starts to write fom "data"
                    fos.close();                                                //close the FileOutputStream fos
                    camera.startPreview();                                     //Start the camera again

                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    };

    public void takeThePicture(View view) {
        if (camera!=null){
            camera.takePicture(null,null, mPictureCallback);
        }
    }
    //Create a folder to save picture in unless it already exist. Also create a unique name for every pick using "SimpleDateFormat"
    //returns a File
    private File getOutputMediaFile(){
        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED)){
            return null;
        }
        else{
            File folder_gui = new File(Environment.getExternalStorageDirectory()+File.separator+"PicturesApp");

            if(!folder_gui.exists()){
                folder_gui.mkdirs();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd:HHmmss");
            String timestamp = sdf.format(new Date());

            File outPutFile = new File(folder_gui,timestamp+".jpg");
            return outPutFile;
        }
    }
}
