package com.dima.devtest.goeuro;

import com.dima.devtest.goeuro.api.LocationLoader;
import com.dima.devtest.goeuro.api.LocationsRepository;
import com.dima.devtest.goeuro.model.LocationDetails;
import com.dima.devtest.goeuro.service.FilesystemLocationsRepository;
import com.dima.devtest.goeuro.service.RestLocationLoader;

import java.io.IOException;
import java.util.Optional;

/**
 * GoEuro dev test application.
 */
public class GoEuroTest
{
    public static void main( String[] args )
    {
        if (args == null || args.length == 0 || args[0].isEmpty()){
            System.out.println("Usage: GoEuroTest.jar <location_name>");
            return;
        }

        String locationName = args[0];

        LocationLoader loader  = new RestLocationLoader();
        LocationsRepository repository = new FilesystemLocationsRepository();

        Optional<LocationDetails[]> locations = loader.load(locationName);

        if (locations.isPresent()) {
            try {
                repository.save(locationName, locations.get());
            } catch (IOException e) {
                System.err.println("Failed to save location data to file: " + e.getMessage());
            }
        }
    }
}
