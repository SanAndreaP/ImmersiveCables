/*
 * ****************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 * *****************************************************************************************************************
 */
package de.sanandrew.mods.immersiveintegration.block;

import de.sanandrew.mods.immersiveintegration.util.IIConstants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

@Mod.EventBusSubscriber
public class BlockRegistry
{
    public static final BlockAeFluixCoil FLUIX_COIL = new BlockAeFluixCoil();
//    public static final BlockTurretAssembly TURRET_ASSEMBLY = new BlockTurretAssembly();
//    public static final BlockElectrolyteGenerator ELECTROLYTE_GENERATOR = new BlockElectrolyteGenerator();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(FLUIX_COIL);
//
//        GameRegistry.registerTileEntity(TileEntityTurretAssembly.class, TmrConstants.ID + ":te_turret_assembly");
//        GameRegistry.registerTileEntity(TileEntityElectrolyteGenerator.class, TmrConstants.ID + ":te_potato_generator");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        Block[] blocks = {
//                FLUX_COIL
        };
        event.getRegistry().register(new BlockAeFluixCoil.ItemBlockCoil(FLUIX_COIL));
        for( Block block : blocks ) {
            ResourceLocation regName = block.getRegistryName();
            if( regName != null ) {
                event.getRegistry().register(new ItemBlock(block).setRegistryName(regName));
            } else {
                IIConstants.LOG.log(Level.ERROR, String.format("Cannot register Item for Block %s as it does not have a registry name!", block));
            }
        }
    }
}
