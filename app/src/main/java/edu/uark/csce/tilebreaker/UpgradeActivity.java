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


public class UpgradeActivity extends Activity implements SensorEventListener {

    public static final String PREF_NAME = "myPrefsFile";
    public Button ug1,ug2,ug3,ug4,ug5,ug6,ug7,ug8,ug9;
    public int score;
    private ImageView inventory1, inventory2, inventory3;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static long lastUpdate = 0;
    private static float last_x = 0;
    private static float last_y = 0;
    private static float last_z = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        SharedPreferences pref = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        score = pref.getInt("score",score);

        TextView scoreText = (TextView)findViewById(R.id.creditsView);
        scoreText.setText("$"+score);

        ug1 = (Button)findViewById(R.id.ug1);
        ug1.setTag("UG1");
        ug1.setOnLongClickListener(new UpgradeClickListener());

        ug2 = (Button)findViewById(R.id.ug2);
        ug2.setTag("UG2");
        ug2.setOnLongClickListener(new UpgradeClickListener());

        ug3 = (Button)findViewById(R.id.ug3);
        ug3.setTag("UG3");
        ug3.setOnLongClickListener(new UpgradeClickListener());

        ug4 = (Button)findViewById(R.id.ug4);
        ug4.setTag("UG4");
        ug4.setOnLongClickListener(new UpgradeClickListener());

        ug5 = (Button)findViewById(R.id.ug5);
        ug5.setTag("UG5");
        ug5.setOnLongClickListener(new UpgradeClickListener());

        ug6 = (Button)findViewById(R.id.ug6);
        ug6.setTag("UG6");
        ug6.setOnLongClickListener(new UpgradeClickListener());

        ug7 = (Button)findViewById(R.id.ug7);
        ug7.setTag("UG7");
        ug7.setOnLongClickListener(new UpgradeClickListener());

        ug8 = (Button)findViewById(R.id.ug8);
        ug8.setTag("UG8");
        ug8.setOnLongClickListener(new UpgradeClickListener());

        ug9 = (Button)findViewById(R.id.ug9);
        ug9.setTag("UG9");
        ug9.setOnLongClickListener(new UpgradeClickListener());

        inventory1 = (ImageView)findViewById(R.id.inventory1);
        inventory1.setTag("inv1");
        inventory1.setOnDragListener(new UpgradeDragListener());
        inventory2 = (ImageView)findViewById(R.id.inventory2);
        inventory2.setTag("inv2");
        inventory2.setOnDragListener(new UpgradeDragListener());
        inventory3 = (ImageView)findViewById(R.id.inventory3);
        inventory3.setTag("inv3");
        inventory3.setOnDragListener(new UpgradeDragListener());

