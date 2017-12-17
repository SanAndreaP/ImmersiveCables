/*
 * ****************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 * *****************************************************************************************************************
 */
package de.sanandrew.mods.immersivewiring.client.util;

import de.sanandrew.mods.immersivewiring.util.IWConfiguration;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public final class ModelRegistry
{
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) throws Exception {
        if( IWConfiguration.isAe2Enabled() ) {
            ModelRegistryAE2.registerModels(event);
        }
    }

    static void registerModelPre112() {
        if( IWConfiguration.isAe2Enabled() ) {
            ModelRegistryAE2.registerModelPre112();
        }
    }

    static void registerModelBlockItems(Block block, Map<Integer, ModelResourceLocation> metaToLocation) {
        Item blockItem = Item.getItemFromBlock(block);
        if( blockItem != null ) {
            metaToLocation.forEach((meta, location) -> ModelLoader.setCustomModelResourceLocation(blockItem, meta, location));
        }
    }
}
