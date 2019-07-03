package se.ingoproduction.example.application_v1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Search extends Activity{
    private static final String END_POINT = "http://10.0.2.2:8080/search-chemicals?";
    private static final String SUBSTANCE = "substance=";
    private static final String LOG_TAG = "Failed";
    private ListView lv;
    private String [] recieveStringArray;
    private List <Chemical> chemicalList = new ArrayList<>();
    private List <Message> messageList= new ArrayList<>();
    private int counter = 0;

    StringBuilder chemicalNames = new StringBuilder();
    List<Object> jsonObjectList = new ArrayList<>();
    List<Object> tempObjectList;
    ArrayList<String> presentableList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        chemicalNames.append(END_POINT);

        Intent intent = getIntent();                                         //Get Intent from ScanThePic class with
        recieveStringArray = intent.getStringArrayExtra("sending");   //array including all words in the picture.
        //System.out.println("test"+recieveStringArray[0]+recieveStringArray.length);

        if (recieveStringArray!=null && !(recieveStringArray[0].isEmpty())){  //Check to see if there is anything in array
            onAutoStart();
            showUrl();
        }
    }
    //Method that adds the words found in picture to the URL through the Search method.
    private void onAutoStart(){
        for (int i = 0; i<recieveStringArray.length; i++){
            Search(recieveStringArray[i]);
            chemicalNames.append("&");
        }
        chemicalNames.setLength(chemicalNames.length()-1);
        counter++;
    }
    //Creates the searchterms for the URL
    public void Search(String chemical){
        chemicalNames.append(SUBSTANCE);
        chemicalNames.append(chemical);
    }

    public void startSearch(View view) throws IOException {
        //Reminder!!: Add a toast with info if you couldn't connect
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                chemicalNames.toString(),
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject object) {
                        try {
                            jsonObjectList = jsonToList(object); //Does the search
                            setContentView(R.layout.showsearch_layout); //Open up a new layout
                            showChemicals(); //Shows the results on that new layout
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, " cause: " + error.getCause().getMessage());
                Toast toast = Toast.makeText(getApplicationContext(),"No database connection",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        queue.add(jsonObjectRequest);
    }
    //Adds the json to a list, returns a List<Object>
    private List<Object> jsonToList(JSONObject object) throws JSONException{
        tempObjectList=new ArrayList<>();
        //System.out.println(Debug: printing chemicalobject: " + object);
        JSONArray chemicals = object.getJSONArray("chemicals found:"); // Search in the array to find array with name "chemical found" and
        //System.out.println("Debug: printing chemicals" + chemicals);        //place in JSONArray chemicals.
        for (int i = 0; i < chemicals.length(); i++) {                       //As long as there is a object in the array the loop keeps adding
            try {
                JSONObject row = chemicals.getJSONObject(i);
                String substance = row.getString("substance");
                String casnr = row.getString("cas_nr");
                String egnr = row.getString("eg_nr");
                String priolvl = row.getString("priority_level");
                String criteria = row.getString("criteria");

                Chemical c = new Chemical(substance, casnr, egnr, priolvl, criteria);
                chemicalList.add(c);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONArray message = object.getJSONArray("messages:");         //Search in the array to find array with name "messages" and
                                                                            //place in JSONArray message.
        for (int k = 0; k < message.length(); k++) {                        //As long as there is a object in the array the loop keeps adding
            try {
                JSONObject row = message.getJSONObject(k);
                String name = row.getString("name");
                String info = row.getString("info");

                Message m = new Message(name, info);
                messageList.add(m);
            } catch(JSONException e){
                e.printStackTrace();
            }
        }
        //These methods adds a headline to the lists and makes sure the right kind of list is saved to the jsonObjectList
        //based on the json. The if else statemens checks if chemicalList and messageList are empty and gives the different outcomes depanding on that.
        if (chemicalList.isEmpty()&&messageList.isEmpty()){
            tempObjectList.add("You forgot to add any chemicals or the picture didn't include any words!");
        }
        else if (chemicalList.isEmpty()&& !messageList.isEmpty()){
            tempObjectList.add("None of the chemicals you searched for were in the database:");
            tempObjectList.add("\n"+messageList);
        }
        else if (!chemicalList.isEmpty() && messageList.isEmpty()){
            tempObjectList.add("List of the chemicals existing in our database that you searched for:");
            tempObjectList.add("\n"+chemicalList);
        }
        else{
            tempObjectList.add("Chemicals in the database:");
            tempObjectList.add("\n"+chemicalList);
            tempObjectList.add("\n"+"Chemicals which didn't exist in the database:");
            tempObjectList.add("\n"+messageList);
        }
        return tempObjectList;
    }

    //go back to main
    public void goBack(View view) {
        Intent goBacktoStart = new Intent
                (this, MainActivity.class);
        startActivity(goBacktoStart);
    }
    //Add chemicals to the URL
    public void addTheChem(View view) {
        EditText chemName = (EditText)findViewById(R.id.enterChemName);
        String chemicalName = String.valueOf(chemName.getText());
        if (chemicalName.isEmpty()){
            Toast toast = Toast.makeText(getApplicationContext(),"No chemical added",Toast.LENGTH_SHORT);//Shows what you (not)added in a toast.
            toast.show();

        }
        else{
            if(counter>0) {
                chemicalNames.append("&");//Adds "&" if counter>0. Does this because it shouldn't add it the before the first "searchterm"
            }
            Search(chemicalName);
            counter ++;
            Toast toast = Toast.makeText(getApplicationContext(),("Chemical "+chemicalName+" added"),Toast.LENGTH_SHORT); //Shows what you added in a toast.
            toast.show();
            chemName.getText().clear();
            showUrl();
        }
    }
    //Shows the URL you generated
    private void showUrl() {
        TextView textView = (TextView)findViewById(R.id.yourUrl);
        textView.setText(chemicalNames);
    }
    //Presenting the results in a Listview.
    private void showChemicals(){
        String jsonObjectString = Arrays.toString(jsonObjectList.toArray()) //makes the list to a String
                .replace(",","")                            // Removes all ,[]
                .replace("[","")
                .replace("]","");

        lv = (ListView) findViewById(R.id.showChemical);
        presentableList.add(jsonObjectString);          //Add the String to a List
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this,android.R.layout.simple_list_item_1,presentableList);
        lv.setAdapter(adapter);
    }
    //Method that resets what user searched for. Calls when user presses "GO BACK" button
    //Instead of clearing like this we could declare them in the methods they are used instead of the beginning of the class
    //to make sure they are empty whenever we want to add something
    public void goBacktoSearch(View view) {
        lv.setAdapter(null);
        presentableList.clear();
        jsonObjectList.clear();
        chemicalList.clear();
        messageList.clear();
        counter=0;
        setContentView(R.layout.search_layout);
        chemicalNames.setLength(0);
        chemicalNames.append(END_POINT);
    }
}
