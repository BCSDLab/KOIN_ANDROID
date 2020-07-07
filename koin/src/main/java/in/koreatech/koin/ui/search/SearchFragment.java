package in.koreatech.koin.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.koreatech.koin.core.activity.WebViewActivity;
import in.koreatech.koin.ui.koinfragment.KoinBaseFragment;
import in.koreatech.koin.ui.main.MainActivity;
import in.koreatech.koin.R;

import in.koreatech.koin.core.appbar.AppBarBase;
import in.koreatech.koin.core.appbar.AppBarSearchBase;
import in.koreatech.koin.data.network.entity.SearchedArticle;
import in.koreatech.koin.data.network.interactor.SearchArticleRestInteractor;
import in.koreatech.koin.ui.search.presenter.SearchArticleContract;
import in.koreatech.koin.ui.search.presenter.SearchArticlePresenter;
import in.koreatech.koin.util.NavigationManger;

import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_ANONYMOUS;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_FREE;
import static in.koreatech.koin.constant.URLConstant.COMMUNITY.ID_RECRUIT;

public class SearchFragment extends KoinBaseFragment implements AppBarSearchBase.SearchTextChange, SearchArticleContract.View, AppBarSearchBase.SearchEditorAction {

    @BindView(R.id.koin_base_appbar_dark)
    AppBarSearchBase appbarSearchBase;
    @BindView(R.id.search_main_framelayout)
    FrameLayout searchMainFrameLayout;


    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private SearchArticlePresenter articleSearchPresenter;
    private String currentText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment_main, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        new SearchArticlePresenter(this, new SearchArticleRestInteractor());
        appbarSearchBase.setOnTextChange(this);
        appbarSearchBase.setSearchEditorAction(this);
        showRecentSearch();
        hideSoftwareKeyBoard();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @OnClick(R.id.koin_base_appbar_dark)
    public void koinBaseAppbarClick(View view) {
        int id = view.getId();
        if (id == AppBarBase.getLeftButtonId()) {
            onBackPressed();
        } else if (id == AppBarBase.getRightButtonId()) {
            searchCurrentText();
        }
    }

    private void changeFragment(Fragment fragment) {
        if (fragment != null) {
            fragmentManager = getChildFragmentManager();
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
        ((MainActivity)getActivity()).showProgressDialog(R.string.loading);
    }

    @Override
    public void hideLoading() {
        ((MainActivity)getActivity()).hideProgressDialog();
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
    public void setPresenter(SearchArticlePresenter presenter) {
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
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(getContext().getApplicationContext().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setSearchText(String text) {
        if (text != null)
            appbarSearchBase.setText(text);
    }

    public void startFragmentByTableId(SearchedArticle searchedArticleItem){
        if (searchedArticleItem == null) return;
        Bundle bundle = new Bundle();
        switch (searchedArticleItem.getTableId()) {
            case 5: // 자유게시판
                bundle.putInt("BOARD_UID", ID_FREE);
                bundle.putInt("ARTICLE_UID", searchedArticleItem.getId());
                NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_free_article_action,bundle, NavigationManger.getNavigationAnimation());
                break;
            case 6: // 자유게시판
                bundle.putInt("BOARD_UID", ID_RECRUIT);
                bundle.putInt("ARTICLE_UID", searchedArticleItem.getId());
                NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_recruit_article_action,bundle, NavigationManger.getNavigationAnimation());
                break;
            case 7: // 취업게시판
                bundle.putInt("BOARD_UID", ID_ANONYMOUS);
                bundle.putInt("ARTICLE_UID", searchedArticleItem.getId());
                NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_anonymous_article_action,bundle, NavigationManger.getNavigationAnimation());
                break;
            case 9: // 분실물
                bundle.putInt("ID", searchedArticleItem.getId());
                NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_lostfound_detail_action,bundle, NavigationManger.getNavigationAnimation());
                break;
            case 10: // 중고장터
                bundle.putInt("ITEM_ID", searchedArticleItem.getId());
                if (searchedArticleItem.getPermalink().contains("buy")) {
                    bundle.putInt("MARKET_ID", 1);
                    NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_market_used_buy_detail_action, bundle,NavigationManger.getNavigationAnimation());
                } else {
                    bundle.putInt("MARKET_ID", 0);
                    NavigationManger.getNavigationController(getActivity()).navigate(R.id.navi_market_used_sell_detail_action, bundle,NavigationManger.getNavigationAnimation());
                }
                break;
            default:
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", searchedArticleItem.getServiceName());
                intent.putExtra("url", searchedArticleItem.getPermalink());
                getActivity().startActivity(intent);
                break;
        }


    }
}