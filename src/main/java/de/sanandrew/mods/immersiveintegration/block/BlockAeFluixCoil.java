package de.sanandrew.mods.immersiveintegration.block;

import de.sanandrew.mods.immersiveintegration.util.IIConstants;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockAeFluixCoil
        extends Block
{
    public static final PropertyEnum<Coil> TYPE = PropertyEnum.create("type", Coil.class);

    public BlockAeFluixCoil() {
        super(Material.IRON);
        this.setHardness(2.5F);
        this.blockSoundType = SoundType.METAL;
        this.setUnlocalizedName(IIConstants.ID + ":fluix_coil");
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, Coil.FLUIX_COIL));
        this.setRegistryName(IIConstants.ID, "fluix_coil");
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
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, stack);
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

    public enum Coil implements IStringSerializable
    {
        FLUIX_COIL, FLUIX_COIL_DENSE;

        public static final Coil[] VALUES = values();

        public String getName() {
            return this.name().toLowerCase();
        }

        @Override
        public String toString() {
            return this.getName();
        }
    }

    public static final class ItemBlockCoil
            extends ItemBlock
    {
        public ItemBlockCoil(Block block) {
            super(block);
            this.setHasSubtypes(true);
            this.setMaxDamage(0);
            this.setRegistryName(block.getRegistryName());
        }

        @Override
        public int getMetadata(int damage) {
            return damage;
        }
    }
}