        inventory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackground(getResources().getDrawable(R.drawable.empty_upgrade_slot));
                view.setTag("inv1");
                score += 100;
                TextView scoreText = (TextView) findViewById(R.id.creditsView);
                scoreText.setText("$" + score);
            }
        });
        inventory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackground(getResources().getDrawable(R.drawable.empty_upgrade_slot));
                view.setTag("inv2");
                score += 100;
                TextView scoreText = (TextView) findViewById(R.id.creditsView);
                scoreText.setText("$" + score);
            }
        });
        inventory3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackground(getResources().getDrawable(R.drawable.empty_upgrade_slot));
                view.setTag("inv3");
                score += 100;
                TextView scoreText = (TextView) findViewById(R.id.creditsView);
                scoreText.setText("$" + score);
            }
        });


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
            ClipData.Item item = new ClipData.Item((CharSequence)view.getTag());
            String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
            ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
            DragShadowBuilder shadowBuilder = new DragShadowBuilder(view);

            view.startDrag(data,shadowBuilder,view,0);
            //view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    class UpgradeDragListener implements OnDragListener {

        Drawable upgradeSlot = getResources().getDrawable(R.drawable.empty_upgrade_slot);
        Drawable currentInv1Background = upgradeSlot;
        Drawable currentInv2Background = upgradeSlot;
        Drawable currentInv3Background = upgradeSlot;

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //stuff
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackground(getResources().getDrawable(R.drawable.gre_btn));
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    if (v == findViewById(R.id.inventory1))
                        v.setBackground(currentInv1Background);
                    else if (v == findViewById(R.id.inventory2))
                        v.setBackground(currentInv2Background);
                    else if (v == findViewById(R.id.inventory3))
                        v.setBackground(currentInv3Background);
                    //View view = (View) event.getLocalState();
                    //v.setBackground(view.getBackground());
                    break;
                case DragEvent.ACTION_DROP:
                    View vie = (View) event.getLocalState();
                    if (v == findViewById(R.id.inventory1) || v == findViewById(R.id.inventory2) || v == findViewById(R.id.inventory3)) {
                            Log.d("DRAGGED_VIEW", vie.toString());
                            Log.d("DESTINATION_VIEW", "View = " + v.toString());
                        //no money
                        if(score < 100) {
                            Toast.makeText(getApplicationContext(),"Not enough Cash! Each upgrade costs: $100",Toast.LENGTH_LONG).show();
                        //already selected this upgrade
                        } else if (inventory1.getTag() == vie.getTag() || inventory2.getTag() == vie.getTag() || inventory3.getTag() == vie.getTag()){
                            if (v.getTag() != vie.getTag())
                                Toast.makeText(getApplicationContext(),"Duplicate Upgrades not allowed! Please choose another",Toast.LENGTH_LONG).show();
                        //good
                        } else {
                            v.setBackground(vie.getBackground());
                            if (v == findViewById(R.id.inventory1))
                                currentInv1Background = v.getBackground();
                            else if (v == findViewById(R.id.inventory2))
                                currentInv2Background = v.getBackground();
                            else if (v == findViewById(R.id.inventory3))
                                currentInv3Background = v.getBackground();
                            v.setTag(vie.getTag());

                            //force redraw of view and drop view
                            v.invalidate();
                            vie.invalidate();

                            //Log.d("TAG", v.toString() + " tag = " + v.getTag());
                            score -= 100;
                            TextView scoreText = (TextView) findViewById(R.id.creditsView);
                            scoreText.setText("$" + score);
                            return true;
                        }
                    } else {
                        vie.setVisibility(View.VISIBLE);
                        return false;
                    }
                case DragEvent.ACTION_DRAG_ENDED:
                    if (v == findViewById(R.id.inventory1))
                        v.setBackground(currentInv1Background);
                    else if (v == findViewById(R.id.inventory2))
                        v.setBackground(currentInv2Background);
                    else if (v == findViewById(R.id.inventory3))
                        v.setBackground(currentInv3Background);
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    //-------------------------------------------------------

    private void saveUpgrades() {
        //store upgrade values (all of them)
        SharedPreferences.Editor pref = getSharedPreferences(PREF_NAME,MODE_PRIVATE).edit();

        ImageView ug1 = (ImageView) findViewById(R.id.inventory1);
        ImageView ug2 = (ImageView) findViewById(R.id.inventory2);
        ImageView ug3 = (ImageView) findViewById(R.id.inventory3);

        String UG1 = ug1.getTag().toString();
        String UG2 = ug2.getTag().toString();
        String UG3 = ug3.getTag().toString();

        //Log.i("UG1", UG1);

        pref.putBoolean("doubleBall", false);
        pref.putBoolean("shotgunBall", false);
        pref.putBoolean("flameThrower", false);
        pref.putBoolean("extendedPaddle", false);
        pref.putBoolean("laserShot", false);
        pref.putBoolean("net", false);
        pref.putBoolean("doubleDamageBall", false);
        pref.putBoolean("turrets", false);
        pref.putBoolean("stickyPaddle", false);
        pref.putInt("score",score);


        if(UG1.equals("UG1")||UG2.equals("UG1")||UG3.equals("UG1"))
            pref.putBoolean("doubleBall", true);
        if(UG1.equals("UG2")||UG2.equals("UG2")||UG3.equals("UG2"))
            pref.putBoolean("shotgunBall", true);
        if(UG1.equals("UG3")||UG2.equals("UG3")||UG3.equals("UG3"))
            pref.putBoolean("flameThrower", true);
        if(UG1.equals("UG4")||UG2.equals("UG4")||UG3.equals("UG4"))
            pref.putBoolean("extendedPaddle", true);
        if(UG1.equals("UG5")||UG2.equals("UG5")||UG3.equals("UG5"))
            pref.putBoolean("laserShot", true);
        if(UG1.equals("UG6")||UG2.equals("UG6")||UG3.equals("UG6"))
            pref.putBoolean("net", true);
        if(UG1.equals("UG7")||UG2.equals("UG7")||UG3.equals("UG7"))
            pref.putBoolean("doubleDamageBall", true);
        if(UG1.equals("UG8")||UG2.equals("UG8")||UG3.equals("UG8"))
            pref.putBoolean("turrets", true);
        if(UG1.equals("UG9")||UG2.equals("UG9")||UG3.equals("UG9"))
            pref.putBoolean("stickyPaddle", true);

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
