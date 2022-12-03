package com.obcbo.cfbest;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

public class GetIP {
    static String[] ipv4 = { "https://www.cloudflare.com/ips-v4" };
    static String[] ipv6 = { "https://www.cloudflare.com/ips-v6" };

    public static List<List<InetSocketAddress>> get() {
        List<List<InetSocketAddress>> ips = new ArrayList<>(1);
        ips.add(formal(download(ipv4)));
        ips.add(formal(download(ipv6)));
        return ips;
    }

    private static String download(String[] urls) {
        StringBuilder sb = new StringBuilder();
        for (String url : urls) {
            now: try {
                sb.append(doGet(url)).append("\n");
            } catch (IOException | InterruptedException e) {
                break now;
            }
        }
        return sb.toString();
    }

    /**
     * GET获取内容
     * @param url
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private static String doGet(String url) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        var client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    
    /**
     * 格式化文本
     * @param text
     * @return
     */
    private static List<InetSocketAddress> formal(String text) {
        String[] line = text.split("\n");// 分割每一行
        List<InetSocketAddress> list = new ArrayList<>();
        for (String n : line) {
            String[] split = n.split("/");
            list.add(new InetSocketAddress(split[0], Integer.parseInt(split[1])));
        }
        return list;
    }
}