/* ******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.recipes;

import de.sanandrew.mods.immersivecables.block.rs.BlockRegistryRS;
import de.sanandrew.mods.immersivecables.util.ItemBlockRegistry;
import de.sanandrew.mods.immersivecables.wire.Wires;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public final class RecipeRegistryRS
{
    public static void initialize() {
        ItemStack enrichedIron = new ItemStack(Item.getByNameOrId("refinedstorage:quartz_enriched_iron"), 1, 0);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemBlockRegistry.WIRE_COIL, 4, Wires.REFINED.ordinal()),
                                                   " I ", "WSW", " I ",
                                                   'I', enrichedIron,
                                                   'W', "wireAluminum",
                                                   'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemBlockRegistry.WIRE_COIL, 4, Wires.REFINED.ordinal()),
                                                   " I ", "WSW", " I ",
                                                   'I', enrichedIron,
                                                   'W', "wireAluminum",
                                                   'S', "stickTreatedWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockRegistryRS.RELAY_RS, 8, 0),
                               " I ", "SIS",
                               'I', enrichedIron,
                               'S', "stone"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockRegistryRS.TRANSFORMER_RS, 1, 0),
                                                   " R ", "ICI", "III",
                                                   'R', new ItemStack(BlockRegistryRS.RELAY_RS, 1, 0),
                                                   'I', enrichedIron,
                                                   'C', new ItemStack(ItemBlockRegistry.BLOCK_COIL, 1, Wires.REFINED.ordinal())));
    }
}
