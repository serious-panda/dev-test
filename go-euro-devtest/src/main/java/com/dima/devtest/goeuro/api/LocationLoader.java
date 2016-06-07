package com.dima.devtest.goeuro.api;

import com.dima.devtest.goeuro.model.LocationDetails;

import java.util.Optional;

public interface LocationLoader {

    Optional<LocationDetails[]> load(String location);

}
