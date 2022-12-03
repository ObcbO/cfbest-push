package com.obcbo.cfbest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Speed {
    static int timeOut = 15000;
    public static String getFastest(List<InetSocketAddress> ips) {
        TreeMap<String, Long> pingMap = new TreeMap<>();
        for (InetSocketAddress n : ips) {
            long delay = getDeplay(n);
            if (!n.equals(0)) {
                pingMap.put(n.toString(), delay);
            }
        }

        Comparator<Map.Entry<String, Integer>> valueComparator = new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2) {
                return o1.getValue() - o2.getValue();
            }
        };
        // map转换成list进行排序
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>();
        // 排序
        Collections.sort(list, valueComparator);

        return pingMap.firstKey();
    }

    private static long getDeplay(InetSocketAddress isa) {
        long delay = 0l;
        String name = isa.getHostString() + ":" + isa.getPort();
        try (Socket socket = new Socket()) {
            long start = System.currentTimeMillis();
            socket.connect(isa, timeOut);
            long end = System.currentTimeMillis();

            delay = end - start;
            System.out.println(name + " - " + delay);
        } catch (IOException e) {
            System.out.println(name + " - " + e.getMessage());
        }
        return delay;
    }
}
