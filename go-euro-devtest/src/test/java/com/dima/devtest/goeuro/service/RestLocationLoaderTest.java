package com.dima.devtest.goeuro.service;

import com.dima.devtest.goeuro.model.LocationDetails;
import com.mashape.unirest.http.Unirest;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Optional;

/**
 * Test performed here should be actually performed at integration test
 * phase, not module level. However it would be an overkill for such a
 * small utility tool. If this project evolve to something bigger
 * HTTP client should be wrapped with adapter and all local using
 * classes will be tested against its' mock as module level.
 */
public class RestLocationLoaderTest extends TestCase {

    public void testLoad() throws Exception {
        String name = "Berlin";
        RestLocationLoader loader = new RestLocationLoader();

        Optional<LocationDetails[]> result = loader.load(name);
        String expected = name.toLowerCase();
        assertTrue(result.isPresent());
        assertTrue(Arrays.stream(result.get())
                    .allMatch(x->x.name.toLowerCase().contains(expected)));
    }

}