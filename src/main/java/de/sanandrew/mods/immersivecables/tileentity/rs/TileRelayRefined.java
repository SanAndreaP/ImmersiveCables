/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.tileentity.rs;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import com.raoulvdberge.refinedstorage.api.network.INetwork;
import com.raoulvdberge.refinedstorage.api.network.INetworkNodeVisitor;
import de.sanandrew.mods.immersivecables.block.rs.BlockRegistryRS;
import de.sanandrew.mods.immersivecables.util.ICConfiguration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class TileRelayRefined
        extends TileRefinedConnectable
{
    @Override
    public Vec3d getRaytraceOffset(IImmersiveConnectable link) {
        EnumFacing facing = this.getFacing();
        return new Vec3d(0.5D + facing.getFrontOffsetX() * 0.1D, 0.5D + facing.getFrontOffsetY() * 0.1D, 0.5D + facing.getFrontOffsetZ() * 0.1D);
    }

    @Override
    public Vec3d getConnectionOffset(ImmersiveNetHandler.Connection con) {
        EnumFacing facing = this.getFacing();
        return new Vec3d(0.5D + facing.getFrontOffsetX() * 0.0D, 0.5D + facing.getFrontOffsetY() * 0.0D, 0.5D + facing.getFrontOffsetZ() * 0.0D);
    }

    @Override
    public List<AxisAlignedBB> getAdvancedSelectionBounds() {
        if( this.cachedSelectionBounds == null ) {
            EnumFacing facing = this.getFacing();
            switch( facing ) {
                case UP:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 0.6875, 0.6875).offset(this.pos));
                    break;
                case DOWN:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.3125, 0.5625, 0.3125, 1, 0.4375, 0.6875).offset(this.pos));
                    break;
                case NORTH:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.3125, 0.3125, 0.5625, 0.6875, 0.6875, 1).offset(this.pos));
                    break;
                case SOUTH:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.3125, 0.3125, 0, 0.6875, 0.6875, 0.4375).offset(this.pos));
                    break;
                case EAST:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0, 0.3125, 0.3125, 0.4375, 0.6875, 0.6875).offset(this.pos));
                    break;
                case WEST:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.5625, 0.3125, 0.3125, 1, 0.6875, 0.6875).offset(this.pos));
                    break;
            }
        }

        return this.cachedSelectionBounds;
    }

    @Override
    public int getEnergyUsage() {
        return ICConfiguration.rsRelayPowerDrain;
    }

//    @Override
//    public boolean canConduct(EnumFacing enumFacing) {
//        return false;
//    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(BlockRegistryRS.RELAY_RS, 1, 0);
    }

    @Override
    public void onConnectionChanged(INetwork iNetworkMaster) { }

    @Override
    public void visitNodes(INetworkNodeVisitor.Operator operator) { }

    @Override
    public boolean canConduct(@Nullable EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canConnectCable(WireType cableType, TargetingInfo target) {
        return super.canConnectCable(cableType, target) && (this.limitType == null || this.limitType == cableType);
    }
}
