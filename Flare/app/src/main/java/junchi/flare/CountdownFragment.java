package junchi.flare;

import android.Manifest;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class CountdownFragment extends Fragment implements View.OnClickListener, View.OnDragListener, View.OnTouchListener {
    EditText countDown;
    String phoneNumber;
    String phoneNumber2;
    String phoneNumber3;
    boolean isPaused = false;
    boolean textDefault = true;
    boolean flashlightDefault = true;
    long timeRemaining;
    boolean text;
    boolean text2;
    boolean text3;
    boolean flashlight2;
    boolean flashlight3;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    int current_mode;
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE1 = 10;
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 11;
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE3 = 12;
    final int MY_PERMISSIONS_REQUEST_SEND_SMS1 = 1;
    final int MY_PERMISSIONS_REQUEST_SEND_SMS2= 2;
    final int MY_PERMISSIONS_REQUEST_SEND_SMS3 = 3;
    final int MY_PERMISSIONS_REQUEST_STORE_GPS1 = 21;
    final int MY_PERMISSIONS_REQUEST_STORE_GPS2 = 22;
    final int MY_PERMISSIONS_REQUEST_STORE_GPS3 = 23;


    //shared prefs constant
    final int DEFAULTPOS = 0;

    private int NUMBER = 0;
    private String DEFAULT = "";
    Camera mCam;
    SurfaceTexture mPreviewTexture;
    MyDatabase db;



    //color and shared prefs
    int[] colors;
    int[] storedcolors;
    TextView c1, c2, c3, c4, s1, s2, s3, s4;
    Button confirm;

    int value;

    //shared pref


    public CountdownFragment() { /* Required empty public constructor*/ }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_countdown, container, false);
        countDown = (EditText) v.findViewById(R.id.countDownTimer);
        sharedPrefs = getActivity().getSharedPreferences("junchi.flare_preferences", Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
        current_mode = sharedPrefs.getInt("current_mode", NUMBER);
        phoneNumber = sharedPrefs.getString("example_phone_basic", DEFAULT);
        phoneNumber2 = sharedPrefs.getString("example_phone_caution", DEFAULT);
        phoneNumber3 = sharedPrefs.getString("example_phone_emergency", DEFAULT);
        text = sharedPrefs.getBoolean("enable_text", textDefault);
        text2 = sharedPrefs.getBoolean("enable_text2", textDefault);
        text3 = sharedPrefs.getBoolean("enable_text3", textDefault);
        flashlight2 = sharedPrefs.getBoolean("enable_flash2", flashlightDefault);
        flashlight3 = sharedPrefs.getBoolean("enable_flash3", flashlightDefault);
        Log.i("var_current_mode", String.valueOf(current_mode));
        Log.i("phoneNumber", phoneNumber);
        Log.i("phoneNumber2", phoneNumber2);
        Log.i("phoneNumber3", phoneNumber3);
        Log.i("sendSMS", String.valueOf(text));
        Log.i("flashlight2", String.valueOf(flashlight2));
        Log.i("flashlight3", String.valueOf(flashlight3));
        db = new MyDatabase(getActivity());

        c1=(TextView)v.findViewById(R.id.f1);
        c2=(TextView)v.findViewById(R.id.f2);
        c3=(TextView)v.findViewById(R.id.f3);
        c4=(TextView)v.findViewById(R.id.f4);

        s1=(TextView)v.findViewById(R.id.h1);
        s2=(TextView)v.findViewById(R.id.h2);
        s3=(TextView)v.findViewById(R.id.h3);
        s4=(TextView)v.findViewById(R.id.h4);

        c1.setOnTouchListener(this);
        c2.setOnTouchListener(this);
        c3.setOnTouchListener(this);
        c4.setOnTouchListener(this);

        s1.setOnDragListener(this);
        s2.setOnDragListener(this);
        s3.setOnDragListener(this);
        s4.setOnDragListener(this);

        //stored color array
        storedcolors = new int[4];
        storedcolors[0] = sharedPrefs.getInt("Pos0", DEFAULTPOS);
        storedcolors[1] = sharedPrefs.getInt("Pos1", DEFAULTPOS);
        storedcolors[2] = sharedPrefs.getInt("Pos2", DEFAULTPOS);
        storedcolors[3] = sharedPrefs.getInt("Pos3", DEFAULTPOS);

        colors = new int[4];

        Toast.makeText(getActivity(), Arrays.toString(storedcolors), Toast.LENGTH_SHORT).show();



        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (storedcolors[0] == colors[0] && storedcolors[1] == colors[1] && storedcolors[2] == colors[2] && storedcolors[3] == colors[3]) { /*If the user request to cancel or paused the CountDownTimer we will cancel the current instance*/
                    cancel();
                    getActivity().getFragmentManager().popBackStack();
                } else { /*Display the remaining seconds to app interface 1 second = 1000 milliseconds*/
                    countDown.setText("Seconds Remaining: " + millisUntilFinished / 1000); /*Put count down timer remaining time in a variable*/
                    timeRemaining = millisUntilFinished;
                }
            }

            public void onFinish() {
                if (current_mode == 1) {
                    if (text) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS1);
                        } else {
                            //  startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber, null)));
                            sendSMS(phoneNumber, "test");

                        }
                    }

                    countDown.setText("Calling: " + phoneNumber + " now!");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_STORE_GPS1);
                    }

