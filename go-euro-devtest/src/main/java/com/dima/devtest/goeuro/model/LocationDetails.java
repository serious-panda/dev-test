package com.dima.devtest.goeuro.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO Container for for single location entry.
 * It is a good practice to make such a container immutable,
 * but since the application is single threaded there is no
 * real need for an extra work.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDetails {

    @JsonProperty("_id")
    public long id;

    public String type;

    public String name;

    @JsonProperty("geo_position")
    public GeoPosition geoPosition;

    public final static class GeoPosition {
        public double latitude,longitude;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GeoPosition that = (GeoPosition) o;

            if (Double.compare(that.latitude, latitude) != 0) return false;
            return Double.compare(that.longitude, longitude) == 0;

        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(latitude);
            result = (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(longitude);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }

    public List<String> toList(){
        List<String> lst = new ArrayList<>(5);
        lst.add(Long.toString(id));
        lst.add(type);
        lst.add(name);
        lst.add(Double.toString(geoPosition.latitude));
        lst.add(Double.toString(geoPosition.longitude));
        return lst;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationDetails that = (LocationDetails) o;

        if (id != that.id) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return geoPosition != null ? geoPosition.equals(that.geoPosition) : that.geoPosition == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (geoPosition != null ? geoPosition.hashCode() : 0);
        return result;
    }
}
