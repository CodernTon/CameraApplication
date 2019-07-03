package se.ingoproduction.example.application_v1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;



public class ScanThePic extends Activity {

    ImageView image;
    Bitmap bitmap;
    Button choosePicture;
    String temp;
    String[] sending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_thepic);
        choosePicture=(Button)findViewById(R.id.getPicture); //Gives choosePicture the "getPicture-button"

        //Method that runs whenever you press the "Choose picture" button.
        //Creates intent and makes you able to choose a pick from "image/*". Both pre-existing and pictures taken by camera.
        //Start an startActivityForResult and passes the arguments to it.
        choosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a picture"), 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestcode,int resultcode, Intent data){
        if (requestcode==1 && data!=null &&data.getData() !=null){

            try{
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData()); // Get the picture you choose and store in bitmap
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build(); //Gives textrecognizer correct context

                //Checks if textRecognizer has the required dependencies
                if (!textRecognizer.isOperational()){
                    Toast.makeText(getApplicationContext(),"TextRecognizer is not operational",Toast.LENGTH_SHORT).show();
                }
                else {
                    Frame f = new Frame.Builder().setBitmap(bitmap).build();  // Uses Google API to create a Frame instance
                    SparseArray<TextBlock> sArray = textRecognizer.detect(f); // Uses Google API to store text from picture in SparseArray
                    StringBuilder sb = new StringBuilder();

                    //Adding all the values from sArray to a StringBuilder
                    for (int i = 0; i<sArray.size(); i++){
                        TextBlock tb = sArray.valueAt(i);

                        sb.append(tb.getValue());
                        sb.append("\n");
                    }
                    temp=sb.toString();                                 //Transform StringBuilder to String
                    temp = temp.replaceAll("\\.", "") //remove all the "." and ","
                            .replaceAll("\\,", "");   //and puts ""(nothing).
                    sending = temp.split("\\s+");                 //splits the string into an array.

                    if(temp==""){
                        System.out.println("Testing!!!!");
                    }

                    Intent sendStringArray=new Intent(this, Search.class); //Create an intent to Search.class
                    sendStringArray.putExtra("sending",sending);                   //Adding String array to sendStringArray
                    startActivity(sendStringArray);                                      //Using StartActivity to send the String array
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    //Go back to main menu without choosing picture
    public void goBackToMain(View view) {
        Intent goBackToMainIntent = new Intent
                (this, MainActivity.class);
        startActivity(goBackToMainIntent);
    }
}
