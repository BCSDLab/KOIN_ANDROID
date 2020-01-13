package in.koreatech.koin.ui.search;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity;
import in.koreatech.koin.R;

import in.koreatech.koin.core.appbar.AppbarBase;
import in.koreatech.koin.core.appbar.AppbarSearchBase;
import in.koreatech.koin.data.network.entity.SearchedArticle;
import in.koreatech.koin.data.network.interactor.SearchArticleRestInteractor;
import in.koreatech.koin.ui.search.presenter.SearchArticleContract;
import in.koreatech.koin.ui.search.presenter.SeachArticlePresenter;

/**
 * Created by seongyun on 2019. 11. 16....
 */
public class SearchActivity extends KoinNavigationDrawerActivity implements AppbarSearchBase.SearchTextChange, SearchArticleContract.View, AppbarSearchBase.SearchEditorAction {

    @BindView(R.id.koin_base_appbar_dark)
    AppbarSearchBase appbarSearchBase;
    @BindView(R.id.search_main_framelayout)
    FrameLayout searchMainFrameLayout;


    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private SearchArticleContract.Presenter articleSearchPresenter;
    private String currentText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        new SeachArticlePresenter(this, new SearchArticleRestInteractor());
        appbarSearchBase.setOnTextChange(this);
        appbarSearchBase.setSearchEditorAction(this);
        showRecentSearch();
        hideSoftwareKeyBoard();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @OnClick(R.id.koin_base_appbar_dark)
    public void koinBaseAppbarClick(View view) {
        int id = view.getId();
        if (id == AppbarBase.getLeftButtonId()) {
            onBackPressed();
        } else if (id == AppbarBase.getRightButtonId()) {
            searchCurrentText();
        }
    }

    private void changeFragment(Fragment fragment) {
        if (fragment != null) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.search_main_framelayout, fragment).commit();
        }
    }

    @Override
    public void getString(String text) {
        currentText = text;
        if (currentText.isEmpty())
            showRecentSearch();
    }

    @Override
    public void showLoading() {
        showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    @Override
    public void showEmptyItem() {
        Fragment fragment = new SearchNoResultFragment();
        changeFragment(fragment);
    }

    @Override
    public void showSearchedArticle(SearchedArticle searchedArticle) {
        Fragment fragment = new SearchResultFragment();
        Bundle item = new Bundle();
        item.putSerializable("SEARCH_ITEMS", searchedArticle);
        item.putString("CURRENT_TEXT", currentText);
        fragment.setArguments(item);
        changeFragment(fragment);
    }

    @Override
    public void showRecentSearch() {
        Fragment fragment = new SearchRecentFragment();
        changeFragment(fragment);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void setPresenter(SearchArticleContract.Presenter presenter) {
        this.articleSearchPresenter = presenter;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEARCH:
                searchCurrentText();
                break;
        }
        return false;
    }

    public void searchCurrentText() {
        if (articleSearchPresenter != null && currentText != null) {
            articleSearchPresenter.getArticleSearched(currentText, 1);
            articleSearchPresenter.saveText(currentText);
            hideSoftwareKeyBoard();
        }
    }

    public void hideSoftwareKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setSearchText(String text) {
        if (text != null)
            appbarSearchBase.setText(text);
    }
}