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

public class Downloader extends Activity {
    public static String accessTokenImage;
    ProgressDialog dialog;

    public void downloadDoctorsData (Context context, String doctorName){

        getAccessToken(context, doctorName);

    }

    // to get access token for authorization

    private void getAccessToken(final Context context, final String doctorName) {
        dialog = new ProgressDialog(context);
        dialog.setMessage(MainActivity.DownloaderProgressDialogMessage);
        dialog.show();

        // url to get access token
        String url = "https://auth.staging.vivy.com/oauth/token";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JsonObject jobj = new Gson().fromJson(response, JsonObject.class);

                String accessToken = jobj.get("token_type").getAsString() + " " + jobj.get("access_token").getAsString();

                // start the request to download doctor's data after getting the access token
                getDoctorsData(context, doctorName, accessToken);

                // storing the access token to download the image of the location
                accessTokenImage = accessToken;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // toast message to say download failed
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

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

    // to download doctor's data
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

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();

                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                DoctorsData doctorsData =  gson.fromJson(response, DoctorsData.class );
                Doctor[] doctors = doctorsData.getDoctors().toArray(new Doctor[0]);

                if(doctors.length >0)
                    MainActivity.doctorsList.setAdapter(new listAdapter(context,doctors));
                else {
                    MainActivity.doctorsList.setAdapter(new listAdapter(context,doctors));

                    // toast message to say that "no results found"
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
