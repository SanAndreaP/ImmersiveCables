/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.util;

import blusunrize.immersiveengineering.api.tool.BelljarHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;

public final class IEFixes
{
    static void postInit() {
        if( Loader.isModLoaded("forestry") ) {
            final Item itemFertilizer = Item.REGISTRY.getObject(new ResourceLocation("forestry:fertilizerCompound"));
            if( itemFertilizer != null ) {
                BelljarHandler.registerItemFertilizer(new BelljarHandler.ItemFertilizerHandler() {
                    public boolean isValid(@Nullable ItemStack fertilizer) {
                        return fertilizer != null && fertilizer.getItem() == itemFertilizer;
                    }

                    public float getGrowthMultiplier(ItemStack fertilizer, ItemStack seed, ItemStack soil, TileEntity tile) {
                        return 1.5F;
                    }
                });
            }
        }
    }
}
