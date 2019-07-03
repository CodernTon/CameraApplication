package se.ingoproduction.example.application_v1;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;


// Using camera(1) api to set up the camera
public class OpenTheCamera extends SurfaceView implements SurfaceHolder.Callback {

    Camera camera;
    SurfaceHolder holder;

    public OpenTheCamera(Context context, Camera camera) {
        super(context);
        this.camera=camera;
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Camera.Parameters params = camera.getParameters();

        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        Camera.Size mSize = null;

        for (Camera.Size size : sizes) {
            mSize = size;
        }

        //Sets upp the parameters for the camera.
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            params.set("orientation", "portrait");
            camera.setDisplayOrientation(90);
            params.setRotation(90);
        } else {
            params.set("orientation", "landscape");
            camera.setDisplayOrientation(0);
            params.setRotation(0);
        }
        try {
            params.setPictureSize(mSize.width, mSize.height);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        camera.setParameters(params);
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Method that is called immediately before the surface is destroyed
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview(); //Closes preview.
        camera.release(); //Makes the camera available for other applications
    }
}
