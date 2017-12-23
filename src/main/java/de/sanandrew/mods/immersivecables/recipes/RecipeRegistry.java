/* ******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.recipes;

import de.sanandrew.mods.immersivecables.util.ICConfiguration;
import de.sanandrew.mods.immersivecables.util.ICConstants;
import de.sanandrew.mods.immersivecables.util.ItemBlockRegistry;
import de.sanandrew.mods.immersivecables.wire.Wires;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod.EventBusSubscriber(Side.CLIENT)
public final class RecipeRegistry
{
    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        ResourceLocation group = new ResourceLocation(ICConstants.ID, "recipes");
        for( Wires wire : Wires.VALUES ) {
            event.getRegistry().register(new ShapedOreRecipe(group, new ItemStack(ItemBlockRegistry.BLOCK_COIL, 1, wire.ordinal()),
                                                             "CCC", "CIC", "CCC",
                                                             'C', new ItemStack(ItemBlockRegistry.WIRE_COIL, 1, wire.ordinal()),
                                                             'I', "ingotIron")
                                                 .setRegistryName(ICConstants.ID, "wire_coil_block_" + wire.getName().toLowerCase()));
        }

        if( ICConfiguration.isAe2Enabled() ) {
            RecipeRegistryAE2.initialize(event, group);
        }

        if( ICConfiguration.isRsEnabled() ) {
            RecipeRegistryRS.initialize(event, group);
        }
    }
}
