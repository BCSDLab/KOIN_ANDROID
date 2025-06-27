package in.koreatech.koin.core.progressdialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import in.koreatech.koin.core.R;

public class CustomProgressDialog extends AsyncTask<Void, Void, Void> {
    private final String TAG = "CustomProgressDialog";

    private WeakReference<Activity> activityRef;
    private ProgressDialog progressDialog;
    private String message;

    public CustomProgressDialog(Context context, String msg) {
        if (context instanceof Activity) {
            this.activityRef = new WeakReference<>((Activity) context);
        } else {
            throw new IllegalArgumentException("Context must be an Activity");
        }
        this.message = msg;
    }

    @Override
    protected void onPreExecute() {
        Activity activity = activityRef.get();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            progressDialog = new ProgressDialog(activity, R.style.KAPProgress);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        while (!isCancelled()) {
            try {
                Thread.sleep(10); // isCancelled() 검사 속도
            } catch (InterruptedException e) {
                break;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        dismissDialog();
    }

    @Override
    protected void onCancelled(Void aVoid) {
        dismissDialog();
        super.onCancelled(aVoid);
    }

    private void dismissDialog() {
        Activity activity = activityRef.get();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed() && progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }
}