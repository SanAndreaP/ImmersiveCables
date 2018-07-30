/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.block;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.common.IESaveData;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public abstract class BlockConnectable
        extends BlockDirectional
{
    protected BlockConnectable(Material materialIn) {
        super(materialIn);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if( !worldIn.isRemote ) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if( tile != null ) {
                ImmersiveNetHandler.INSTANCE.clearAllConnectionsFor(Utils.toCC(tile), worldIn, new TargetingInfo(EnumFacing.UP, 0.0F, 0.0F, 0.0F));
                IESaveData.setDirty(worldIn.provider.getDimension());
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isSideSolid(IBlockState baseState, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isTopSolid(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getStateFromMeta(meta).withProperty(FACING, facing);
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        if( !world.isRemote ) {
            TileEntity tile = world.getTileEntity(pos);
            if( tile != null ) {
                ImmersiveNetHandler.INSTANCE.clearAllConnectionsFor(Utils.toCC(tile), world, new TargetingInfo(EnumFacing.UP, 0.0F, 0.0F, 0.0F));
                IESaveData.setDirty(world.provider.getDimension());
                tile.invalidate();
            }

            return super.rotateBlock(world, pos, axis);
        }

        return true;
    }

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isPistonExtending) {
        TileEntity tile = world.getTileEntity(pos);
        if( tile instanceof IEBlockInterfaces.IAdvancedSelectionBounds ) {
            ((IEBlockInterfaces.IAdvancedSelectionBounds) tile).getAdvancedSelectionBounds().forEach(aabb -> {
                if( entityBox.intersects(aabb) ) {
                    collidingBoxes.add(aabb);
                }
            });
        } else {
            super.addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, entityIn, isPistonExtending);
        }
    }

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public RayTraceResult collisionRayTrace(IBlockState blockState, World world, BlockPos pos, Vec3d start, Vec3d end) {
        TileEntity tile = world.getTileEntity(pos);
        if( tile instanceof IEBlockInterfaces.IAdvancedSelectionBounds ) {
            RayTraceResult closestRT = null;
            for( AxisAlignedBB aabb : ((IEBlockInterfaces.IAdvancedSelectionBounds) tile).getAdvancedSelectionBounds() ) {
                RayTraceResult rt = rayTrace(pos, start, end, aabb.offset(-pos.getX(), -pos.getY(), -pos.getZ()));
                double distance = closestRT == null || rt == null ? 0.0D : closestRT.hitVec.lengthSquared() - rt.hitVec.lengthSquared();
                if( rt != null && (closestRT == null || (start.lengthSquared() > end.lengthSquared() ? -1.0 : 1.0) * distance > 0.0) ) {
                    closestRT = rt;
                }
            }

            return closestRT;
        }

        return null;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        if( world.isAirBlock(pos.offset(getFacing(world, pos).getOpposite())) ) {
            if( world instanceof World ) {
                ((World) world).destroyBlock(pos, true);
            }
        }
    }

    @Override
    @Deprecated
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        this.onNeighborChange(worldIn, pos, fromPos);
    }

    private static EnumFacing getFacing(IBlockAccess world, BlockPos pos) {
        return !world.isAirBlock(pos) ? world.getBlockState(pos).getValue(BlockDirectional.FACING) : EnumFacing.UP;
    }

    public static List<AxisAlignedBB> getTransformerBB(EnumFacing facing, BlockPos pos) {
        switch( facing ) {
            case UP:
                return Arrays.asList(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D).offset(pos),
                                     new AxisAlignedBB(0.3125D, 0.5625D, 0.3125D, 0.6875D, 1.0D, 0.6875D).offset(pos));
            case DOWN:
                return Arrays.asList(new AxisAlignedBB(0.0D, 0.4375D, 0.0D, 1.0D, 1.0D, 1.0D).offset(pos),
                                     new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.4375D, 0.6875D).offset(pos));
            case NORTH:
                return Arrays.asList(new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D).offset(pos),
                                     new AxisAlignedBB(0.3125D, 0.3125D, 0.0D, 0.6875D, 0.6875D, 0.4375D).offset(pos));
            case SOUTH:
                return Arrays.asList(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D).offset(pos),
                                     new AxisAlignedBB(0.3125D, 0.3125D, 0.5625D, 0.6875D, 0.6875D, 1.0D).offset(pos));
            case EAST:
                return Arrays.asList(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D).offset(pos),
                                     new AxisAlignedBB(0.5625D, 0.3125D, 0.3125D, 1.0D, 0.6875D, 0.6875D).offset(pos));
            case WEST:
                return Arrays.asList(new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D).offset(pos),
                                     new AxisAlignedBB(0.0D, 0.3125D, 0.3125D, 0.4375D, 0.6875D, 0.6875D).offset(pos));
        }

        return null;
    }
}
