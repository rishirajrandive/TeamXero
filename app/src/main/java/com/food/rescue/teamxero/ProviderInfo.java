package com.food.rescue.teamxero;

import android.content.Context;
import android.util.Log;

import com.food.rescue.teamxero.pojo.Address;
import com.food.rescue.teamxero.pojo.SearchTerm;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.iid.FirebaseInstanceId;

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
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
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
    private String registrationToken = "";

    public static ProviderInfo getsProviderInfo(Context context){
        if(sProviderInfo == null){
            sProviderInfo = new ProviderInfo();
        }
        return sProviderInfo;
    }

    public ProviderInfo(){
        registrationToken = FirebaseInstanceId.getInstance().getToken();
    }

    public List<Provider> fetchProducts(SearchTerm searchTerm){
        try{
            //mSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");
            String requestURL = "https://teamxero.herokuapp.com/find";
            JSONObject jsonObject = getJSONObject(searchTerm);
            mProviderList = new ArrayList<Provider>(0);
            parseResponse(requestProducts(requestURL, jsonObject));
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

    private JSONObject getJSONObject(SearchTerm searchTerm) throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("latitude",searchTerm.getLatitude());
        jsonObject.put("longitude",searchTerm.getLongitude());
        jsonObject.put("radius",searchTerm.getRadius());
        return jsonObject;
    }

    public String requestProducts(String requestURL, JSONObject jsonObject) throws IllegalStateException, IOException{
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String text = null;
        try {
            HttpPost httpPost = new HttpPost(requestURL);
            StringEntity postData = new StringEntity(jsonObject.toString());
            httpPost.addHeader("content-type", "application/json");
            httpPost.setEntity(postData);
            HttpResponse response = httpClient.execute(httpPost, localContext);
            HttpEntity entity = response.getEntity();
            text = search(entity);
            Log.d(TAG, "Property Data posted ID "+ entity);
        } catch (Exception e) {
            Log.e(TAG, e.getStackTrace().toString());
        }
        return text;
    }

    protected String search(HttpEntity entity) throws IllegalStateException, IOException {
        InputStream in = entity.getContent();
        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n>0) {
            byte[] b = new byte[4096];
            n =  in.read(b);
            if (n>0) out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    private void parseResponse(String response) throws JSONException{
        JSONArray resultArray = new JSONArray(response);
        //JSONObject jsonObject = new JSONObject(response);
        //JSONArray resultArray = jsonObject.getJSONArray("");

        for(int index=0; index<resultArray.length(); index++){
            JSONObject productObject = resultArray.getJSONObject(index);

            Provider provider = new Provider(productObject.getString("_id"), productObject.getString("contact"),
                    productObject.getBoolean("available"),
                    productObject.getString("foodType"),
                    productObject.getString("quantity"),
                    productObject.getString("description"),
                    productObject.getString("imageLink"),
                    productObject.getDouble("timestamp"),
                    productObject.getDouble("expiryDate"),
                    productObject.getString("firstName"),
                    productObject.getString("lastName"),
                    parseLocation(productObject.getJSONArray("location")),
                    parseAddress(productObject.getString("address")));
            mProviderList.add(provider);
        }
    }

    private Address parseAddress(String address) throws JSONException {
        JSONObject jsonObject = new JSONObject(address);

        return new Address(jsonObject.getString("address"),
                jsonObject.getString("city"),
                jsonObject.getString("State"),
                jsonObject.getString("zipcode"));
    }

    private SearchTerm parseLocation(JSONArray location) throws JSONException {

        return new SearchTerm( location.getDouble(1), location.getDouble(0), 0);
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

    public Provider getProviderById(String id){
        for(Provider provider : mProviderList){
            if(provider.getId().equalsIgnoreCase(id)){
                return provider;
            }
        }
        return null;
    }

    public String getRegistrationToken() {
        if(registrationToken == null){
            return "";
        }
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public void sendNotifyMeEnable(LatLng latLng){
        try{
            //mSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");
            String requestURL = "https://teamxero.herokuapp.com/notifications";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("latitude", latLng.latitude);
            jsonObject.put("longitude", latLng.longitude);
            jsonObject.put("token", getRegistrationToken());
            Log.d(TAG, "Value to be sent "+ jsonObject.toString());
            requestProducts(requestURL, jsonObject);
        }catch (IllegalStateException ex){
            Log.e(TAG, "Couldn't fetch data from endpoints "+ ex.getMessage());
        }catch (IOException ex){
            Log.e(TAG, "Couldn't fetch data from endpoints " + ex.getMessage());
        }catch (JSONException ex){
            Log.e(TAG, "Couldn't parse the data " + ex.getMessage());
        }catch (Exception ex){
            Log.e(TAG, "Product fetch failed "+ ex.getMessage());
        }
    }
}
