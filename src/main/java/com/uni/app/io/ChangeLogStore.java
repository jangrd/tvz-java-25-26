package com.uni.app.io;

import com.uni.app.entities.ChangeLog;
import com.uni.app.exceptions.ResourceMissingException;
import com.uni.app.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Persists {@link ChangeLog} entries to a binary file using Java serialization.
 *
 * <p>The file stores a single serialized {@code ArrayList<ChangeLog>}. Each call
 * to {@link #save(List)} overwrites the file completely. Call {@link #readAll()}
 * to load the existing history before appending and saving.</p>
 *
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public final class ChangeLogStore {

    private static final Logger log = LoggerFactory.getLogger(ChangeLogStore.class);
    private final String path;

    /**
     * Creates a store bound to the given file path.
     *
     * @param path path to the binary change-log file
     * @throws ValidationException if {@code path} is {@code null}
     */
    public ChangeLogStore(String path) {
        if (path == null) {
            throw new ValidationException("ChangeLogStore 'path' must not be null");
        }
        this.path = path;
    }

    /**
     * Reads all change-log entries from the binary file.
     *
     * <p>Returns an empty list if the file does not yet exist.</p>
     *
     * @return mutable list of all stored {@link ChangeLog} entries
     * @throws ResourceMissingException if the file exists but cannot be read
     */
    public List<ChangeLog> readAll() throws ResourceMissingException {
        File file = new File(path);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            List<ChangeLog> list = (List<ChangeLog>) in.readObject();
            return list;
        } catch (IOException | ClassNotFoundException e) {
            log.error("Failed to read change log from {}", path, e);
            throw new ResourceMissingException("ChangeLogStore failed to read " + path, e);
        }
    }

    /**
     * Writes the given list of change-log entries to the binary file,
     * replacing any existing content.
     *
     * @param changeLogs the entries to persist
     * @throws ValidationException      if {@code changeLogs} is {@code null}
     * @throws ResourceMissingException if the file cannot be written
     */
    public void save(List<ChangeLog> changeLogs) throws ResourceMissingException {
        if (changeLogs == null) {
            throw new ValidationException("ChangeLogStore 'changeLogs' must not be null");
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(new ArrayList<>(changeLogs));
        } catch (IOException e) {
            log.error("Failed to write change log to {}", path, e);
            throw new ResourceMissingException("ChangeLogStore failed to write " + path, e);
        }
    }

    /**
     * Returns the file path this store was initialised with.
     *
     * @return the change-log file path
     */
    public String getPath() {
        return path;
    }
}
