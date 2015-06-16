package com.example.aaron.shoppinglist;

/**
 * Created by Aaron on 6/2/2015.
 */
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class JSONHandler {

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    public final static String API_KEY = "f51b28604e5068d547bf1b79b041df0d";
    public final static String API_URL = "http://api.upcdatabase.org/json/";
    public static final String TAG_VALID = "valid";
    public static final String TAG_ITEMNAME = "itemname";
    public static final String TAG_REASON = "reason";
    public static final String FALSE_NOT_FOUND = "Code not found in database.";
    public static final String FALSE_INVALID_CODE = "Non-numeric code entered.";


    public JSONHandler() {

    }

    public String makeServiceCall(String pURL, int pMethod) {
        return this.makeServiceCall(pURL, pMethod, null);
    }

    public String makeServiceCall(String pURL, int pMethod, List<NameValuePair> pOptions) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            if (pMethod == POST) {
                HttpPost httpPost = new HttpPost(pURL);
                if (pOptions != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(pOptions));
                }

                httpResponse = httpClient.execute(httpPost);

            } else if (pMethod == GET) {
                // appending params to url
                if (pOptions != null) {
                    String paramString = URLEncodedUtils
                            .format(pOptions, "utf-8");
                    pURL += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(pURL);

                httpResponse = httpClient.execute(httpGet);

            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
