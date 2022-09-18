import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
        httpClient.close();
        Answer post = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {
        });
        fileSaver(post.getUrl());
    }

//    public void saveUrl(final String filename, final String urlString)
//            throws MalformedURLException, IOException {
//        BufferedInputStream in = null;
//        FileOutputStream fout = null;
//        try {
//            in = new BufferedInputStream(new URL(urlString).openStream());
//            fout = new FileOutputStream(filename);
//
//            final byte data[] = new byte[1024];
//            int count;
//            while ((count = in.read(data, 0, 1024)) != -1) {
//                fout.write(data, 0, count);
//            }
//        } finally {
//            if (in != null) {
//                in.close();
//            }
//            if (fout != null) {
//                fout.close();
//            }
//        }
//    }

    private static void fileSaver(String url) {
        if(url.isEmpty()) {
            System.out.println("URL is empty: " + url);
        } else {
            String[] parts = url
                    .split("/");
            String fileName = parts[parts.length - 1];
            // we alse need to download picture or video in a buffer?

            File data = new File("C:\\Users\\user\\IdeaProjects\\Request_NASA\\" + fileName);
            try {
                if (data.createNewFile()) {
                    FileWriter writer = new FileWriter(data);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
