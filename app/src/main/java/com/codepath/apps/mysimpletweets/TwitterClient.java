package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "uNBCTY7oVPZr4ZfIc10T7dbaW";       // Change this
	public static final String REST_CONSUMER_SECRET = "I7NCDO8pza7eB87xNaJL7oqnJh1aupw4KHhQFVkAvImsZc2jE0"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	private int tweetsPerLoad;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
		tweetsPerLoad = 15;
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}

	// home timeline , page begins from 0
	public void getHomeTimeline(long maxId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();

		// set parameters
		params.put("count", tweetsPerLoad);
		if (maxId != 0) {
			maxId--;
			params.put("max_id", maxId);
		}
		else {
			Log.d("GET TIMELINE since_id", Long.toString(maxId));
			params.put("since_id", 1);
		}

		// Log.d("OH MY GOD", getClient().get(apiUrl, params, handler).toString());
		// makeText(TwitterClient.this, getClient().get(apiUrl, params, handler).toString(), LENGTH_SHORT).show();
		getClient().get(apiUrl, params, handler);
	}

	// get my name and thumbnail from my profile
	// see https://dev.twitter.com/rest/reference/get/users/show
	// see also https://dev.twitter.com/overview/api/users
	public void getMyProfile(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();

		// set parameters
		params.put("user_id", "hermitfang");
		params.put("screen_name", "twitterdev");

		// Log.d("OH MY Profile", getClient().get(apiUrl, params, handler).toString());
		// makeText(TwitterClient.this, getClient().get(apiUrl, params, handler).toString(), LENGTH_SHORT).show();
		getClient().get(apiUrl, params, handler);

	}

	// post a tweet
	// see https://dev.twitter.com/rest/reference/post/statuses/update
	public void postMyTweet(String encodedText, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();

		// set parameters
		params.put("status", encodedText);

/*
		String q = "random word £500 bank $";
		String url = "http://example.com/query?q=" + URLEncoder.encode(q, "UTF-8");
*/

		// getClient().get(apiUrl, params, handler);
		getClient().post(apiUrl, params, handler);
	}

	// compose tweets


	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}