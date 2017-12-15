package de.sanandrew.mods.immersivewiring.block;

import de.sanandrew.mods.immersivewiring.util.IWConstants;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockCoil
        extends Block
{
    public static final PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);

    public BlockCoil() {
        super(Material.IRON);
        this.setHardness(2.5F);
        this.blockSoundType = SoundType.METAL;
        this.setUnlocalizedName(IWConstants.ID + ":coil_block");
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, Type.FLUIX));
        this.setRegistryName(IWConstants.ID, "coil_block");
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(TYPE).ordinal();
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta).withProperty(TYPE, Type.VALUES[meta >= 0 && meta < Type.VALUES.length ? meta : 0]);
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
        for( int i = 0; i < Type.VALUES.length; i++ ) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    public enum Type
            implements IStringSerializable
    {
        FLUIX, FLUIX_DENSE;

        public static final Type[] VALUES = values();

        @Override
        public String getName() {
            return this.name().toLowerCase();
        }

        @Override
        public String toString() {
            return this.getName();
        }
    }
}
