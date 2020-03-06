package com.example.atpeople.myapplication.ui.calendar.model;


/**
 * Create by peng on 2020/3/3
 */
public class AxonEventModel {
   private int axon_id;
   private String axon_title;

    public AxonEventModel(int axon_id, String axon_title) {
        this.axon_id = axon_id;
        this.axon_title = axon_title;
    }

    public int getAxon_id() {
        return axon_id;
    }

    public void setAxon_id(int axon_id) {
        this.axon_id = axon_id;
    }

    public String getAxon_title() {
        return axon_title;
    }

    public void setAxon_title(String axon_title) {
        this.axon_title = axon_title;
    }
}
