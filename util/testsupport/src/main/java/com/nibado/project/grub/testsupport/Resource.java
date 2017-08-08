package com.nibado.project.grub.testsupport;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Resource {
    private Resource(){}

    public static String read(final String name) {
        InputStream ins =  Resource.class.getResourceAsStream(name);

        if(ins == null) {
            throw new RuntimeException("Resource " + name + " not found.");
        }

        return new BufferedReader(new InputStreamReader(ins))
                .lines().collect(Collectors.joining("\n"));
    }

    public static <T> T deserialize(final String name, Class<T> clazz) {
        return Json.deserialize(read(name), clazz);
    }
}
