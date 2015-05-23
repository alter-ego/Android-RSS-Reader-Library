/*
 * Copyright (C) 2011 Mats Hofman <http://matshofman.nl/contact/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.matshofman.saxrssreader;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import java.lang.reflect.Method;

public class FeedHandler extends DefaultHandler {

    private static final String TAG = "FeedHandler";

    private final boolean mLoggingEnabled;

    private Feed mFeed;

    private Link mFeedLink;

    private FeedItem mFeedItem;

    private Link mFeedItemLink;

    private StringBuilder stringBuilder;

    public FeedHandler(boolean enableLogging) {
        mLoggingEnabled = enableLogging;
    }

    @Override
    public void startDocument() {
        mFeed = new Feed();
    }

    /**
     * Return the parsed Feed with its FeedItems
     */
    public Feed getResult() {
        return mFeed;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        stringBuilder = new StringBuilder();
        if (mLoggingEnabled) {
            Log.d(TAG, "startElement qName = " + qName);
        }

        if (qName.equals("feed") && mFeed != null) {
            mFeed.setFeedtype(Feed.FEED_TYPE.ATOM);
        } else if (qName.equals("rss") && mFeed != null) {
            mFeed.setFeedtype(Feed.FEED_TYPE.RSS);
        } else if (qName.equals("item") && mFeed != null) {
            mFeedItem = new FeedItem();
            mFeedItem.setFeedtype(Feed.FEED_TYPE.RSS);
            mFeed.addItem(mFeedItem);
        } else if (qName.equals("entry") && mFeed != null) {
            mFeedItem = new FeedItem();
            mFeedItem.setFeedtype(Feed.FEED_TYPE.ATOM);
            mFeed.addItem(mFeedItem);
        } else if (qName.equals("link")) {
            //link inside an entry
            if (mFeedItem != null) {
                mFeedItemLink = new Link();
                mFeedItemLink.setHref(attributes.getValue("href"));
                mFeedItemLink.setRel(attributes.getValue("rel"));
                mFeedItem.addLink(mFeedItemLink);
            } else { //link of the feed
                mFeedLink = new Link();
                mFeedLink.setHref(attributes.getValue("href"));
                mFeedLink.setRel(attributes.getValue("rel"));
                mFeed.addLink(mFeedLink);
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        stringBuilder.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (mLoggingEnabled) {
            Log.d(TAG, "endElement qName = " + qName + ", localName = " + localName + ", uri = " + uri);
        }

        if (qName != null && qName.length() > 0) {
            if (mFeed != null && mFeedItem == null) {
                // Parse feed properties
                if (qName.equals("link") && mFeedLink != null) {
                    mFeedLink.setValue(stringBuilder.toString());
                } else {
                    try {
                        String methodName = "set" + qName.substring(0, 1).toUpperCase() + qName.substring(1);
                        Method method = mFeed.getClass().getMethod(methodName, String.class);
                        method.invoke(mFeed, stringBuilder.toString());
                    } catch (Exception e) {
                        if (mLoggingEnabled) {
                            Log.w(TAG, "mFeed exception = " + e.toString());
                        }
                    }
                }

            } else if (mFeedItem != null) {
                // Parse item properties
                if (qName.equals("link") && mFeedItemLink != null) {
                    mFeedItemLink.setValue(stringBuilder.toString());
                } else {
                    try {
                        if (qName.equals("content:encoded")) {
                            qName = "content";
                        }
                        String methodName = "set" + qName.substring(0, 1).toUpperCase() + qName.substring(1);
                        Method method = mFeedItem.getClass().getMethod(methodName, String.class);
                        method.invoke(mFeedItem, stringBuilder.toString());
                    } catch (Exception e) {
                        if (mLoggingEnabled) {
                            Log.w(TAG, "mFeedItem exception = " + e.toString());
                        }
                    }
                }
            }
        }
    }

}
