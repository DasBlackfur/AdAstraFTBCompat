package me.blackfur.adastraftbcompat;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AdAstraFTBCompat.MODID)
public class AdAstraFTBCompat {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "adastraftbcompat";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "adastraftbcompat" namespace
    public AdAstraFTBCompat() {
    }
}
