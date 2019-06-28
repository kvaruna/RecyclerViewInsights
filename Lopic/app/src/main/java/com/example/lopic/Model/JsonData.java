package com.example.lopic.Model;

        import java.util.List;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class JsonData {

    @SerializedName("Data")
    @Expose
    private List<InputModel> data = null;

    public List<InputModel> getData() {
        return data;
    }

    public void setData(List<InputModel> data) {
        this.data = data;
    }




}