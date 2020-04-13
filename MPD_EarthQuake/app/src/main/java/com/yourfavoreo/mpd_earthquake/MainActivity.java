// Student ID: S1803434
// Student Name: Abdulrahman Salum Diwani

package com.yourfavoreo.mpd_earthquake;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class MainActivity extends AppCompatActivity
{
    ArrayList<EarthQuake> earthQuakeList = new ArrayList();
    private TextView rawDataDisplay;
    private String result;
    private Button startButton;
    private String urlSource = "http://quakes.bgs.ac.uk/feeds/WorldSeismology.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isNetworkAvailable()){
            startProgress();
        }
        else {

            ((ProgressBar)findViewById(R.id.loadingIcon)).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.errorText)).setVisibility(View.VISIBLE);
        }

    }

    // Code found at https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager;
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {
            ArrayList<String[]> earthQuakeStrings = new ArrayList();
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            try
            {
                aurl = new URL(url);
                System.out.println(aurl);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                in.readLine();
                in.readLine();

                while ((inputLine = in.readLine()) != null)
                {
                    System.out.println(result);
                    result = result + inputLine;
                    Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", ae.toString());
            }

            XmlPullParserFactory factory = null;
            try {
                factory = XmlPullParserFactory.newInstance();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            factory.setNamespaceAware(false);
            XmlPullParser xpp = null;
            try {
                xpp = factory.newPullParser();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            try {
                System.out.println(result);
                xpp.setInput( new StringReader( result ) );
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            int eventType = 0;
            try {
                eventType = xpp.getEventType();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            Boolean isDescription = false;
            Boolean isItem = false;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    System.out.println("Start document");
                } else if(eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("item")){
                        isItem = true;
                    }
                    if (xpp.getName().equals("description")){
                        isDescription = true;
                    }
                } else if(eventType == XmlPullParser.END_TAG) {
                    if (xpp.getName().equals("item")){
                        isItem = false;
                    }
                    if (xpp.getName().equals("description")){
                        isDescription = false;
                    }
                } else if(eventType == XmlPullParser.TEXT) {
                    if (isDescription && isItem){
                        earthQuakeStrings.add(xpp.getText().split(";"));
                    }
                }

                try {
                    eventType = xpp.next();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("End document");

            for (int i=0;i<earthQuakeStrings.size();i++){
                earthQuakeList.add(new EarthQuake(earthQuakeStrings.get(i)));
            }

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Intent ide = new Intent(MainActivity.this,Dashboard.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("earthquakeList",earthQuakeList);
                    ide.putExtras(bundle);
                    startActivity(ide);
                    finish();
                }
            });
        }

    }

} // End of MainActivity
