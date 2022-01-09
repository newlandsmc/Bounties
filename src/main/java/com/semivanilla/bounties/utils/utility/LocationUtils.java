package com.semivanilla.bounties.utils.utility;

import com.semivanilla.bounties.utils.UtilityManager;
import org.bukkit.Location;

import java.util.Random;

public class LocationUtils {

    private final UtilityManager manager;
    private static final Random random = new Random();;

    public LocationUtils(UtilityManager manager) {
        this.manager = manager;
    }

    public static String getRandomLocationFromARadius(Location location, int radius){
        final int l1X = location.getBlockX()+radius;
        final int l2X = location.getBlockX()-radius;

        final int l1Z = location.getBlockZ()+radius;
        final int l2Z = location.getBlockZ()-radius;

        final int randX = (int) (random.nextInt(Math.max(l1X,l2X) - Math.min(l1X,l2X) ) + Math.min(l1X,l2X));
        final int randZ = (int) (random.nextInt(Math.max(l1Z,l2Z) - Math.min(l1Z,l2Z) ) + Math.min(l1Z,l2Z));

        return randX+" "+randZ;
    }
}
