package com.uni.app.entities;

import com.uni.app.exceptions.ValidationException;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A generic in-memory collection of {@link Identifiable} entities, keyed by id.
 *
 * <p>Backed by a {@link LinkedHashMap}, so entities are unique by id and kept in
 * insertion order. Provides id-based lookup and removal, and lambda-based
 * filtering and sorting that return unmodifiable lists.</p>
 *
 * @param <T> the type of entity stored, which must be {@link Identifiable}
 * @author Jan Grdanjski
 * @version 1.0
 * @since 1.0
 */
public final class EntityCollection<T extends Identifiable> {
    private final Map<String, T> entities = new LinkedHashMap<>();

    /**
     * Creates an empty collection.
     */
    public EntityCollection() {
    }

    /**
     * Adds a new entity.
     *
     * @param entity the entity to add
     * @throws ValidationException if {@code entity} is {@code null}, or an entity
     *                             with the same id already exists
     */
    public void add(T entity) {
        if (entity == null) {
            throw new ValidationException("EntityCollection 'entity' must not be null");
        }
        if (entities.containsKey(entity.getId())) {
            throw new ValidationException("EntityCollection already contains an entity with id " + entity.getId());
        }
        entities.put(entity.getId(), entity);
    }

    /**
     * Replaces an existing entity with one carrying the same id.
     *
     * @param entity the entity holding the new values
     * @throws ValidationException if {@code entity} is {@code null}, or no entity
     *                             with the same id exists
     */
    public void update(T entity) {
        if (entity == null) {
            throw new ValidationException("EntityCollection 'entity' must not be null");
        }
        if (!entities.containsKey(entity.getId())) {
            throw new ValidationException("EntityCollection has no entity with id " + entity.getId());
        }
        entities.put(entity.getId(), entity);
    }

    /**
     * Removes the entity with the given id, if present.
     *
     * @param id the id of the entity to remove
     * @throws ValidationException if {@code id} is {@code null}
     */
    public void removeById(String id) {
        if (id == null) {
            throw new ValidationException("EntityCollection 'id' must not be null");
        }
        entities.remove(id);
    }

    /**
     * Finds the entity with the given id.
     *
     * @param id the id to look for
     * @return an {@link Optional} containing the entity, or empty if none has that id
     * @throws ValidationException if {@code id} is {@code null}
     */
    public Optional<T> findById(String id) {
        if (id == null) {
            throw new ValidationException("EntityCollection 'id' must not be null");
        }
        return Optional.ofNullable(entities.get(id));
    }

    /**
     * Returns all entities in this collection.
     *
     * @return an unmodifiable list of all entities, in insertion order
     */
    public List<T> getAll() {
        return List.copyOf(entities.values());
    }

    /**
     * Returns the entities that match the given predicate.
     *
     * @param predicate the condition each returned entity must satisfy
     * @return an unmodifiable list of the matching entities, in insertion order
     */
    public List<T> filter(Predicate<T> predicate) {
        return entities.values().stream()
                .filter(predicate)
                .toList();
    }

    /**
     * Returns all entities sorted by the given comparator.
     *
     * @param comparator the comparator that determines the order
     * @return an unmodifiable list of all entities in the comparator's order
     */
    public List<T> sorted(Comparator<T> comparator) {
        return entities.values().stream()
                .sorted(comparator)
                .toList();
    }

    /**
     * Returns the number of entities in this collection.
     *
     * @return the entity count
     */
    public int size() {
        return entities.size();
    }

    /**
     * Returns whether an entity with the given id is in this collection.
     *
     * @param id the id to look for
     * @return {@code true} if an entity with that id exists, {@code false} otherwise
     */
    public boolean contains(String id) {
        return entities.containsKey(id);
    }

    /**
     * Returns the ids of all entities in this collection.
     *
     * @return an unmodifiable set of all entity ids
     */
    public Set<String> getIds() {
        return Set.copyOf(entities.keySet());
    }
}
