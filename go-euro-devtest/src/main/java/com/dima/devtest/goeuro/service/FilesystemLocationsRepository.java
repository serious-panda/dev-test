package com.dima.devtest.goeuro.service;

import com.dima.devtest.goeuro.api.LocationsRepository;
import com.dima.devtest.goeuro.model.LocationDetails;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Filesystem based repository for location data.
 */
public class FilesystemLocationsRepository implements LocationsRepository {

    private final static String SEPARATOR = "; ";
    /**
     * Stores location details in current folder in file named 'location'.csv
     * @param locationName location name
     * @param details location details each entry translated to a csv record
     * @throws IOException
     */
    public Optional<Path> save(String locationName, LocationDetails[] details) throws IOException {
        if (locationName == null || locationName.isEmpty() || details == null){
            return Optional.empty();
        }
        Path path = getPathForLocation(locationName);
        Files.deleteIfExists(path); // to prevent appending the same data.

        // UTF-8 encoding by default
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            Arrays.stream(details)
                    .map(x-> String.join(SEPARATOR, x.toList()))
                    .flatMap(line -> {
                        try {
                            writer.write(String.format("%s%n", line));
                            return null;
                        } catch (IOException ex) {
                            return Stream.of(new RuntimeException(ex));
                        }
                    })
                    // now a stream of thrown exceptions.
                    // can collect them to list or reduce into one exception
                    .reduce((ex1, ex2) -> {
                        ex1.addSuppressed(ex2);
                        return ex1;
                    }).ifPresent(ex -> {throw ex;});
        } // the file will be automatically closed
        return Optional.of(path);
    }

    private Path getPathForLocation(String locationName){
        return Paths.get(locationName + ".csv");
    }
}
