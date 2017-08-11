package com.nibado.project.grub.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Dates {
    public static LocalDate toLocalDate(final Date date) {
        return LocalDate.from(date.toInstant());
    }

    public static LocalDate toLocalDate(final java.sql.Date date) {
        return date.toLocalDate();
    }

    public static String toString(final LocalDate date) {
        return date.format(DateTimeFormatter.ISO_DATE);
    }
}
