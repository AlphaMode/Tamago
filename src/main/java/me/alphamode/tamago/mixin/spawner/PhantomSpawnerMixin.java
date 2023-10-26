package me.alphamode.tamago.mixin.spawner;

import me.alphamode.tamago.Tamago;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.levelgen.PhantomSpawner;

@Mixin(PhantomSpawner.class)
public abstract class PhantomSpawnerMixin implements CustomSpawner {
    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/EntityType;create(Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/entity/Entity;"
            )
    )
    private Entity fireSpawnEvent(Entity phantom,
                                  ServerLevel level, boolean spawnMonsters, boolean spawnAnimals,
                                  @Local(ordinal = 1) BlockPos pos) { // blockPos2, random offset after 72000
        return Tamago.NATURAL_SPAWN.invoker().canSpawnMob(
                (Phantom) phantom, pos.getX(), pos.getY(), pos.getZ(), level, this, MobSpawnType.NATURAL
        ).orElse(true) ? phantom : null;

    }
}