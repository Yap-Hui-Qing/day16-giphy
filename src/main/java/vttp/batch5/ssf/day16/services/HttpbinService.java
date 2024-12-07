package vttp.batch5.ssf.day16.services;

import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class HttpbinService {

    // Inject the API key from the properties file
    @Value("${giphy.api.key}")
    private String apiKey;

    // https://api.giphy.com/v1/gifs/search
    public static final String SEARCH_URL = "https://api.giphy.com/v1/gifs/search";

    // build the URL with the query parameters
    public List<String> getWithQueryParams(MultiValueMap<String, String> form){
        String url = UriComponentsBuilder
            .fromUriString(SEARCH_URL)
            .queryParam("api_key", apiKey)
            .queryParam("q", form.getFirst("query"))
            .queryParam("limit", form.getFirst("limit"))
            .queryParam("rating", form.getFirst("rating"))
            .toUriString();

        System.out.printf("URL with query params: \n\t%s\n", url);

        // construct the request
        RequestEntity<Void> req = RequestEntity
            .get(url)
            .accept(MediaType.APPLICATION_JSON)
            .build();

        // create REST template
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp;

        try{
            // make the call
            resp = template.exchange(req, String.class);

            // extract the payload
            String payload = resp.getBody();

            return getImages(payload);

        } catch (Exception ex){
            ex.printStackTrace();
            return List.of();
        } 
    }

    private List<String> getImages(String json){
        // create JsonBuilder
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject result = reader.readObject();
        JsonArray data = result.getJsonArray("data");

        // List<String> urlList = new LinkedList<>();
        // for (int i = 0; i < data.size(); i ++){
        //     String imageURL = data.getJsonObject(i)
        //         .getJsonObject("images")
        //         .getJsonObject("fixed_height")
        //         .getString("url");
        //     urlList.add(imageURL);
        // }

        // convert to use stream
        List<String> urlList = data.stream()
            .map(jsonValue -> jsonValue.asJsonObject()
                    .getJsonObject("images")
                    .getJsonObject("fixed_height")
                    .getString("url"))
            .collect(Collectors.toList());
            
        return urlList;

    }
    
    
}
