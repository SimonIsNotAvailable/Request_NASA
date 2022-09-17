import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    private static final String NASA_SOURCE = "https://api.nasa.gov/planetary/apod?api_key=QYjPmA3rmtTqnP5oYWd5K4b1hMPbvdGvahSTO5Q3";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        HttpGet request = new HttpGet(NASA_SOURCE);
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        CloseableHttpResponse response = httpClient.execute(request);

        String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
        System.out.println(body);

        Answer post = mapper.readValue(response.getEntity().getContent(), new TypeReference<Answer>() {

        });

//        List<Answer> posts = mapper.readValue(
//                response.getEntity().getContent(),
//                new TypeReference<List<Answer>>() {
//                });
//
//        posts.forEach(System.out::println);
        System.out.println(post);
        httpClient.close();
    }

}
