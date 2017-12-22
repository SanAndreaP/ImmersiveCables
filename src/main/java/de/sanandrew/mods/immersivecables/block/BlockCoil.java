package de.sanandrew.mods.immersivecables.block;

import de.sanandrew.mods.immersivecables.util.ICConstants;
import de.sanandrew.mods.immersivecables.util.ICCreativeTab;
import de.sanandrew.mods.immersivecables.wire.Wires;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BlockCoil
        extends Block
{
    public static final PropertyEnum<Wires> TYPE = PropertyEnum.create("type", Wires.class);

    public BlockCoil() {
        super(Material.IRON);
        this.setHardness(2.5F);
        this.blockSoundType = SoundType.METAL;
        this.setUnlocalizedName(ICConstants.ID + ":coil_block");
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, Wires.FLUIX));
        this.setRegistryName(ICConstants.ID, "coil_block");
        this.setCreativeTab(ICCreativeTab.INSTANCE);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(TYPE).ordinal();
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta).withProperty(TYPE, Wires.VALUES[meta >= 0 && meta < Wires.VALUES.length ? meta : 0]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        for( int i = 0; i < Wires.VALUES.length; i++ ) {
            list.add(new ItemStack(item, 1, i));
        }
    }
}