//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE1);
//
//                    } else {
//                        startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber, null)));
//                    }
                }

                if (current_mode == 2) {
                    if (text2) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS2);
                        } else {
//                            startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber2, null)));
                            sendSMS(phoneNumber2, "test2");
                        }
                    }
                    countDown.setText("Calling: " + phoneNumber2 + " now!");

//                    if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
//                        if (flashlight2) {
//                            flashLightOn();
//                        }
//                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_STORE_GPS2);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE2);

                    } else {
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber2, null)));

                    }

                }

                if (current_mode == 3) {
                    if (text3) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS3);

                        } else {
//                            startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber3, null)));
                            sendSMS(phoneNumber3, "test3");
                        }
                    }
                    countDown.setText("Calling: " + phoneNumber3 + " now!");

                    if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                        if (flashlight3) {
                            flashLightOn();
                        }
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_STORE_GPS3);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE3);

                    } else {
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber3, null)));
                    }

                }
//                countDown.setText("Calling: " + phoneNumber + " now!");
            }
        }.start(); /* Inflate the layout for this fragment*/
        return v;
    }

    // changes button text to Resume from Pause to match functionality when button is pressed
    @Override
    public void onStop() {
        super.onStop();
        isPaused = true;
    }

    public void sendSMS(String phoneNo, String msg) {
        Log.i("sendSMS", "called");
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        Log.i("Message", "sent");
    }

    void flashLightOn() {
        mCam = Camera.open();
        Camera.Parameters p = mCam.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        mCam.setParameters(p);
        mPreviewTexture = new SurfaceTexture(0);
        try {
            mCam.setPreviewTexture(mPreviewTexture);
        } catch (IOException ex) {
            // Ignore
        }
        mCam.startPreview();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_CALL_PHONE1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //                    phoneGranted = true;
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber, null)));
//                    startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber2, null)));
//                    startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber3, null)));
                }
            }
            break;

            case MY_PERMISSIONS_REQUEST_CALL_PHONE2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //                    phoneGranted = true;
//                    startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber, null)));
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber2, null)));
//                    startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber3, null)));
                }
            }
            break;

            case MY_PERMISSIONS_REQUEST_CALL_PHONE3: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //                    phoneGranted = true;
//                    startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber, null)));
//                    startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber2, null)));
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber3, null)));

                }
            }
            break;

            case MY_PERMISSIONS_REQUEST_SEND_SMS1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMS(phoneNumber, "test");
//                    sendSMS(phoneNumber2, "testC");
//                    sendSMS(phoneNumber3, "testE");


                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
//                break;
            }
            break;


            case MY_PERMISSIONS_REQUEST_SEND_SMS2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    sendSMS(phoneNumber, "test");
                    sendSMS(phoneNumber2, "testC");
//                    sendSMS(phoneNumber3, "testE");


                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
//                break;
            }
            break;


            case MY_PERMISSIONS_REQUEST_SEND_SMS3: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    sendSMS(phoneNumber, "test");
//                    sendSMS(phoneNumber2, "testC");
                    sendSMS(phoneNumber3, "testE");


                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
