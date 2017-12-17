/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public final class ItemBlockMeta
        extends ItemBlock
{
    public ItemBlockMeta(Block block) {
        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setRegistryName(block.getRegistryName());
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return String.format("%s.%d", this.block.getUnlocalizedName(), stack.getItemDamage());
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}
