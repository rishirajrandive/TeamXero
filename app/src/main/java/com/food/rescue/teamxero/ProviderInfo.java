package com.food.rescue.teamxero;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.protocol.BasicHttpContext;
import cz.msebera.android.httpclient.protocol.HttpContext;

/**
 * Created by rishi on 9/24/16.
 */
public class ProviderInfo {

    private static final String TAG = "ProviderInfo";
    private static ProviderInfo sProviderInfo;

    private List<Provider> mProviderList;

    public static ProviderInfo getsProviderInfo(Context context){
        if(sProviderInfo == null){
            sProviderInfo = new ProviderInfo();
        }
        return sProviderInfo;
    }

    public List<Provider> fetchProducts(String searchTerm){
        try{
            //mSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");
            String requestURL = "";
            mProviderList = new ArrayList<>();

            parseResponse(requestProducts(requestURL));
        }catch (IllegalStateException ex){
            Log.e(TAG, "Couldn't fetch data from endpoints "+ ex.getMessage());
        }catch (IOException ex){
            Log.e(TAG, "Couldn't fetch data from endpoints " + ex.getMessage());
        }catch (JSONException ex){
            Log.e(TAG, "Couldn't parse the data " + ex.getMessage());
        }catch (Exception ex){
            Log.e(TAG, "Product fetch failed "+ ex.getMessage());
        }
        return mProviderList;
    }

    public String requestProducts(String requestURL) throws IllegalStateException, IOException{
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet(requestURL);

        String text = null;
        HttpResponse response = httpClient.execute(httpGet, localContext);
        HttpEntity entity = response.getEntity();
        text = getResponse(entity);

        return text;
    }

    private void parseResponse(String response) throws JSONException{
        JSONObject jsonObject = new JSONObject(response);
        JSONArray resultArray = jsonObject.getJSONArray("");

        for(int index=0; index<resultArray.length(); index++){
            JSONObject productObject = resultArray.getJSONObject(index);
            Provider provider = new Provider();

            mProviderList.add(provider);
        }
    }

    public String getResponse(HttpEntity entity) throws IllegalStateException, IOException{
        InputStream inputStream = entity.getContent();
        StringBuffer stringBuffer = new StringBuffer();
        int lines = 1;
        while (lines>0) {
            byte[] b = new byte[4096];
            lines =  inputStream.read(b);
            if (lines>0) stringBuffer.append(new String(b, 0, lines));
        }
        return stringBuffer.toString();
    }
}
