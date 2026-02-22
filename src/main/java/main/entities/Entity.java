package main.entities;

public interface Entity {

    /**
     * Retrieves the name of the entity.
     * Any object in the simulation can be identified by name.
     *
     * @return A string representing the name of the entity.
     */
    String getName();
}
