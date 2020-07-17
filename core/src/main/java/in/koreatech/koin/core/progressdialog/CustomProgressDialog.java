package in.koreatech.koin.core.progressdialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import in.koreatech.koin.core.R;

public class CustomProgressDialog extends AsyncTask<Void, Void, Void> {
    private final String TAG = "CustomProgressDialog";
    private LottieProgressDialog lottieProgressDialog;


    public CustomProgressDialog(Context context) {
        lottieProgressDialog = new LottieProgressDialog(context);
        lottieProgressDialog.setCancelable(false);
        lottieProgressDialog.setCanceledOnTouchOutside(false);

    }

    @Override
    protected void onPreExecute() {
        lottieProgressDialog.show();

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
        if (lottieProgressDialog != null)
            lottieProgressDialog.dismiss();

    }

    @Override
    protected void onCancelled(Void aVoid) {
        if (lottieProgressDialog != null)
            lottieProgressDialog.dismiss();
        super.onCancelled(aVoid);

        lottieProgressDialog = null;

    }
}
