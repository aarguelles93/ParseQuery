package com.uninorte.andresarguelles.parsequery;

import android.os.Bundle;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Andres Arguelles on 01/05/2015.
 */
@ParseClassName("Posts")
public class LocationPost extends ParseObject{
    public void setLocation(ParseGeoPoint value){
        put("location", value);
    }
    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public static ParseQuery<LocationPost> getQuery() {
        return ParseQuery.getQuery(LocationPost.class);
    }


}
