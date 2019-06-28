package com.example.lopic.Model;

import android.graphics.Bitmap;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataMap {

    @SerializedName("options")
    @Expose
    private List<String> options = null;
    private Bitmap bitmap;
    private String comment;
    private String optionSelection;

    public String getOptionSelection() {
        return optionSelection;
    }

    public void setOptionSelection(String optionSelection) {
        this.optionSelection = optionSelection;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }



    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

}