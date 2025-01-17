/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shenyu.sync.data.etcd;

import com.google.protobuf.ByteString;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.Watch;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.options.WatchOption;
import org.apache.shenyu.common.exception.ShenyuException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * test case for {@link EtcdClient}.
 */
@RunWith(MockitoJUnitRunner.class)
public class EtcdClientTest {

    private static final String KEY1 = "/foo/key1";

    private static final String KEY2 = "/foo/key2";

    private static final String VALUE = "value";

    private static final String GET_KEY = "getKey";

    private static final String PREFIX = "/foo";

    private static final String SEPARATOR = "/";

    private static final String EXPECTED1 = "key1";

    private static final String EXPECTED2 = "key2";

    private static final String WATCH_DATA_CHANGE_KEY = "watchDataChange";

    private static final String WATCH_CHILD_CHANGE_KEY = "WatchChildChange";

    @InjectMocks
    private EtcdClient etcdClient;

    @Mock
    private Client client;

    @Mock
    private Watch.Watcher watcher;

    @Before
    public void setUp() {
        KV kv = mock(KV.class);
        final CompletableFuture<GetResponse> future = mock(CompletableFuture.class);
        GetResponse getResponse = mock(GetResponse.class);
        List<KeyValue> keyValues = new ArrayList<>(2);
        KeyValue keyValue1 = mock(KeyValue.class);
        KeyValue keyValue2 = mock(KeyValue.class);
        keyValues.add(keyValue1);
        keyValues.add(keyValue2);
        final ByteString key1 = ByteString.copyFromUtf8(KEY1);
        final ByteString key2 = ByteString.copyFromUtf8(KEY2);
        final ByteString value1 = ByteString.copyFromUtf8(VALUE);

        /**
         *  mock get method.
         */
        when(client.getKVClient()).thenReturn(kv);
        when(kv.get(any())).thenReturn(future);
        try {
            when(future.get()).thenReturn(getResponse);
        } catch (Exception e) {
            throw new ShenyuException(e.getCause());
        }
        when(getResponse.getKvs()).thenReturn(keyValues);
        when(keyValue1.getValue()).thenReturn(ByteSequence.from(value1));
        /**
         * mock getChildrenKeys method.
         */
        when(kv.get(any(), any())).thenReturn(future);
        when(keyValue1.getKey()).thenReturn(ByteSequence.from(key1));
        when(keyValue2.getKey()).thenReturn(ByteSequence.from(key2));
        /**
         * mock watchDataChange method.
         */
        Watch watch = mock(Watch.class);
        when(client.getWatchClient()).thenReturn(watch);
        when(watch.watch(any(ByteSequence.class), any(Watch.Listener.class))).thenReturn(watcher);
        /**
         * mock watchChildChange method.
         */
        when(watch.watch(any(ByteSequence.class), any(WatchOption.class), any(Watch.Listener.class))).thenReturn(watcher);
    }

    @Test
    public void testGet() {
        String result = etcdClient.get(GET_KEY);
        assertEquals(VALUE, result);
    }

    @Test
    public void testGetChildrenKeys() throws Exception {
        List<String> result = etcdClient.getChildrenKeys(PREFIX, SEPARATOR);
        List<String> expected = new ArrayList<>();
        expected.add(EXPECTED1);
        expected.add(EXPECTED2);
        assertEquals(expected, result);
    }

    @Test
    public void testWatchDataChange() {
        BiConsumer<String, String> updateHandler = mock(BiConsumer.class);
        Consumer<String> deleteHandler = mock(Consumer.class);
        etcdClient.watchDataChange(WATCH_DATA_CHANGE_KEY, updateHandler, deleteHandler);
        etcdClient.watchClose(WATCH_DATA_CHANGE_KEY);
        verify(watcher).close();
    }
    
    @Test
    public void testWatchChildChange() {
        BiConsumer<String, String> updateHandler = mock(BiConsumer.class);
        Consumer<String> deleteHandler = mock(Consumer.class);
        etcdClient.watchChildChange(WATCH_CHILD_CHANGE_KEY, updateHandler, deleteHandler);
        etcdClient.watchClose(WATCH_CHILD_CHANGE_KEY);
        verify(watcher).close();
    }

}
