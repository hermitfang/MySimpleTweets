package com.codepath.apps.mysimpletweets.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PostTweetActivity extends ActionBarActivity {
    private TwitterClient client;
    private ImageView ivMyThumb;
    private TextView tvMyName;

    private ArrayList<String> myProfile;

    private void readItems () {
        // return;
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "myProfile.txt");
        // Toast.makeText(this, todoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        try {
            myProfile = new ArrayList<String>(FileUtils.readLines(todoFile));
        }
        catch (IOException e) {
            myProfile = new ArrayList<String>();
        }

    }
    //"hermitfang";
    //"https://abs.twimg.com/sticky/default_profile_images/default_profile_5_bigger.png";
    private void writeItems () {
        myProfile.add("hermitfang");
        myProfile.add("https://abs.twimg.com/sticky/default_profile_images/default_profile_5_bigger.png");

        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "myProfile.txt");
        try {
            FileUtils.writeLines(todoFile, myProfile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_tweet);

        client = TwitterApplication.getRestClient(); //singleton...

        myProfile = new ArrayList<>();

        showMyProfile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    "name": "TwitterDev",
    "profile_image_url": "http://pbs.twimg.com/profile_images/530814764687949824/npQQVkq8_normal.png",
    "profile_image_url_https": "https://pbs.twimg.com/profile_images/530814764687949824/npQQVkq8_normal.png",
     */
    private void showMyProfile () {
        ivMyThumb = (ImageView) findViewById(R.id.ivMyThumb);
        tvMyName = (TextView) findViewById(R.id.tvMyName);

        readItems();
        String name;
        String thumb;

        try {
            name = myProfile.get(0);
            thumb = myProfile.get(1);
        }
        catch (Exception e) {
            name = "hermitfang";
            thumb = "https://abs.twimg.com/sticky/default_profile_images/default_profile_5_bigger.png";
        }

        if (!name.equals("")) {
            tvMyName.setText(name);
            Picasso.with(getApplicationContext()).load(thumb).into(ivMyThumb);
        }
        // https://abs.twimg.com/sticky/default_profile_images/default_profile_5_bigger.png
        /*
        client.getMyProfile(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                ivMyThumb = (ImageView) findViewById(R.id.ivMyThumb);
                tvMyName = (TextView) findViewById(R.id.tvMyName);

                try {
                    String myName = json.getString("name");
                    String myThumbUrl = json.getString("profile_image_url");
                    tvMyName.setText(myName);
                    Picasso.with(getApplicationContext()).load(myThumbUrl).into(ivMyThumb);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
        */
    }

    public void onCancelTweet (View v) {
        finish();
    }

    public void onPostTweet (View v) {
        final Context c = this;

        EditText etPost = (EditText)findViewById(R.id.etPost);
        String q = etPost.getText().toString();
        /*
        try {
            q = URLEncoder.encode(q, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        */

        JsonHttpResponseHandler j = new JsonHttpResponseHandler () {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Toast.makeText(c, "Post Success", Toast.LENGTH_SHORT).show();

                Intent result = new Intent();
                result.putExtra("isSuccess", "success");
                setResult(RESULT_OK, result);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(c, "Post Failure", Toast.LENGTH_SHORT).show();

                Intent result = new Intent();
                result.putExtra("isSuccess", "failure");
                setResult(RESULT_OK, result);
                finish();
            }
        };

        try {
            client.postMyTweet(q, j);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("EXcpetion: !!!", "");
        }

    }
}
