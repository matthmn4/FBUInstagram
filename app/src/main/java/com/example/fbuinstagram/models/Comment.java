package com.example.fbuinstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

//@Parcel//(analyze = Comment.class)
@ParseClassName("Comment")
public class Comment extends ParseObject {


    public static final String KEY_COMMENT = "comment";
    public static final String KEY_USER = "user";
    public static final String KEY_POST = "post";

    public String getDescription(){
        return getString(KEY_COMMENT);
    }

    public void setDescription(String comment) {
        put(KEY_COMMENT, comment);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser (ParseUser user) {
        put(KEY_USER, user);
    }

    public static String getPost() { return KEY_POST; }

    public void setPost(Post post) {  put(KEY_POST, post);  }
}
