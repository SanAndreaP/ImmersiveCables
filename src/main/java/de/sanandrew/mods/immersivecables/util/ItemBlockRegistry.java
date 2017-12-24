/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.util;

import de.sanandrew.mods.immersivecables.block.BlockCoil;
import de.sanandrew.mods.immersivecables.block.ae2.BlockRegistryAE2;
import de.sanandrew.mods.immersivecables.block.rs.BlockRegistryRS;
import de.sanandrew.mods.immersivecables.item.ItemBlockMeta;
import de.sanandrew.mods.immersivecables.item.ItemCoil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ICConstants.ID)
public final class ItemBlockRegistry
{
    public static final BlockCoil BLOCK_COIL = new BlockCoil();

    public static final ItemCoil WIRE_COIL = new ItemCoil();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(BLOCK_COIL);

        if( ICConfiguration.isAe2Enabled() ) {
            BlockRegistryAE2.registerBlocks(event);
        }
        if( ICConfiguration.isRsEnabled() ) {
            BlockRegistryRS.registerBlocks(event);
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlockMeta(BLOCK_COIL));

        event.getRegistry().registerAll(WIRE_COIL);

        if( ICConfiguration.isAe2Enabled() ) {
            BlockRegistryAE2.registerItems(event);
        }
        if( ICConfiguration.isRsEnabled() ) {
            BlockRegistryRS.registerItems(event);
        }
    }
}
