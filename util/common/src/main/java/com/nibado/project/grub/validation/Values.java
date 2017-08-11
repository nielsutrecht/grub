package com.nibado.project.grub.validation;


public class Values {
    public static <T> T notNull(final T value) {
        if (value == null) {
            throw new IllegalArgumentException("Can not be null");
        }

        return value;
    }

    public static <T> T notNull(final T value, final String name) {
        if (value == null) {
            throw new IllegalArgumentException(String.format("%s can not be null", name));
        }

        return value;
    }
}
