package net.ersei.dml.mixin;/*
 *
 *  Copyright (C) 2021 Nathan P. Bombana, IterationFunk
 *
 *  This file is part of Deep Mob Learning: Backported.
 *
 *  Deep Mob Learning: Backported is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Deep Mob Learning: Backported is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Deep Mob Learning: Backported.  If not, see <https://www.gnu.org/licenses/>.
 */

import net.ersei.dml.event.VanillaEvents;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public class EndermanEntityMixin {

    @Inject(at = @At("HEAD"), method = "teleportTo(DDD)Z", cancellable = true)
    public void teleport(double x, double y, double z, CallbackInfoReturnable<Boolean> ci) {
        EndermanEntity entity = (EndermanEntity) (Object) this;
        Vec3d pos = new Vec3d(x, y, z);
        ActionResult result = VanillaEvents.INSTANCE.getEndermanTeleportEvent()
            .invoker()
            .invoke(entity, pos);

        if (result == ActionResult.FAIL) {
            ci.setReturnValue(false);
            ci.cancel();
        }
    }

}
