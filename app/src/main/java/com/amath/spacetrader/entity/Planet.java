package com.amath.spacetrader.entity;

import android.util.Log;

import org.w3c.dom.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Planet extends SpaceBody {

    private final TechLevel techLevel;
    private final ResourceLevel resourceLevel;
    boolean marketAvailable;
//    private double radius;
    private Event status;
//    private Coordinate location;
    private final Map<Good, Integer> inventory = new HashMap<>();

    private Sun sun;
    private String solarSystem;
    private final double HALF = 0.5;

    private static LinkedList<String> AVAILABLE_PLANET_NAMES;
    private static final Collection<String> USED_PLANET_NAMES = new HashSet<>();

    public Planet(int sunSize, Iterable<Planet> planets) {
        this(sunSize, planets, null, null);
    }
    public Planet(int sunSize, Iterable<Planet> planets, Sun sun, String solarSystem) {
        super(null, 0);
        this.solarSystem = solarSystem;
        // Get name from the list of available planet names. This was randomized on start up,
        // so everything should be ok.Removes a name from the list to ensure that
        // AVAILABLE_PLANET_NAMES will never have anything from USED_PLANET_NAMES.
        this.sun = sun;
        String name = null;
        while (name == null) {
            Log.d("pnames", String.valueOf(AVAILABLE_PLANET_NAMES.size()));
            if (AVAILABLE_PLANET_NAMES.isEmpty()) {
                throw new RuntimeException
                        ("App does not handle when AVAILABLE_PLANET_NAMES becomes empty");
            }
            Log.d("initialization", "making planets: inside Planet()");
            String temp = AVAILABLE_PLANET_NAMES.pop();
            if (!USED_PLANET_NAMES.contains(temp)) {
                name = temp;
                USED_PLANET_NAMES.add(name);
                break;
            }
        }


        // Makes a completely random coordinate with respect to Solar system size.

        Random rand = new Random();
        radius = Math.random() * 8;
        while (location == null) {
            double x = ((rand.nextBoolean() ? -1 : 1) * (Math.random()
                    * (SolarSystem.BOUNDS.getX() / 2 - sunSize / 2)) + sunSize/2)
                    + SolarSystem.BOUNDS.getX() / 2;
            double y = ((rand.nextBoolean() ? -1 : 1) * (Math.random()
                    * (SolarSystem.BOUNDS.getY() / 2 - sunSize / 2)) + sunSize/2)
                    + SolarSystem.BOUNDS.getY() / 2;
//            location = new Coordinate(x, y);

            Coordinate temp = new Coordinate(x, y);
            boolean overlapping = false;
            for (Planet planet: planets) {
                if (overlap(temp, planet)) {
                    overlapping = true;
                    break;
                }
            }
            if (!overlapping) this.location = temp;
            else if (radius > 1) {
                radius *= HALF;  //decrease size of radius to decrease future chance of
                                        // overlapping.
            }
        }

        status = Event.values()[(int) (Math.random() * 7)];
        techLevel = TechLevel.values()[(int) (Math.random() * 8)];
        resourceLevel = ResourceLevel.values()[(int) (Math.random() * 13)];
        initializeInventory();


        Log.d("initialization", "making planets: inside Planet()");
    }
    public Planet(String name, Coordinate location, int techLevel, int resourceLevel,
                  double radius) {
        this(name, location, TechLevel.values()[techLevel], ResourceLevel.values()[resourceLevel],
                radius);
    }
    public Planet(String name, Coordinate location, int techLevel, int resourceLevel,
                  double radius, Sun sun, String solarSystem) {
        this(name, location, TechLevel.values()[techLevel], ResourceLevel.values()[resourceLevel],
                radius, sun, solarSystem);
    }
    public Planet(String name, Coordinate location, TechLevel techLevel,
                  ResourceLevel resourceLevel, double radius) {
        this(name, location, techLevel, resourceLevel, radius, null, null);
    }
    public Planet(String name, Coordinate location, TechLevel techLevel,
                  ResourceLevel resourceLevel, double radius, Sun sun, String solarSystem) {

        super(null, 0);
        USED_PLANET_NAMES.add(name);

        this.solarSystem = solarSystem;
        this.name = name;
        this.location = location;
        this.techLevel = techLevel;
        this.resourceLevel = resourceLevel;
        this.radius = radius;
        this.sun = sun;
        initializeInventory();

    }

    public String getName() {
        return name;
    }

    @Override
    public Coordinate getLocation() {
        return location;
    }

    public TechLevel getTechLevel() {
        return techLevel;
    }

    public ResourceLevel getResourceLevel() {
        return resourceLevel;
    }

    @Override
    public double getRadius() { return this.radius; }

    /**
     * Called at startup by Model. Initalizes the LinkedList
     * with a randomized list of possible names.
     *
     * Using LinkedList because .removeFirst() / .pop() have
     * efficiency of O(1).
     *
     * @param availablePlanetNames
     */
    public static void setAvailablePlanetNames(List<String> availablePlanetNames) {
        AVAILABLE_PLANET_NAMES = (LinkedList<String>) availablePlanetNames;
    }

    /**
     * Called if and when player wants to create a custom planet
     * Will check whether or not the name has already been used.
     *
     * @param name
     * @return whether or not the name has been used
     */
    public static boolean isValidName(String name) {
        return USED_PLANET_NAMES.contains(name);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "Planet %s:\n" +
                        "Location with respect to sun: (%f, %f)\n" +
                        "Radius:\t\t%f%n" +
                        "Technology level:%s\n" +
                        "Resources:%s",
                name, location.getX(), location.getY(), this.getRadius(),
                techLevel.toString(), resourceLevel.toString());
    }


    /**
     * Initialize the inventory for the planet
     * Right now the algo for each good is:
     *
     * Minimum = 1 (rarely 0): Maximum = 5 * techLevel
     *
     */
    public void initializeInventory() {
        Log.d("debugLOLZ", Arrays.toString(TechLevel.values()));
        for (Good good: Good.values()) {
            int value = (int) Math.ceil(Math.random() * this.techLevel.getLevel() * 5); //
            this.inventory.put(good, value);
        }
    }

    public String getSolarSystem() {
        return this.solarSystem;
    }
//
    public void setSolarSystem(String solarSystem) {
        if (this.solarSystem == null) this.solarSystem = solarSystem;
        else {
            throw new IllegalArgumentException
                    ("Cannot set the solar system of a planet if it already exists in one");
        }
    }

    public double getDistanceFromSun() {
        if (sun == null) return 0;
        return Math.sqrt(Math.pow(location.getX() - sun.getLocation().getX(), 2)
            + Math.pow(location.getY() - sun.getLocation().getY(), 2));
    }

    public Event getStatus() {
        return status;
    }

    public boolean hasStatus() {
        return status != null;
    }

}
