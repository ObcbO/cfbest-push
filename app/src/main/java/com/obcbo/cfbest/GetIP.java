package com.obcbo.cfbest;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.net.URI;

public class GetIP {
    static String[] urls = { "https://www.cloudflare.com/ips-v4","https://www.cloudflare.com/ips-v6" };

    public static String[] get() {
        return formal(download());
    }

    private static String download() {
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
    private static String[] formal(String text) {
        return text.split("\n");
    }
}