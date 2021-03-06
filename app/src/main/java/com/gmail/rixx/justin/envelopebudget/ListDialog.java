package com.gmail.rixx.justin.envelopebudget;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by justin on 6/20/15.
 */
public class ListDialog extends DialogFragment {

    private String [] data;
    private String result;
    private String title;


    public String getResult() {
        return result;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // get all the arguments
        Bundle args = getArguments();
        title = args.getString("TITLE");
        data = args.getStringArray("DATA");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String choice = ((TextView) getActivity().findViewById(R.id.dropdown)).getText().toString();
        int position = 0;

        // find out which one should be checked
        for (int i = 0; i < data.length; i++) {
            if (data[i].equals(choice)) {
                position = i;
            }
        }

        // set the title
        builder.setTitle(title)

        // set the list of choices
            .setSingleChoiceItems(data, position, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result = data[which];
                }
            })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Set the text of the textview
                        if (result != null) {
                            ((TextView) getActivity().findViewById(R.id.dropdown)).setText(result);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });

        return builder.create();
    }
}
