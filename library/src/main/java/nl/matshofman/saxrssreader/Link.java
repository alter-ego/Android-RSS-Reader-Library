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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Link implements Parcelable {

    @Getter
    @Setter
    protected String value;

    @Getter
    @Setter
    protected String href;

    @Getter
    @Setter
    protected String rel;


    public Link() {

    }

    public Link(Parcel source) {

        Bundle data = source.readBundle();
        value = data.getString("value");
        href = data.getString("href");
        rel = data.getString("rel");
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Bundle data = new Bundle();
        data.putString("value", value);
        data.putString("href", href);
        data.putString("rel", rel);
        dest.writeBundle(data);
    }

    public static final Creator<Link> CREATOR = new Creator<Link>() {
        public Link createFromParcel(Parcel data) {
            return new Link(data);
        }

        public Link[] newArray(int size) {
            return new Link[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

}
