/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.tileentity.ae;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import de.sanandrew.mods.immersivewiring.block.ae2.FluixType;
import de.sanandrew.mods.immersivewiring.util.IWConfiguration;
import de.sanandrew.mods.immersivewiring.wire.IWireFluixType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class TileEntityRelayFluix
        extends TileEntityFluixConnectable
{
    @Override
    public double getIdlePowerUsage() {
        IWireFluixType type = this.getType();
        return type == FluixType.FLUIX_DENSE
               ? IWConfiguration.ae2DenseRelayPowerDrain
               : IWConfiguration.ae2FluixRelayPowerDrain;
    }

    @Override
    public EnumSet<EnumFacing> getConnectableSides() {
        return EnumSet.noneOf(EnumFacing.class);
    }

    @Override
    public boolean canConnectCable(WireType cableType, TargetingInfo target) {
        return super.canConnectCable(cableType, target) && (this.limitType == null || this.limitType == cableType);
    }

    @Override
    public Vec3d getRaytraceOffset(IImmersiveConnectable link) {
        EnumFacing facing = this.getFacing();
        double offset = this.getType().getRelayOffset() + 0.1D;
        return new Vec3d(0.5D + facing.getFrontOffsetX() * offset, 0.5D + facing.getFrontOffsetY() * offset, 0.5D + facing.getFrontOffsetZ() * offset);
    }

    @Override
    public Vec3d getConnectionOffset(ImmersiveNetHandler.Connection con) {
        EnumFacing facing = this.getFacing();
        double offset = this.getType().getRelayOffset();
        return new Vec3d(0.5D + facing.getFrontOffsetX() * offset, 0.5D + facing.getFrontOffsetY() * offset, 0.5D + facing.getFrontOffsetZ() * offset);
    }

    protected IWireFluixType getType() {
        return !this.world.isAirBlock(this.pos) ? this.world.getBlockState(this.pos).getValue(FluixType.TYPE) : FluixType.FLUIX;
    }

    @Override
    public List<AxisAlignedBB> getAdvancedSelectionBounds() {
        if( this.cachedSelectionBounds == null ) {
            if( this.loaded ) {
                EnumFacing facing = this.getFacing();
                double height = this.getType().getRelayHeight();
                switch( facing ) {
                    case UP:
                        this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, height, 0.6875).offset(this.pos));
                        break;
                    case DOWN:
                        this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.3125, 1 - height, 0.3125, 0.6875, 1, 0.6875).offset(this.pos));
                        break;
                    case NORTH:
                        this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.3125, 0.3125, 1 - height, 0.6875, 0.6875, 1).offset(this.pos));
                        break;
                    case SOUTH:
                        this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.3125, 0.3125, 0, 0.6875, 0.6875, height).offset(this.pos));
                        break;
                    case EAST:
                        this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0, 0.3125, 0.3125, height, 0.6875, 0.6875).offset(this.pos));
                        break;
                    case WEST:
                        this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(1 - height, 0.3125, 0.3125, 1, 0.6875, 0.6875).offset(this.pos));
                        break;
                }
            } else {
                return Collections.emptyList();
            }
        }

        return this.cachedSelectionBounds;
    }
}
