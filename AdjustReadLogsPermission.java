// code for http://forum.xda-developers.com/showthread.php?p=43035431

package enter.your.package.name.here;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import android.content.Intent;
import eu.chainfire.libsuperuser.Shell;

public class AdjustReadLogsPermission extends AsyncTask<Void, Void, Void> {
    private ProgressDialog dialog = null;
    private Activity activity = null;

    public static void adjustIfNeeded(Activity activity) {
           if(activity.getPackageManager().checkPermission(android.Manifest.permission.READ_LOGS, activity.getPackageName())!=android.content.pm.PackageManager.PERMISSION_GRANTED) {
               if(!activity.getIntent().hasExtra(activity.getPackageName()+".PERMISSIONS_ADJUSTED")) {
                   (new AdjustReadLogsPermission()).setActivity(activity).execute();
               } else {
                   Toast.makeText(activity, "Failed to adjust permissions.\nLog most likely will be INCOMPLETE!", Toast.LENGTH_LONG).show();
                   Log.e(activity.getTitle().toString(), "Failed to adjust permissions. Log most likely will be INCOMPLETE!");
                   Log.e(activity.getTitle().toString(), "Starting from Android 4.1 full logcat is available for applications only by using root features.");
               }
           }
    }

    private AdjustReadLogsPermission setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(activity);
        dialog.setMessage("Adjusting permissions...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try { Thread.sleep(1000); } catch(Exception e) { }
        Shell.SU.run("pm grant "+activity.getPackageName()+" "+android.Manifest.permission.READ_LOGS);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Intent nIntent = activity.getIntent();
        nIntent.putExtra(activity.getPackageName()+".PERMISSIONS_ADJUSTED", true);
        PendingIntent pIntent = PendingIntent.getActivity(activity, 0, nIntent, 0);
        ((AlarmManager)activity.getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.RTC, System.currentTimeMillis() + 1000, pIntent);
        System.exit(0);
    }        
}
