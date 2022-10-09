package net.ersei.dml.modular_armor.mixin;/*
 * Copyright (C) 2020 Nathan P. Bombana, IterationFunk
 *
 * This file is part of Deep Mob Learning: Backported.
 *
 * Deep Mob Learning: Backported is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Deep Mob Learning: Backported is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Deep Mob Learning: Backported.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
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

import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPredicateProviderRegistry.class)
public class ModelPredicateProviderRegistryMixin {

    // @Shadow
    private static void register(Item item, Identifier id, ModelPredicateProvider provider) {}

    @SuppressWarnings("all")
    @Inject(at = @At("RETURN"), method = "<clinit>")
    private static void modifyCrossbowModel(CallbackInfo ci) {
        /*
        fuck this shit I'm out
        // TODO redo this
        register(Items.CROSSBOW, new Identifier("pull"), (ItemStack stack, ClientWorld world, LivingEntity entity) -> {
            return Safer.run(0.0F, () -> {
                if (entity == null) {
                    return 0.0F;
                } else if(!CrossbowItem.isCharged(stack)) {
                    float maxUseTime = stack.getMaxUseTime();
                    float pullTime = CrossbowItem.getPullTime(stack);
                    float reducedTicks = 0;

                    if (entity instanceof PlayerEntity) {
                        reducedTicks = ArcheryEffect.Companion.crossbowFastpullReducedTicks((PlayerEntity) entity);
                        maxUseTime -= reducedTicks;
                        pullTime -= reducedTicks;
                    }

                    return (Math.max(0, maxUseTime) - Math.max(0, entity.getItemUseTimeLeft() - reducedTicks)) / pullTime;
                } else {
                    return 0.0F;
                }
            });
        });
        register(Items.BOW, new Identifier("pull"), (ItemStack stack, ClientWorld world, LivingEntity entity) -> {
            return Safer.run(0.0F, () -> {
                if (entity != null && entity.getActiveItem() == stack) {
                    float maxUseTime = stack.getMaxUseTime();
                    float reducedTicks = 1;

                    if (entity instanceof PlayerEntity) {
                        reducedTicks = ArcheryEffect.Companion.bowFastpullLevels((PlayerEntity) entity) + 1;
                        maxUseTime -= reducedTicks;
                    }

                    return (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / (20.0F / reducedTicks);
                }
                return 0F;
            });
        });*/
    }
}
