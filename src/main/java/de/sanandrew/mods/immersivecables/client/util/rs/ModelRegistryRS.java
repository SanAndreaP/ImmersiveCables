/*
 * ****************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 * *****************************************************************************************************************
 */
package de.sanandrew.mods.immersivecables.client.util.rs;

import de.sanandrew.mods.immersivecables.block.rs.BlockRegistryRS;
import de.sanandrew.mods.immersivecables.client.render.RenderTileIWConnectable;
import de.sanandrew.mods.immersivecables.client.util.ModelRegistry;
import de.sanandrew.mods.immersivecables.tileentity.rs.TileRelayRefined;
import de.sanandrew.mods.immersivecables.tileentity.rs.TileTransformerRefined;
import de.sanandrew.mods.immersivecables.util.ICConstants;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

@SideOnly(Side.CLIENT)
public final class ModelRegistryRS
{
    @SuppressWarnings("serial")
    public static void registerModels() {
        ModelRegistry.registerModelBlockItems(BlockRegistryRS.TRANSFORMER_RS, new HashMap<Integer, ModelResourceLocation>() {{
            put(0, new ModelResourceLocation(ICConstants.ID + ":transformer_refined", "inventory"));
        }});
        ModelRegistry.registerModelBlockItems(BlockRegistryRS.RELAY_RS, new HashMap<Integer, ModelResourceLocation>() {{
            put(0, new ModelResourceLocation(ICConstants.ID + ":relay_refined", "inventory"));
        }});

        ClientRegistry.bindTileEntitySpecialRenderer(TileTransformerRefined.class, new RenderTileIWConnectable());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRelayRefined.class, new RenderTileIWConnectable());
    }
}
