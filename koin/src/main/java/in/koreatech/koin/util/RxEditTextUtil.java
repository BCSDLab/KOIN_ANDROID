package in.koreatech.koin.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import io.reactivex.Observable;

public class RxEditTextUtil {
    public static Observable<CharSequence> getObservable(EditText editText) {
        return Observable.create(emitter -> editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emitter.onNext(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        }));
    }
}
