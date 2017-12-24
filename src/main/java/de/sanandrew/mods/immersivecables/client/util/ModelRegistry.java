/*
 * ****************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 * *****************************************************************************************************************
 */
package de.sanandrew.mods.immersivecables.client.util;

import de.sanandrew.mods.immersivecables.client.util.ae.ModelRegistryAE2;
import de.sanandrew.mods.immersivecables.client.util.rs.ModelRegistryRS;
import de.sanandrew.mods.immersivecables.util.ICConfiguration;
import de.sanandrew.mods.immersivecables.util.ICConstants;
import de.sanandrew.mods.immersivecables.util.ItemBlockRegistry;
import de.sanandrew.mods.immersivecables.wire.Wires;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = ICConstants.ID, value = Side.CLIENT)
public final class ModelRegistry
{
    @SubscribeEvent
    @SuppressWarnings("serial")
    public static void registerModels(ModelRegistryEvent event) {
        ModelRegistry.registerModelBlockItems(ItemBlockRegistry.BLOCK_COIL, new HashMap<Integer, ModelResourceLocation>() {{
            Arrays.stream(Wires.VALUES).forEach(wire -> put(wire.ordinal(), new ModelResourceLocation(ICConstants.ID + ":coil_block_" + wire.getName(), "inventory")));
        }});

        Arrays.stream(Wires.VALUES).forEach(wire -> ModelLoader.setCustomModelResourceLocation(ItemBlockRegistry.WIRE_COIL, wire.ordinal(),
                                                                                               new ModelResourceLocation(ICConstants.ID + ":wire_" + wire.getName(), "inventory")));

        if( ICConfiguration.isAe2Enabled() ) {
            ModelRegistryAE2.registerModels();
        }
        if( ICConfiguration.isRsEnabled() ) {
            ModelRegistryRS.registerModels();
        }
    }

    public static void registerModelBlockItems(Block block, Map<Integer, ModelResourceLocation> metaToLocation) {
        Item blockItem = Item.getItemFromBlock(block);
        metaToLocation.forEach((meta, location) -> ModelLoader.setCustomModelResourceLocation(blockItem, meta, location));
    }
}
