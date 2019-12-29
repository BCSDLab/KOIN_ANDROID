package in.koreatech.koin.core.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import in.koreatech.koin.core.R;

/**
 * Created by hyerim on 2018. 9. 17....
 */
public class GenerateProgressTask extends AsyncTask<Void, Void, Void> {
    private final String TAG = GenerateProgressTask.class.getSimpleName();

    private ProgressDialog mProgressDialog;

    private Context mContext;
    private String mMessage;


    public GenerateProgressTask(Context context, String msg) {
        this.mContext = context;
        this.mMessage = msg;

        mProgressDialog = new ProgressDialog(context, R.style.KAPProgress);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setIndeterminate(true);

    }

    @Override
    protected void onPreExecute() {
        mProgressDialog.setMessage(mMessage);
        mProgressDialog.show();
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
        mProgressDialog.dismiss();
    }

    @Override
    protected void onCancelled(Void aVoid) {
        mProgressDialog.dismiss();
        super.onCancelled(aVoid);

        mProgressDialog = null;
    }
}
