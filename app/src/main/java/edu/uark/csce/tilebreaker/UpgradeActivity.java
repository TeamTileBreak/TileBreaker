package edu.uark.csce.tilebreaker;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class UpgradeActivity extends Activity implements SensorEventListener {

    public static final String PREF_NAME = "myPrefsFile";
    public int score;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static long lastUpdate = 0;
    private static float last_x = 0;
    private static float last_y = 0;
    private static float last_z = 0;
    Drawable emptyUpgradeSlot;
    ArrayList<Drawable> currentInventoryBackgrounds;
    ArrayList<Upgrade> upgrades;
    ArrayList<Button> upgradeBtns;
    ArrayList<ImageView> inventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        upgrades = new ArrayList<Upgrade>();
        upgradeBtns = new ArrayList<Button>();
        inventory = new ArrayList<ImageView>();

        //add each upgrade
        upgrades.add(new Upgrade("NONE","NONE",0));
        upgrades.add(new Upgrade("net", "Net", 100));
        upgrades.add(new Upgrade("extendedPaddle", "Long Paddle", 100));
        upgrades.add(new Upgrade("stickyPaddle", "Sticky Paddle", 250));
        upgrades.add(new Upgrade("turrets", "Turret", 250));
        upgrades.add(new Upgrade("doubleBall", "Double Ball", 500));
        upgrades.add(new Upgrade("laserShot", "Lasers", 500));
        upgrades.add(new Upgrade("doubleDamageBall", "2X Damage", 750));
        upgrades.add(new Upgrade("shotgunBall", "Shotgun", 1000));
        upgrades.add(new Upgrade("flameThrower", "Hellfire", 1000));


        SharedPreferences pref = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        score = pref.getInt("score",score);

        TextView scoreText = (TextView)findViewById(R.id.creditsView);
        scoreText.setText("$"+score);

        //define upgrade buttons, assign their upgrade object tags, set listeners
        upgradeBtns.add((Button)findViewById(R.id.ug5)); //throw away value, were starting at 1
        for (int i = 1; i < 10; i++) {
            String id = "ug"+i;
            int resid = getResources().getIdentifier (id, "id", getPackageName());
            upgradeBtns.add((Button)findViewById(resid));
            upgradeBtns.get(i).setTag(upgrades.get(i));
            upgradeBtns.get(i).setText(upgrades.get(i).getDisplayName() + " $" + upgrades.get(i).getCost());
            upgradeBtns.get(i).setOnLongClickListener(new UpgradeClickListener());
        }

        //define empty upgrade slot, initialize all current inventory backgrounds to empty
        emptyUpgradeSlot = getResources().getDrawable(R.drawable.empty_upgrade_slot);
        currentInventoryBackgrounds = new ArrayList<Drawable>();
        for (int i = 0; i < 4; i++) {
            currentInventoryBackgrounds.add(emptyUpgradeSlot);
        }

        //defining inventory views, initialize their upgrade object tag to null upgrade, set listeners
        inventory.add((ImageView)findViewById(R.id.inventory1)); //throw away value, were starting at 1
        for (int i = 1; i < 4; i++) {
            final String id = "inventory"+i;
            int resid = getResources().getIdentifier (id, "id", getPackageName());
            inventory.add((ImageView)findViewById(resid));
            inventory.get(i).setTag(upgrades.get(0));
            inventory.get(i).setBackground(emptyUpgradeSlot);
            inventory.get(i).setOnDragListener(new UpgradeDragListener());
            //if click on inventory item, clear that item from inventory
            inventory.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Upgrade upgrade = (Upgrade)view.getTag();
                    score += upgrade.getCost();

                    for (int i = 1; i < 4; i++) {
                        if (view == inventory.get(i))
                            currentInventoryBackgrounds.set(i,emptyUpgradeSlot);
                    }

                    view.setBackground(emptyUpgradeSlot);
                    view.setTag(upgrades.get(0));

                    TextView scoreText = (TextView) findViewById(R.id.creditsView);
                    scoreText.setText("$" + score);
                }
            });
        }

        //X BUTTON LISTENER
        Button xBtn = (Button) findViewById(R.id.xBtn);
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
        Button checkBtn = (Button) findViewById(R.id.checkBtn);
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

        //define accelerometer
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long curTime = System.currentTimeMillis();
        //listen for screen shake
        if (lastUpdate == 0)
            lastUpdate = System.currentTimeMillis();
        // only allow one update every 100ms.
        if ((curTime - lastUpdate) > 100) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float a = sensorEvent.values[0] + sensorEvent.values[1] + sensorEvent.values[2];
            float b = last_x+last_y+last_z;
            float speed = Math.abs(a-b) / diffTime * 10000;

            //register shake, reset upgrade slots
            if (speed > 3000) {
                Log.d("sensor", "shake detected w/ speed: " + speed);
                Intent i = new Intent(UpgradeActivity.this,UpgradeActivity.class);
                finish();
                startActivity(i);
            }
            last_x = sensorEvent.values[0];
            last_y = sensorEvent.values[1];
            last_z = sensorEvent.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private final class UpgradeClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            Upgrade upgrade = (Upgrade)view.getTag();
            ClipData.Item item = new ClipData.Item((String)upgrade.getName());
            String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
            ClipData data = new ClipData(upgrade.getName(), mimeTypes, item);
            DragShadowBuilder shadowBuilder = new DragShadowBuilder(view);

            view.startDrag(data,shadowBuilder,view,0);
            //view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    class UpgradeDragListener implements OnDragListener {



        @Override
        public boolean onDrag(View v, DragEvent event) {

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackground(getResources().getDrawable(R.drawable.gre_btn));
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //dont keep any green backgrounds
                    for (int i = 1; i < 4; i++){
                        if (v == inventory.get(i))
                            v.setBackground(currentInventoryBackgrounds.get(i));
                    }
                    break;
                case DragEvent.ACTION_DROP:
                    View vie = (View) event.getLocalState();

                    Boolean alreadySelected = false;
                    for (int i = 1; i < 4; i++) {
                        if (inventory.get(i).getTag() == vie.getTag()) {
                            if (v.getTag() != vie.getTag())
                                alreadySelected = true;
                        }
                    }

                    if (inventory.contains(v)) {
                            //Log.d("DRAGGED_VIEW", vie.toString());
                            //Log.d("DESTINATION_VIEW", "View = " + v.toString());
                        Upgrade newUpgrade = (Upgrade)vie.getTag();
                        Upgrade oldUpgrade = (Upgrade)v.getTag();
                        //no money
                        if(score < newUpgrade.getCost()) {
                            Toast.makeText(getApplicationContext(), "Not enough Cash! " + newUpgrade.getDisplayName() + " costs $" + newUpgrade.getCost(), Toast.LENGTH_LONG).show();
                        //already selected this upgrade
                        } else if (alreadySelected) {
                            Toast.makeText(getApplicationContext(),"Duplicate Upgrades not allowed! Please choose another",Toast.LENGTH_LONG).show();
                        //good
                        } else {
                            //give the user back their money for the upgrade that was there previously (0 if it was blank)
                            score += oldUpgrade.getCost();
                            v.setBackground(vie.getBackground());
                            for (int i = 1; i < 4; i++) {
                                if (v == inventory.get(i))
                                    currentInventoryBackgrounds.set(i,v.getBackground());
                            }
                            v.setTag(vie.getTag());

                            //force redraw of view
                            v.invalidate();

                            //take the cost of the new upgrade from their bank account
                            score -= newUpgrade.getCost();
                            TextView scoreText = (TextView) findViewById(R.id.creditsView);
                            scoreText.setText("$" + score);
                            return true;
                        }
                    } else {
                        vie.setVisibility(View.VISIBLE);
                        return false;
                    }
                case DragEvent.ACTION_DRAG_ENDED:
                    //dont keep any green backgrounds
                    for (int i = 1; i < 4; i++){
                        if (v == inventory.get(i))
                            v.setBackground(currentInventoryBackgrounds.get(i));
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    //-------------------------------------------------------

    private void saveUpgrades() {
        SharedPreferences.Editor pref = getSharedPreferences(PREF_NAME,MODE_PRIVATE).edit();

        //set all to false first
        for (int i = 1; i < 10; i++)
            pref.putBoolean(upgrades.get(i).getName(), false);

        //set true the ones the user chose (empty will just set "NONE" to true, nbd)
        for (int i = 1; i < 4; i++) {
            Upgrade u = (Upgrade)inventory.get(i).getTag();
            pref.putBoolean(u.getName(), true);
        }

        pref.putInt("score",score);
        pref.commit();

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
