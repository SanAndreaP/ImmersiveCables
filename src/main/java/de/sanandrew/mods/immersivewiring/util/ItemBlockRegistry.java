/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.util;

import de.sanandrew.mods.immersivewiring.block.ae2.BlockRegistryAE2;
import de.sanandrew.mods.immersivewiring.item.ItemRegistryAE2;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public final class ItemBlockRegistry
{

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        if( IWConfiguration.isAe2Enabled() ) {
            BlockRegistryAE2.registerBlocks(event);
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        if( IWConfiguration.isAe2Enabled() ) {
            BlockRegistryAE2.registerItems(event);
            ItemRegistryAE2.registerItems(event);
        }
    }
}
