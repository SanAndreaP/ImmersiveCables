/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ICCreativeTab
        extends CreativeTabs
{
    public static final ICCreativeTab INSTANCE = new ICCreativeTab();

    private ItemStack tabIcon = ItemStack.EMPTY;

    public ICCreativeTab() {
        super(ICConstants.ID);
    }

    @Override
    public ItemStack getTabIconItem() {
        if( this.tabIcon.isEmpty() ) {
            this.tabIcon = new ItemStack(ItemBlockRegistry.WIRE_COIL, 1, 0);
        }

        return this.tabIcon;
    }
}
