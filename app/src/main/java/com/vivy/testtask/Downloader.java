package com.vivy.testtask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to fetch the data about the doctors after getting the access token
 * {@Link #getAccessToken} Get the access token to access information using API
 */
public class Downloader extends Activity {
    public static String accessTokenImage;
    ProgressDialog dialog;

    public void downloadDoctorsData (Context context, String doctorName){

        /**
         * Calls the function to get access token
         * @param context
         * @param dostorName
         */
        getAccessToken(context, doctorName);

    }

    /**
     * Get the access token to access the API
     * @param context
     * @param doctorName
     */
    private void getAccessToken(final Context context, final String doctorName) {

        // Progress dialog box
        dialog = new ProgressDialog(context);
        dialog.setMessage(MainActivity.DownloaderProgressDialogMessage);
        dialog.show();

        // URL to get access token
        String url = "https://auth.staging.vivy.com/oauth/token";

        /**
         * Creates POST request
         * Headers : Access, Authorization and Content type
         * Data: username and password
         * parameters: grand type
         *
         */
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Creates JSON object to fetch the access token from the JSON response
                JsonObject jobj = new Gson().fromJson(response, JsonObject.class);

                // Stores access token in variable
                String accessToken = jobj.get("token_type").getAsString() + " " + jobj.get("access_token").getAsString();

                // Start the request to download doctor's data after getting the access token
                getDoctorsData(context, doctorName, accessToken);

                // Storing the access token to download the image of the location
                accessTokenImage = accessToken;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Message to inform the user about download failure
                Toast.makeText(context, MainActivity.DownloaderToast, Toast.LENGTH_SHORT).show();
            }
        }){
            // Passing parameters
            protected Map<String,String> getParams(){
                Map<String,String> MyData = new HashMap<String,String>();
                MyData.put("username","androidChallenge@vivy.com");
                MyData.put("password","88888888");
                MyData.put("grant_type","password");

                return  MyData;
            }

            // Passing some request headers
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Basic " +
                        "aXBob25lOmlwaG9uZXdpbGxub3RiZXRoZXJlYW55bW9yZQ==");
                return headers;
            }

        };

        // Add te request to the request queue
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    /**
     * Get the data of the doctor from the API
     * Data will be displayed based on some parameters like latitude, longitude
     * location and sorted by the distance
     * @param context
     * @param doctorName
     * @param accessToken
     */
    private void getDoctorsData(final Context context, String doctorName, final String accessToken) {
        String lat = "52.534709";
        String lng = "13.3976972";
        String latDelta = "0.0122858883093357";
        String lngDelta = "0.0151786495888473";
        String location = "Neukölln, Berlin";
        String sort = "distance";

        // url to access doctor's data
        String url = "https://api.staging.uvita.eu/api/users/me/doctors?search="+doctorName+"&lat="+lat+"&lng="+lng+"&latDelta="+latDelta+"&lngDelta="+lngDelta+"&location=Neuk%C3%B6lln%2C%20Berlin&sort="+sort;

        MainActivity.doctorsList.setLayoutManager(new LinearLayoutManager(context));

        /**
         * Creates GET request
         * Headers: Authorization and Access
         */
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                DoctorsData doctorsData =  gson.fromJson(response, DoctorsData.class );
                Doctor[] doctors = doctorsData.getDoctors().toArray(new Doctor[0]);

                if(doctors.length >0)
                    // Populate the list with the retrieved information of the doctors
                    MainActivity.doctorsList.setAdapter(new listAdapter(context,doctors));
                else {
                    MainActivity.doctorsList.setAdapter(new listAdapter(context,doctors));

                    //Message to inform the user that no result is found for the provided information
                    Toast.makeText(context, MainActivity.TryAgainToast, Toast.LENGTH_SHORT).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                // toast message to say download failed
                Toast.makeText(context, MainActivity.DownloaderToast, Toast.LENGTH_SHORT).show();
            }
        }){

            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Accept", "application/json");
                headers.put("Authorization", accessToken);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }
}
