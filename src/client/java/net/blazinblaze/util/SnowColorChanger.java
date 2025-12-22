package net.blazinblaze.util;

import net.blazinblaze.BlazinChristmasMod;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class SnowColorChanger {

    public static ArrayList<Long> chunksRendered = new ArrayList<>();

    private static int CURRENT_MULTIPLIER;
    private static int numOfTicksPrecipitated;

    private static void setMultiplier() {
        if(numOfTicksPrecipitated > 180*20) {
            if(CURRENT_MULTIPLIER != 4) {
                chunksRendered.clear();
            }
            CURRENT_MULTIPLIER = 4;
        }else if(numOfTicksPrecipitated > 120*20) {
            if(CURRENT_MULTIPLIER != 3) {
                chunksRendered.clear();
            }
            CURRENT_MULTIPLIER = 3;
        }else if(numOfTicksPrecipitated > 60*20) {
            if(CURRENT_MULTIPLIER != 2) {
                chunksRendered.clear();
            }
            CURRENT_MULTIPLIER = 2;
        }else if(numOfTicksPrecipitated > 30*20) {
            if(CURRENT_MULTIPLIER != 1) {
                chunksRendered.clear();
            }
            CURRENT_MULTIPLIER = 1;
        }else {
            if(CURRENT_MULTIPLIER != 0) {
                chunksRendered.clear();
            }
            CURRENT_MULTIPLIER = 0;
        }
    }

    public static void currentSnowColor(Level world) {
        RegistryAccess registryManager = world.registryAccess();
        if(Objects.equals(world.dimensionType(), registryManager.getOrThrow(Registries.DIMENSION_TYPE).value().getValue(BuiltinDimensionTypes.OVERWORLD.registry()))) {
            if(!world.isRaining()) {
                if(numOfTicksPrecipitated > 0) {
                    numOfTicksPrecipitated--;
                }
                setMultiplier();
            }else {
                numOfTicksPrecipitated++;
                setMultiplier();
            }
        }
    }

    public static int getColorForMultiplier(int origColor) {
        if(CURRENT_MULTIPLIER > 0) {
            Color color = new Color(origColor);
            int red = color.getRed();
            int green = color.getGreen();
            int blue = color.getBlue();
            int delta = (40*CURRENT_MULTIPLIER);
            red = red+delta;
            green = green+delta;
            blue = blue+delta;
            if(red>255) {red=255;}
            if(green>255) {green=255;}
            if(blue>255) {blue=255;}
            return blue * 65536 + green * 256 + red;
        }else {
            return origColor;
        }
    }

    public static void addChunkToList(long chunkSectionPos) {
        chunksRendered.add(chunkSectionPos);
    }

    public static boolean isChunkInList(long chunkSectionPos) {
        return chunksRendered.contains(chunkSectionPos);
    }
}
