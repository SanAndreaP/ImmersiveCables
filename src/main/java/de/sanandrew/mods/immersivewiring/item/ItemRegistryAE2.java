/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;

public final class ItemRegistryAE2
{
    public static final ItemCoil WIRE_COIL = new ItemCoil();

    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(WIRE_COIL);
    }
}
