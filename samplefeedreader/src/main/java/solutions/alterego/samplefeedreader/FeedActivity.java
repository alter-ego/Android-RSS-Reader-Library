package solutions.alterego.samplefeedreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.net.URL;

import nl.matshofman.saxrssreader.Feed;
import nl.matshofman.saxrssreader.FeedReader;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class FeedActivity extends AppCompatActivity {

    private static final String TAG = "SampleFeedReader";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        final TextView textView = (TextView) findViewById(R.id.textview);

        try {
            FeedReader.readWithObservable(new URL("http://www.repubblica.it/rss/la-repubblica-delle-idee/genova2015/feed.atom"))
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
        } catch (Exception e) {
            //do nothing
            Log.e(TAG, "error making url = " + e.toString());
        }
    }

}
