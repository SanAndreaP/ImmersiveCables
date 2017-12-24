/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.block.ae2;

import de.sanandrew.mods.immersivecables.block.BlockConnectable;
import de.sanandrew.mods.immersivecables.tileentity.ae.TileFluixConnectable;
import de.sanandrew.mods.immersivecables.tileentity.ae.TileTransformerFluix;
import de.sanandrew.mods.immersivecables.util.ICConstants;
import de.sanandrew.mods.immersivecables.util.ICCreativeTab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTransformerFluix
        extends BlockConnectable
{
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockTransformerFluix() {
        super(Material.IRON);
        this.setHardness(2.5F);
        this.blockSoundType = SoundType.METAL;
        this.setUnlocalizedName(ICConstants.ID + ":transformer_fluix");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FluixType.TYPE, FluixType.FLUIX).withProperty(FACING, EnumFacing.UP));
        this.setRegistryName(ICConstants.ID, "transformer_fluix");
        this.setCreativeTab(ICCreativeTab.INSTANCE);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(FluixType.TYPE).ordinal();
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FluixType.TYPE, FluixType.VALUES[meta & 1])
                   .withProperty(FACING, EnumFacing.VALUES[(meta >> 1) & 7]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(FluixType.TYPE).ordinal() & 1) | ((state.getValue(FACING).getIndex() & 7) << 1);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileTransformerFluix();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FluixType.TYPE, FACING, ACTIVE);
    }

    @Override
    public void getSubBlocks(CreativeTabs creativeTabs, NonNullList<ItemStack> items) {
        for( int i = 0; i < FluixType.VALUES.length; i++ ) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if( !world.isRemote && placer instanceof EntityPlayer ) {
            TileEntity transformer = world.getTileEntity(pos);
            if( transformer instanceof TileFluixConnectable ) {
                ((TileFluixConnectable) transformer).ownerCache = (EntityPlayer) placer;
            }
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) { }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(ACTIVE, isMeActive(world, pos));
    }

    private static boolean isMeActive(IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        return te instanceof TileTransformerFluix && ((TileTransformerFluix) te).isMeActive();
    }
}
