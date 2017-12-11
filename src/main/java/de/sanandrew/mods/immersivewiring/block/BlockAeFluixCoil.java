package de.sanandrew.mods.immersivewiring.block;

import de.sanandrew.mods.immersivewiring.util.IWConstants;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import java.util.List;

public class BlockAeFluixCoil
        extends Block
{
    public static final PropertyEnum<Coil> TYPE = PropertyEnum.create("type", Coil.class);

    public BlockAeFluixCoil() {
        super(Material.IRON);
        this.setHardness(2.5F);
        this.blockSoundType = SoundType.METAL;
        this.setUnlocalizedName(IWConstants.ID + ":fluix_coil");
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, Coil.FLUIX_COIL));
        this.setRegistryName(IWConstants.ID, "fluix_coil");
        this.setCreativeTab(CreativeTabs.REDSTONE);
//        setCreativeTab(ImmersiveIntegration.iiCreativeTab);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(TYPE).ordinal();
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta).withProperty(TYPE, meta >= 0 && meta < Coil.VALUES.length ? Coil.VALUES[meta] : Coil.FLUIX_COIL);
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
        for( int i = 0; i < Coil.VALUES.length; i++ ) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    public enum Coil
            implements IStringSerializable
    {
        FLUIX_COIL, FLUIX_COIL_DENSE;

        public static final Coil[] VALUES = values();

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
