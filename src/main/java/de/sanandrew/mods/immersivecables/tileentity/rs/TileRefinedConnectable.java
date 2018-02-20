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
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireApi;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.util.Utils;
import com.raoulvdberge.refinedstorage.api.network.INetwork;
import com.raoulvdberge.refinedstorage.api.network.INetworkNodeVisitor;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNode;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNodeManager;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNodeProxy;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import com.raoulvdberge.refinedstorage.apiimpl.network.node.NetworkNode;
import com.raoulvdberge.refinedstorage.capability.CapabilityNetworkNodeProxy;
import de.sanandrew.mods.immersivecables.item.ICoilConnectable;
import de.sanandrew.mods.immersivecables.wire.Wires;
import net.minecraft.block.BlockDirectional;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class TileRefinedConnectable
        extends TileEntityImmersiveConnectable
        implements IEBlockInterfaces.IAdvancedSelectionBounds, ICoilConnectable,
                           INetworkNodeProxy<TileRefinedConnectable.NetworkNodeRefinedConnectable>
{
    List<AxisAlignedBB> cachedSelectionBounds;
    private NetworkNodeRefinedConnectable node;

    @Override
    public boolean isOverrideBox(AxisAlignedBB box, EntityPlayer entityPlayer, RayTraceResult ray, ArrayList<AxisAlignedBB> arrayList) {
        return box.grow(0.002D, 0.002D, 0.002D).contains(ray.hitVec);
    }

    @Override
    public float[] getBlockBounds() {
        return null;
    }

    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public void invalidate() {
        super.invalidate();

        if( this.getNode().getNetwork() != null ) {
            this.getNode().getNetwork().getNodeGraph().rebuild();
        }
    }

    protected EnumFacing getFacing() {
        return !this.world.isAirBlock(this.pos) ? this.world.getBlockState(this.pos).getValue(BlockDirectional.FACING) : EnumFacing.UP;
    }

    @Override
    public void connectCable(WireType cableType, TargetingInfo target, IImmersiveConnectable other) {
        super.connectCable(cableType, target, other);
        if( other instanceof TileRefinedConnectable ) {
            TileRefinedConnectable refinedTile = (TileRefinedConnectable) other;
            if( this.getNode().getNetwork() != null && refinedTile.getNode().getNetwork() == null ) {
                this.getNode().getNetwork().getNodeGraph().rebuild();
            }
        }
    }

    @Override
    public void removeCable(ImmersiveNetHandler.Connection connection) {
        super.removeCable(connection);
        if( this.getNode().getNetwork() != null ) {
            this.getNode().getNetwork().getNodeGraph().rebuild();
        }
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public boolean canConnectCable(TileEntity targetEntity, WireType type) {
        if( targetEntity instanceof INetworkNodeProxy ) {
            INetwork tgtController = ((INetworkNodeProxy) targetEntity).getNode().getNetwork();
            return tgtController == null || this.getNode().getNetwork() == null || tgtController == this.getNode().getNetwork();
        }
        return false;
    }

    @Override
    public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset) {
        String category = cableType.getCategory();
        return Wires.REFINED.category.equals(category) && (this.limitType == null || this.isRelay() && WireApi.canMix(this.limitType, cableType));
    }

    @Nonnull
    @Override
    public NetworkNodeRefinedConnectable getNode() {
        if( this.world.isRemote ) {
            if( this.node == null ) {
                this.node = new NetworkNodeRefinedConnectable();
            }

            return this.node;
        }

        INetworkNodeManager mgr = API.instance().getNetworkNodeManager(this.world);
        INetworkNode node = mgr.getNode(this.pos);

        if( node == null || !node.getId().equals(NetworkNodeRefinedConnectable.ID) ) {
            mgr.setNode(this.pos, node = new NetworkNodeRefinedConnectable());
            mgr.markForSaving();
        }

        return (NetworkNodeRefinedConnectable) node;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityNetworkNodeProxy.NETWORK_NODE_PROXY_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if( capability == CapabilityNetworkNodeProxy.NETWORK_NODE_PROXY_CAPABILITY ) {
            return CapabilityNetworkNodeProxy.NETWORK_NODE_PROXY_CAPABILITY.cast(this);
        }

        return super.getCapability(capability, facing);
    }

    public abstract int getEnergyUsage();
    public abstract ItemStack getItemStack();
    public abstract void onConnectionChanged(INetwork iNetworkMaster);
    public abstract void visitNodes(INetworkNodeVisitor.Operator operator);
    public abstract boolean canConduct(@Nullable EnumFacing direction);

    public final class NetworkNodeRefinedConnectable
            extends NetworkNode
    {
        public static final String ID = "ic_refined_connectable";

        NetworkNodeRefinedConnectable() {
            super(TileRefinedConnectable.this.world, TileRefinedConnectable.this.pos);
        }

        @Override
        public void visit(Operator operator) {
            super.visit(operator);
            TileRefinedConnectable.this.visitNodes(operator);
            Set<ImmersiveNetHandler.Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(TileRefinedConnectable.this.world,
                                                                                                          Utils.toCC(TileRefinedConnectable.this));
            if( connections != null ) {
                for( ImmersiveNetHandler.Connection connection : connections ) {
                    BlockPos opposite = connection.end;
                    if( opposite.equals(Utils.toCC(TileRefinedConnectable.this)) ) {
                        continue;
                    }

                    TileEntity connTile = TileRefinedConnectable.this.world.getTileEntity(opposite);
                    if( connTile instanceof TileRefinedConnectable ) {
                        TileRefinedConnectable connectable = (TileRefinedConnectable) connTile;
                        operator.apply(TileRefinedConnectable.this.world, opposite, connectable.getFacing().getOpposite());
                    }
                }
            }
        }

        @Override
        public int getEnergyUsage() {
            return TileRefinedConnectable.this.getEnergyUsage();
        }

        @Nonnull
        @Override
        public ItemStack getItemStack() {
            return TileRefinedConnectable.this.getItemStack();
        }

        @Override
        public boolean canConduct(@Nullable EnumFacing direction) {
            return TileRefinedConnectable.this.canConduct(direction);
        }

        @Override
        public void onConnected(INetwork iNetworkMaster) {
            super.onConnected(iNetworkMaster);
            TileRefinedConnectable.this.onConnectionChanged(iNetworkMaster);
        }

        @Override
        public void onDisconnected(INetwork iNetworkMaster) {
            super.onDisconnected(iNetworkMaster);
            TileRefinedConnectable.this.onConnectionChanged(iNetworkMaster);
        }

        @Override
        public String getId() {
            return ID;
        }
    }
}
