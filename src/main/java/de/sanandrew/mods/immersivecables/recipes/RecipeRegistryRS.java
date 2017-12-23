/* ******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.recipes;

import de.sanandrew.mods.immersivecables.block.rs.BlockRegistryRS;
import de.sanandrew.mods.immersivecables.util.ICConstants;
import de.sanandrew.mods.immersivecables.util.ItemBlockRegistry;
import de.sanandrew.mods.immersivecables.wire.Wires;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.Objects;

public final class RecipeRegistryRS
{
    public static void initialize(RegistryEvent.Register<IRecipe> event, ResourceLocation group) {
        ItemStack enrichedIron = new ItemStack(Objects.requireNonNull(Item.getByNameOrId("refinedstorage:quartz_enriched_iron")), 1, 0);

        event.getRegistry().register(new ShapedOreRecipe(group, new ItemStack(ItemBlockRegistry.WIRE_COIL, 4, Wires.REFINED.ordinal()),
                                                         " I ", "WSW", " I ",
                                                         'I', enrichedIron,
                                                         'W', "wireAluminum",
                                                         'S', "stickWood")
                                             .setRegistryName(ICConstants.ID, "wire_coil_refined_s"));
        event.getRegistry().register(new ShapedOreRecipe(group, new ItemStack(ItemBlockRegistry.WIRE_COIL, 4, Wires.REFINED.ordinal()),
                                                         " I ", "WSW", " I ",
                                                         'I', enrichedIron,
                                                         'W', "wireAluminum",
                                                         'S', "stickTreatedWood")
                                             .setRegistryName(ICConstants.ID, "wire_coil_refined_t"));

        event.getRegistry().register(new ShapedOreRecipe(group, new ItemStack(BlockRegistryRS.RELAY_RS, 8, 0),
                                                         " I ", "SIS",
                                                         'I', enrichedIron,
                                                         'S', "stone")
                                             .setRegistryName(ICConstants.ID, "relay_refined"));

        event.getRegistry().register(new ShapedOreRecipe(group, new ItemStack(BlockRegistryRS.TRANSFORMER_RS, 1, 0),
                                                         " R ", "ICI", "III",
                                                         'R', new ItemStack(BlockRegistryRS.RELAY_RS, 1, 0),
                                                         'I', enrichedIron,
                                                         'C', new ItemStack(ItemBlockRegistry.BLOCK_COIL, 1, Wires.REFINED.ordinal()))
                                             .setRegistryName(ICConstants.ID, "transformer_refined"));
    }
}
