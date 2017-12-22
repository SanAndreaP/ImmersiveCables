/* ******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.recipes;

import de.sanandrew.mods.immersivecables.block.ae2.BlockRegistryAE2;
import de.sanandrew.mods.immersivecables.block.ae2.FluixType;
import de.sanandrew.mods.immersivecables.util.ItemBlockRegistry;
import de.sanandrew.mods.immersivecables.wire.Wires;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public final class RecipeRegistryAE2
{
    public static void initialize() {
        ItemStack fluixCrystal = new ItemStack(Item.getByNameOrId("appliedenergistics2:material"), 1, 12);
        ItemStack denseFluixCrystal = new ItemStack(Item.getByNameOrId("appliedenergistics2:material"), 1, 7);
        ItemStack quartzCrystal = new ItemStack(Item.getByNameOrId("appliedenergistics2:material"), 1, 11);
        ItemStack skyStone = new ItemStack(Item.getByNameOrId("appliedenergistics2:smooth_sky_stone_block"), 1);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemBlockRegistry.WIRE_COIL, 4, Wires.FLUIX.ordinal()),
                                                   " F ", "WSW", " F ",
                                                   'F', fluixCrystal,
                                                   'W', "wireAluminum",
                                                   'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemBlockRegistry.WIRE_COIL, 4, Wires.FLUIX.ordinal()),
                                                   " F ", "WSW", " F ",
                                                   'F', fluixCrystal,
                                                   'W', "wireAluminum",
                                                   'S', "stickTreatedWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemBlockRegistry.WIRE_COIL, 4, Wires.FLUIX_DENSE.ordinal()),
                                                   " F ", "WSW", " F ",
                                                   'F', denseFluixCrystal,
                                                   'W', "wireSteel",
                                                   'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemBlockRegistry.WIRE_COIL, 4, Wires.FLUIX_DENSE.ordinal()),
                                                   " F ", "WSW", " F ",
                                                   'F', denseFluixCrystal,
                                                   'W', "wireSteel",
                                                   'S', "stickTreatedWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemBlockRegistry.WIRE_COIL, 4, Wires.QUARTZ.ordinal()),
                                                   " F ", "WSW", " F ",
                                                   'F', quartzCrystal,
                                                   'W', "wireCopper",
                                                   'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemBlockRegistry.WIRE_COIL, 4, Wires.QUARTZ.ordinal()),
                                                   " F ", "WSW", " F ",
                                                   'F', quartzCrystal,
                                                   'W', "wireCopper",
                                                   'S', "stickTreatedWood"));

        GameRegistry.addRecipe(new ItemStack(BlockRegistryAE2.RELAY_FLUIX, 8, FluixType.FLUIX.ordinal()),
                               " F ", "SFS",
                               'F', fluixCrystal,
                               'S', skyStone);

        GameRegistry.addRecipe(new ItemStack(BlockRegistryAE2.RELAY_FLUIX, 8, FluixType.FLUIX_DENSE.ordinal()),
                               " F ", "SFS", "SFS",
                               'F', denseFluixCrystal,
                               'S', skyStone);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockRegistryAE2.CONNECTOR_QUARTZ, 4),
                                                   " Q ", "GQG",
                                                   'Q', quartzCrystal,
                                                   'G', "blockGlass"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockRegistryAE2.TRANSFORMER_FLUIX, 1, FluixType.FLUIX.ordinal()),
                                                   " R ", "ICI", "III",
                                                   'R', new ItemStack(BlockRegistryAE2.RELAY_FLUIX, 1, FluixType.FLUIX.ordinal()),
                                                   'I', "ingotIron",
                                                   'C', new ItemStack(ItemBlockRegistry.BLOCK_COIL, 1, Wires.FLUIX.ordinal())));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockRegistryAE2.TRANSFORMER_FLUIX, 1, FluixType.FLUIX_DENSE.ordinal()),
                                                   " R ", "ICI", "III",
                                                   'R', new ItemStack(BlockRegistryAE2.RELAY_FLUIX, 1, FluixType.FLUIX_DENSE.ordinal()),
                                                   'I', "ingotIron",
                                                   'C', new ItemStack(ItemBlockRegistry.BLOCK_COIL, 1, Wires.FLUIX_DENSE.ordinal())));
    }
}
