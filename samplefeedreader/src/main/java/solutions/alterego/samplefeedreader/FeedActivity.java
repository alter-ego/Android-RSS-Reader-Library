package solutions.alterego.samplefeedreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import nl.matshofman.saxrssreader.Feed;
import nl.matshofman.saxrssreader.FeedItem;
import nl.matshofman.saxrssreader.FeedReader;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func3;
import rx.schedulers.Schedulers;


public class FeedActivity extends AppCompatActivity {

    private static final String TAG = "SampleFeedReader";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        final TextView textView = (TextView) findViewById(R.id.textview);
        final TextView textView2 = (TextView) findViewById(R.id.textview2);

        try {
            FeedReader.readWithObservable(true, new URL("http://feeds.arstechnica.com/arstechnica/index?format=xml"))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Feed>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            textView.setText("read feed error = " + e.toString());
                        }

                        @Override
                        public void onNext(Feed feed) {
                            textView.setText("read feed type = " + feed.getFeedtype() + ", size = " + feed.getItems().size());
                            Log.i(TAG, "read feed type = " + feed.getFeedtype() + ", size = " + feed.getItems().size());
                            Log.i(TAG, "read feed = " + feed.toString());
                        }
                    });

            Observable.zip(FeedReader.readWithObservable(false, new URL("http://www.repubblica.it/rss/la-repubblica-delle-idee/genova2015/feed.atom")),
                    FeedReader.readWithObservable(false, new URL("http://www.repubblica.it/rss/la-repubblica-delle-idee/genova2015/other/feed.atom")),
                    FeedReader.readWithObservable(false, new URL("http://feeds.arstechnica.com/arstechnica/index?format=xml")),
                    new Func3<Feed, Feed, Feed, List<FeedItem>>() {
                        @Override
                        public List<FeedItem> call(Feed feed, Feed feed2, Feed feed3) {
                            List<FeedItem> allFeedItems = new ArrayList<FeedItem>();
                            Log.i(TAG, "read feed sizes, feed  = " + feed.getItems().size() + ", feed 2 = " + feed2.getItems().size() + ", feed 3 = "
                                    + feed3.getItems().size());
                            allFeedItems.addAll(feed.getItems());
                            allFeedItems.addAll(feed2.getItems());
                            allFeedItems.addAll(feed3.getItems());
                            Collections.sort(allFeedItems);
                            return allFeedItems;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<List<FeedItem>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            textView.setText("read feed error = " + e.toString());
                        }

                        @Override
                        public void onNext(List<FeedItem> feed) {
                            List<FeedItem> dedupeFeed = new ArrayList<>(new LinkedHashSet<>(feed));
                            textView2.setText("read feed items size = " + feed.size() + ", deduped size = " + dedupeFeed.size());
                            Log.i(TAG, "read feed size = " + feed.size() + ", deduped size = " + dedupeFeed.size());
                        }
                    });
        } catch (Exception e) {
            //do nothing
            Log.e(TAG, "error making url = " + e.toString());
        }
    }

}
