package com.vimalcvs.upgkhindi.activities;

import static com.vimalcvs.upgkhindi.utils.Constant.CLICK_DELAY_TIME;
import static com.vimalcvs.upgkhindi.utils.Constant.DEFAULT_PAGE_NO;
import static com.vimalcvs.upgkhindi.utils.Constant.FALSE;
import static com.vimalcvs.upgkhindi.utils.Constant.TRUE;
import static com.vimalcvs.upgkhindi.utils.Constant.ZERO;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.vimalcvs.upgkhindi.Config;
import com.vimalcvs.upgkhindi.R;
import com.vimalcvs.upgkhindi.adapters.AdapterChapter;
import com.vimalcvs.upgkhindi.callbacks.CallbackChapter;
import com.vimalcvs.upgkhindi.models.Category;
import com.vimalcvs.upgkhindi.models.Chapter;
import com.vimalcvs.upgkhindi.rests.ApiInterface;
import com.vimalcvs.upgkhindi.rests.RestAdapter;
import com.vimalcvs.upgkhindi.utils.Constant;
import com.vimalcvs.upgkhindi.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChaptersActivity extends BaseAppActivity {

    private RecyclerView recyclerView;
    private AdapterChapter adapterChapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Call<CallbackChapter> callbackChapterCall = null;
    private int post_total = 0;
    private int failed_page = 0;
    private Category category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        category = (Category) getIntent().getSerializableExtra(Constant.EXTRA_OBJC);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);

        refreshRecyclerView();


        swipeRefreshLayout.setOnRefreshListener(() -> {
            try {
                if (callbackChapterCall != null && callbackChapterCall.isExecuted()) {
                    callbackChapterCall.cancel();
                }
                requestAction(DEFAULT_PAGE_NO);
            } catch (Exception e) {
                Utils.getErrors(e);
            }
        });
        requestAction(DEFAULT_PAGE_NO);
        setupToolbar();
    }

    private void refreshRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(TRUE);
        adapterChapter = new AdapterChapter(recyclerView, new ArrayList<>());
        recyclerView.setAdapter(adapterChapter);

        adapterChapter.setOnLoadMoreListener(current_page -> {
            try {
                if (post_total > adapterChapter.getItemCount() && current_page != 0) {
                    int next_page = current_page + 1;
                    showProgress();
                    requestAction(next_page);
                } else {
                    adapterChapter.setLoaded();
                }
            } catch (Exception e) {
                Utils.getErrors(e);
            }
        });

        adapterChapter.setOnItemClickListener(obj -> {
            try {
                Intent intent = new Intent(this, PdfViewActivity.class);
                intent.putExtra(Constant.EXTRA_OBJC, obj);
                startActivity(intent);
            } catch (Exception e) {
                Utils.getErrors(e);
            }
        });
    }

    private void setupToolbar() {
        try {
            final MaterialToolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(TRUE);
                getSupportActionBar().setHomeButtonEnabled(TRUE);
                getSupportActionBar().setTitle(category.title);
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    private void showAPIResult(final List<Chapter> questions, int page_no) {
        try {
            if (page_no == DEFAULT_PAGE_NO) {
                adapterChapter.clearListData();
            }
            if (page_no > DEFAULT_PAGE_NO) {
                hideProgress();
            }
            adapterChapter.addItemsData(questions);
            swipeProgress(FALSE);
            if (questions.size() == ZERO) {
                if (page_no == DEFAULT_PAGE_NO) {
                    adapterChapter.clearListData();
                }
                showNoItemView(TRUE);
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    private void callAPIRequest(final int page_no) {
        try {
            ApiInterface apiInterface = RestAdapter.createAPI();
            callbackChapterCall = apiInterface.getChapter(category.category, page_no, Config.LOAD_MORE);
            callbackChapterCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<CallbackChapter> call, @NonNull Response<CallbackChapter> response) {
                    CallbackChapter resp = response.body();
                    if (resp != null && resp.status.equals(Constant.SUCCESS)) {
                        post_total = resp.count_total;
                        showAPIResult(resp.data, page_no);
                    } else {
                        onFailRequest(page_no);
                    }
                }


                @Override
                public void onFailure(@NonNull Call<CallbackChapter> call, @NonNull Throwable t) {
                    if (!call.isCanceled()) onFailRequest(page_no);
                }

            });
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    private void onFailRequest(int page_no) {
        try {
            if (page_no > DEFAULT_PAGE_NO) {
                hideProgress();
            }
            failed_page = page_no;
            adapterChapter.setLoaded();
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

    private void requestAction(final int page_no) {
        try {
            if (page_no == DEFAULT_PAGE_NO) {
                refreshRecyclerView();
            }
            showFailedView(FALSE, "");
            showNoItemView(FALSE);
            if (page_no == DEFAULT_PAGE_NO) {
                swipeProgress(TRUE);
            } else {
                adapterChapter.setLoading();
            }
            new Handler().postDelayed(() -> callAPIRequest(page_no), Constant.DELAY_TIME);
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    private void showFailedView(boolean show, String message) {
        try {
            View view = findViewById(R.id.layoutFailed);
            ((TextView) findViewById(R.id.failedMessage)).setText(message);
            if (show) {
                recyclerView.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
            }
            (findViewById(R.id.layoutNoConnection)).setOnClickListener(view1 -> new Handler().postDelayed(() -> {
                if (failed_page == DEFAULT_PAGE_NO) {
                    adapterChapter.clearListData();
                }
                requestAction(failed_page);
            }, CLICK_DELAY_TIME));
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }


    private void showNoItemView(boolean show) {
        try {
            View view = findViewById(R.id.layoutNoItem);
            ((TextView) findViewById(R.id.noItemMessage)).setText(R.string.no_post_found);
            if (show) {
                recyclerView.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
            }
            (findViewById(R.id.failed_retry)).setOnClickListener(view1 -> {
                if (failed_page == DEFAULT_PAGE_NO) {
                    adapterChapter.clearListData();
                }
                requestAction(failed_page);
            });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            swipeProgress(FALSE);
            if (callbackChapterCall != null && callbackChapterCall.isExecuted()) {
                callbackChapterCall.cancel();
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }


}
