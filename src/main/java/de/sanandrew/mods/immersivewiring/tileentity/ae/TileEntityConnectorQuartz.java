/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.tileentity.ae;

import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridHost;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.energy.IEnergyGridProvider;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import de.sanandrew.mods.immersivewiring.block.ae2.BlockRegistryAE2;
import de.sanandrew.mods.immersivewiring.block.ae2.QuartzType;
import de.sanandrew.mods.immersivewiring.util.IWConfiguration;
import de.sanandrew.mods.immersivewiring.wire.IWireFluixType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityConnectorQuartz
        extends TileEntityFluixConnectable
        implements IGridProxyable, IEnergyGridProvider
{
    private AENetworkProxy proxy;
    private AENetworkProxy outerProxy;

    public TileEntityConnectorQuartz() {
        super();
    }

    @Override
    public void update() {
        super.update();
        if( this.world != null && !this.world.isRemote && this.outerProxy != null && this.outerProxy.getNode().getConnections().size() < 1 ) {
            TileEntity otherGrid = this.world.getTileEntity(this.pos.offset(this.getFacing().getOpposite()));
            if( otherGrid instanceof IGridHost ) {
                this.outerProxy.getNode().updateState();
            }
        }
    }

    @Override
    public double getIdlePowerUsage() {
        return IWConfiguration.ae2QuartzConnectorPowerDrain;
    }

    @Override
    public EnumSet<EnumFacing> getConnectableSides() {
        return EnumSet.of(this.getFacing().getOpposite());
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
        return QuartzType.QUARTZ;
    }

    @Override
    public void connectTo(BlockPos pos) {
        super.connectTo(pos);
        if( this.outerProxy != null ) {
            this.outerProxy.getNode().updateState();
        }
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

    @Override
    public void createAELink() {
        if( !world.isRemote ) {
            if( this.gridNode == null || this.proxy == null || this.outerProxy == null ) {
                this.proxy = new AENetworkProxy(this, "proxy", new ItemStack(BlockRegistryAE2.CONNECTOR_QUARTZ, 1), true);
                this.proxy.setValidSides(EnumSet.noneOf(EnumFacing.class));
                this.proxy.setIdlePowerUsage(0.0D);
                this.proxy.setFlags(GridFlags.CANNOT_CARRY);
                this.proxy.onReady();

                this.outerProxy = new AENetworkProxy(this, "outer", this.proxy.getMachineRepresentation(), true);
                this.outerProxy.setIdlePowerUsage(0.0D);
                this.outerProxy.setFlags(GridFlags.CANNOT_CARRY);
                this.outerProxy.setValidSides(this.getConnectableSides());
                this.outerProxy.onReady();

                this.gridNode = this.proxy.getNode();
            }

            this.proxy.getNode().updateState();
            this.outerProxy.getNode().updateState();
        }
    }

    @Override
    public void destroyAELink() {
        if( this.proxy != null ) {
            this.proxy.invalidate();
        }
        if( this.outerProxy != null ) {
            this.outerProxy.invalidate();
        }
    }

    public double extractAEPower(double amt, Actionable mode, Set<IEnergyGrid> seen) {
        double acquiredPower = 0.0D;

        IEnergyGrid eg;
        try {
            eg = this.proxy.getEnergy();
            acquiredPower += eg.extractAEPower(amt - acquiredPower, mode, seen);
        } catch (GridAccessException ignored) { }

        try {
            eg = this.outerProxy.getEnergy();
            acquiredPower += eg.extractAEPower(amt - acquiredPower, mode, seen);
        } catch (GridAccessException ignored) { }

        return acquiredPower;
    }

    public double injectAEPower(double amt, Actionable mode, Set<IEnergyGrid> seen) {
        IEnergyGrid eg;
        try {
            eg = this.proxy.getEnergy();
            if (!seen.contains(eg)) {
                return eg.injectAEPower(amt, mode, seen);
            }
        } catch (GridAccessException ignored ) { }

        try {
            eg = this.outerProxy.getEnergy();
            if (!seen.contains(eg)) {
                return eg.injectAEPower(amt, mode, seen);
            }
        } catch (GridAccessException ignored ) { }

        return amt;
    }

    public double getEnergyDemand(double amt, Set<IEnergyGrid> seen) {
        double demand = 0.0D;

        IEnergyGrid eg;
        try {
            eg = this.proxy.getEnergy();
            demand += eg.getEnergyDemand(amt - demand, seen);
        } catch (GridAccessException ignored ) { }

        try {
            eg = this.outerProxy.getEnergy();
            demand += eg.getEnergyDemand(amt - demand, seen);
        } catch (GridAccessException ignored ) { }

        return demand;
    }

    @Override
    public AENetworkProxy getProxy() {
        return this.proxy;
    }
}
