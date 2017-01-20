package com.flickr.imgallery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText edit_tags;
    Button btn_search;
    Spinner spin_sort;
    GridView grid_feeds;
    ProgressDialog pDialog;
    String url = "https://api.flickr.com/services/feeds/photos_public.gne";
    boolean feedByTag = false;
    String tag = "";
    String finalurl = "";
    GridAdapter adapter;
    String[] sortStr = {"Sort by Relavance", "Sort by Date"};
    boolean selectSort = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        if (NetworkUtil.getConnectivityStatus(MainActivity.this)) {
            clearFeeds();
            loadPublicFeed();
            eventListeners();
        } else {
            Toast.makeText(MainActivity.this, "Internet not available, try again later", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Initialize the User Interface
     **/
    public void initUI() {
        edit_tags = (EditText) findViewById(R.id.edit_tag);
        btn_search = (Button) findViewById(R.id.btn_search);
        spin_sort = (Spinner) findViewById(R.id.spin_sort);
        grid_feeds = (GridView) findViewById(R.id.grid_gallery);
        edit_tags.setBackgroundResource(R.drawable.edit_selector);
        edit_tags.setSingleLine(true);
        edit_tags.setImeOptions(EditorInfo.IME_ACTION_DONE);
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sortStr);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_sort.setAdapter(spinAdapter);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading feeds..");
    }
    /**
     * Event listeners for button and grid view
     **/
    public void eventListeners() {
        edit_tags.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btn_search.performClick();
                }
                return false;
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tag = edit_tags.getText().toString();
                if (!tag.equals("") || !tag.equals(null) || tag.length() >= 2) {
                    if (NetworkUtil.getConnectivityStatus(MainActivity.this)) {
                        feedByTag = true;
                        clearFeeds();
                        loadPublicFeed();
                    } else {
                        Toast.makeText(MainActivity.this, "Internet not available, try again later", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter the valid tag.", Toast.LENGTH_LONG).show();
                }
            }
        });
        grid_feeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ViewImageActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        spin_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sortByRelevance();
                } else if (position == 1) {
                    sortByDate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    /**
     * Clear the Array
     **/
    public void clearFeeds() {
        if (Utils.mFeeds.size() > 0) {
            Utils.mFeeds.clear();
        }
    }
    /**
     * Load the feed
     **/
    public void loadPublicFeed() {
        pDialog.show();
        Log.i("isTag", String.valueOf(feedByTag));
        Log.i("tagname", tag);
        if (feedByTag) {
            finalurl = url + "?format=json&tags=" + tag + "&api_key=6a363fd6f4dc99508254f88ca3e6c04c";
        } else {
            finalurl = url + "?format=json&api_key=6a363fd6f4dc99508254f88ca3e6c04c";
        }
        StringRequest request = new StringRequest(finalurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("view", "onresponse");
                Log.i("response", response.toString());
                try {
                    JSONObject result = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    JSONArray array = result.getJSONArray("items");
                    JSONObject object;
                    for (int i = 0; i < array.length(); i++) {
                        object = array.getJSONObject(i);
                        String title = object.getString("title");
                        String dateTaken = object.getString("date_taken");
                        String datePublished = object.getString("published");
                        String description = object.getString("description");
                        String author = object.getString("author");
                        String author_id = object.getString("author_id");
                        String tags = object.getString("tags");
                        String link = object.getString("link");
                        JSONObject innerObject = object.getJSONObject("media");
                        String pictureUrl = innerObject.getString("m");
                        pictureUrl = pictureUrl.substring(0, pictureUrl.indexOf("_m.")) + "_n.jpg";
                        Utils.mFeeds.add(new FlickrModel(i, title, dateTaken, datePublished, description, author, author_id, tags, link, pictureUrl));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    adapter = new GridAdapter(MainActivity.this, Utils.mFeeds);
                    grid_feeds.setAdapter(adapter);
                    pDialog.hide();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", error.getLocalizedMessage());
                pDialog.dismiss();
            }
        }
        );
        AppController.getInstance().getRequestQueue().add(request);
    }
    /**
     * Sort the grid by date
     **/
    public void sortByDate() {
        Collections.sort(Utils.mFeeds, new Comparator<FlickrModel>() {
            @Override
            public int compare(FlickrModel o1, FlickrModel o2) {
                Date d1 = Utils.intoDate(o1.getmDateTaken());
                Date d2 = Utils.intoDate(o2.getmDateTaken());
                return d1.compareTo(d2);
            }
        });
        adapter.notifyDataSetChanged();
        selectSort = true;
    }
    /**
     * Sort the grid by relevance
     **/
    public void sortByRelevance() {
        if (selectSort) {
            Collections.sort(Utils.mFeeds, new Comparator<FlickrModel>() {
                @Override
                public int compare(FlickrModel o1, FlickrModel o2) {
                    int p1 = o1.get_id();
                    int p2 = o2.get_id();
                    return p2 - p1;
                }
            });
            adapter.notifyDataSetChanged();
            selectSort = false;
        }
    }

    public void onStop() {
        super.onStop();
        pDialog.dismiss();
    }

    public void onDestroy() {
        super.onDestroy();
        pDialog.dismiss();
    }
}