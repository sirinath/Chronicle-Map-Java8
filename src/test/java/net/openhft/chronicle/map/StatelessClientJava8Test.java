/*
 * Copyright 2014 Higher Frequency Trading
 *
 * http://www.higherfrequencytrading.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.openhft.chronicle.map;

import net.openhft.chronicle.hash.replication.TcpTransportAndNetworkConfig;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Rob Austin.
 */
public class StatelessClientJava8Test {

    @Test
    public void testMapForKeyLambda() throws IOException, InterruptedException {

        try (ChronicleMap<Integer, StringBuilder> serverMap = ChronicleMapBuilder.of(Integer.class,
                StringBuilder.class)
                .replication((byte) 2, TcpTransportAndNetworkConfig.of(8056)).create()) {
            serverMap.put(10, new StringBuilder("Hello World"));

            try (ChronicleMap<Integer, StringBuilder> statelessMap = ChronicleMapBuilder.of(Integer
                    .class, StringBuilder.class)
                    .statelessClient(new InetSocketAddress("localhost", 8056)).create()) {
                String actual = statelessMap.mapForKey(10, Object::toString);

                Assert.assertEquals("Hello World", actual);

                String actual2 = statelessMap.updateForKey(10, s -> s + " Updated");

                Assert.assertEquals("Hello World Updated", actual2);
            }
        }
    }

    @Test
    public void testMapForKeyLambdaUnknownEntry() throws IOException, InterruptedException {

        try (ChronicleMap<Integer, StringBuilder> map = ChronicleMapBuilder
                .of(Integer.class, StringBuilder.class)
                .defaultValue(new StringBuilder())
                .create()) {
            map.put(10, new StringBuilder("Hello World"));

            String actual = map.mapForKey(1, StringBuilder::toString);

            Assert.assertEquals(null, actual);

            String actual2 = map.updateForKey(1, s -> {
                s.append(" Updated");
                return s.toString();
            });

            Assert.assertEquals(" Updated", actual2);

            String actual3 = map.mapForKey(1, StringBuilder::toString);

            Assert.assertEquals(" Updated", actual3);
        }

        try (ChronicleMap<Integer, StringBuilder> serverMap = ChronicleMapBuilder
                .of(Integer.class, StringBuilder.class)
                .defaultValue(new StringBuilder())
                .replication((byte) 2, TcpTransportAndNetworkConfig.of(8056)).create()) {
            serverMap.put(10, new StringBuilder("Hello World"));

            try (ChronicleMap<Integer, StringBuilder> statelessMap = ChronicleMapBuilder.of(Integer
                    .class, StringBuilder.class)
                    .statelessClient(new InetSocketAddress("localhost", 8056)).create()) {
                String actual = statelessMap.mapForKey(1, StringBuilder::toString);

                Assert.assertEquals(null, actual);

                String actual2 = statelessMap.updateForKey(1, s -> {
                    s.append(" Updated");
                    return s.toString();
                });

                Assert.assertEquals(" Updated", actual2);

                String actual3 = statelessMap.mapForKey(1, StringBuilder::toString);

                Assert.assertEquals(" Updated", actual3);
            }
        }
    }

}