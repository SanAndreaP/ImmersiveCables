/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.tileentity.ae;

import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridHost;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.energy.IEnergyGridProvider;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import de.sanandrew.mods.immersivecables.block.ae2.BlockRegistryAE2;
import de.sanandrew.mods.immersivecables.util.ICConfiguration;
import de.sanandrew.mods.immersivecables.wire.Wires;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

public class TileConnectorQuartz
        extends TileFluixConnectable
        implements IGridProxyable, IEnergyGridProvider
{
    private AENetworkProxy proxy;
    private AENetworkProxy outerProxy;

    public TileConnectorQuartz() {
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
        return ICConfiguration.ae2QuartzConnectorPowerDrain;
    }

    @Override
    public EnumSet<EnumFacing> getConnectableSides() {
        return EnumSet.of(this.getFacing().getOpposite());
    }

    @Override
    public boolean canConnectCable(WireType cableType, TargetingInfo target) {
        return Wires.QUARTZ.type == cableType && (this.limitType == null || this.limitType == cableType);
    }

    @Override
    public EnumSet<GridFlags> getFlags() {
        return EnumSet.of(GridFlags.CANNOT_CARRY);
    }

    @Override
    public AECableType getCableConnectionType(AEPartLocation aePartLocation) {
        return AECableType.GLASS;
    }

    @Override
    public Vec3d getRaytraceOffset(IImmersiveConnectable link) {
        EnumFacing facing = this.getFacing();
        return new Vec3d(0.5D + facing.getFrontOffsetX(), 0.5D + facing.getFrontOffsetY(), 0.5D + facing.getFrontOffsetZ());
    }

    @Override
    public Vec3d getConnectionOffset(ImmersiveNetHandler.Connection con) {
        EnumFacing facing = this.getFacing();
        return new Vec3d(0.5D - facing.getFrontOffsetX() * 0.1D, 0.5D - facing.getFrontOffsetY() * 0.1D, 0.5D - facing.getFrontOffsetZ() * 0.1D);
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
            EnumFacing facing = this.getFacing();
            switch( facing ) {
                case UP:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.4375D, 0.6875).offset(this.pos));
                    break;
                case DOWN:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.3125D, 0.5625D, 0.3125D, 0.6875D, 1.0D, 0.6875).offset(this.pos));
                    break;
                case NORTH:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.3125D, 0.3125D, 0.5625D, 0.6875D, 0.6875D, 1.0D).offset(this.pos));
                    break;
                case SOUTH:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.3125D, 0.3125D, 0.0D, 0.6875D, 0.6875D, 0.4375D).offset(this.pos));
                    break;
                case EAST:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.0D, 0.3125D, 0.3125D, 0.4375D, 0.6875D, 0.6875).offset(this.pos));
                    break;
                case WEST:
                    this.cachedSelectionBounds = Collections.singletonList(new AxisAlignedBB(0.5625D, 0.3125D, 0.3125D, 1.0D, 0.6875D, 0.6875).offset(this.pos));
                    break;
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

    public Collection<IEnergyGridProvider> providers() {
        LinkedList<IEnergyGridProvider> providers = new LinkedList<>();

        IEnergyGrid eg;
        try {
            eg = this.proxy.getEnergy();
            providers.add(eg);
        } catch (GridAccessException ignored) { }

        try {
            eg = this.outerProxy.getEnergy();
            providers.add(eg);
        } catch (GridAccessException ignored) { }

        return providers;
    }

    @Override
    public double extractProviderPower(double amt, Actionable mode) {
        return 0;
//        double acquiredPower = 0.0D;
//
//        IEnergyGrid eg;
//        try {
//            eg = this.proxy.getEnergy();
//            acquiredPower += eg.extractAEPower(amt - acquiredPower, mode, PowerMultiplier.ONE);
//        } catch (GridAccessException ignored) { }
//
//        try {
//            eg = this.outerProxy.getEnergy();
//            acquiredPower += eg.extractAEPower(amt - acquiredPower, mode, PowerMultiplier.ONE);
//        } catch (GridAccessException ignored) { }
//
//        return acquiredPower;
    }

    @Override
    public double injectProviderPower(double v, @Nonnull Actionable actionable) {
        return v;
//        IEnergyGrid eg;
//        try {
//            eg = this.proxy.getEnergy();
//            if (!seen.contains(eg)) {
//                return eg.injectAEPower(amt, mode, seen);
//            }
//        } catch (GridAccessException ignored ) { }
//
//        try {
//            eg = this.outerProxy.getEnergy();
//            if (!seen.contains(eg)) {
//                return eg.injectAEPower(amt, mode, seen);
//            }
//        } catch (GridAccessException ignored ) { }
//
//        return amt;
    }

    @Override
    public double getProviderEnergyDemand(double amt) {
        return 0.0D;
    }

    @Override
    public double getProviderStoredEnergy() {
        return 0.0D;
    }

    @Override
    public double getProviderMaxEnergy() {
        return 0.0D;
    }

//    public double getEnergyDemand(double amt, Set<IEnergyGrid> seen) {
//        double demand = 0.0D;
//
//        IEnergyGrid eg;
//        try {
//            eg = this.proxy.getEnergy();
//            demand += eg.getEnergyDemand(amt - demand, seen);
//        } catch (GridAccessException ignored ) { }
//
//        try {
//            eg = this.outerProxy.getEnergy();
//            demand += eg.getEnergyDemand(amt - demand, seen);
//        } catch (GridAccessException ignored ) { }
//
//        return demand;
//    }

    @Override
    public AENetworkProxy getProxy() {
        return this.proxy;
    }
}
