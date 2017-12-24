/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.block.rs;

import de.sanandrew.mods.immersivecables.block.BlockConnectable;
import de.sanandrew.mods.immersivecables.tileentity.rs.TileRelayRefined;
import de.sanandrew.mods.immersivecables.util.ICConstants;
import de.sanandrew.mods.immersivecables.util.ICCreativeTab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRelayRefined
        extends BlockConnectable
{
    public BlockRelayRefined() {
        super(Material.IRON);
        this.setHardness(2.5F);
        this.blockSoundType = SoundType.METAL;
        this.setUnlocalizedName(ICConstants.ID + ":relay_refined");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        this.setRegistryName(ICConstants.ID, "relay_refined");
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
        return new TileRelayRefined();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        return false;
    }
}
