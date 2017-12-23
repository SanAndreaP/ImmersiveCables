/*
 * ****************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 * *****************************************************************************************************************
 */
package de.sanandrew.mods.immersivecables.block.ae2;

import de.sanandrew.mods.immersivecables.item.ItemBlockMeta;
import de.sanandrew.mods.immersivecables.tileentity.ae.TileConnectorQuartz;
import de.sanandrew.mods.immersivecables.tileentity.ae.TileRelayFluix;
import de.sanandrew.mods.immersivecables.tileentity.ae.TileTransformerFluix;
import de.sanandrew.mods.immersivecables.util.ICConstants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Objects;

public final class BlockRegistryAE2
{
    public static final BlockRelayFluix RELAY_FLUIX = new BlockRelayFluix();
    public static final BlockTransformerFluix TRANSFORMER_FLUIX = new BlockTransformerFluix();
    public static final BlockConnectorQuartz CONNECTOR_QUARTZ = new BlockConnectorQuartz();

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(TRANSFORMER_FLUIX, RELAY_FLUIX, CONNECTOR_QUARTZ);

        GameRegistry.registerTileEntity(TileTransformerFluix.class, ICConstants.ID + ":te_transformer_fluix");
        GameRegistry.registerTileEntity(TileRelayFluix.class, ICConstants.ID + ":te_relay_fluix");
        GameRegistry.registerTileEntity(TileConnectorQuartz.class, ICConstants.ID + ":te_connector_quartz");
    }

    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlockMeta(TRANSFORMER_FLUIX));
        event.getRegistry().register(new ItemBlockMeta(RELAY_FLUIX));
        event.getRegistry().register(new ItemBlock(CONNECTOR_QUARTZ).setRegistryName(Objects.requireNonNull(CONNECTOR_QUARTZ.getRegistryName())));
    }
}
