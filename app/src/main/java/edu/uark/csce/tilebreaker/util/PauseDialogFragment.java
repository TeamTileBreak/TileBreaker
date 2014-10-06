package edu.uark.csce.tilebreaker.util;




import android.app.Activity;
import android.content.DialogInterface;

import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;

import edu.uark.csce.tilebreaker.TileBreakerActivity;


public class PauseDialogFragment extends DialogFragment{

    Activity mActivity;

    public PauseDialogFragment(Activity activity){
        super();
        mActivity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Paused")
                .setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        dialog.dismiss();
                        mActivity.finish();
                    }
                })
                .setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                        //TileBreakerActivity.finish();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
