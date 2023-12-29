package me.blackfur.adastraftbcompat.mixin;

import dev.ftb.mods.ftbteamdimensions.dimensions.DimensionsManager;
import dev.ftb.mods.ftbteamdimensions.dimensions.level.DimensionStorage;
import earth.terrarium.ad_astra.common.util.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Debug(export = true)
@Mixin(ModUtils.class)
public class DimensionTeleportMixin {
    @Unique
    private static Entity adAstraFTBCompat$entity;
    @Unique
    private static BlockPos adAstraFTBCompat$blockPos = null;
    @Inject(
            method = "teleportToLevel",
            at = @At("HEAD"),
            remap = false
    )
    private static void dummyInject(ResourceKey<Level> targetWorld, Entity entity, CallbackInfo ci) {
        DimensionTeleportMixin.adAstraFTBCompat$entity = entity;
    }
    @ModifyVariable(
            method = "teleportToLevel",
            at = @At("HEAD"),
            remap = false,
            argsOnly = true
    )
    private static ResourceKey<Level> modifyTargetDimension(ResourceKey<Level> targetWorld) {
        adAstraFTBCompat$blockPos = null;
        if (targetWorld.equals(Level.OVERWORLD) && adAstraFTBCompat$entity instanceof ServerPlayer player) {
            ResourceKey<Level> dimension = DimensionsManager.INSTANCE.getDimension(player);
            if (dimension != null) {
                adAstraFTBCompat$blockPos = Objects.requireNonNull(
                        DimensionStorage.get()).getDimensionSpawnLocation(dimension.location());
                return dimension;
            }
        }
        return targetWorld;
    }

    @Redirect(
            method = "teleportToLevel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getX()D", ordinal = 0),
            remap = false
    )
    private static double modifyXPos(Entity instance) {
        if (adAstraFTBCompat$blockPos != null) {
            return adAstraFTBCompat$blockPos.getX();
        }
        return instance.getX();
    }

    @Redirect(
            method = "teleportToLevel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getZ()D", ordinal = 0),
            remap = false
    )
    private static double modifyZPos(Entity instance) {
        if (adAstraFTBCompat$blockPos != null) {
            return adAstraFTBCompat$blockPos.getZ();
        }
        return instance.getZ();
    }
}
