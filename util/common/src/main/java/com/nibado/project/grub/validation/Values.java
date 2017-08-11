package com.nibado.project.grub.validation;


public class Values {
    public static <T> T notNull(final T value) {
        if(value == null) {
            throw new IllegalArgumentException("Can not be null");
        }

        return value;
    }
}
