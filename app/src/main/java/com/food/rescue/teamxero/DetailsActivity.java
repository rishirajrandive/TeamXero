package com.food.rescue.teamxero;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

public class DetailsActivity extends AppCompatActivity {

    private ImageView foodImage;
    private TextView nameView;
    private TextView addressLine1;
    private TextView addressLine2;
    private Button callButton;
    private Button getDirectionsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String providerId = intent.getStringExtra("provider_id");
        Provider provider = ProviderInfo.getsProviderInfo(this).getProviderById(providerId);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        foodImage = (ImageView) findViewById(R.id.product_image);
        nameView = (TextView) findViewById(R.id.name);
        addressLine1 = (TextView) findViewById(R.id.address_line_1);
        addressLine2 = (TextView) findViewById(R.id.address_line_2);
        callButton = (Button) findViewById(R.id.call_button);
        getDirectionsButton = (Button) findViewById(R.id.get_directions_button);

        populateData(provider);
    }

    private void populateData(final Provider provider) {
        new DownloadImageTask(foodImage).execute(provider.getImageLink());
        nameView.setText(provider.getFirstName() + " " + provider.getLastName());
        addressLine1.setText(provider.getAddress().getAddress());
        addressLine2.setText(provider.getAddress().getCity() + ", " +
                provider.getAddress().getState() + ", " + provider.getAddress().getZipcode());
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + provider.getContact()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        getDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+ provider.getLocation().getLatitude() + " ,"+ provider.getLocation().getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) { // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result){
            Log.d("setting image"," now");
            if(result != null){
                Log.d("DOwnloadTask", "Width of image "+ imageView.getId() + "  width "+ imageView.getWidth());
                imageView.setImageBitmap(scaleToFitWidth(result, imageView.getWidth()));
            }
        }

    }

    public static Bitmap scaleToFitWidth(Bitmap b, int width)
    {
        if(width != 0){
            float factor = width / (float) b.getWidth();
            return Bitmap.createScaledBitmap(b, width, (int) (b.getHeight() * factor), true);
        }else {
            return b;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
