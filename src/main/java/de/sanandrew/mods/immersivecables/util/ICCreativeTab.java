/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ICCreativeTab
        extends CreativeTabs
{
    public static final ICCreativeTab INSTANCE = new ICCreativeTab();

    public ICCreativeTab() {
        super(ICConstants.ID);
    }

    @Override
    public Item getTabIconItem() {
        return ItemBlockRegistry.WIRE_COIL;
    }
}
