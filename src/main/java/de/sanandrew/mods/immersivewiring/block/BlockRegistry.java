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
import de.sanandrew.mods.immersivewiring.tileentity.TileEntityMETransformer;
import de.sanandrew.mods.immersivewiring.util.IWConstants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Level;

@Mod.EventBusSubscriber
public final class BlockRegistry
{
    public static final BlockAeFluixCoil FLUIX_COIL = new BlockAeFluixCoil();
    public static final BlockRelayFluix RELAY_FLUIX = new BlockRelayFluix();
    public static final BlockTransformerFluix ME_TRANSFORMER = new BlockTransformerFluix();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(FLUIX_COIL, ME_TRANSFORMER, RELAY_FLUIX);
//
        GameRegistry.registerTileEntity(TileEntityMETransformer.class, IWConstants.ID + ":te_me_transformer");
//        GameRegistry.registerTileEntity(TileEntityElectrolyteGenerator.class, TmrConstants.ID + ":te_potato_generator");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlockMeta(FLUIX_COIL));
        event.getRegistry().register(new ItemBlockMeta(ME_TRANSFORMER));
        event.getRegistry().register(new ItemBlockMeta(RELAY_FLUIX));
    }

    private static void registerBlockItem(RegistryEvent.Register<Item> event, Block block) {
        ResourceLocation regName = block.getRegistryName();
        if( regName != null ) {
            event.getRegistry().register(new ItemBlock(block).setRegistryName(regName));
        } else {
            IWConstants.LOG.log(Level.ERROR, String.format("Cannot register Item for Block %s as it does not have a registry name!", block));
        }
    }
}
