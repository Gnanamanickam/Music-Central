package com.gnani.musiccentral;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class MusicModel implements Parcelable {

    private int id;
    private String title;
    private String artist;
    private String url;
    private Bitmap image;


    protected MusicModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        artist = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
        url = in.readString();
    }

    public static final Creator<MusicModel> CREATOR = new Creator<MusicModel>() {
        @Override
        public MusicModel createFromParcel(Parcel in) {
            return new MusicModel(in);
        }

        @Override
        public MusicModel[] newArray(int size) {
            return new MusicModel[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeParcelable(image, flags);
        dest.writeString(url);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "MusicCentralModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", image=" + image +
                ", url='" + url + '\'' +
                '}';
    }

    public MusicModel(int id, String title, String artist, String url, Bitmap image) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.image = image;
        this.url = url;
    }
}
