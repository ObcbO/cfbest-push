package com.obcbo.cfbest;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.IOException;
import java.net.URI;

public class GetIP {
    static String[] ipv4 = { "https://www.cloudflare.com/ips-v4" };
    static String[] ipv6 = { "https://www.cloudflare.com/ips-v6" };

    public static Set<List<String>> get() {
        Set<List<String>> ips = new HashSet<>(1);
        ips.add(formalV4(download(ipv4)));
        ips.add(formalV6(download(ipv6)));
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
    private static List<String> formalV4(String text) {
        return Arrays.asList(text.replace("/", ":").split("\n"));
    }
    private static List<String> formalV6(String text) {
        String[] line = text.split("\n");// 分割每一行
        List<String> list = new ArrayList<>();
        for (String n : line) {
            String[] split = n.split("/");
            list.add("[" + split[0] + "]:" + split[1]);
        }
        return list;
    }
}