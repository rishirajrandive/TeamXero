package com.food.rescue.teamxero;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private ImageView foodImage;
    private TextView nameView;
    private TextView addressLine1;
    private TextView addressLine2;
    private ImageButton callButton;
    private ImageButton getDirectionsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        foodImage = (ImageView) findViewById(R.id.product_image);
        nameView = (TextView) findViewById(R.id.name);
        addressLine1 = (TextView) findViewById(R.id.address_line_1);
        addressLine2 = (TextView) findViewById(R.id.address_line_2);
        callButton = (ImageButton) findViewById(R.id.call_button);
        getDirectionsButton = (ImageButton) findViewById(R.id.get_directions_button);

        populateData(new Provider());
    }

    private void populateData(Provider provider) {
        foodImage.setImageURI(Uri.parse(provider.getImageLink()));
        nameView.setText(provider.getFirstName() + " " + provider.getLastName());
        addressLine1.setText(provider.getAddress().getAddress());
        addressLine2.setText(provider.getAddress().getCity() + ", " +
                provider.getAddress().getState() + ", " + provider.getAddress().getZipcode());
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO open calling app with contact number.
            }
        });

        getDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO open Maps app with directions.
            }
        });
    }
}
