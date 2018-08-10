package com.vivy.testtask;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    Context context = this;

    // Define static variables
    public static RecyclerView doctorsList;
    public static String doctorName;
    public static String doctorAddress;
    public static String doctorPhone;

    public static String DownloaderProgressDialogMessage;
    public static String DownloaderToast;
    public static String TryAgainToast;

    // UI elements
    EditText searchField;
    Button searchButton;
    ImageView mic;

    /**
     * OnCreate calls when activity starts
     *
     * Get the name of the doctor to search by pressing the search button {@link #searchButton}
     * with the help of API to find the doctor near you.
     *
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Variables used to show the required information on the screen
        doctorName =  getString(R.string.DownloaderName) ;
        doctorAddress = getString(R.string.DownloaderAddress);
        doctorPhone = getString(R.string.DownloaderPhone);

        DownloaderProgressDialogMessage = getString(R.string.DownloaderProgressDialogMessage);
        DownloaderToast = getString(R.string.DownloaderToast);
        TryAgainToast = getString(R.string.tryAgainToast);

        // UI componenets
        searchField = findViewById(R.id.searchField);
        searchButton = findViewById(R.id.searchButton);
        doctorsList = findViewById(R.id.docList);
        mic = findViewById(R.id.mic);

        // Hide keyboard on starting the activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        /**
         * Binding onclick listener with the search button
         */
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hide keyboard after pressing search button
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                // Checking internet connectivity
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //'if' condition becomes true if connected to Wifi network

                    // Creating the object of Downloader class
                    Downloader download = new Downloader();

                    // Fetching the name of the doctor form search field
                    String doctorName = searchField.getText().toString();

                    // Replace empty spaces from the text
                    doctorName = doctorName.replaceAll(" ", "%20");

                    /**
                     * Calls the downloadDoctorsData method of Downloader class to search for doctors near by
                     *@param context
                     *@param doctorName
                     */
                    download.downloadDoctorsData(context, doctorName);

                }
                else {
                    // Display toast message device is not connected to the internet
                    Toast.makeText(context, getString(R.string.MainActivityToastNoInternet), Toast.LENGTH_SHORT).show();
                }

            }
        });

        /**
         * Binding onclick listener with the Mic button to enable the user to give speech input
         */
        mic.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                // Starting the speech recognizer to get the voice input
                Intent input = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                input.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                input.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                input.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

                try {
                    startActivityForResult(input, 10 );
                }catch (Exception e){}
            }
        });


    }

    /**
     * Runs after finish the activity of listening the speech input
     * @param requestCode request code send from the intent
     * @param resultCode status of the activity
     * @param data contains the data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Display the spoken sentence/word in the edit text field on the screen
        if (requestCode == 10 && data != null){

            // Get the data
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            searchField.setText(result.get(0));
        }
    }

}
