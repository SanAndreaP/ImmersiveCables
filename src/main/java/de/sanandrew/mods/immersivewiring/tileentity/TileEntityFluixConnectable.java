/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.tileentity;

import appeng.api.AEApi;
import appeng.api.exceptions.FailedConnection;
import appeng.api.networking.GridFlags;
import appeng.api.networking.GridNotification;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridBlock;
import appeng.api.networking.IGridConnection;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.util.AEColor;
import appeng.api.util.AEPartLocation;
import appeng.api.util.DimensionalCoord;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.util.Utils;
import de.sanandrew.mods.immersivewiring.util.IWConstants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

public abstract class TileEntityFluixConnectable
        extends TileEntityImmersiveConnectable
        implements IGridHost, IGridBlock, ITickable
{
    public IGridNode gridNode;
    public ArrayList<IGridConnection> connections = new ArrayList<>();

    private boolean loaded = false;

    @Override
    public boolean isWorldAccessible() {
        return true;
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this);
    }

    @Override
    public AEColor getGridColor() {
        return AEColor.TRANSPARENT;
    }

    @Override
    public void onGridNotification(GridNotification gridNotification) {

    }

    @Override
    public void setNetworkStatus(IGrid iGrid, int i) {

    }

    @Override
    public IGridHost getMachine() {
        return this;
    }

    @Override
    public void gridChanged() {

    }

    @Override
    public IGridNode getGridNode(AEPartLocation aePartLocation) {
        if( this.gridNode == null ) {
            this.createAELink();
        }
        return this.gridNode;
    }

    @Override
    public void securityBreak() {

    }

    @Override
    public boolean canConnect() {
        return true;
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
    public void removeCable(ImmersiveNetHandler.Connection connection) {
        if (!this.world.isRemote) {
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

    @Override
    public void update() {
        if( !this.loaded && this.hasWorld() && !this.world.isRemote ) {
            this.loaded = true;
            this.createAELink();

            Set<ImmersiveNetHandler.Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(this.world, Utils.toCC(this));
            if( connections != null && connections.iterator().hasNext() ) {
                ImmersiveNetHandler.Connection connection = connections.iterator().next();
                BlockPos opposite = connection.end;
                if( opposite.equals(Utils.toCC(this)) ) {
                    return;
                }

                this.connectTo(opposite);
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
        if( this.world != null && !this.world.isRemote ) {
            this.destroyAELink();
        }
    }

    @Override
    public void onChunkUnload() {
        if( this.world != null && !this.world.isRemote ) {
            this.destroyAELink();
        }
    }

    public boolean isMeActive() {
        return this.gridNode != null && this.gridNode.isActive();
    }

    public void createAELink() {
        if( !this.world.isRemote ) {
            if( this.gridNode == null ) {
                this.gridNode = AEApi.instance().createGridNode(this);
            }
            this.gridNode.updateState();
            this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
        }
    }

    public void destroyAELink() {
        if( this.gridNode != null ) {
            this.gridNode.destroy();
        }
    }

    public void connectTo(BlockPos pos) {
        TileEntity teOpposite = this.world.getTileEntity(pos);
        if( teOpposite instanceof IGridHost ) {
            IGridNode nodeA = ((IGridHost) teOpposite).getGridNode(AEPartLocation.INTERNAL);
            IGridNode nodeB = getGridNode(AEPartLocation.INTERNAL);
            try {
                this.connections.add(AEApi.instance().createGridConnection(nodeA, nodeB));
            } catch( FailedConnection ex ) {
                IWConstants.LOG.log(Level.DEBUG, ex.getMessage());
            }
        }
    }

    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);

        if( this.gridNode == null ) {
            this.createAELink();
        }
    }
}
