package com.example.tourplannerbackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


@Service
public class OpenRouteService {

    private static final Logger logger = LoggerFactory.getLogger(OpenRouteService.class);

    @Value("${openroute.api.key}")
    private String apiKey;


    public double[] getCoordinates(String location){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject("https://api.openrouteservice.org/geocode/search?api_key=" + apiKey + "&text=" + location, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(result);

            double x, y;
            x = root.get("features").get(0).get("geometry").get("coordinates").get(0).asDouble();
            y = root.get("features").get(0).get("geometry").get("coordinates").get(1).asDouble();

            return new double[]{x,y};
        }
        catch (Exception e){
            logger.error("Error getting coordinates", e);
            return null;
        }
    }

    public double[] getRouteInfo(String from, String to, String transportType){

        try{
            double[] fromCoords = getCoordinates(from);
            double[] toCoords = getCoordinates(to);

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            String body = "{ \"coordinates\": [[" + fromCoords[0] + "," + fromCoords[1] + "],[" + toCoords[0] + "," + toCoords[1] + "]" + "]}";

            HttpEntity<String> entity = new HttpEntity<>(body,headers);
            String result = restTemplate.postForObject("https://api.openrouteservice.org/v2/directions/" + transportType + "/json", entity, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(result);
            JsonNode summary = root.get("routes").get(0).get("summary");

            double distance = summary.get("distance").asDouble();
            double duration = summary.get("duration").asDouble();

            return new double[]{distance,duration};
        }
        catch (Exception e){
            logger.error("No route availabe for this coordinates", e);
            return null;
        }

    }

}

