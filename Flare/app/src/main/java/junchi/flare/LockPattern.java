package junchi.flare;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;


/**
 * Created by Eivan on 2016-08-03.
 */
public class LockPattern extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener, View.OnClickListener {

    int[] colors;
    TextView c1, c2, c3, c4, s1, s2, s3, s4;
    Button confirm;

    int value;

    //shared pref
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "LockPattern: on Create!", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.acticity_setlock);

        c1=(TextView)findViewById(R.id.t1);
        c2=(TextView)findViewById(R.id.t2);
        c3=(TextView)findViewById(R.id.t3);
        c4=(TextView)findViewById(R.id.t4);

        s1=(TextView)findViewById(R.id.b1);
        s2=(TextView)findViewById(R.id.b2);
        s3=(TextView)findViewById(R.id.b3);
        s4=(TextView)findViewById(R.id.b4);

        c1.setOnTouchListener(this);
        c2.setOnTouchListener(this);
        c3.setOnTouchListener(this);
        c4.setOnTouchListener(this);

        s1.setOnDragListener(this);
        s2.setOnDragListener(this);
        s3.setOnDragListener(this);
        s4.setOnDragListener(this);

        confirm = (Button)findViewById(R.id.setPatternButton);
        confirm.setOnClickListener(this);

        //array
        colors = new int[4];

        //shared prefs
        sharedPrefs = this.getSharedPreferences("junchi.flare_preferences", Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();


    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        switch(dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:

                View v = (View)dragEvent.getLocalState();
                TextView dropTarget = (TextView) v;
                TextView dropped = (TextView) view;

                if (view.getId() == R.id.b1) {
                    if (value == 1) {
                        int c = Color.parseColor("#ff352e");
                        colors[0]=value;
                        dropped.setBackgroundColor(c);
                    }
                    if (value == 2) {
                        colors[0]=value;
                        int c = Color.parseColor("#00c000");
                        dropped.setBackgroundColor(c);
                    }
                    if (value == 3) {
                        colors[0]=value;
                        int c = Color.parseColor("#243fff");
                        dropped.setBackgroundColor(c);
                    }
                    if (value == 4) {
                        colors[0]=value;
                        int c = Color.parseColor("#ffeb00");
                        dropped.setBackgroundColor(c);
                    }

                    Toast.makeText(this, Arrays.toString(colors) , Toast.LENGTH_SHORT).show();
                }

                if (view.getId() == R.id.b2) {
                    if (value == 1) {
                        int c = Color.parseColor("#ff352e");
                        colors[1]=value;
                        dropped.setBackgroundColor(c);
                    }
                    if (value == 2) {
                        colors[1]=value;
                        int c = Color.parseColor("#00c000");
                        dropped.setBackgroundColor(c);
                    }
                    if (value == 3) {
                        colors[1]=value;
                        int c = Color.parseColor("#243fff");
                        dropped.setBackgroundColor(c);
                    }
                    if (value == 4) {
                        colors[1]=value;
                        int c = Color.parseColor("#ffeb00");
                        dropped.setBackgroundColor(c);
                    }
                    Toast.makeText(this, Arrays.toString(colors) , Toast.LENGTH_SHORT).show();
                }

                if (view.getId() == R.id.b3) {
                    if (value == 1) {
                        int c = Color.parseColor("#ff352e");
                        colors[2]=value;
                        dropped.setBackgroundColor(c);
                    }
                    if (value == 2) {
                        colors[2]=value;
                        int c = Color.parseColor("#00c000");
                        dropped.setBackgroundColor(c);
                    }
                    if (value == 3) {
                        colors[2]=value;
                        int c = Color.parseColor("#243fff");
                        dropped.setBackgroundColor(c);
                    }
                    if (value == 4) {
                        colors[2]=value;
                        int c = Color.parseColor("#ffeb00");
                        dropped.setBackgroundColor(c);
                    }
                    Toast.makeText(this, Arrays.toString(colors) , Toast.LENGTH_SHORT).show();
                }
                if (view.getId() == R.id.b4) {
                    if (value == 1) {
                        int c = Color.parseColor("#ff352e");
                        colors[3]=value;
                        dropped.setBackgroundColor(c);
                    }
                    if (value == 2) {
                        colors[3]=value;
                        int c = Color.parseColor("#00c000");
                        dropped.setBackgroundColor(c);
                    }
                    if (value == 3) {
                        colors[3]=value;
                        int c = Color.parseColor("#243fff");
                        dropped.setBackgroundColor(c);
                    }
                    if (value == 4) {
                        colors[3]=value;
                        int c = Color.parseColor("#ffeb00");
                        dropped.setBackgroundColor(c);
                    }
                    Toast.makeText(this, Arrays.toString(colors) , Toast.LENGTH_SHORT).show();
                }

                break;
            case DragEvent.ACTION_DRAG_ENDED:
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(view.getId()) {
            case R.id.t1:
                value = 1;
                break;
            case R.id.t2:
                value = 2;
                break;
            case R.id.t3:
                value = 3;
                break;
            case R.id.t4:
                value = 4;
                break;

        }

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            //start dragging item
            view.startDrag(data, shadowBuilder, view, 0);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.setPatternButton) {
            editor.putInt("Pos0", colors[0]);
            editor.putInt("Pos1", colors[1]);
            editor.putInt("Pos2", colors[2]);
            editor.putInt("Pos3", colors[3]);

            editor.commit();
            finish();
            startActivity(getIntent());
        }
    }
}
