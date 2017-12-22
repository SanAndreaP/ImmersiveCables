/* ******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.recipes;

import de.sanandrew.mods.immersivecables.util.ICConfiguration;
import de.sanandrew.mods.immersivecables.util.ItemBlockRegistry;
import de.sanandrew.mods.immersivecables.wire.Wires;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public final class RecipeRegistry
{
    public static void initialize() {
        for( Wires wire : Wires.VALUES ) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemBlockRegistry.BLOCK_COIL, 1, wire.ordinal()),
                                                       "CCC", "CIC", "CCC",
                                                       'C', new ItemStack(ItemBlockRegistry.WIRE_COIL, 1, wire.ordinal()),
                                                       'I', "ingotIron"));
        }

        if( ICConfiguration.isAe2Enabled() ) {
            RecipeRegistryAE2.initialize();
        }

        if( ICConfiguration.isRsEnabled() ) {
            RecipeRegistryRS.initialize();
        }
    }
}
