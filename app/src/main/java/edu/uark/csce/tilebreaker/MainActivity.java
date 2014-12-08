package edu.uark.csce.tilebreaker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    public void playGame(View view) {
        Intent intent = new Intent(this, TileBreakerActivity.class);
        SharedPreferences.Editor pref = getSharedPreferences(UpgradeActivity.PREF_NAME,MODE_PRIVATE).edit();
        pref.putInt("score",0);
        pref.putBoolean("doubleBall", false);
        pref.putBoolean("shotgunBall", false);
        pref.putBoolean("flameThrower", false);
        pref.putBoolean("extendedPaddle", false);
        pref.putBoolean("laserShot", false);
        pref.putBoolean("net", false);
        pref.putBoolean("doubleDamageBall", false);
        pref.putBoolean("turrets", false);
        pref.putBoolean("stickyPaddle", false);
        pref.commit();
        
        String xtra  = "1";
        intent.putExtra("newGame", xtra);
        MainActivity.this.finish();
        startActivity(intent);
    }

    public void chooseUpgrade(View view) {
        Intent intent = new Intent(this, UpgradeActivity.class);
        MainActivity.this.finish();
        startActivity(intent);
    }
}
