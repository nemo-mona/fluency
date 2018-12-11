/*
 * Copyright 2018 Mitsunori Komatsu (komamitsu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.komamitsu.fluency.ingester;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.komamitsu.fluency.RetryableException;
import org.komamitsu.fluency.ingester.sender.treasuredata.TreasureDataSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class TreasureDataIngester
        implements Ingester
{
    private static final Logger LOG = LoggerFactory.getLogger(TreasureDataIngester.class);
    private final Config config;
    private final TreasureDataSender sender;

    public TreasureDataIngester(Config config, TreasureDataSender sender)
    {
        this.config = config;
        this.sender = sender;
    }

    @Override
    public void ingest(String tag, ByteBuffer dataBuffer)
            throws IOException
    {
        sender.send(tag, dataBuffer);
    }

    @Override
    public void close()
            throws IOException
    {
    }

    public static class Config
        implements Instantiator<TreasureDataSender>
    {
        @Override
        public TreasureDataIngester createInstance(TreasureDataSender sender)
        {
            return new TreasureDataIngester(this, sender);
        }
    }
}
