/*
 * ****************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 * *****************************************************************************************************************
 */
package de.sanandrew.mods.immersivecables.client.util.ae;

import de.sanandrew.mods.immersivecables.block.ae2.BlockRegistryAE2;
import de.sanandrew.mods.immersivecables.block.ae2.FluixType;
import de.sanandrew.mods.immersivecables.client.render.RenderTileIWConnectable;
import de.sanandrew.mods.immersivecables.client.util.ModelRegistry;
import de.sanandrew.mods.immersivecables.tileentity.ae.TileConnectorQuartz;
import de.sanandrew.mods.immersivecables.tileentity.ae.TileRelayFluix;
import de.sanandrew.mods.immersivecables.tileentity.ae.TileTransformerFluix;
import de.sanandrew.mods.immersivecables.util.ICConstants;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

@SideOnly(Side.CLIENT)
public final class ModelRegistryAE2
{
    @SuppressWarnings("serial")
    public static void registerModels() {

        ModelRegistry.registerModelBlockItems(BlockRegistryAE2.RELAY_FLUIX, new HashMap<Integer, ModelResourceLocation>() {{
            put(0, new ModelResourceLocation(ICConstants.ID + ":relay_" + FluixType.FLUIX, "inventory"));
            put(1, new ModelResourceLocation(ICConstants.ID + ":relay_" + FluixType.FLUIX_DENSE, "inventory"));
        }});

        ModelRegistry.registerModelBlockItems(BlockRegistryAE2.CONNECTOR_QUARTZ, new HashMap<Integer, ModelResourceLocation>() {{
            put(0, new ModelResourceLocation(ICConstants.ID + ":connector_quartz", "inventory"));
        }});

        ModelRegistry.registerModelBlockItems(BlockRegistryAE2.TRANSFORMER_FLUIX, new HashMap<Integer, ModelResourceLocation>() {{
            put(0, new ModelResourceLocation(ICConstants.ID + ":transformer_" + FluixType.FLUIX, "inventory"));
            put(1, new ModelResourceLocation(ICConstants.ID + ":transformer_" + FluixType.FLUIX_DENSE, "inventory"));
        }});

        ClientRegistry.bindTileEntitySpecialRenderer(TileTransformerFluix.class, new RenderTileIWConnectable());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRelayFluix.class, new RenderTileIWConnectable());
        ClientRegistry.bindTileEntitySpecialRenderer(TileConnectorQuartz.class, new RenderTileIWConnectable());
    }

    public static void registerModelPre112() {
        Item blockItem = Item.getItemFromBlock(BlockRegistryAE2.RELAY_FLUIX);
        if( blockItem != null ) {
            ModelBakery.registerItemVariants(blockItem, new ResourceLocation(ICConstants.ID, "relay_" + FluixType.FLUIX.getName()),
                                                        new ResourceLocation(ICConstants.ID, "relay_" + FluixType.FLUIX_DENSE.getName()));
        }

        blockItem = Item.getItemFromBlock(BlockRegistryAE2.CONNECTOR_QUARTZ);
        if( blockItem != null ) {
            ModelBakery.registerItemVariants(blockItem, new ResourceLocation(ICConstants.ID, "connector_quartz"));
        }

        blockItem = Item.getItemFromBlock(BlockRegistryAE2.TRANSFORMER_FLUIX);
        if( blockItem != null ) {
            ModelBakery.registerItemVariants(blockItem, new ResourceLocation(ICConstants.ID, "transformer_" + FluixType.FLUIX.getName()),
                                                        new ResourceLocation(ICConstants.ID, "transformer_" + FluixType.FLUIX_DENSE.getName()));
        }
    }
}
