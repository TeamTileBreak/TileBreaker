package edu.uark.csce.tilebreaker.util;




import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;

import edu.uark.csce.tilebreaker.MainActivity;
import edu.uark.csce.tilebreaker.TileBreakerActivity;
import edu.uark.csce.tilebreaker.UpgradeActivity;


public class PauseDialogFragment extends DialogFragment{

    Activity mActivity;
    public int score;

    public PauseDialogFragment(Activity activity){
        super();
        mActivity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Cash: $"+score)
                .setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent it = new Intent(mActivity, MainActivity.class);
                        // FIRE ZE MISSILES!
                        dialog.dismiss();
                        mActivity.finish();
                        startActivity(it);
                    }
                })
                .setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                        TileBreakerActivity.paused = false;
                        //TileBreakerActivity.finish();
                    }
                })
                .setTitle("Paused")
                .setNeutralButton("Upgrade", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Intent it = new Intent(mActivity, UpgradeActivity.class);
                        TileBreakerActivity.paused = false;
                        dialog.dismiss();
                        mActivity.finish();
                        startActivity(it);

                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog temp = builder.create();
        temp.setCanceledOnTouchOutside(false);
        return temp;
    }

}
