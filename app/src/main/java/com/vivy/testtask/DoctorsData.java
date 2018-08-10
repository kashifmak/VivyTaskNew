
package com.vivy.testtask;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorsData {

    @SerializedName("doctors")
    @Expose
    private List<Doctor> doctors = new ArrayList<Doctor>();
    @SerializedName("lastKey")
    @Expose
    private String lastKey;

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public String getLastKey() {
        return lastKey;
    }

    public void setLastKey(String lastKey) {
        this.lastKey = lastKey;
    }

}
