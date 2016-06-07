package com.dima.devtest.goeuro.api;

import com.dima.devtest.goeuro.model.LocationDetails;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public interface LocationsRepository {

    Optional<Path> save(String location, LocationDetails[] details) throws IOException;

}
