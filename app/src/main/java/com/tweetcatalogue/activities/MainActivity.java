package com.tweetcatalogue.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.tweetcatalogue.R;
import com.tweetcatalogue.adapters.TweetAdapter;
import com.tweetcatalogue.comunicators.TwitterAPICommunicator;
import com.tweetcatalogue.modelobjects.TweetObject;

import java.util.ArrayList;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;
import twitter4j.Query;
import twitter4j.QueryResult;

public class MainActivity extends AppCompatActivity {

    private RecyclerView tweetRecyclerView;
    private LinearLayoutManager layoutManager;
    private TweetAdapter tweetAdapter;
    private ArrayList<TweetObject> tweetObjects;
    private Query query = null;
    private String searchText = "";

    private static final String QUERY_TAG = "query";
    private static final String TWEETS_TAG = "tweets";
    private static final String SEARCH_TAG = "search";
    private boolean loading;
    private CompositeSubscription subscription = new CompositeSubscription();
    private int itemCount, lastVisibleItem;
    private int visibleThreshold = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutManager = new LinearLayoutManager(this);
        tweetRecyclerView = (RecyclerView) findViewById(R.id.tweetRecyclerView);
        tweetRecyclerView.setLayoutManager(layoutManager);

        if (savedInstanceState != null) {
            this.tweetObjects = savedInstanceState.getParcelableArrayList(TWEETS_TAG);
            this.query = (Query) savedInstanceState.getSerializable(QUERY_TAG);
            this.searchText = savedInstanceState.getString(SEARCH_TAG);
        } else {
            tweetObjects = new ArrayList<>();
        }
        tweetAdapter = new TweetAdapter(tweetObjects, this);
        tweetRecyclerView.setAdapter(tweetAdapter);
        tweetRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tweetRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                itemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!loading && itemCount <= (lastVisibleItem + visibleThreshold)) {
                    loading = true;
                    fetchTweets();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tweetAdapter.destroyDialog();
        subscription.unsubscribe();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(QUERY_TAG, this.query);
        savedInstanceState.putParcelableArrayList(TWEETS_TAG, this.tweetObjects);
        savedInstanceState.putString(SEARCH_TAG, searchText);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setSubmitButtonEnabled(true);
        searchView.setQuery(searchText, true);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search Tweets ..");

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String queryValue) {
                searchText = queryValue;
                tweetObjects.clear();
                tweetAdapter.notifyDataSetChanged();
                query = new Query(queryValue);
                query.setCount(20);
                fetchTweets();
                return true;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);
        return true;
    }

    private void fetchTweets() {
        if (this.query == null) {
            loading = false;
            return;
        }
        subscription.add(TwitterAPICommunicator.getInstance().getTweets(this.query).subscribe(new Subscriber<QueryResult>() {
            @Override
            public void onNext(QueryResult result) {
                if (result != null) {
                    for (twitter4j.Status status : result.getTweets()) {
                        tweetObjects.add(new TweetObject(status.getUser().getScreenName(), status.getText(), status.getUser().getMiniProfileImageURL(), status.getUser().getURL()));
                    }
                    if (result.hasNext()) {
                        query = result.nextQuery();
                    } else {
                        query = null;
                    }
                } else {
                    query = null;
                }

            }

            @Override
            public void onCompleted() {
                loading = false;
                tweetAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                loading = false;
                Toast.makeText(getApplicationContext(), getString(R.string.error_occurred), Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
