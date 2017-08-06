package com.nibado.project.grub.users.components;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

import static com.nibado.project.grub.validation.Values.notNull;

public class Hash {
    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    public static final int SALT_BYTES = 24;
    public static final int HASH_BYTES = 24;
    public static final int PBKDF2_ITERATIONS = 1000;

    public static final int ITERATION_INDEX = 0;
    public static final int SALT_INDEX = 1;
    public static final int PBKDF2_INDEX = 2;

    public static String create(String password) {
        notNull(password);

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);

        byte[] hash = pbkdf2(password.toCharArray(), salt, PBKDF2_ITERATIONS, HASH_BYTES);

        return PBKDF2_ITERATIONS + ":" + base64(salt) + ":" + base64(hash);
    }

    public static boolean validate(final String password, final String hash) {
        String[] params = notNull(hash).split(":");

        if (params.length < 3) {
            throw new IllegalArgumentException("Hash should be a : separated string with 3 parts");
        }

        int iterations = Integer.parseInt(params[ITERATION_INDEX]);
        byte[] salt = base64(params[SALT_INDEX]);
        byte[] hashed = base64(params[PBKDF2_INDEX]);

        byte[] testHash = pbkdf2(notNull(password).toCharArray(), salt, iterations, hashed.length);
        return slowEquals(hashed, testHash);
    }

    private static String base64(final byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static byte[] base64(final String string) {
        return Base64.getDecoder().decode(string);
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        boolean same = true;
        for (int i = 0; i < a.length && i < b.length; i++) {
            if(a[i] != b[i]) {
                same = false;
            }
        }
        return same;
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            return skf.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
