package com.king.drainhelper.sound;

import com.king.drainhelper.WaterDrainTools;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

    public static final SoundEvent TORNADO_SIREN = register("tornado_siren");

    private static SoundEvent register(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(WaterDrainTools.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void initialize() {
        // Forces registration
    }
}