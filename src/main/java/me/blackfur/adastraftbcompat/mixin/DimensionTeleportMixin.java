package me.blackfur.adastraftbcompat.mixin;

import earth.terrarium.ad_astra.common.util.ModUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(ModUtils.class)
public class DimensionMixin {
    @Inject(
            method = "teleportToLevel",
            at = @At("HEAD")
    )
    public modifyTargetDimension() {
        
    }
}
