package com.vimalcvs.upgkhindi.activities;

import static com.vimalcvs.upgkhindi.utils.Constant.CLICK_DELAY_TIME;
import static com.vimalcvs.upgkhindi.utils.Constant.FALSE;
import static com.vimalcvs.upgkhindi.utils.Constant.TRUE;
import static com.vimalcvs.upgkhindi.utils.Constant.ZERO;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.vimalcvs.upgkhindi.R;
import com.vimalcvs.upgkhindi.adapters.AdapterCategory;
import com.vimalcvs.upgkhindi.callbacks.CallbackCategory;
import com.vimalcvs.upgkhindi.databinding.ActivityMainBinding;
import com.vimalcvs.upgkhindi.models.Category;
import com.vimalcvs.upgkhindi.rests.ApiInterface;
import com.vimalcvs.upgkhindi.rests.RestAdapter;
import com.vimalcvs.upgkhindi.utils.Constant;
import com.vimalcvs.upgkhindi.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseAppActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AdapterCategory adapterCategory;
    private Call<CallbackCategory> callbackCategoryCall = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);

        recyclerView = findViewById(R.id.recyclerViewCategory);
        recyclerView.setHasFixedSize(TRUE);

        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapterCategory = new AdapterCategory(new ArrayList<>(), this);
        recyclerView.setAdapter(adapterCategory);

        adapterCategory.setOnItemClickListener(obj -> {
            try {
                Intent intent = new Intent(this, ChaptersActivity.class);
                intent.putExtra(Constant.EXTRA_OBJC, obj);
                startActivity(intent);
            } catch (Exception e) {
                Utils.getErrors(e);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this::requestAction);
        requestAction();

        setupToolbar();
    }

    private void setupToolbar() {
        try {
            final MaterialToolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                getSupportActionBar().setTitle("");
            }

        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    private void showAPIResultSubject(final List<Category> subjects) {
        try {
            adapterCategory.clearListData();
            adapterCategory.setListData(subjects);
            swipeProgress(FALSE);
            if (subjects.size() == ZERO) {
                adapterCategory.clearListData();
                showNoItemView(TRUE);
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    private void requestSubjectApi() {
        try {
            ApiInterface apiInterface = RestAdapter.createAPI();
            callbackCategoryCall = apiInterface.getCategory();
            callbackCategoryCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<CallbackCategory> call, @NonNull Response<CallbackCategory> response) {
                    CallbackCategory resp = response.body();
                    if (resp != null && resp.status.equals(Constant.SUCCESS)) {
                        showAPIResultSubject(resp.data);
                    } else {
                        onFailRequest();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CallbackCategory> call, @NonNull Throwable t) {
                    if (!call.isCanceled()) onFailRequest();
                }
            });
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    private void onFailRequest() {
        try {
            adapterCategory.clearListData();
            swipeProgress(FALSE);
            if (Utils.isOnline(mActivity)) {
                showNoItemView(TRUE);
            } else {
                showFailedView(TRUE, getString(R.string.no_internet_text));
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    private void requestAction() {
        try {
            showFailedView(FALSE, "");
            swipeProgress(TRUE);
            showNoItemView(FALSE);
            new Handler().postDelayed(this::requestSubjectApi, Constant.DELAY_TIME);

        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            swipeProgress(FALSE);
            if (callbackCategoryCall != null && callbackCategoryCall.isExecuted()) {
                callbackCategoryCall.cancel();
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    private void showFailedView(boolean flag, String message) {
        try {
            View lyt_failed = findViewById(R.id.lyt_failed_category);
            ((TextView) findViewById(R.id.failedMessage)).setText(message);
            if (flag) {
                recyclerView.setVisibility(View.GONE);
                lyt_failed.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                lyt_failed.setVisibility(View.GONE);
            }
            (findViewById(R.id.layoutNoConnection)).setOnClickListener(view -> new Handler().postDelayed(this::requestAction, CLICK_DELAY_TIME));
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    private void showNoItemView(boolean show) {
        try {
            View lyt_no_item = findViewById(R.id.lyt_no_item_category);
            ((TextView) findViewById(R.id.noItemMessage)).setText(R.string.no_category_found);
            if (show) {
                recyclerView.setVisibility(View.GONE);
                lyt_no_item.setVisibility(View.VISIBLE);

            } else {
                recyclerView.setVisibility(View.VISIBLE);
                lyt_no_item.setVisibility(View.GONE);
            }
            (findViewById(R.id.layoutNoItemFound)).setOnClickListener(view -> new Handler().postDelayed(this::requestAction, CLICK_DELAY_TIME));
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    private void swipeProgress(final boolean show) {
        try {
            if (!show) {
                swipeRefreshLayout.setRefreshing(FALSE);
                return;
            }
            swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(TRUE));
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }


}
