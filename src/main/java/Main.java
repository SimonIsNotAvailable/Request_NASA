import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

public class Main {
    private static final String NASA_SOURCE = "https://api.nasa.gov/planetary/apod?api_key=QYjPmA3rmtTqnP5oYWd5K4b1hMPbvdGvahSTO5Q3";
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        Answer post = mapper.readValue(nasaRequest(NASA_SOURCE)
                .getEntity()
                .getContent(), new TypeReference<>() {
        });
        System.out.println(post);
        fileSaver(post.getUrl(), post);
    }

    private static CloseableHttpResponse nasaRequest(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        CloseableHttpResponse response = httpClient.execute(request);
        return response;
    }


    private static void fileSaver(String url, Answer post) throws IOException {
        if (url.isEmpty()) {
            System.out.println("URL is empty: " + url);
        } else {
            String[] parts = url
                    .split("/");
            String fileName = parts[parts.length - 1];
            byte[] bytes = nasaRequest(url).getEntity().getContent().readAllBytes();
            File file = new File("C:\\Users\\user\\IdeaProjects\\Request_NASA\\resources\\" + fileName);
            try {
                if (file.createNewFile()) {
                    try (OutputStream outputStream =
                                 new BufferedOutputStream(new FileOutputStream(file))) {
                        outputStream.write(bytes);
                         System.out.println(post.getTitle() + "has been saved");
                    }
                } else if (file.exists()){
                    System.out.println("File \"" + fileName +"\" already exists in the directory");
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}