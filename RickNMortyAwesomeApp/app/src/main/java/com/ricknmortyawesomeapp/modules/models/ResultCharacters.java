package com.ricknmortyawesomeapp.modules.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResultCharacters implements Serializable {

    public int id;
    public String name;
    public String air_date;
    public List<String> episode;
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
