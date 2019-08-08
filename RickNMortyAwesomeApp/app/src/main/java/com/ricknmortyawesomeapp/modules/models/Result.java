package com.ricknmortyawesomeapp.modules.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Result implements Serializable {

    public int id;
    public String name;
    public String air_date;
    public String episode;
    public List<String> characters;
    public String url;
    public String created;
    public String status;
    public String species;
    public String type;
    public String gender;
    public Origin origin;
    public Origin location;
    public String image;



}
