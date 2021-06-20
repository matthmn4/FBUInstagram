package com.example.fbuinstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_DATE = "createdAt";
    public static final String KEY_USERSLIKED = "usersLiked";

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser (ParseUser user) {
        put(KEY_USER, user);
    }

    public JSONArray getUsersLiked() {
        return getJSONArray(KEY_USERSLIKED);
    }

    public boolean isLikedBy(ParseUser user) {
        if(getUsersLiked() != null) {
            JSONArray usersLiked = getUsersLiked();

            for (int i = 0; i < usersLiked.length(); i++) {
                JSONObject userPointer = null;
                try {
                    userPointer = usersLiked.getJSONObject(i);
                    if(userPointer.getString("objectId").equals(user.getObjectId())) {
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public void addToLikeArray(ParseUser user) {
        add(KEY_USERSLIKED, user);
    }

    public void removeFromLikeArray(ParseUser user) throws JSONException {
        JSONArray usersLiked = getUsersLiked();

        if(usersLiked == null) {
            usersLiked = new JSONArray();
        }

        for (int i = 0; i < usersLiked.length(); i++) {
            JSONObject userPointer = usersLiked.getJSONObject(i);
            if (userPointer.getString("objectId").equals(user.getObjectId())) {
                usersLiked.remove(i);
            }
        }

        put(KEY_USERSLIKED, usersLiked);
    }

}
