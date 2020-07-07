package in.koreatech.koin.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import in.koreatech.koin.R;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;

public class SearchNoResultFragment extends KoinBaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.search_noresult_fragment, container , false);
        return view;
    }
}
