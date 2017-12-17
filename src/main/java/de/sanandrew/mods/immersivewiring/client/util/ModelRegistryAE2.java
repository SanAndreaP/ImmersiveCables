/*
 * ****************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 * *****************************************************************************************************************
 */
package de.sanandrew.mods.immersivewiring.client.util;

import de.sanandrew.mods.immersivewiring.block.ae2.BlockCoil;
import de.sanandrew.mods.immersivewiring.block.ae2.BlockRegistryAE2;
import de.sanandrew.mods.immersivewiring.block.ae2.FluixType;
import de.sanandrew.mods.immersivewiring.block.ae2.QuartzType;
import de.sanandrew.mods.immersivewiring.client.render.RenderTileIWConnectable;
import de.sanandrew.mods.immersivewiring.item.ItemRegistryAE2;
import de.sanandrew.mods.immersivewiring.tileentity.ae.TileEntityConnectorQuartz;
import de.sanandrew.mods.immersivewiring.tileentity.ae.TileEntityRelayFluix;
import de.sanandrew.mods.immersivewiring.tileentity.ae.TileEntityTransformerFluix;
import de.sanandrew.mods.immersivewiring.util.IWConstants;
import de.sanandrew.mods.immersivewiring.wire.Wires;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

@SideOnly(Side.CLIENT)
public final class ModelRegistryAE2
{
    @SuppressWarnings("serial")
    public static void registerModels(ModelRegistryEvent event) throws Exception {
        ModelRegistry.registerModelBlockItems(BlockRegistryAE2.BLOCK_COIL, new HashMap<Integer, ModelResourceLocation>() {{
            put(0, new ModelResourceLocation(IWConstants.ID + ":coil_block_" + BlockCoil.Type.FLUIX, "inventory"));
            put(1, new ModelResourceLocation(IWConstants.ID + ":coil_block_" + BlockCoil.Type.FLUIX_DENSE, "inventory"));
            put(2, new ModelResourceLocation(IWConstants.ID + ":coil_block_" + BlockCoil.Type.QUARTZ, "inventory"));
        }});

        ModelRegistry.registerModelBlockItems(BlockRegistryAE2.RELAY_FLUIX, new HashMap<Integer, ModelResourceLocation>() {{
            put(0, new ModelResourceLocation(IWConstants.ID + ":relay_" + FluixType.FLUIX, "inventory"));
            put(1, new ModelResourceLocation(IWConstants.ID + ":relay_" + FluixType.FLUIX_DENSE, "inventory"));
        }});

        ModelRegistry.registerModelBlockItems(BlockRegistryAE2.CONNECTOR_QUARTZ, new HashMap<Integer, ModelResourceLocation>() {{
            put(0, new ModelResourceLocation(IWConstants.ID + ":connector_" + QuartzType.QUARTZ, "inventory"));
        }});

        ModelRegistry.registerModelBlockItems(BlockRegistryAE2.TRANSFORMER_FLUIX, new HashMap<Integer, ModelResourceLocation>() {{
            put(0, new ModelResourceLocation(IWConstants.ID + ":transformer_" + FluixType.FLUIX, "inventory"));
            put(1, new ModelResourceLocation(IWConstants.ID + ":transformer_" + FluixType.FLUIX_DENSE, "inventory"));
        }});

        ModelLoader.setCustomModelResourceLocation(ItemRegistryAE2.WIRE_COIL, 0, new ModelResourceLocation(IWConstants.ID + ":wire_" + Wires.FLUIX, "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistryAE2.WIRE_COIL, 1, new ModelResourceLocation(IWConstants.ID + ":wire_" + Wires.FLUIX_DENSE, "inventory"));
        ModelLoader.setCustomModelResourceLocation(ItemRegistryAE2.WIRE_COIL, 2, new ModelResourceLocation(IWConstants.ID + ":wire_" + Wires.QUARTZ, "inventory"));

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTransformerFluix.class, new RenderTileIWConnectable());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRelayFluix.class, new RenderTileIWConnectable());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityConnectorQuartz.class, new RenderTileIWConnectable());
    }

    static void registerModelPre112() {
        Item blockItem = Item.getItemFromBlock(BlockRegistryAE2.BLOCK_COIL);
        if( blockItem != null ) {
            ModelBakery.registerItemVariants(blockItem, new ResourceLocation(IWConstants.ID, "coil_block_" + BlockCoil.Type.FLUIX.getName()),
                                                        new ResourceLocation(IWConstants.ID, "coil_block_" + BlockCoil.Type.FLUIX_DENSE.getName()),
                                                        new ResourceLocation(IWConstants.ID, "coil_block_" + BlockCoil.Type.QUARTZ.getName()));
        }

        blockItem = Item.getItemFromBlock(BlockRegistryAE2.RELAY_FLUIX);
        if( blockItem != null ) {
            ModelBakery.registerItemVariants(blockItem, new ResourceLocation(IWConstants.ID, "relay_" + FluixType.FLUIX.getName()),
                                                        new ResourceLocation(IWConstants.ID, "relay_" + FluixType.FLUIX_DENSE.getName()));
        }

        blockItem = Item.getItemFromBlock(BlockRegistryAE2.CONNECTOR_QUARTZ);
        if( blockItem != null ) {
            ModelBakery.registerItemVariants(blockItem, new ResourceLocation(IWConstants.ID, "connector_" + QuartzType.QUARTZ.getName()));
        }

        blockItem = Item.getItemFromBlock(BlockRegistryAE2.TRANSFORMER_FLUIX);
        if( blockItem != null ) {
            ModelBakery.registerItemVariants(blockItem, new ResourceLocation(IWConstants.ID, "transformer_" + FluixType.FLUIX.getName()),
                                                        new ResourceLocation(IWConstants.ID, "transformer_" + FluixType.FLUIX_DENSE.getName()));
        }

        ModelBakery.registerItemVariants(ItemRegistryAE2.WIRE_COIL, new ResourceLocation(IWConstants.ID, "wire_" + Wires.FLUIX),
                                                                    new ResourceLocation(IWConstants.ID, "wire_" + Wires.FLUIX_DENSE),
                                                                    new ResourceLocation(IWConstants.ID, "wire_" + Wires.QUARTZ));
    }
}
