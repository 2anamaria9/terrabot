package main;

import main.entities.Entity;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import lombok.Getter;

@Getter
public final class TerraBot {
    private int x;
    private int y;
    private int energy;
    private final List<Entity> inventory;
    private final Map<String, List<String>> dataBase;

    public TerraBot(final int energy) {
        this.x = 0;
        this.y = 0;
        this.energy = energy;
        this.inventory = new ArrayList<>();
        this.dataBase = new LinkedHashMap<>();
    }

    /**
     * Updates the robot's coordinates on the map.
     *
     * @param newX The new X coordinate.
     * @param newY The new Y coordinate.
     */
    public void move(final int newX, final int newY) {
        this.x = newX;
        this.y = newY;
    }

    /**
     * Decreases the robot's energy level.
     *
     * @param value The amount of energy to consume.
     */
    public void consumeEnergy(final int value) {
        this.energy -= value;
    }

    /**
     * Increases the robot's energy level.
     *
     * @param value The amount of energy to add.
     */
    public void rechargeEnergy(final int value) {
        this.energy += value;
    }

    /**
     * Adds a scanned entity to the robot's physical inventory.
     *
     * @param entity The entity object to store.
     */
    public void addToInventory(final Entity entity) {
        inventory.add(entity);
    }

    /**
     * Removes an entity from the inventory based on its name.
     * Used when an item is consumed for environment improvement.
     *
     * @param name The name of the entity to remove.
     */
    public void removeFromInventory(final String name) {
        Entity removed = null;
        for (Entity entity : inventory) {
            if (entity.getName().equals(name)) {
                removed = entity;
                break;
            }
        }
        if (removed != null) {
            inventory.remove(removed);
        }
    }

    /**
     * Checks if a specific entity exists in the inventory.
     *
     * @param name The name of the entity to search for.
     * @return true if the entity is in the inventory, false otherwise.
     */
    public boolean hasEntity(final String name) {
        for (Entity entity : inventory) {
            if (entity.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a subject exists in the knowledge base.
     *
     * @param key The subject name.
     * @return true if the subject has been initialized, false otherwise.
     */
    public boolean hasSubject(final String key) {
        return dataBase.containsKey(key);
    }

    /**
     * Adds a fact string to a specific subject in the knowledge base.
     * If the subject doesn't exist, it creates the entry first.
     *
     * @param key The subject name.
     * @param value The fact to add.
     */
    public void addToDataBase(final String key, final String value) {
        if (!hasSubject(key)) {
            dataBase.put(key, new ArrayList<>());
        }
        dataBase.get(key).add(value);
    }

    /**
     * Checks if the robot knows a specific fact about a subject.
     *
     * @param key The subject name.
     * @param value The fact to verify.
     * @return true if the fact exists in the database for that key, false otherwise.
     */
    public boolean knowsFact(final String key, final String value) {
        if (dataBase.containsKey(key)) {
            return dataBase.get(key).contains(value);
        }
        return false;
    }
}
