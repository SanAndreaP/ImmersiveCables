/*
 * ****************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 * *****************************************************************************************************************
 */
package de.sanandrew.mods.immersivewiring.block;

import de.sanandrew.mods.immersivewiring.item.ItemBlockMeta;
import de.sanandrew.mods.immersivewiring.tileentity.TileEntityRelayFluix;
import de.sanandrew.mods.immersivewiring.tileentity.TileEntityTransformerFluix;
import de.sanandrew.mods.immersivewiring.util.IWConstants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public final class BlockRegistry
{
    public static final BlockCoil BLOCK_COIL = new BlockCoil();
    public static final BlockRelayFluix RELAY_FLUIX = new BlockRelayFluix();
    public static final BlockTransformerFluix TRANSFORMER_FLUIX = new BlockTransformerFluix();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        if( Loader.isModLoaded(IWConstants.COMPAT_APPLIEDENERGISTICS) ) {
            event.getRegistry().registerAll(BLOCK_COIL, TRANSFORMER_FLUIX, RELAY_FLUIX);

            GameRegistry.registerTileEntity(TileEntityTransformerFluix.class, IWConstants.ID + ":te_transformer_fluix");
            GameRegistry.registerTileEntity(TileEntityRelayFluix.class, IWConstants.ID + ":te_relay_fluix");
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlockMeta(BLOCK_COIL));
        event.getRegistry().register(new ItemBlockMeta(TRANSFORMER_FLUIX));
        event.getRegistry().register(new ItemBlockMeta(RELAY_FLUIX));
    }
}
