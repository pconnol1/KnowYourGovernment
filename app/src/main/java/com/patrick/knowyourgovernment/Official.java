package com.patrick.knowyourgovernment;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Official implements Serializable, Comparable<Official>{
    private String name;
    private String title;
    private String address;
    private String party;
    private String phone;
    private String url;
    private String email;
    private String photoURL;
    private String googlePlus;
    private String facebook;
    private String twitter;
    private String youtube;

    public Official(){

    }
    public Official(String name, String title, String address, String party, String phone, String url, String email, String photoURL, String googlePlus,String facebook, String twitter, String youtube){
        this.name = name;
        this.title = title;
        this.address = address;
        this.party = party;
        this.phone = phone;
        this.url = url;
        this.email = email;
        this.photoURL = photoURL;
        this.googlePlus = googlePlus;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube = youtube;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getGooglePlus() {
        return googlePlus;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getUrl() {
        return url;
    }

    public String getYoutube() {
        return youtube;
    }

    public String getTitle() { return title; }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setGooglePlus(String googlePlus) {
        this.googlePlus = googlePlus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Official)) return false;

        Official official = (Official) o;

        return getName() != null ? getName().equals(official.getName()) : official.getName() == null;
    }

    @Override
    public int hashCode() { return getName() != null ? getName().hashCode() : 0; }

    @Override
    public int compareTo(@NonNull Official o) { return getName().compareTo(o.getName());}
}
