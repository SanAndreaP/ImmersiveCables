/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.tileentity.ae;

import appeng.api.AEApi;
import appeng.api.exceptions.FailedConnectionException;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridConnection;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.util.AEPartLocation;
import appeng.api.util.DimensionalCoord;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireApi;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.util.Utils;
import de.sanandrew.mods.immersivecables.util.ICConstants;
import de.sanandrew.mods.immersivecables.util.ImmersiveCables;
import net.minecraft.block.BlockDirectional;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class TileFluixConnectable
        extends TileEntityImmersiveConnectable
        implements IActionHost, IGridProxyable, ITickable, IEBlockInterfaces.IAdvancedSelectionBounds
{
    private Queue<IGridConnection> connections = new ConcurrentLinkedQueue<>();
    AENetworkProxy proxy = this.createProxy();
    public EntityPlayer ownerCache;

    List<AxisAlignedBB> cachedSelectionBounds;

    private boolean loaded = false;

    private AENetworkProxy createProxy() {
        return new AENetworkProxy(this, "proxy", new ItemStack(Blocks.BONE_BLOCK), true);
    }

    @Override
    public IGridNode getGridNode(AEPartLocation aePartLocation) {
        return this.proxy.getNode();
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        this.connections.forEach(IGridConnection::destroy);
        this.connections.clear();
        this.proxy.onChunkUnload();
    }

    @Override
    public void securityBreak() {
        this.world.destroyBlock(this.pos, true);
    }

    protected void onReady() {
        this.proxy.setVisualRepresentation(this.getMachineRepresentation());
        this.proxy.setIdlePowerUsage(this.getIdlePowerUsage());
        this.proxy.setValidSides(this.getConnectableSides());
        this.proxy.setFlags(this.getFlags());
        if( this.ownerCache != null ) {
            this.proxy.setOwner(this.ownerCache);
        }
        this.proxy.onReady();

        this.proxy.getNode().updateState();
    }

    @Override
    public boolean canConnect() {
        return true;
    }

    public ItemStack getMachineRepresentation() {
        return new ItemStack(this.getBlockType(), 1, this.getBlockMetadata() & 1);
    }

    @Override
    public void connectCable(WireType cableType, TargetingInfo target, IImmersiveConnectable other) {
        super.connectCable(cableType, target, other);
        this.connectTo(Utils.toCC(other));
    }

    @Override
    public boolean allowEnergyToPass(ImmersiveNetHandler.Connection con) {
        return false;
    }

    @Override
    public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset) {
        String category = cableType.getCategory();
        return this.getWireCategory().equals(category) && (this.limitType == null || this.isRelay() && WireApi.canMix(this.limitType, cableType));
    }

    @Override
    public void removeCable(ImmersiveNetHandler.Connection connection) {
        if( !this.world.isRemote && connection != null ) {
            BlockPos opposite = connection.end;
            if (opposite.equals(Utils.toCC(this))) {
                return;
            }

            for (IGridConnection gridConnection : this.connections) {
                DimensionalCoord locA = gridConnection.a().getGridBlock().getLocation();
                DimensionalCoord locB = gridConnection.b().getGridBlock().getLocation();
                if( (opposite.getX() == locA.x && opposite.getZ() == locA.z && opposite.getY() == locA.y) || (opposite.getX() == locB.x && opposite.getZ() == locB.z && opposite.getY() == locB.y)) {
                    gridConnection.destroy();
                    this.connections.remove(gridConnection);
                    break;
                }
            }
        }

        super.removeCable(connection);
    }

    public abstract double getIdlePowerUsage();
    public abstract EnumSet<EnumFacing> getConnectableSides();
    public abstract GridFlags[] getFlags();
    protected abstract String getWireCategory();

    @Override
    public void update() {
        if( !this.loaded && this.hasWorld() ) {
            this.loaded = true;
            if( !this.world.isRemote ) {
                this.onReady();

                Set<ImmersiveNetHandler.Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(this.world, Utils.toCC(this));
                if( connections != null ) {
                    for( ImmersiveNetHandler.Connection connection : connections ) {
                        BlockPos opposite = connection.end;
                        if( opposite.equals(Utils.toCC(this)) ) {
                            continue;
                        }

                        this.connectTo(opposite);
                    }
                }
            }
        }
    }

    @Override
    public boolean hasFastRenderer() {
        return true;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.connections.forEach(IGridConnection::destroy);
        this.connections.clear();
        this.proxy.invalidate();
    }

    @Override
    public void validate() {
        super.validate();
        this.proxy.validate();
    }

    @Override
    public AENetworkProxy getProxy() {
        return this.proxy;
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this);
    }

    @Override
    public void gridChanged() {
        for( Iterator<IGridConnection> it = this.connections.iterator(); it.hasNext(); ) {
            IGridConnection conn = it.next();
            if( isNodeInUnloadedChunk(conn.a()) || isNodeInUnloadedChunk(conn.b()) ) {
                conn.destroy();
                it.remove();
            }
        }
    }

    @Override
    public IGridNode getActionableNode() {
        return this.proxy.getNode();
    }

    private static boolean isNodeInUnloadedChunk(IGridNode node) {
        DimensionalCoord coord = node.getGridBlock().getLocation();
        return !ImmersiveCables.isChunkLoaded(node.getWorld().getChunkProvider(), coord.x >> 4, coord.z >> 4);
    }

    public void connectTo(BlockPos pos) {
        if( ImmersiveCables.isChunkLoaded(this.world.getChunkProvider(), pos.getX() >> 4, pos.getZ() >> 4) ) {
            TileEntity teOpposite = this.world.getTileEntity(pos);
            if( teOpposite instanceof IGridHost ) {
                IGridNode nodeA = ((IGridHost) teOpposite).getGridNode(AEPartLocation.INTERNAL);
                IGridNode nodeB = getGridNode(AEPartLocation.INTERNAL);
                try {
                    if( nodeA != null ) {
                        IGridConnection conn = AEApi.instance().grid().createGridConnection(nodeA, nodeB);
                        this.connections.add(conn);
                    }
                } catch( FailedConnectionException ex ) {
                    ICConstants.LOG.log(Level.DEBUG, ex.getMessage());
                }
            }
        }
    }

    protected EnumFacing getFacing() {
        return !this.world.isAirBlock(this.pos) ? this.world.getBlockState(this.pos).getValue(BlockDirectional.FACING) : EnumFacing.UP;
    }

    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        this.proxy.writeToNBT(compound);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.proxy.readFromNBT(compound);
    }

    @Override
    public boolean isOverrideBox(AxisAlignedBB box, EntityPlayer entityPlayer, RayTraceResult ray, ArrayList<AxisAlignedBB> arrayList) {
        return box.grow(0.002D, 0.002D, 0.002D).contains(ray.hitVec);
    }

    @Override
    public float[] getBlockBounds() {
        return null;
    }
}
