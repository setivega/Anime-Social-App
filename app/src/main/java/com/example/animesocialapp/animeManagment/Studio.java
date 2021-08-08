package com.example.animesocialapp.animeManagment;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

@Nullable
@ParseClassName("Studio")
public class Studio extends ParseObject {

    public String studioID;
    public String name;

    public static final String KEY_STUDIO_ID = "studioID";
    public static final String KEY_NAME = "name";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_USER = "user";


    public Studio() {}

    public Studio(JSONObject jsonObject) throws JSONException {
        studioID = String.valueOf(jsonObject.getInt("mal_id"));
        name = jsonObject.getString("name");
    }

    public static List<Studio> fromJSONArray(JSONArray studioJsonArray) throws JSONException {
        List<Studio> studios = new ArrayList<>();
        for (int i = 0; i < studioJsonArray.length(); i++) {
            studios.add(new Studio(studioJsonArray.getJSONObject(i)));
        }
        return studios;
    }

    public String getStudioID() { return getString(KEY_STUDIO_ID); }

    public void setStudioID(String studioID) { put(KEY_STUDIO_ID, studioID); }

    public String getName() { return getString(KEY_NAME); }

    public void setName(String name) { put(KEY_NAME, name); }

    public Integer getWeight() { return getInt(KEY_WEIGHT); }

    public void setWeight(int weight) { put(KEY_WEIGHT, weight); }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

}