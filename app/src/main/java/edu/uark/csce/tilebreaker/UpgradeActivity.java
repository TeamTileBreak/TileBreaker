package edu.uark.csce.tilebreaker;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class UpgradeActivity extends Activity {

    public static final String PREF_NAME = "myPrefsFile";
    private ImageButton ug1,ug2,ug3,ug4,ug5,ug6,ug7,ug8,ug9;
    private ImageView inventory1, inventory2, inventory3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        ug1 = (ImageButton)findViewById(R.id.ug1);
            ug1.setTag("UG1");
            ug1.setOnLongClickListener(new UpgradeClickListener());

        ug2 = (ImageButton)findViewById(R.id.ug2);
            ug2.setTag("UG2");
            ug2.setOnLongClickListener(new UpgradeClickListener());

        ug3 = (ImageButton)findViewById(R.id.ug3);
            ug3.setTag("UG3");
            ug3.setOnLongClickListener(new UpgradeClickListener());

        ug4 = (ImageButton)findViewById(R.id.ug4);
            ug4.setTag("UG4");
            ug4.setOnLongClickListener(new UpgradeClickListener());

        ug5 = (ImageButton)findViewById(R.id.ug5);
            ug5.setTag("UG5");
            ug5.setOnLongClickListener(new UpgradeClickListener());

        ug6 = (ImageButton)findViewById(R.id.ug6);
            ug6.setTag("UG6");
            ug6.setOnLongClickListener(new UpgradeClickListener());

        ug7 = (ImageButton)findViewById(R.id.ug7);
            ug7.setTag("UG7");
            ug7.setOnLongClickListener(new UpgradeClickListener());

        ug8 = (ImageButton)findViewById(R.id.ug8);
            ug8.setTag("UG8");
            ug8.setOnLongClickListener(new UpgradeClickListener());

        ug9 = (ImageButton)findViewById(R.id.ug9);
            ug9.setTag("UG9");
            ug9.setOnLongClickListener(new UpgradeClickListener());

        inventory1 = (ImageView)findViewById(R.id.inventory1);
            inventory1.setOnDragListener(new UpgradeDragListener());
        inventory2 = (ImageView)findViewById(R.id.inventory2);
            inventory2.setOnDragListener(new UpgradeDragListener());
        inventory3 = (ImageView)findViewById(R.id.inventory3);
            inventory3.setOnDragListener(new UpgradeDragListener());


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
        Drawable currentInv1 = upgradeSlot;
        Drawable currentInv2 = upgradeSlot;
        Drawable currentInv3 = upgradeSlot;

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //stuff
                    break;
                //drop space entered, basically an onhover case to set the drop zone green
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackground(getResources().getDrawable(R.drawable.gre_btn));
                    break;
                //drop space exited, revert the view back to whatever it was before
                case DragEvent.ACTION_DRAG_EXITED:
                    if (v == findViewById(R.id.inventory1))
                        v.setBackground(currentInv1);
                    else if (v == findViewById(R.id.inventory2))
                        v.setBackground(currentInv2);
                    else if (v == findViewById(R.id.inventory3))
                        v.setBackground(currentInv3);
                    //View view = (View) event.getLocalState();
                    //v.setBackground(view.getBackground());
                    break;
                case DragEvent.ACTION_DROP:
                    View vie = (View) event.getLocalState();
                    if (v == findViewById(R.id.inventory1) || v == findViewById(R.id.inventory2) || v == findViewById(R.id.inventory3)) {
                        //Log.d("DRAGGED_VIEW",vie.toString());
                        //Log.d("DESTINATION_VIEW","View = " + v.toString());
                        //vie.setVisibility(View.INVISIBLE);",
                        Log.d("BG_Check", "CurrentInv1 = " + currentInv1.toString());
                        Log.d("BG_Check", "CurrentInv2 = " + currentInv2.toString());
                        Log.d("BG_Check", "CurrentInv3 = " + currentInv3.toString());
                        Log.d("ThisBG", "vie.getBackground() = " + vie.getBackground().toString());
                        if (v == findViewById(R.id.inventory1)) {
                            //if (currentInv2 != vie.getBackground() && currentInv3 != vie.getBackground()) {
                                v.setBackground(vie.getBackground());
                                currentInv1 = v.getBackground();
                                v.setTag(vie.getTag());
                            //}
                        } else if (v == findViewById(R.id.inventory2)) {
                            //if (currentInv1 != vie.getBackground() && currentInv3 != vie.getBackground()) {
                                v.setBackground(vie.getBackground());
                                currentInv2 = v.getBackground();
                                v.setTag(vie.getTag());
                            //}
                        } else if (v == findViewById(R.id.inventory3)) {
                            //if (currentInv1 != vie.getBackground() && currentInv2 != vie.getBackground()) {
                                v.setBackground(vie.getBackground());
                                currentInv3 = v.getBackground();
                                v.setTag(vie.getTag());
                            //}
                        }
                        //Log.d("TAG",v.toString() + " tag = " + v.getTag());
                        return true;
                    } else {
                        vie.setVisibility(View.VISIBLE);
                        return false;
                    }
                case DragEvent.ACTION_DRAG_ENDED:
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
