package junchi.flare;
//TODO: link panic button to fab buttons
//TODO: Need images, change up the UI

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class PanicButtonFragment extends Fragment implements View.OnClickListener {

    public static final String PANIC_BUTTON = "PanicButton";
    final private int NUMBER = 0;

    ImageButton panicButton;

    private FloatingActionMenu modes;

    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;
    FloatingActionButton locationfab;

    private int basic;
    private int cautious;
    private int danger;
    private int current_mode;

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;

    MyDatabase db;

    public PanicButtonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_panic_button, container, false);
        sharedPrefs = getActivity().getSharedPreferences("junchi.flare_preferences", Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        current_mode = sharedPrefs.getInt("current_mode", NUMBER);
        Log.i("var_current_mode", String.valueOf(current_mode));
        panicButton = (ImageButton) v.findViewById(R.id.panicButton);
        if (current_mode == 1) {
            panicButton.setImageResource(R.drawable.basicbuttonsmall);
        } else if (current_mode == 2){
            panicButton.setImageResource(R.drawable.cautionbuttonsmall);
        } else {
            panicButton.setImageResource(R.drawable.emergencybuttonsmall);

        }
        db = new MyDatabase(getActivity());
        panicButton.setOnClickListener(this);
        modes = (FloatingActionMenu) v.findViewById(R.id.modes);
        fab1 = (FloatingActionButton) v.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) v.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) v.findViewById(R.id.fab3);
        locationfab = (FloatingActionButton) v.findViewById(R.id.locationfab);

        basic = Color.parseColor("#2E3192");
        cautious = Color.parseColor("#FBB040");
        danger = Color.parseColor("#ED1C24");
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        locationfab.setOnClickListener(this);
        modes.setClosedOnTouchOutside(true);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.panicButton:
                CountdownFragment countdownFragment = new CountdownFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                fragmentManager.popBackStack();
                transaction.replace(((ViewGroup) getView().getParent()).getId(), countdownFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                Log.i(PANIC_BUTTON, "panicButton has been pressed.");
                break;


            case R.id.fab1:
                modes.setMenuButtonColorNormal(basic);
                panicButton.setImageResource(R.drawable.basicbuttonsmall);
                Log.i("fab1", "fab1 was pressed");

                editor.putInt("current_mode", 1);
                editor.commit();
                Log.i("current_mode", String.valueOf(sharedPrefs.getInt("current_mode", NUMBER)));
                break;
            case R.id.fab2:
                modes.setMenuButtonColorNormal(cautious);
                panicButton.setImageResource(R.drawable.cautionbuttonsmall);
                Log.i("fab2", "fab2 was pressed");

                editor.putInt("current_mode", 2);
                editor.commit();
                Log.i("current_mode", String.valueOf(sharedPrefs.getInt("current_mode", NUMBER)));
                break;
            case R.id.fab3:
                modes.setMenuButtonColorNormal(danger);
                panicButton.setImageResource(R.drawable.emergencybuttonsmall);
                Log.i("fab3", "fab3 was pressed");

                editor.putInt("current_mode", 3);
                editor.commit();
                Log.i("current_mode", String.valueOf(sharedPrefs.getInt("current_mode", NUMBER)));
                break;
            case R.id.locationfab:

                Intent intent = new Intent(getActivity(), ListActivity.class);
                startActivity(intent);
                Log.i("locationfab", "was clicked");
                break;
        }
    }
}