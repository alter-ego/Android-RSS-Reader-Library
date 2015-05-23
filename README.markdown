With this library you can easily parse Atom and RSS feeds using the SAX APIs available in Android. There's also an rx-java interface. It was forked and inspired from Android-RSS-Reader-Library by Mats Hofman.

Usage
-----

For an example of how to use this library, please take a look at the `FeedActivity` in the `samplefeedreader` project. Or, just take a look at these two common usages:

*Synchronously*

You can use the API by simply calling `FeedReader.read(boolean enableLogging, URL url)`. This will make the request to the url provided and parse it to `Feed` and `FeedItem` objects. 

Here is an example of how to fetch a RSS feed and iterate through every item:

	URL url = new URL("http://feeds.arstechnica.com/arstechnica/index?format=xml");
	Feed feed = FeedReader.read(false, url);
	
	ArrayList<FeedItem> feedItems = feed.getItems();
	for(FeedItem feedItem : feedItems) {
		Log.i("Atom & RSS Reader", feedItem.getTitle());
	}

*Asynchronously through rx-java*

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
                            Log.i("Atom & RSS Reader", "read feed type = " + feed.getFeedtype() + ", size = " + feed.getItems().size());
                            Log.i("Atom & RSS Reader", "read feed = " + feed.toString());
                        }
                    });

License
-----

Copyright (c) 2015 Alter Ego Srls *(based on Android-RSS-Reader-Library, Copyright (c) 2011 Mats Hofman)*

Licensed under the Apache License, Version 2.0