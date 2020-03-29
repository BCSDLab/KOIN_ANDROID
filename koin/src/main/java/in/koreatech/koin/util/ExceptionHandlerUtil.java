package in.koreatech.koin.util;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import in.koreatech.koin.ui.error.ErrorActivity;

public class ExceptionHandlerUtil implements Thread.UncaughtExceptionHandler{
    private Context context;
    public static final String EXTRA_ERROR_TEXT = "EXTRA_ERROR_TEXT";
    public ExceptionHandlerUtil(Context context) {
        this.context = context;
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
        throwable.printStackTrace(new PrintWriter((Writer) stringWriter));  //오류 메시지를 얻는다.
        String ErrorMessage = stringWriter.toString();
        if (ErrorMessage != null) {
            startErrorActivity(context, ErrorMessage);
            System.exit(-1);                    //가장 위에 있는 액티비티를 종료 finish()와 같다.
        } else {
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
