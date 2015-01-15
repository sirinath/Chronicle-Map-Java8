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
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;

import static org.junit.Assert.assertEquals;

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

            try (ChronicleMap<Integer, StringBuilder> statelessMap = ChronicleMapBuilder
                    .of(Integer.class, StringBuilder.class, new InetSocketAddress("localhost", 8056)).create()) {
                String actual = statelessMap.getMapped(10, Object::toString);

                assertEquals("Hello World", actual);

                String actual2 = statelessMap.putMapped(10, s -> s.append(" Updated")).toString();

                assertEquals("Hello World Updated", actual2);
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

            String actual = map.getMapped(1, StringBuilder::toString);

            assertEquals(null, actual);

            String actual2 = map.putMapped(1, s -> s.append(" Updated")).toString();

            assertEquals(" Updated", actual2);

            String actual3 = map.getMapped(1, StringBuilder::toString);

            assertEquals(" Updated", actual3);
        }

        try (ChronicleMap<Integer, StringBuilder> serverMap = ChronicleMapBuilder
                .of(Integer.class, StringBuilder.class)
                .defaultValue(new StringBuilder())
                .replication((byte) 2, TcpTransportAndNetworkConfig.of(8056)).create()) {
            serverMap.put(10, new StringBuilder("Hello World"));

            try (ChronicleMap<Integer, StringBuilder> statelessMap = ChronicleMapStatelessClientBuilder
                    .<Integer, StringBuilder>of(new InetSocketAddress("localhost", 8056)).create()) {
                String actual = statelessMap.getMapped(1, StringBuilder::toString);

                assertEquals(null, actual);

                String actual2 = statelessMap.putMapped(1, s -> s.append(" Updated")).toString();

                assertEquals(" Updated", actual2);

                String actual3 = statelessMap.getMapped(1, StringBuilder::toString);

                assertEquals(" Updated", actual3);
            }
        }
    }

}