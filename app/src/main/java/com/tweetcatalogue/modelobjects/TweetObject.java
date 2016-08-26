package com.tweetcatalogue.modelobjects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vraman on 8/22/16.
 */
public class TweetObject implements Parcelable {

    private String userName;
    private String statusText;
    private String miniProfileImageURL;
    private String userUrl;

    public TweetObject() {

    }

    public TweetObject(String userName, String statusText, String miniProfileImageURL, String userUrl) {
        this.userName = userName;
        this.statusText = statusText;
        this.miniProfileImageURL = miniProfileImageURL;
        this.userUrl = userUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getMiniProfileImageURL() {
        return miniProfileImageURL;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(userName);
        parcel.writeString(statusText);
        parcel.writeString(miniProfileImageURL);
        parcel.writeString(userUrl);
    }

    public static final Parcelable.Creator<TweetObject> CREATOR = new Creator<TweetObject>() {
        public TweetObject createFromParcel(Parcel source) {
            TweetObject tweetObject = new TweetObject();
            tweetObject.userName = source.readString();
            tweetObject.statusText = source.readString();
            tweetObject.miniProfileImageURL = source.readString();
            tweetObject.userUrl = source.readString();
            return tweetObject;
        }

        public TweetObject[] newArray(int size) {
            return new TweetObject[size];
        }
    };
}
