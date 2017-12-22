/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.block.rs;

import de.sanandrew.mods.immersivecables.block.BlockConnectable;
import de.sanandrew.mods.immersivecables.tileentity.rs.TileTransformerRefined;
import de.sanandrew.mods.immersivecables.util.ICConstants;
import de.sanandrew.mods.immersivecables.util.ICCreativeTab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockTransformerRefined
        extends BlockConnectable
{
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockTransformerRefined() {
        super(Material.IRON);
        this.setHardness(2.5F);
        this.blockSoundType = SoundType.METAL;
        this.setUnlocalizedName(ICConstants.ID + ":transformer_refined");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        this.setRegistryName(ICConstants.ID, "transformer_refined");
        this.setCreativeTab(ICCreativeTab.INSTANCE);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.VALUES[meta & 7]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex() & 7;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileTransformerRefined();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ACTIVE);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(ACTIVE, isActive(world, pos));
    }

    private static boolean isActive(IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        return te instanceof TileTransformerRefined && ((TileTransformerRefined) te).isRsActive();
    }
}
