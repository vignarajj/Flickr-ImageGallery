package com.flickr.imgallery;


public class FlickrModel {

    int _id;
    String mTitle;
    String mDateTaken;
    String mDatePublished;
    String mDescription;
    String mAuthor;
    String mAuthor_id;
    String mTags;
    String mLink;
    String mPictureUrl;

    public FlickrModel(int _id, String mTitle, String mDateTaken, String mDatePublished, String mDescription, String mAuthor, String mAuthor_id, String mTags, String mLink, String mPictureUrl) {
        this._id = _id;
        this.mTitle = mTitle;
        this.mDateTaken = mDateTaken;
        this.mDatePublished = mDatePublished;
        this.mDescription = mDescription;
        this.mAuthor = mAuthor;
        this.mAuthor_id = mAuthor_id;
        this.mTags = mTags;
        this.mLink = mLink;
        this.mPictureUrl = mPictureUrl;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDateTaken() {
        return mDateTaken;
    }

    public void setmDateTaken(String mDateTaken) {
        this.mDateTaken = mDateTaken;
    }

    public String getmDatePublished() {
        return mDatePublished;
    }

    public void setmDatePublished(String mDatePublished) {
        this.mDatePublished = mDatePublished;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmAuthor_id() {
        return mAuthor_id;
    }

    public void setmAuthor_id(String mAuthor_id) {
        this.mAuthor_id = mAuthor_id;
    }

    public String getmTags() {
        return mTags;
    }

    public void setmTags(String mTags) {
        this.mTags = mTags;
    }

    public String getmPictureUrl() {
        return mPictureUrl;
    }

    public void setmPictureUrl(String mPictureUrl) {
        this.mPictureUrl = mPictureUrl;
    }

    public String getmLink() {
        return mLink;
    }

    public void setmLink(String mLink) {
        this.mLink = mLink;
    }
}
