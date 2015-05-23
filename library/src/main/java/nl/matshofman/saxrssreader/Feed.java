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
public class Feed implements Parcelable {

    public enum FEED_TYPE {UNKNOWN, RSS, ATOM}

    @Getter
    @Setter
    protected FEED_TYPE feedtype = FEED_TYPE.UNKNOWN;

    @Getter
    @Setter
    protected String title;

    @Getter
    @Setter
    protected String subtitle;

    @Getter
    @Setter
    protected String id;

    @Getter
    @Setter
    protected String link;

    @Getter
    @Setter
    protected String description;

    @Getter
    @Setter
    private String language;

    @Getter
    protected Date updated;

    @Getter
    @Setter
    protected ArrayList<FeedItem> items;

    public Feed() {
        items = new ArrayList<FeedItem>();
    }

    public Feed(Parcel source) {

        Bundle data = source.readBundle();
        feedtype = (FEED_TYPE) data.getSerializable("feedtype");
        title = data.getString("title");
        link = data.getString("link");
        description = data.getString("description");
        language = data.getString("language");
        updated = (Date) data.getSerializable("updated");
        items = data.getParcelableArrayList("items");

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Bundle data = new Bundle();
        data.putSerializable("feedtype", feedtype);
        data.putString("title", title);
        data.putString("link", link);
        data.putString("description", description);
        data.putString("language", language);
        data.putSerializable("updated", updated);
        data.putParcelableArrayList("items", items);
        dest.writeBundle(data);
    }

    public static final Creator<Feed> CREATOR = new Creator<Feed>() {
        public Feed createFromParcel(Parcel data) {
            return new Feed(data);
        }

        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    void addItem(FeedItem feedItem) {
        items.add(feedItem);
    }

}
