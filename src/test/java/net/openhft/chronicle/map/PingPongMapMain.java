package net.openhft.chronicle.map;

import java.io.File;
import java.io.IOException;

public class PingPongMapMain {
    public static void main(String... ignored) throws IOException {
        File file = new File("shared");
        int keys = 10_000_000;
        try (ChronicleMap<String, MyLong> map = ChronicleMapBuilder
                .of(String.class, MyLong.class)
                .averageValueSize(32)
                .entries(keys)
                .create()) {
//            LongValue lv = DataValueClasses.newDirectReference(LongValue.class);
            long start = System.currentTimeMillis();
            long value = 0;
            if (true) throw new UnsupportedOperationException("Not supported in Map 2.0");
/*
            Mutator<MyLong, Long> mutator = v -> v.addAtomicValue(1);
            for (int j = 0; j < keys; j++) {
                String key = "hello-" + j;
//                for (int i = 0; i < 10; i++) {
                value = map.updateForKey(key, mutator);
//                }
            }
*/
            long time = System.currentTimeMillis() - start;
            System.out.println("value=" + value + " took " + time + " ms");
/*
            try (WriteContext wc = map.acquireUsingLocked("hello", lv)) {
                value = lv.addValue(1);
            }
*/
        }
    }
}

interface MyLong {
    public long getValue();

    public void setValue(long l);

    public long addAtomicValue(long l);
}