/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.tileentity.ae;

import appeng.api.networking.GridFlags;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import de.sanandrew.mods.immersivecables.block.ae2.FluixType;
import de.sanandrew.mods.immersivecables.util.ICConfiguration;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class TileRelayFluix
        extends TileFluixConnectable
{
    @Override
    public double getIdlePowerUsage() {
        FluixType type = this.getType();
        return type == FluixType.FLUIX_DENSE
               ? ICConfiguration.ae2DenseRelayPowerDrain
               : ICConfiguration.ae2FluixRelayPowerDrain;
    }

    @Override
    public EnumSet<EnumFacing> getConnectableSides() {
        return EnumSet.noneOf(EnumFacing.class);
    }

    @Override
    public GridFlags[] getFlags() {
        return this.getType().flags;
    }

    @Override
    public AECableType getCableConnectionType(AEPartLocation aePartLocation) {
        return this.getType().cableType;
    }

    @Override
    public Vec3d getConnectionOffset(ImmersiveNetHandler.Connection con) {
        EnumFacing facing = this.getFacing();
        double offset = this.getType() == FluixType.FLUIX_DENSE ? 0.3D : 0.1D;
        return new Vec3d(0.5D + facing.getFrontOffsetX() * offset, 0.5D + facing.getFrontOffsetY() * offset, 0.5D + facing.getFrontOffsetZ() * offset);
    }

    @Override
    protected String getWireCategory() {
        return this.getType().wireType.getCategory();
    }

    @Override
    protected boolean isRelay() {
        return true;
    }

    @Override
    public List<AxisAlignedBB> getAdvancedSelectionBounds() {
        if( this.cachedSelectionBounds == null ) {
            EnumFacing facing = this.getFacing();
            double height = this.getType() == FluixType.FLUIX_DENSE ? 0.875D : 0.625D;
            switch( facing ) {
                case UP:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, height, 0.6875D).offset(this.pos));
                    break;
                case DOWN:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.3125D, 1.0D - height, 0.3125D, 0.6875D, 1.0D, 0.6875D).offset(this.pos));
                    break;
                case NORTH:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.3125D, 0.3125D, 1.0D - height, 0.6875D, 0.6875, 1.0D).offset(this.pos));
                    break;
                case SOUTH:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.3125D, 0.3125D, 0.0D, 0.6875D, 0.6875D, height).offset(this.pos));
                    break;
                case EAST:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.0D, 0.3125D, 0.3125D, height, 0.6875D, 0.6875D).offset(this.pos));
                    break;
                case WEST:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(1.0D - height, 0.3125D, 0.3125D, 1.0D, 0.6875D, 0.6875D).offset(this.pos));
                    break;
            }
        }

        return this.cachedSelectionBounds;
    }

    private FluixType getType() {
        return this.world.isAirBlock(this.pos) ? FluixType.FLUIX : this.world.getBlockState(this.pos).getValue(FluixType.TYPE);
    }
}
