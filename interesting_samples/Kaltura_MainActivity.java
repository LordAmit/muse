package com.oF2pks.kalturadeviceinfos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.MediaDrm;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.oF2pks.kalturadeviceinfos.Utils.getProp;
import static com.oF2pks.kalturadeviceinfos.Utils.getZinfo;


public class MainActivity extends AppCompatActivity {

    String dataLeAk12 = "12";

	String dataLeAk10 = "10";

	String dataLeAk9 = "9";

	String dataLeAk8 = "8";

	String dataLeAk6 = "6";

	String report;

    private void showReport(String report) {
        TextView reportView = (TextView) findViewById(R.id.textView);
		android.util.Log.d("leak-6-24", dataLeAk6);
		android.util.Log.d("leak-8-24", dataLeAk8);
		android.util.Log.d("leak-9-24", dataLeAk9);
		android.util.Log.d("leak-10-24", dataLeAk10);
		android.util.Log.d("leak-12-24", dataLeAk12);
        assert reportView != null;
        reportView.setText(report);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		android.util.Log.d("leak-6-25", dataLeAk6);
		android.util.Log.d("leak-8-25", dataLeAk8);
		android.util.Log.d("leak-9-25", dataLeAk9);
		android.util.Log.d("leak-10-25", dataLeAk10);
		android.util.Log.d("leak-12-25", dataLeAk12);
		dataLeAk6 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Collect data
        new CollectorTask().execute(false);

        FloatingActionButton fab = findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            
            String dataLeAk7 = "7";

			@Override
            public void onClick(View view) {
                dataLeAk8 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
				dataLeAk7 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
				showActionsDialog();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        dataLeAk9 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
		android.util.Log.d("leak-6-26", dataLeAk6);
		android.util.Log.d("leak-8-26", dataLeAk8);
		android.util.Log.d("leak-9-26", dataLeAk9);
		android.util.Log.d("leak-10-26", dataLeAk10);
		android.util.Log.d("leak-12-26", dataLeAk12);
		// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        dataLeAk10 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
		android.util.Log.d("leak-6-27", dataLeAk6);
		android.util.Log.d("leak-8-27", dataLeAk8);
		android.util.Log.d("leak-9-27", dataLeAk9);
		android.util.Log.d("leak-10-27", dataLeAk10);
		android.util.Log.d("leak-12-27", dataLeAk12);
		// Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            TextView showText = new TextView(this);
            showText.setText(R.string.exoweb);
            showText.setTextIsSelectable(true);
            showText.setAutoLinkMask(Linkify.ALL);
            Linkify.addLinks(showText, Linkify.WEB_URLS);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(showText)
                    .setTitle("About")
                    .setCancelable(true)
                    .setNegativeButton(android.R.string.ok, null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showActionsDialog() {
        final String sTvndk=getProp("ro.vndk.version");
		android.util.Log.d("leak-6-28", dataLeAk6);
		android.util.Log.d("leak-8-28", dataLeAk8);
		android.util.Log.d("leak-9-28", dataLeAk9);
		android.util.Log.d("leak-10-28", dataLeAk10);
		android.util.Log.d("leak-12-28", dataLeAk12);
        String[] actions = {
                "Share...",
                "Refresh",
                "PRIVATE Ids GSF/Serial/Android" ,
                "proc/meminfo",
                "proc/cpuinfo",
                "?/etc/gps.conf",
                "getprop (aio)",
                "df (mounts)",
                "dumpsys media.extractor",
                "Treble linker namespace"+" ("+sTvndk+(getProp("ro.vndk.lite").equals("true")? "lite)":")"),
                "Matrix",
                //"cat /proc/self/mounts"
//                "Refresh with SafetyNet",
                //"(Provision Widevine)"
        };
        String dataLeAk11 = "11";

        new AlertDialog.Builder(this).setTitle("Select action").setItems(actions, new DialogInterface.OnClickListener() {

			@Override
            public void onClick(DialogInterface dialog, int which) {
                dataLeAk12 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
				dataLeAk11 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
				switch (which) {
                    case 0:
                        shareReport();
                        break;
                    case 1:
                        new CollectorTask().execute(false);
                        break;
                    case 2:
//                        new CollectorTask().execute(true);
//                        break;
//                    case 3:
                        showIDs();
                        break;
                    case 3:
                        showZinfo("cat /proc/meminfo",true,false);
                        break;
                    case 4:
                        showZinfo("cat /proc/cpuinfo", false,true);
                        break;
                    case 5:
                        if (new File("/system/etc/gps.conf").exists())
                            showZinfo("cat /system/etc/gps.conf",true,true);
                        else showZinfo("cat /vendor/etc/gps.conf",true,true);
                        break;
                    case 6:
                        showZinfo("getprop" , false,false);
                        break;
                    case 7:
                        showZinfo("df",true,false);
                        break;
                    case 8:
                        showZinfo("dumpsys media.extractor" , false,true);
                        break;
                    case 9:
                        //https://source.android.com/devices/architecture/vndk#vndk-versioning
                        if (getProp("ro.vndk.lite").equals("true"))showZinfo("cat /system/etc/ld.config.vndk_lite.txt",false,false);
                        else showZinfo("cat /system/etc/ld.config"+(sTvndk.equals("") ?"": "."+sTvndk)+".txt"  ,false,false);
                        break;
                    case 10:
                        showZinfo("cat /system/compatibility_matrix.xml" ,false,false);
                        break;
                    case 11:
                        showZinfo("cat /proc/self/mounts",false,false);
                        break;
                    /*case 6:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            new AlertDialog.Builder(MainActivity.this).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startProvision();
                                }
                            }).setNegativeButton("No", null).setMessage("Are you sure you want to attempt Widevine Provisioning?").show();
                        }
                        break;
                    */
                }
            }
        }).show();
    }

    private void showZinfo(String s , boolean b, boolean linky) {
        TextView showText = new TextView(this);
		android.util.Log.d("leak-6-29", dataLeAk6);
		android.util.Log.d("leak-8-29", dataLeAk8);
		android.util.Log.d("leak-9-29", dataLeAk9);
		android.util.Log.d("leak-10-29", dataLeAk10);
		android.util.Log.d("leak-12-29", dataLeAk12);
        showText.setText(getZinfo(s,"\n\u25A0",false));
        showText.setTextIsSelectable(true);
        if (b) {
            Typeface face = Typeface.createFromAsset(getAssets(), "fonts/RobotoMono-Bold.ttf");
            showText.setTypeface(face);
        }
        if (linky) {
            showText.setAutoLinkMask(Linkify.ALL);
            Linkify.addLinks(showText, Linkify.WEB_URLS);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        builder.setView(showText)
                .setTitle(s)
                .setCancelable(true)
                .setNegativeButton(android.R.string.ok, null)
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void startProvision() {
        new ProvisionTask(this).execute();
		android.util.Log.d("leak-6-30", dataLeAk6);
		android.util.Log.d("leak-8-30", dataLeAk8);
		android.util.Log.d("leak-9-30", dataLeAk9);
		android.util.Log.d("leak-10-30", dataLeAk10);
		android.util.Log.d("leak-12-30", dataLeAk12);
    }

    private void provisionFailed(Exception e) {
    }

    private void provisionSuccessful() {
    }

    private void shareReport() {
        String subject = "Kaltura Device Info - Report" + Build.BRAND + "/" + Build.MODEL + "/" + Build.VERSION.RELEASE + "/" + Build.VERSION.SDK_INT;
		android.util.Log.d("leak-6-33", dataLeAk6);
		android.util.Log.d("leak-8-33", dataLeAk8);
		android.util.Log.d("leak-9-33", dataLeAk9);
		android.util.Log.d("leak-10-33", dataLeAk10);
		android.util.Log.d("leak-12-33", dataLeAk12);
        Intent shareIntent = intentWithText(subject, report);
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }


    private Intent intentWithText(String subject, String report) {
        Intent sendIntent = new Intent();
		android.util.Log.d("leak-6-34", dataLeAk6);
		android.util.Log.d("leak-8-34", dataLeAk8);
		android.util.Log.d("leak-9-34", dataLeAk9);
		android.util.Log.d("leak-10-34", dataLeAk10);
		android.util.Log.d("leak-12-34", dataLeAk12);
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, report);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.setType("text/plain");
        return sendIntent;
    }

    private class CollectorTask extends AsyncTask<Boolean, Void, String> {

        @Override
        protected String doInBackground(Boolean... params) {
            return Collector.getReport(MainActivity.this, params[0]);
        }

        @Override
        protected void onPostExecute(String jsonString) {
            report = jsonString;
            showReport(jsonString);
            File output = new File(getExternalFilesDir(null), (Build.VERSION.RELEASE+Build.VERSION.INCREMENTAL+".json").replaceAll(" ",""));
            try {
                FileWriter writer;
                writer = new FileWriter(output);
                writer.write(report);
                writer.close();
                Toast.makeText(MainActivity.this, "Wrote report to " + output, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Failed writing report: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private static class ProvisionTask extends AsyncTask<Context, Void, String> {

        private final Context context;

        public ProvisionTask(Context context) {
            this.context = context;
			android.util.Log.d("leak-11-37", dataLeAk11);
			android.util.Log.d("leak-7-37", dataLeAk7);
			android.util.Log.d("leak-6-37", dataLeAk6);
			android.util.Log.d("leak-8-37", dataLeAk8);
			android.util.Log.d("leak-9-37", dataLeAk9);
			android.util.Log.d("leak-10-37", dataLeAk10);
			android.util.Log.d("leak-12-37", dataLeAk12);
			android.util.Log.d("leak-7-37", dataLeAk7);
			android.util.Log.d("leak-6-37", dataLeAk6);
			android.util.Log.d("leak-8-37", dataLeAk8);
			android.util.Log.d("leak-9-37", dataLeAk9);
			android.util.Log.d("leak-10-37", dataLeAk10);
			android.util.Log.d("leak-12-37", dataLeAk12);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        protected String doInBackground(Context... contexts) {
            try {
                provisionWidevine();
                android.util.Log.d("leak-11-38", dataLeAk11);
                android.util.Log.d("leak-7-38", dataLeAk7);
                android.util.Log.d("leak-6-38", dataLeAk6);
                android.util.Log.d("leak-8-38", dataLeAk8);
                android.util.Log.d("leak-9-38", dataLeAk9);
                android.util.Log.d("leak-10-38", dataLeAk10);
                android.util.Log.d("leak-12-38", dataLeAk12);
                android.util.Log.d("leak-7-38", dataLeAk7);
                return null;
            } catch (Exception e) {
                android.util.Log.d("leak-6-38", dataLeAk6);
                android.util.Log.d("leak-8-38", dataLeAk8);
                android.util.Log.d("leak-9-38", dataLeAk9);
                android.util.Log.d("leak-10-38", dataLeAk10);
                android.util.Log.d("leak-12-38", dataLeAk12);
                return e.toString();
            }


        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                android.util.Log.d("leak-11-39", dataLeAk11);
                android.util.Log.d("leak-7-39", dataLeAk7);
                android.util.Log.d("leak-6-39", dataLeAk6);
                android.util.Log.d("leak-8-39", dataLeAk8);
                android.util.Log.d("leak-9-39", dataLeAk9);
                android.util.Log.d("leak-10-39", dataLeAk10);
                android.util.Log.d("leak-12-39", dataLeAk12);

                Toast.makeText(context, "Provision Successful", Toast.LENGTH_LONG).show();
            } else {
                android.util.Log.d("leak-7-39", dataLeAk7);
                android.util.Log.d("leak-6-39", dataLeAk6);
                android.util.Log.d("leak-8-39", dataLeAk8);
                android.util.Log.d("leak-9-39", dataLeAk9);
                android.util.Log.d("leak-10-39", dataLeAk10);
                android.util.Log.d("leak-12-39", dataLeAk12);

                new AlertDialog.Builder(context).setTitle("Provision Failed").setMessage(s).show();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        private void provisionWidevine() throws Exception {
            MediaDrm mediaDrm = new MediaDrm(Collector.WIDEVINE_UUID);
			android.util.Log.d("leak-11-40", dataLeAk11);
			android.util.Log.d("leak-7-40", dataLeAk7);
			android.util.Log.d("leak-6-40", dataLeAk6);
			android.util.Log.d("leak-8-40", dataLeAk8);
			android.util.Log.d("leak-9-40", dataLeAk9);
			android.util.Log.d("leak-10-40", dataLeAk10);
			android.util.Log.d("leak-12-40", dataLeAk12);
			android.util.Log.d("leak-7-40", dataLeAk7);
			android.util.Log.d("leak-6-40", dataLeAk6);
			android.util.Log.d("leak-8-40", dataLeAk8);
			android.util.Log.d("leak-9-40", dataLeAk9);
			android.util.Log.d("leak-10-40", dataLeAk10);
			android.util.Log.d("leak-12-40", dataLeAk12);
            MediaDrm.ProvisionRequest provisionRequest = mediaDrm.getProvisionRequest();
            String url = provisionRequest.getDefaultUrl() + "&signedRequest=" + new String(provisionRequest.getData());

            // send as empty post
            final HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            int responseCode = con.getResponseCode();
            if (responseCode >= 300) {
                throw new Exception("Bad response code " + responseCode);
            }
            BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int b;
            while ((b = bis.read()) >= 0) {
                baos.write(b);
            }
            bis.close();

            final byte[] response = baos.toByteArray();
            Log.d("RESULT", Base64.encodeToString(response, Base64.NO_WRAP));
            baos.close();
            
            mediaDrm.provideProvisionResponse(response);
            mediaDrm.release();
        }
    }
    private void showIDs() {
        TextView showText = new TextView(this);
		android.util.Log.d("leak-6-41", dataLeAk6);
		android.util.Log.d("leak-8-41", dataLeAk8);
		android.util.Log.d("leak-9-41", dataLeAk9);
		android.util.Log.d("leak-10-41", dataLeAk10);
		android.util.Log.d("leak-12-41", dataLeAk12);
        showText.setText(displayIDs());
        showText.setTextIsSelectable(true);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/RobotoMono-Bold.ttf");
        showText.setTypeface(face);
        showText.setAutoLinkMask(Linkify.ALL);
        Linkify.addLinks(showText, Linkify.WEB_URLS);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(showText)
                .setTitle("(LongPress to select...) PRIVATE Ids")
                .setCancelable(true)
                .setNegativeButton(android.R.string.ok, null)
                .show();
    }

    private String displayIDs() {
        String marshmalow ="\nANDROIDid: "+ Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID)+"\n";
		android.util.Log.d("leak-6-42", dataLeAk6);
		android.util.Log.d("leak-8-42", dataLeAk8);
		android.util.Log.d("leak-9-42", dataLeAk9);
		android.util.Log.d("leak-10-42", dataLeAk10);
		android.util.Log.d("leak-12-42", dataLeAk12);
        if (Build.VERSION.SDK_INT < 28) marshmalow+="SERIALid:  "+Build.SERIAL+"\n";

        Cursor query = getContentResolver().query(Uri.parse("content://com.google.android.gsf.gservices"), null, null, new String[] { "android_id" }, null);
        if (query == null) {
            marshmalow+="GSFid:     unknow\n";
            return marshmalow;
        }
        if (!query.moveToFirst() || query.getColumnCount() < 2 || query.getString(1) == null) {
            marshmalow+="GSFid:     unknow\n";
            if (query.getString(1) == null) marshmalow+="No account, nu gsf...";
            query.close();
            return marshmalow;
        }
        final String toHexString = Long.toHexString(Long.parseLong(query.getString(1)));
        query.close();

        marshmalow+="\nGSFid:     "+ toHexString.toUpperCase().trim()+"\n\n";
        marshmalow+="REGISTER GSF https://www.google.com/android/uncertified\n\n";
        marshmalow+="More info https://www.xda-developers.com/how-to-fix-device-not-certified-by-google-error/";
        return marshmalow;
    }

}
