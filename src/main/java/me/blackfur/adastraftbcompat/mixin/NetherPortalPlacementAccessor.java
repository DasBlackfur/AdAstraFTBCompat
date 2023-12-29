package me.blackfur.adastraftbcompat.mixin;

import dev.ftb.mods.ftbteamdimensions.dimensions.NetherPortalPlacement;
import dev.ftb.mods.ftbteams.data.Team;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NetherPortalPlacement.class)
public interface NetherPortalPlacementAccessor {
    @Invoker
    static BlockPos callGetBasePos(ServerPlayer serverPlayer, @Nullable Team team) {
        throw new UnsupportedOperationException();
    }
}
