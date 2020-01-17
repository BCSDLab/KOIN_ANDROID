package in.koreatech.koin.core.progressdialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import in.koreatech.koin.core.R;

/**
 * Created by hyerim on 2018. 9. 17....
 */
public class CustomProgressDialog extends AsyncTask<Void, Void, Void> {
    private final String TAG = "CustoprogressDialog";

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
        progressDialog.dismiss();
    }

    @Override
    protected void onCancelled(Void aVoid) {
        progressDialog.dismiss();
        super.onCancelled(aVoid);

        progressDialog = null;
    }
}
