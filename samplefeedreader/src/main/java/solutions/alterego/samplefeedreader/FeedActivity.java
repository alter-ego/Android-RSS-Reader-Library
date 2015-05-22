package solutions.alterego.samplefeedreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.FeedReader;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;


public class FeedActivity extends AppCompatActivity {

    private static final String TAG = "SampleFeedReader";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        final TextView textView = (TextView) findViewById(R.id.textview);

        try {
            FeedReader.readWithObservable(new URL("http://www.repubblica.it/rss/la-repubblica-delle-idee/genova2015/feed.atom"))
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<RssFeed>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            textView.setText("read feed error = " + e.toString());
                        }

                        @Override
                        public void onNext(RssFeed rssFeed) {
                            textView.setText("read feed size = " + rssFeed.getRssItems().size());
                            Log.i(TAG, "read feed size = " + rssFeed.getRssItems().size());
                            Toast.makeText(FeedActivity.this, "read feed size = " + rssFeed.getRssItems().size(), Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (Exception e) {
            //do nothing
            Log.e(TAG, "error making url = " + e.toString());
        }
    }

}
