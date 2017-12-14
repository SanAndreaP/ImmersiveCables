/*
 * ****************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 * *****************************************************************************************************************
 */
package de.sanandrew.mods.immersivewiring.client.util;

import de.sanandrew.mods.immersivewiring.block.BlockCoil;
import de.sanandrew.mods.immersivewiring.block.BlockRegistry;
import de.sanandrew.mods.immersivewiring.block.BlockRelayFluix;
import de.sanandrew.mods.immersivewiring.client.render.RenderTileIWConnectable;
import de.sanandrew.mods.immersivewiring.item.ItemRegistry;
import de.sanandrew.mods.immersivewiring.tileentity.TileEntityRelayFluix;
import de.sanandrew.mods.immersivewiring.tileentity.TileEntityTransformerFluix;
import de.sanandrew.mods.immersivewiring.util.IWConstants;
import de.sanandrew.mods.immersivewiring.wire.WireRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public final class ModelRegistry
{
    @SubscribeEvent
    @SuppressWarnings("serial")
    public static void registerModels(ModelRegistryEvent event) throws Exception {
        if( Loader.isModLoaded(IWConstants.COMPAT_APPLIEDENERGISTICS) ) {
            registerModelBlockItems(BlockRegistry.BLOCK_COIL, new HashMap<Integer, ModelResourceLocation>() {{
                put(0, new ModelResourceLocation(IWConstants.ID + ":coil_block_" + BlockCoil.Type.FLUIX, "inventory"));
                put(1, new ModelResourceLocation(IWConstants.ID + ":coil_block_" + BlockCoil.Type.FLUIX_DENSE, "inventory"));
            }});

            registerModelBlockItems(BlockRegistry.RELAY_FLUIX, new HashMap<Integer, ModelResourceLocation>() {{
                put(0, new ModelResourceLocation(IWConstants.ID + ":relay_" + BlockRelayFluix.Type.FLUIX, "inventory"));
                put(1, new ModelResourceLocation(IWConstants.ID + ":relay_" + BlockRelayFluix.Type.FLUIX_DENSE, "inventory"));
            }});

            ModelLoader.setCustomModelResourceLocation(ItemRegistry.WIRE_COIL, 0, new ModelResourceLocation(IWConstants.ID + ":wire_" + WireRegistry.Wire.FLUIX, "inventory"));
            ModelLoader.setCustomModelResourceLocation(ItemRegistry.WIRE_COIL, 1, new ModelResourceLocation(IWConstants.ID + ":wire_" + WireRegistry.Wire.FLUIX_DENSE, "inventory"));

            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTransformerFluix.class, new RenderTileIWConnectable());
            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRelayFluix.class, new RenderTileIWConnectable());
        }
    }

    static void registerModelPre112() {
        if( Loader.isModLoaded(IWConstants.COMPAT_APPLIEDENERGISTICS) ) {
            Item blockItem = Item.getItemFromBlock(BlockRegistry.BLOCK_COIL);
            if( blockItem != null ) {
                ModelBakery.registerItemVariants(blockItem, new ResourceLocation(IWConstants.ID, "coil_block_" + BlockCoil.Type.FLUIX.getName()),
                                                            new ResourceLocation(IWConstants.ID, "coil_block_" + BlockCoil.Type.FLUIX_DENSE.getName()));
            }

            blockItem = Item.getItemFromBlock(BlockRegistry.RELAY_FLUIX);
            if( blockItem != null ) {
                ModelBakery.registerItemVariants(blockItem, new ResourceLocation(IWConstants.ID, "relay_" + BlockRelayFluix.Type.FLUIX.getName()),
                                                            new ResourceLocation(IWConstants.ID, "relay_" + BlockRelayFluix.Type.FLUIX_DENSE.getName()));
            }

            ModelBakery.registerItemVariants(ItemRegistry.WIRE_COIL, new ResourceLocation(IWConstants.ID, "wire_" + WireRegistry.Wire.FLUIX),
                                                                     new ResourceLocation(IWConstants.ID, "wire_" + WireRegistry.Wire.FLUIX_DENSE));
        }
    }

    private static void registerModelBlockItems(Block block, Map<Integer, ModelResourceLocation> metaToLocation) {
        Item blockItem = Item.getItemFromBlock(block);
        if( blockItem != null ) {
            metaToLocation.forEach((meta, location) -> ModelLoader.setCustomModelResourceLocation(blockItem, meta, location));
        }
    }
}
