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

    // define static variables
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // variables used to show the required information un the screen
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

        // to hide keyboard on starting the activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // to hide keyboard after pressing search button
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                // checking internet connectivity
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //Connected to Wifi network
                    Downloader download = new Downloader();
                    String doctorName = searchField.getText().toString();

                    // to replace empty spaces from the text
                    doctorName = doctorName.replaceAll(" ", "%20");

                    // to get the information of the doctor
                    download.downloadDoctorsData(context, doctorName);

                }
                else {
                    // to display toast message device is not connected to the internet
                    Toast.makeText(context, getString(R.string.MainActivityToastNoInternet), Toast.LENGTH_SHORT).show();
                }

            }
        });

        // speech to text input
        mic.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // to display the spoken sentence in the edit text field on the screen
        if (requestCode == 10 && data != null){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            searchField.setText(result.get(0));
        }
    }

}
