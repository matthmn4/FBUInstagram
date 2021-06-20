package com.example.fbuinstagram;

import android.app.Application;

import com.example.fbuinstagram.models.Comment;
import com.example.fbuinstagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("NWTFnV3Kd1YccojQAs3mNPTmE33MYsTCxl5JJJNL")
                .clientKey("zGJYaIHdoMBhvKKa7qhvw1qNRCapX2vPK8uoIIwt")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
