package com.flickr.imgallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;


public class ViewImageActivity extends Activity {
    public int feedPosition = 0;
    ImageView btn_share, img_feed;
    String title, dateTaken, datePublished, description, author, author_id, tags, link, pictureUrl;
    TextView txt_title, txt_author, txt_date, txt_publishDate, txt_headtags, txt_tags, txt_headdata, txt_imgdata;
    Button btn_more;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_showimage);
        feedPosition = getIntent().getExtras().getInt("position", -1);
        Log.i("pos", String.valueOf(feedPosition));
        initUI();
        try {
            setUpFeed();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    /**
     * Initialize the User Interface
     **/
    public void initUI() {
        btn_share = (ImageView) findViewById(R.id.btn_share);
        img_feed = (ImageView) findViewById(R.id.feed_image);
        txt_title = (TextView) findViewById(R.id.feed_title);
        txt_author = (TextView) findViewById(R.id.feed_author);
        txt_date = (TextView) findViewById(R.id.feed_date);
        txt_publishDate = (TextView) findViewById(R.id.feed_publishdate);
        txt_headtags = (TextView) findViewById(R.id.head_tag);
        txt_tags = (TextView) findViewById(R.id.feed_tag);
        txt_headdata = (TextView) findViewById(R.id.head_imageInfo);
        txt_imgdata = (TextView) findViewById(R.id.image_data);
        btn_more = (Button) findViewById(R.id.feed_description);
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedPosition != -1) {
                    if (NetworkUtil.getConnectivityStatus(ViewImageActivity.this)) {
                        Intent webIntent = new Intent(Intent.ACTION_VIEW);
                        webIntent.setData(Uri.parse(link));
                        startActivity(webIntent);
                    } else {
                        Toast.makeText(ViewImageActivity.this, "Internet not available, try again later", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedPosition != -1) {
                    shareImage();
                }
            }
        });
    }
    /**
     * Load the feed into views
     **/
    public void setUpFeed() throws ParseException {
        if (feedPosition != -1) {
            title = Utils.mFeeds.get(feedPosition).getmTitle();
            dateTaken = Utils.mFeeds.get(feedPosition).getmDateTaken();
            datePublished = Utils.mFeeds.get(feedPosition).getmDatePublished();
            description = Utils.mFeeds.get(feedPosition).getmDescription();
            author = Utils.mFeeds.get(feedPosition).getmAuthor();
            author_id = Utils.mFeeds.get(feedPosition).getmAuthor_id();
            tags = Utils.mFeeds.get(feedPosition).getmTags();
            link = Utils.mFeeds.get(feedPosition).getmLink();
            pictureUrl = Utils.mFeeds.get(feedPosition).getmPictureUrl();
            Picasso.with(this).load(pictureUrl).placeholder(R.drawable.loading_image).noFade().into(img_feed);
            txt_title.setText(title);
            author = author.replace("nobody@flickr.com", "").replace("(", "").replace(")", "").replace("\"", "");
            txt_author.setText(author);
            txt_date.setText(Utils.formattedDated(Utils.toCalendar(dateTaken)));
            txt_publishDate.setText(Utils.formattedDated(Utils.toCalendar(datePublished)));
            metaTagInfo();
            if (!tags.equals("")) {
                tags = tags.replace(" ", ", ");
                txt_tags.setText(tags);
            } else {
                txt_tags.setVisibility(View.GONE);
                txt_headtags.setVisibility(View.GONE);
            }
        } else {
            txt_imgdata.setVisibility(View.GONE);
            txt_headdata.setVisibility(View.GONE);
        }
    }
    /**
     * Share the Flickr Image
     **/
    public void shareImage() {
        Picasso.with(getApplicationContext()).load(pictureUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                startActivity(Intent.createChooser(i, "Share Picture"));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }
    /**
     * Store the image into local
     **/
    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_img_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
    /**
     * Get the meta data info of image
     **/
    public void metaTagInfo() {
        StringBuilder builder = new StringBuilder();
        try {
            InputStream is = new URL(pictureUrl).openStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            Metadata metadata = ImageMetadataReader.readMetadata(bis);
            for (Directory directory : metadata.getDirectories()) {
                for (com.drew.metadata.Tag tag : directory.getTags()) {
                    builder.append(tag.getTagName() + " : " + tag.getTagType());
                    builder.append("\n");
                }
            }
        } catch (ImageProcessingException e) {
        } catch (IOException e) {
        }
        txt_imgdata.setText(builder.toString());
    }
}
