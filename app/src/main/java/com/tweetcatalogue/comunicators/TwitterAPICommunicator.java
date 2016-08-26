package com.tweetcatalogue.comunicators;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by vraman on 8/22/16.
 */
public class TwitterAPICommunicator {
    private volatile static TwitterAPICommunicator instance = null;
    private Twitter twitter;
    private static final String CONSUMER_KEY = "TXNThLpTKH20Px4X8uhtRhH9i";
    private static final String CONSUMER_SECRET = "mJhYxMwXk3K0SrDwbJfJTFyn5RqNbS68hRg9lMxCuOi8xdTEAF";
    private static final String ACCESS_TOKEN = "535568126-P2lq4XxxM9dqF0weo6JIW7mOmTItPzGHpcWDAXUS";
    private static final String ACCESS_TOKEN_SECRET = "TuMu2lU5IeW9rp8aDO23R7E2aE6ckqIlmdslTtHAj6hG6";

    private TwitterAPICommunicator() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
        twitter = new TwitterFactory(configurationBuilder.build()).getInstance();
    }

    public static TwitterAPICommunicator getInstance() {
        if (instance == null) {
            synchronized (TwitterAPICommunicator.class) {
                if (instance == null) {
                    instance = new TwitterAPICommunicator();
                }
            }
        }
        return instance;
    }


    public Observable<QueryResult> getTweets(final Query query) {

        return Observable.create(new Observable.OnSubscribe<QueryResult>() {
            @Override
            public void call(Subscriber<? super QueryResult> subscriber) {
                subscriber.onNext(getTweetResult(query));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private QueryResult getTweetResult(Query query) {
        try {
            return  twitter.search(query);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
