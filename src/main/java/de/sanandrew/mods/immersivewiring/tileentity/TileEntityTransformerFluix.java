/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.tileentity;

import appeng.api.networking.GridFlags;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import de.sanandrew.mods.immersivewiring.block.BlockTransformerFluix;
import de.sanandrew.mods.immersivewiring.block.BlockRegistry;
import de.sanandrew.mods.immersivewiring.wire.WireRegistry;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;

public class TileEntityTransformerFluix
        extends TileEntityFluixConnectable
{
    @Override
    public double getIdlePowerUsage() {
        return 15;//TODO: add ImmersiveIntegration.cfg.meTransformerPowerDrain;
    }

    @Override
    public EnumSet<EnumFacing> getConnectableSides() {
        return EnumSet.of(this.world.getBlockState(this.pos).getValue(BlockDirectional.FACING).getOpposite());
    }

    @Override
    public EnumSet<GridFlags> getFlags() {
        return this.isDense() ? EnumSet.of(GridFlags.DENSE_CAPACITY) : EnumSet.noneOf(GridFlags.class);
    }

    @Override
    public ItemStack getMachineRepresentation() {
        return new ItemStack(BlockRegistry.TRANSFORMER_FLUIX, 1, this.getBlockMetadata() & 1);
    }

    @Override
    public AECableType getCableConnectionType(AEPartLocation aePartLocation) {
        return this.world.getBlockState(this.pos).getValue(BlockTransformerFluix.TYPE) == BlockTransformerFluix.Type.FLUIX_DENSE ? AECableType.DENSE : AECableType.SMART;
    }

    @Override
    public boolean canConnectCable(WireType cableType, TargetingInfo target) {
        return (this.isDense() ? cableType == WireRegistry.Wire.FLUIX_DENSE.getType() : cableType == WireRegistry.Wire.FLUIX.getType()) && this.limitType == null;
    }

    @Override
    public Vec3d getRaytraceOffset(IImmersiveConnectable link) {
        EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockDirectional.FACING);
        return new Vec3d(0.5D + facing.getFrontOffsetX() * 0.5D, 0.5D + facing.getFrontOffsetY() * 0.5D, 0.5D + facing.getFrontOffsetZ() * 0.5D);
    }

    @Override
    public Vec3d getConnectionOffset(ImmersiveNetHandler.Connection con) {
        EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockDirectional.FACING);
        return new Vec3d(0.5D + facing.getFrontOffsetX() * 0.4D, 0.5D + facing.getFrontOffsetY() * 0.4D, 0.5D + facing.getFrontOffsetZ() * 0.4D);
    }

    private boolean isDense() {
        return !this.world.isAirBlock(this.pos) && this.world.getBlockState(this.pos).getValue(BlockTransformerFluix.TYPE) == BlockTransformerFluix.Type.FLUIX_DENSE;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return newState.getBlock() == BlockRegistry.TRANSFORMER_FLUIX && newState.getValue(BlockTransformerFluix.TYPE) == oldState.getValue(BlockTransformerFluix.TYPE);
    }
}