//                break;
            }
            break;

            case MY_PERMISSIONS_REQUEST_STORE_GPS1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location recentLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Log.i("Location", String.valueOf(recentLoc));

                    Calendar c = Calendar.getInstance();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd 'T:' HH:mmZ");
                    String now = df.format(new Date());

                    db.insertData(now, String.valueOf(recentLoc));
                }
            }
            break;

            case MY_PERMISSIONS_REQUEST_STORE_GPS2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location recentLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Log.i("Location", String.valueOf(recentLoc));

                    Calendar c = Calendar.getInstance();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd 'T:' HH:mmZ");
                    String now = df.format(new Date());

                    db.insertData(now, String.valueOf(recentLoc));
                }
            }
            break;

            case MY_PERMISSIONS_REQUEST_STORE_GPS3: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Double latitude, longitude;
                    latitude = 0.0;
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }

                    Log.i("Location", String.valueOf(latitude));
//                    Location recentLoc = locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


//                    Calendar c = Calendar.getInstance();
//                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd 'T:' HH:mmZ");
//                    String now = df.format(new Date());
//                    Log.i("asdf", "asdflkj");
//                    db.insertData(now, String.valueOf(recentLoc));

//                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//                    Location recentLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                    Log.i("Location", recentLoc.toString());
//
//                    Calendar c = Calendar.getInstance();
//                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd 'T:' HH:mmZ");
//                    String now = df.format(new Date());
//
//                    db.insertData(now, recentLoc.toString());
                }
            }
            break;

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onClick(View v) {


//        switch (v.getId()) {
//            case R.id.pauseResumeButton:
//                if (!isPaused) {
//                    isPaused = true;
//                    pauseResumeButton.setText("Resume");
//                } else {
//                    isPaused = false;
//                    pauseResumeButton.setText("Pause");
//                    //Initialize a new CountDownTimer instance
//                    long millisInFuture = timeRemaining;
//                    long countDownInterval = 1000;
//                    new CountDownTimer(millisInFuture, countDownInterval) {
//                        public void onTick(long millisUntilFinished) {
//                            //Do something in every tick
//                            if (isPaused) {
//                                //If user requested to pause or cancel the count down timer
//                                cancel();
//                            } else {
//                                //Display the remaining seconds to app interface
//                                //1 second = 1000 milliseconds
//                                countDown.setText("seconds remaining: " + millisUntilFinished / 1000);
//                                //Put count down timer remaining time in a variable
//                                timeRemaining = millisUntilFinished;
//                            }
//                        }
//
//                        public void onFinish() {
//                            if (current_mode == 1) {
//                                if (text) {
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
//                                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS1);
//                                    } else {
////                            startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber, null)));
//                                        sendSMS(phoneNumber, "test");
//                                    }
//                                } else {
////                        Log.i("sendSMS", "is false");
//                                }
//
//                                countDown.setText("Calling: " + phoneNumber + " now!");
//
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE1);
//
//                                } else {
//                                    startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber, null)));
//
//                                }
//                            }
//
//                            if (current_mode == 2) {
//                                if (text2) {
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
//                                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS2);
//                                    } else {
////                            startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber2, null)));
//                                        sendSMS(phoneNumber2, "test2");
//                                    }
//                                }
//                                countDown.setText("Calling: " + phoneNumber2 + " now!");
//
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE2);
//
//                                } else {
//                                    startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber2, null)));
//
//                                }
//
//                            }
//
//                            if (current_mode == 3) {
//                                if (text3) {
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
//                                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS3);
//
//                                    } else {
////                            startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber3, null)));
//                                        sendSMS(phoneNumber3, "test3");
//                                    }
//                                }
//                                countDown.setText("Calling: " + phoneNumber3 + " now!");
//
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE3);
//
//                                } else {
//                                    startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber3, null)));
//                                }
//
//                            }
////                            countDown.setText("Calling: " + phoneNumber3 + " now!");
//                        }
//                    }.start();
//                }
//                break;
//        }
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

                if (view.getId() == R.id.h1) {
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

                    Toast.makeText(getActivity(), Arrays.toString(colors) , Toast.LENGTH_SHORT).show();
                }

                if (view.getId() == R.id.h2) {
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

                    Toast.makeText(getActivity(), Arrays.toString(colors) , Toast.LENGTH_SHORT).show();
                }

                if (view.getId() == R.id.h3) {
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

                    Toast.makeText(getActivity(), Arrays.toString(colors) , Toast.LENGTH_SHORT).show();
                }
                if (view.getId() == R.id.h4) {
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

                    Toast.makeText(getActivity(), Arrays.toString(colors) , Toast.LENGTH_SHORT).show();
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
            case R.id.f1:
                value = 1;
                break;
            case R.id.f2:
                value = 2;
                break;
            case R.id.f3:
                value = 3;
                break;
            case R.id.f4:
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
}
