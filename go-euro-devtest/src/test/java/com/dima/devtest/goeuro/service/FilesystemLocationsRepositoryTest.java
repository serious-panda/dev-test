package com.dima.devtest.goeuro.service;

import com.dima.devtest.goeuro.model.LocationDetails;
import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class FilesystemLocationsRepositoryTest extends TestCase {

    private String testLocation = "test_location";
    private Optional<Path> path;
    private FilesystemLocationsRepository repo;

    public void setUp() throws Exception {
        super.setUp();
        repo = new FilesystemLocationsRepository();
    }

    public void tearDown() throws Exception {
        path.ifPresent(x->{try {Files.deleteIfExists(x);} catch (IOException e) {/*do nothing*/}});
    }

    public void testNull() throws Exception {
        path = repo.save(testLocation, null);
        assertFalse(path.isPresent());
    }

    public void testEmpty() throws Exception {
        LocationDetails[] details = new LocationDetails[]{};
        path = repo.save(testLocation, details);
        assertTrue(path.isPresent());
        Optional<LocationDetails[]> loaded = load(path.get());
        assertTrue(loaded.isPresent());
        assertEquals(0,loaded.get().length);
    }

    public void testSingleLine() throws Exception {
        LocationDetails[] details = new LocationDetails[]{generateLocationDetails()};
        path = repo.save(testLocation, details);
        assertTrue(path.isPresent());
        Optional<LocationDetails[]> loaded = load(path.get());
        assertTrue(loaded.isPresent());
        assertEquals(1,loaded.get().length);
        assertEquals(details[0],loaded.get()[0]);
    }

    private LocationDetails generateLocationDetails(){
        LocationDetails details = new LocationDetails();
        details.id = 111;
        details.name = "name";
        details.type = "type";
        LocationDetails.GeoPosition pos = new LocationDetails.GeoPosition();
        pos.latitude = 1.1;
        pos.longitude = 2.2;
        details.geoPosition = pos;
        return details;
    }

    private Optional<LocationDetails[]> load(final Path path) throws IOException {

        Optional<LocationDetails[]> details = Optional.empty();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            details = Optional.of(
                    reader.lines()
                            .map(line -> line.split("; "))
                            .map(this::parse)
                            .toArray(LocationDetails[]::new));
        }
        return details;
    }

    private LocationDetails parse(String[] values){
        LocationDetails details = new LocationDetails();
        details.id = Long.parseLong(values[0]);
        details.type = values[1];
        details.name = values[2];
        LocationDetails.GeoPosition pos = new LocationDetails.GeoPosition();
        pos.latitude = Double.parseDouble(values[3]);
        pos.longitude = Double.parseDouble(values[4]);
        details.geoPosition = pos;
        return details;
    }

}