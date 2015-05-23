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

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class FeedItem implements Comparable<FeedItem>, Parcelable {

    @Getter
    @Setter
    protected Feed.FEED_TYPE feedtype = Feed.FEED_TYPE.UNKNOWN;

    @Getter
    @Setter
    protected String id;

    @Getter
    @Setter
    protected String title;

    @Getter
    @Setter
    protected String author;

    @Getter
    @Setter
    protected String name;

    @Getter
    @Setter
    protected String summary;

    @Getter
    @Setter
    protected String content;

    protected Date pubDate;

    protected Date updated;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    protected ArrayList<Link> links;

    public FeedItem() {
        links = new ArrayList<Link>();
    }

    public FeedItem(Parcel source) {

        Bundle data = source.readBundle();
        feedtype = (Feed.FEED_TYPE) data.getSerializable("feedtype");
        id = data.getString("id");
        title = data.getString("title");
        pubDate = (Date) data.getSerializable("pubDate");
        updated = (Date) data.getSerializable("updated");
        description = data.getString("description");
        content = data.getString("content");
        author = data.getString("author");
        name = data.getString("name");
        summary = data.getString("summary");
        links = data.getParcelableArrayList("links");
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Bundle data = new Bundle();
        data.putSerializable("feedtype", feedtype);
        data.putString("id", id);
        data.putString("title", title);
        data.putSerializable("pubDate", pubDate);
        data.putSerializable("updated", updated);
        data.putString("description", description);
        data.putString("content", content);
        data.putString("author", author);
        data.putString("name", name);
        data.putString("summary", summary);
        data.putParcelableArrayList("links", links);
        dest.writeBundle(data);
    }

    public static final Creator<FeedItem> CREATOR = new Creator<FeedItem>() {
        public FeedItem createFromParcel(Parcel data) {
            return new FeedItem(data);
        }

        public FeedItem[] newArray(int size) {
            return new FeedItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = Utils.parseDate(pubDate);
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public void setUpdated(String updated) {
        this.updated = Utils.parseDate(updated);
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getDate() {
        if (feedtype == Feed.FEED_TYPE.RSS)
            return pubDate;
        else if (feedtype == Feed.FEED_TYPE.ATOM)
            return updated;

        return null;
    }

    @Override
    public int compareTo(FeedItem another) {
        if (getDate() != null && another.getDate() != null) {
            return getDate().compareTo(another.getDate());
        } else {
            return 0;
        }
    }

    void addLink(Link feedItemLink) {
        links.add(feedItemLink);
    }

}
