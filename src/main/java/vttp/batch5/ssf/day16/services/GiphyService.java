package vttp.batch5.ssf.day16.services;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.ssf.day16.models.SearchParams;

@Service
public class GiphyService {
    
    public static final String SEARCH_URL = "https://api.giphy.com/v1/gifs/search";

    @Value("${giphy.api.key}")
    private String apiKey;

    // https://api.giphy.com/v1/gifs/search?
    // api_key=abc123&
    // q=polar bear&
    // limit=3&
    // rating=pg
    public List<String> search(SearchParams params){
        
        // build the url with the query parameters
        String url = UriComponentsBuilder
            .fromUriString(SEARCH_URL)
            .queryParam("api_key", apiKey)
            .queryParam("q", params.query())
            .queryParam("limit", params.limit())
            .queryParam("rating", params.rating())
            .toUriString();

        // construct the request
        RequestEntity<Void> req = RequestEntity
            .get(url)
            .accept(MediaType.APPLICATION_JSON)
            .build();

        try{
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> resp = template.exchange(req, String.class);
            
            // get the payload
            String payload = resp.getBody();
            return getImages(payload);

        } catch (Exception ex){
            ex.printStackTrace();
            return List.of();
        }

    }

    private List<String> getImages(String json){
        // create JsonReader
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject result = reader.readObject();
        JsonArray data = result.getJsonArray("data");

        // List<String> images = new LinkedList<>();
        // for (int i = 0; i < data.size(); i ++){
        //     JsonObject imgData = data.getJsonObject(i);
        //     String imageUrl = imgData.getJsonObject("images")
        //         .getJsonObject("fixed_height").getString("url");
        //     images.add(imageUrl);
        // }

        // convert this to using a stream
        List<String> images = data.stream()
            .map(jsonValue -> jsonValue.asJsonObject()
                .getJsonObject("images")
                .getJsonObject("fixed_height")
                .getString("url"))
            .collect(Collectors.toList());

        return images;

    }
}
