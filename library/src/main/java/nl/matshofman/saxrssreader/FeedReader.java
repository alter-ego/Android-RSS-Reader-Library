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

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import rx.Observable;
import rx.functions.Func0;

public class FeedReader {

    public static Feed read(boolean enableLogging, URL url) throws SAXException, IOException {

        return read(enableLogging, url.openStream());

    }

    public static Feed read(boolean enableLogging, InputStream stream) throws SAXException, IOException {

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            FeedHandler handler = new FeedHandler(enableLogging);
            InputSource input = new InputSource(stream);

            reader.setContentHandler(handler);
            reader.parse(input);

            return handler.getResult();

        } catch (ParserConfigurationException e) {
            throw new SAXException();
        }

    }

    public static Feed read(boolean enableLogging, String source) throws SAXException, IOException {
        return read(enableLogging, new ByteArrayInputStream(source.getBytes()));
    }

    public static Observable<? extends Feed> readWithObservable(final boolean enableLogging, final URL url) {
        return Observable.defer(new Func0<Observable<Feed>>() {
            @Override
            public Observable<Feed> call() {
                try {
                    return Observable.just(read(enableLogging, url));
                } catch (Exception e) {
                    Log.e("FeedReader", "exception = " + e.toString());
                    return Observable.just(new Feed());
                }
            }
        });
    }

}
