package com.vivy.testtask;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Display detail information of the doctor
 */

public class DoctorDetails extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        final ImageView detailLocationImage = findViewById(R.id.detailImage);
        TextView detailDoctorInformation = findViewById(R.id.detailTextview);
        TextView detailDoctorWebsite = findViewById(R.id.detailWebsite);

        // Doctor's email
        String email = getIntent().getExtras().getString("email");

        // Doctor's working hours
        String openingHours = getIntent().getExtras().getString("openingHours");

        // Doctor's rating
        String rating = getIntent().getExtras().getString("rating") ;
        float ratingFloat;

        // Doctor's website
        String website = getIntent().getExtras().getString("website");

        String imageUrl = getIntent().getExtras().getString("urlForImage");

        if (email== null)
            email = getString(R.string.notFound);

        if (openingHours == null || openingHours.equals("[]") )
            openingHours = getString(R.string.notFound);

        if (rating == null)
            rating = getString(R.string.notFound);
        else{
            // Conversion of rating upto 2 decimal points
            ratingFloat = Float.valueOf(rating);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            decimalFormat.setMaximumFractionDigits(2);
            rating = decimalFormat.format(ratingFloat);
        }

        if (website == null)
            website = getString(R.string.notFound);

        if (imageUrl == null)
            imageUrl = getString(R.string.notFound);


        StringBuilder detailInformation = new StringBuilder();
        detailInformation.append("\nEmail: " + email + "\n\n");
        detailInformation.append("Opening hours: " + openingHours + "\n\n");
        detailInformation.append("Rating:  " + rating );

        detailDoctorInformation.setText(detailInformation);

        // Open the website of the doctor if available
        detailDoctorWebsite.setText("Website: " + website);

        if (imageUrl != null){

            /**
             * URL to download image of the location
             * Creates image request to download the images
             */
            ImageRequest imageRequest = new ImageRequest(imageUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            detailLocationImage.setImageBitmap(response);
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // If image is not available then show default image
                    detailLocationImage.setImageResource(R.drawable.icon);
                }
            })
            {

                /** Passing some request headers* */
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Accept", "image/jpeg");
                    headers.put("Authorization", Downloader.accessTokenImage);
                    return headers;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(imageRequest);

        }

    }
}
