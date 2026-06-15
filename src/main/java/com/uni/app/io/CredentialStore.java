package com.uni.app.io;

import com.uni.app.entities.User;
import com.uni.app.enums.UserRole;
import com.uni.app.exceptions.ResourceMissingException;
import com.uni.app.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

/**
 * Loads user credentials from a plain-text file and verifies passwords.
 *
 * <p>The file must contain one entry per line in the format
 * {@code username:sha256Hash:ROLE}. Lines that do not match this format
 * are silently skipped. Passwords are never stored in plain text; only the
 * SHA-256 hex digest is kept in memory.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public final class CredentialStore {
    private static final Logger log = LoggerFactory.getLogger(CredentialStore.class);
    private final String path;
    private final List<User> users = new ArrayList<>();

    /**
     * Creates a credential store pointing to the given file path.
     *
     * @param path path to the credentials file
     * @throws ValidationException if {@code path} is {@code null}
     */
    public CredentialStore(String path) {
        if (path == null) {
            throw new ValidationException("CredentialStore 'path' must not be null");
        }
        this.path = path;
    }

    /**
     * Reads and parses the credentials file, replacing any previously loaded data.
     *
     * @throws ResourceMissingException if the credentials file cannot be found
     */
    public void read() throws ResourceMissingException {
        users.clear();
        File credentialsFile = new File(path);
        try (Scanner myReader = new Scanner(credentialsFile)) {
            while (myReader.hasNextLine()) {
                String[] userData = myReader.nextLine().split(":");
                if (userData.length != 3) {
                    continue;
                }
                try {
                    users.add(new User(userData[0], userData[1], UserRole.valueOf(userData[2])));
                } catch (IllegalArgumentException e) {
                    log.warn("Skipping credential entry with unknown role '{}'", userData[2]);
                }
            }
        } catch (FileNotFoundException e) {
            throw new ResourceMissingException("CredentialStore failed to open " + path, e);
        }
    }

    /**
     * Verifies the given credentials against the loaded user list.
     *
     * @param username the username to look up
     * @param password the plain-text password to verify
     * @return the matching {@link User} wrapped in an {@link Optional},
     *         or {@link Optional#empty()} if authentication fails
     */
    public Optional<User> authenticate(String username, String password) {
        for (User user : users) {
            if (Objects.equals(user.username(), username)) {
                if (hash(password).equals(user.passwordHash())) {
                    return Optional.of(user);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Returns an unmodifiable view of all loaded users.
     *
     * @return list of all users currently in memory
     */
    public List<User> getAll() {
        return List.copyOf(users);
    }

    /**
     * Returns the file path this store was initialised with.
     *
     * @return the credentials file path
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns the SHA-256 hex digest of the given input.
     *
     * @param input the string to hash
     * @return lowercase hex string of the SHA-256 digest
     */
    private static String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : bytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
