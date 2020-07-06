package in.koreatech.koin.core.progressdialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import in.koreatech.koin.core.R;

public class CustomProgressDialog extends AsyncTask<Void, Void, Void> {
    private final String TAG = "CustomProgressDialog";

    private ProgressDialog progressDialog;
    private String message;


    public CustomProgressDialog(Context context, String msg) {
        this.message = msg;
        progressDialog = new ProgressDialog(context, R.style.KAPProgress);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);

    }

    @Override
    protected void onPreExecute() {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        while (true) {
            if (isCancelled()) {
                break;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    protected void onCancelled(Void aVoid) {
        if (progressDialog != null)
            progressDialog.dismiss();
        super.onCancelled(aVoid);

        progressDialog = null;
    }
}
