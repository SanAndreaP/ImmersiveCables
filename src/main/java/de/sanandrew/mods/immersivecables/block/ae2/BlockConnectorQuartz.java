/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.block.ae2;

import de.sanandrew.mods.immersivecables.block.BlockConnectable;
import de.sanandrew.mods.immersivecables.tileentity.ae.TileConnectorQuartz;
import de.sanandrew.mods.immersivecables.tileentity.ae.TileFluixConnectable;
import de.sanandrew.mods.immersivecables.util.ICConstants;
import de.sanandrew.mods.immersivecables.util.ICCreativeTab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockConnectorQuartz
        extends BlockConnectable
{
    public BlockConnectorQuartz() {
        super(Material.IRON);
        this.setHardness(2.5F);
        this.blockSoundType = SoundType.METAL;
        this.setUnlocalizedName(ICConstants.ID + ":connector_quartz");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        this.setRegistryName(ICConstants.ID, "connector_quartz");
        this.setCreativeTab(ICCreativeTab.INSTANCE);
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.VALUES[meta & 7]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(FACING).getIndex() & 7);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileConnectorQuartz();
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        return false;
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
    public String getUnlocalizedName() {
        return super.getUnlocalizedName();
    }
}
