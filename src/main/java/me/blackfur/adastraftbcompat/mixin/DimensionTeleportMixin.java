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
    private static ServerPlayer adAstraFTBCompat$entity = null;
    @Unique
    private static BlockPos adAstraFTBCompat$blockPos = null;
    @Inject(
            method = "teleportToLevel",
            at = @At("HEAD"),
            remap = false
    )
    private static void dummyInject(ResourceKey<Level> targetWorld, Entity entity, CallbackInfo ci) {
        adAstraFTBCompat$entity = null;
        if (entity instanceof ServerPlayer serverPlayer) {
            adAstraFTBCompat$entity = serverPlayer;
        }
        if (entity.getFirstPassenger() instanceof ServerPlayer serverPlayer) {
            adAstraFTBCompat$entity = serverPlayer;
        }
    }
    @ModifyVariable(
            method = "teleportToLevel",
            at = @At("HEAD"),
            remap = false,
            argsOnly = true
    )
    private static ResourceKey<Level> modifyTargetDimension(ResourceKey<Level> targetWorld) {
        adAstraFTBCompat$blockPos = null;
        if (targetWorld.equals(Level.OVERWORLD) && adAstraFTBCompat$entity != null) {
            ResourceKey<Level> dimension = DimensionsManager.INSTANCE.getDimension(adAstraFTBCompat$entity);
            if (dimension != null) {
                adAstraFTBCompat$blockPos = Objects.requireNonNull(
                        DimensionStorage.get()).getDimensionSpawnLocation(dimension.location());
                return dimension;
            }
        }
        if (targetWorld.location().getNamespace().equals("ad_astra") && adAstraFTBCompat$entity != null) {
            var worldborder = Objects.requireNonNull(
                    adAstraFTBCompat$entity.getLevel().getServer().getLevel(targetWorld)).getWorldBorder();
            var blockPos = NetherPortalPlacementAccessor.callGetBasePos(adAstraFTBCompat$entity, null);
            adAstraFTBCompat$blockPos = worldborder.clampToBounds(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }
        return targetWorld;
    }

    @Redirect(
            method = "teleportToLevel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getX()D"),
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
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getZ()D"),
            remap = false
    )
    private static double modifyZPos(Entity instance) {
        if (adAstraFTBCompat$blockPos != null) {
            return adAstraFTBCompat$blockPos.getZ();
        }
        return instance.getZ();
    }
}
