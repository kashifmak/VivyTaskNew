package com.vivy.testtask;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class listAdapter extends RecyclerView.Adapter<listAdapter.listViewHolder> {

    private Context context;
    private Doctor[] data;

    // constructor
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

        // doctor's data
        Doctor doctor = data[position];

        // using string builder as it is mutable
        StringBuilder infoOfDoctor = new StringBuilder();

        // to get doctor's name
        String docName = MainActivity.doctorName +" "+ doctor.getName() + "\n";

        // to get doctor's address
        String docAddress = MainActivity.doctorAddress+" " + doctor.getAddress() + "\n";

        // to get doctor's phone no
        String docPhone = MainActivity.doctorPhone+" "  + doctor.getPhoneNumber() + "\n";

        // to get doctor's location picture
        String docPhotoPlace = doctor.getPhotoId() + "\n";

        // To display ß in text, similarly for other special characters
        docAddress = docAddress.replaceAll("Ã\u009F", String.valueOf(Html.fromHtml ( "&#223" )));

        // in case if phone no. is not available
        if (docPhone == null)
            docPhone = "Not found";

        // appending the data to display on the screen
        infoOfDoctor.append(docName);
        infoOfDoctor.append(docAddress);
        infoOfDoctor.append(docPhone);


        if (docPhotoPlace != null){

            // url to download image of the loaction
            String urlForImage = "https://api.staging.uvita.eu/api/users/me/files/"+docPhotoPlace;
            ImageRequest imageRequest = new ImageRequest(urlForImage,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            holder.locationImage.setImageBitmap(response);
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // if image is not available then show default image
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


       // To update UI with the information of the doctor
        holder.infoText.setText(infoOfDoctor.toString());
    }

    // to get the length of the data i.e no.of doctors found
    @Override
    public int getItemCount() {
        return data.length;
    }

    public class listViewHolder extends RecyclerView.ViewHolder{
        // reference to ui elements
        ImageView locationImage;
        TextView infoText;

        public listViewHolder(View itemView) {
            super(itemView);

            locationImage = itemView.findViewById(R.id.image);
            infoText = itemView.findViewById(R.id.textView);


        }
    }
}
