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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FeedHandler extends DefaultHandler {

    private Feed mFeed;

    private FeedItem mFeedItem;

    private StringBuilder stringBuilder;

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
        Log.d("FeedHandler", "startElement qName = " + qName);

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
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        stringBuilder.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {

        if (mFeed != null && mFeedItem == null) {
            // Parse feed properties

            try {
                if (qName != null && qName.length() > 0) {
                    String methodName = "set" + qName.substring(0, 1).toUpperCase() + qName.substring(1);
                    Method method = mFeed.getClass().getMethod(methodName, String.class);
                    method.invoke(mFeed, stringBuilder.toString());
                }
            } catch (SecurityException e) {
            } catch (NoSuchMethodException e) {
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }

        } else if (mFeedItem != null) {
            // Parse item properties

            try {
                if (qName.equals("content:encoded")) {
                    qName = "content";
                }
                String methodName = "set" + qName.substring(0, 1).toUpperCase() + qName.substring(1);
                Method method = mFeedItem.getClass().getMethod(methodName, String.class);
                method.invoke(mFeedItem, stringBuilder.toString());
            } catch (SecurityException e) {
            } catch (NoSuchMethodException e) {
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }

    }

}
