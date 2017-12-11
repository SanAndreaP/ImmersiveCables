package de.sanandrew.mods.immersivewiring.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.IStringSerializable;
import unwrittenfun.minecraft.immersiveintegration.ImmersiveIntegration;

import java.util.List;

public class BlockAEDecoration extends Block
{
    public static final PropertyEnum<AEDecoration> TYPE = PropertyEnum.create("type", AEDecoration.class);

    public BlockAEDecoration(String key) {
        super(Material.iron);
        setBlockName(key);
        setBlockTextureName(key);
        setCreativeTab(ImmersiveIntegration.iiCreativeTab);
        setHardness(2.5f);
        setStepSound(Block.soundTypeMetal);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        for( int i = 0; i < IIBlocks.AE_DECORATION_KEYS.length; i++ ) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    public enum AEDecoration implements IStringSerializable
    {
        FLUIX_COIL,
        DENSE_FLUIX_COIL;

        public String getName() {
            return this.name();
        }
    }
}
