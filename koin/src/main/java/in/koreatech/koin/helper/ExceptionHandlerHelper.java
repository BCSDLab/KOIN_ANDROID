package in.koreatech.koin.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.database.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import in.koreatech.koin.ui.ErrorActivity;

public class ExceptionHandlerHelper implements Thread.UncaughtExceptionHandler {
    private Context context;
    private final Thread.UncaughtExceptionHandler defaultExceptionHandler;
    public static final String EXTRA_ERROR_TEXT = "EXTRA_ERROR_TEXT";

    public ExceptionHandlerHelper(Context context, Thread.UncaughtExceptionHandler defaultExceptionHandler) {
        this.context = context;
        this.defaultExceptionHandler = defaultExceptionHandler;
    }

    /***
     * UncaughtException을 캐치하여 처리하는 함수
     * Error message가 있다면 ErrorActivity로 이동
     * @param thread
     * @param throwable
     */
    @Override
    public void uncaughtException(@Nullable Thread thread, @NotNull Throwable throwable) {

        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter((Writer) stringWriter));
        String ErrorMessage = stringWriter.toString();
        if (ErrorMessage != null) {
            startErrorActivity(context, ErrorMessage);
            Process.killProcess(Process.myPid());
            System.exit(-1);
        }else{
            //this.defaultExceptionHandler.uncaughtException(thread, throwable);
            Process.killProcess(Process.myPid());
            System.exit(-1);
        }


    }

    /***
     * ErrorActivity로 이동하는 함수
     * Error message도 전달
     * @param context
     * @param ErrorMessage
     */
    private final void startErrorActivity(Context context, String ErrorMessage) {
        Intent goToErrorActivityIntent = new Intent(context.getApplicationContext(), ErrorActivity.class);
        goToErrorActivityIntent.putExtra(EXTRA_ERROR_TEXT, ErrorMessage);
        goToErrorActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(goToErrorActivityIntent);
    }

}
