/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.tileentity.ae;

import appeng.api.AEApi;
import appeng.api.exceptions.FailedConnectionException;
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
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.util.Utils;
import de.sanandrew.mods.immersivecables.util.ICConstants;
import net.minecraft.block.BlockDirectional;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class TileFluixConnectable
        extends TileEntityImmersiveConnectable
        implements IGridHost, IGridBlock, ITickable, IEBlockInterfaces.IAdvancedSelectionBounds
{
    public IGridNode gridNode;
    public ArrayList<IGridConnection> connections = new ArrayList<>();

    protected List<AxisAlignedBB> cachedSelectionBounds;

    protected boolean loaded = false;

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
    public ItemStack getMachineRepresentation() {
        return new ItemStack(this.blockType, 1, this.getBlockMetadata() & 1);
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

            this.destroyAELink();
        }

        super.removeCable(connection);
    }

    @Override
    public void update() {
        if( !this.loaded && this.hasWorld() ) {
            this.loaded = true;
            if( !this.world.isRemote ) {
                this.createAELink();

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
        if( this.world != null && !this.world.isRemote ) {
            this.destroyAELink();
        }

        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        if( this.world != null && !this.world.isRemote ) {
            this.destroyAELink();
        }
    }

    public void createAELink() {
        if( !this.world.isRemote ) {
            if( this.gridNode == null ) {
                this.gridNode = AEApi.instance().grid().createGridNode(this);
            }
            this.gridNode.updateState();
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
                this.connections.add(AEApi.instance().grid().createGridConnection(nodeA, nodeB));
            } catch( FailedConnectionException ex ) {
                ICConstants.LOG.log(Level.DEBUG, ex.getMessage());
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
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);

        if( this.gridNode == null ) {
            this.createAELink();
        }
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
