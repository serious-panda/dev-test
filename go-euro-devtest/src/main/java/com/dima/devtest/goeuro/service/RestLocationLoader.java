package com.dima.devtest.goeuro.service;

import com.dima.devtest.goeuro.model.LocationDetails;
import com.dima.devtest.goeuro.api.LocationLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpHost;

import java.io.IOException;
import java.util.Optional;

/**
 * Loads location data from RESTfull API.
 * It uses Unirest for send ing REST requests despite it's
 * a singleton with weird 'shutdown' method. Probably not the
 * best choice for complex multi-threaded server, but it is
 * very convenient for single threaded, one-run application.
 */
public class RestLocationLoader implements LocationLoader {

    private final static int HTTP_STATUS_OK = 200;

    public Optional<LocationDetails[]> load(final String location){
        init();
        Optional<LocationDetails[]> ret = restGetLocationDetails(location);
        shutdown();
        return ret;
    }

    private void init(){

        // One time operation
        Unirest.setObjectMapper(new ObjectMapper() {

            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String s, Class<T> aClass) {
                T ret = null;
                try {
                    ret = mapper.readValue(s,aClass);
                } catch (IOException e) {
                    System.err.println("JSON Parsing error: " + e.getMessage());
                }
                return ret;
            }

            public String writeValue(Object o) {

                try {
                    return mapper.writeValueAsString(o);
                } catch (JsonProcessingException e) {
                    System.err.println("JSON writing error: " + e.getMessage());
                }
                return null;
            }
        });

        Unirest.clearDefaultHeaders();
        Unirest.setProxy(HttpHost.create("http://10.184.220.176:911"));
    }

    private void shutdown(){
        try {
            Unirest.shutdown();
        } catch (IOException e){
            System.err.println("Failed to properly shutdown the REST client: " + e.getMessage());
        }
    }

    private Optional<LocationDetails[]> restGetLocationDetails(final String location){
        Optional<LocationDetails[]> locations = Optional.empty();
        try {
            HttpResponse<LocationDetails[]> apiResponse =
                    Unirest
                    .get("http://api.goeuro.com/api/v2/position/suggest/en/{location}")
                    .routeParam("location", location)
                    .asObject(LocationDetails[].class);

            if (apiResponse.getStatus() == HTTP_STATUS_OK){
                // jackson mapper may return null if something goes wrong
                locations = Optional.ofNullable(apiResponse.getBody());
            } else {
                System.err.println(
                        String.format("Filed to retrieve location data: %d - %s",
                                apiResponse.getStatus(), apiResponse.getStatusText()));
            }

        } catch (UnirestException e) {
            System.err.println("Unable to retrieve location data: " + e.getMessage());
        }
        return locations;
    }
}

