package com.food.rescue.teamxero;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Shows a welcome message dialog to understand the purpose of app
 * Created by rishi on 9/12/16.
 */
public class ConfirmFragment extends DialogFragment {

    private static final String TAG = "ConfirmFragment";
    private Button mGotItButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_confirm, null);

        mGotItButton = (Button) v.findViewById(R.id.got_it);
        mGotItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
                sendResult(Activity.RESULT_OK);
            }
        });


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Thank you!")
                .create();
    }

    /**
     * Send result back to activity
     * @param resultCode
     */
    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
