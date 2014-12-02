package edu.uark.csce.tilebreaker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class UpgradeActivity extends Activity {

    public static final String PREF_NAME = "myPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        //X BUTTON LISTENER
        ImageButton xBtn = (ImageButton) findViewById(R.id.xBtn);
        xBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpgradeActivity.this, MainActivity.class);
                TileBreakerActivity.upgrade = false;
                startActivity(intent);
                finish();
            }
        });
        //CHECK BUTTON LISTENER
        ImageButton checkBtn = (ImageButton) findViewById(R.id.checkBtn);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUpgrades();
                Intent intent = new Intent(UpgradeActivity.this, TileBreakerActivity.class);
                TileBreakerActivity.upgrade = false;
                finish();
                startActivity(intent);
            }
        });
    }

    //-------------------------------------------------------

    private void saveUpgrades() {
        //store upgrade values (all of them)
        SharedPreferences.Editor pref = getSharedPreferences(PREF_NAME,MODE_PRIVATE).edit();

        pref.putBoolean("Lightening", true);
        pref.commit();
        //UpgradeActivity.this.finish();

    }

    //-------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.upgrade, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
