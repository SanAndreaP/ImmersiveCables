/*
 * ****************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 * *****************************************************************************************************************
 */
package de.sanandrew.mods.immersivecables.block.rs;

import de.sanandrew.mods.immersivecables.tileentity.rs.TileRelayRefined;
import de.sanandrew.mods.immersivecables.tileentity.rs.TileTransformerRefined;
import de.sanandrew.mods.immersivecables.util.ICConstants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Objects;

public final class BlockRegistryRS
{
    public static final BlockTransformerRefined TRANSFORMER_RS = new BlockTransformerRefined();
    public static final BlockRelayRefined RELAY_RS = new BlockRelayRefined();

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(TRANSFORMER_RS, RELAY_RS);

        GameRegistry.registerTileEntity(TileTransformerRefined.class, ICConstants.ID + ":te_transformer_refined");
        GameRegistry.registerTileEntity(TileRelayRefined.class, ICConstants.ID + ":te_relay_refined");
    }

    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(TRANSFORMER_RS).setRegistryName(Objects.requireNonNull(TRANSFORMER_RS.getRegistryName())));
        event.getRegistry().register(new ItemBlock(RELAY_RS).setRegistryName(Objects.requireNonNull(RELAY_RS.getRegistryName())));
    }
}
