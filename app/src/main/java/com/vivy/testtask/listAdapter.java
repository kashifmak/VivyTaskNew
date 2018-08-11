package com.vivy.testtask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom list adapter to display images in the list
 * Used recycler view which use the previous views of the list
 * instead of creating new ones when we scroll to the bottom of the list
 */
public class listAdapter extends RecyclerView.Adapter<listAdapter.listViewHolder> {

    private Context context;
    private Doctor[] data;

    /**
     * Parameterized constructor to pass parameters
     * @param context
     * @param data
     */
    public listAdapter (Context context , Doctor[] data){
        this.context = context;
        this.data = data;
    }


    @NonNull
    @Override
    public listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list, parent,false);

        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final listViewHolder holder, int position) {

        String urlForImage = "";
        // Doctor's data
        final Doctor doctor = data[position];

        // Used string builder as it is mutable
        StringBuilder infoOfDoctor = new StringBuilder();

        // Get doctor's name
        String docName = "\n" + MainActivity.doctorName +" "+ doctor.getName() + "\n\n";

        // Get doctor's address
        String docAddress = MainActivity.doctorAddress+" " + doctor.getAddress() + "\n\n";

        // Get doctor's phone number
        String docPhone = MainActivity.doctorPhone+" "  + doctor.getPhoneNumber() ;

        // Get doctor's location picture
        String docPhotoPlace = doctor.getPhotoId() + "\n";

        // To display ß in text, similarly for other special characters
        docAddress = docAddress.replaceAll("Ã\u009F", String.valueOf(Html.fromHtml ( "&#223" )));

        // In case if phone no. is not available
        if (docPhone == null)
            docPhone = "Not found";

        // Append the data to display on the screen
        infoOfDoctor.append(docName);
        infoOfDoctor.append(docAddress);
        infoOfDoctor.append(docPhone);


        if (docPhotoPlace != null){

            /**
             * URL to download image of the location
             * Creates image request to download the images
             */
            urlForImage = "https://api.staging.uvita.eu/api/users/me/files/"+docPhotoPlace;
            ImageRequest imageRequest = new ImageRequest(urlForImage,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            holder.locationImage.setImageBitmap(response);
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // If image is not available then show default image
                    holder.locationImage.setImageResource(R.drawable.icon);
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

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(imageRequest);

        }


       // Update UI with the information of the doctor
        holder.infoText.setText(infoOfDoctor.toString());

        // Show more details of the doctor
        final String finalUrlForImage = urlForImage;
        holder.detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Starting new activity to show more details of the doctor
                Intent intent = new Intent(context, DoctorDetails.class);

                // Start new activity
                intent.putExtra("email", doctor.getEmail()); // Get doctor's email
                intent.putExtra("openingHours", String.valueOf(doctor.getOpeningHours())); // Get doctor's opening hours
                intent.putExtra("rating",String.valueOf(doctor.getRating())); // Get doctor's Rating
                intent.putExtra("website",doctor.getWebsite()); // Get doctor's website
                intent.putExtra("urlForImage", finalUrlForImage);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    // Length of the data i.e no.of doctors found
    @Override
    public int getItemCount() {
        return data.length;
    }

    /**
     * View holder which includes the UI elements i.e image view to display image
     * and text view to display the text information
     */
    public class listViewHolder extends RecyclerView.ViewHolder{
        // Reference to UI elements
        ImageView locationImage;
        TextView infoText;
        Button detailButton;

        public listViewHolder(View itemView) {
            super(itemView);

            locationImage = itemView.findViewById(R.id.image);
            infoText = itemView.findViewById(R.id.textView);
            detailButton = itemView.findViewById(R.id.detailButton);


        }
    }
}
