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
import appeng.api.util.AECableType;
import appeng.api.util.AEColor;
import appeng.api.util.AEPartLocation;
import appeng.api.util.DimensionalCoord;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.util.Utils;
import de.sanandrew.mods.immersivewiring.block.BlockMETransformer;
import de.sanandrew.mods.immersivewiring.block.BlockRegistry;
import de.sanandrew.mods.immersivewiring.util.IWConstants;
import de.sanandrew.mods.immersivewiring.wire.WireRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.Level;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityMETransformer
        extends TileEntityImmersiveConnectable
        implements IGridHost, IGridBlock, ITickable
{
    public IGridNode gridNode;
    public IGridConnection connection;

    private boolean loaded = false;

    @Override
    public double getIdlePowerUsage() {
        return 15;//TODO: add ImmersiveIntegration.cfg.meTransformerPowerDrain;
    }

    @Override
    public EnumSet<GridFlags> getFlags() {
        return EnumSet.noneOf(GridFlags.class);
    }

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
    public EnumSet<EnumFacing> getConnectableSides() {
        return EnumSet.range(EnumFacing.DOWN, EnumFacing.EAST);
    }

    @Override
    public IGridHost getMachine() {
        return this;
    }

    @Override
    public void gridChanged() {

    }

    @Override
    public ItemStack getMachineRepresentation() {
        return new ItemStack(BlockRegistry.ME_TRANSFORMER, 1, this.getBlockMetadata());
    }

    @Override
    public IGridNode getGridNode(AEPartLocation aePartLocation) {
        if( this.gridNode == null ) {
            this.createAELink();
        }
        return this.gridNode;
    }

    @Override
    public AECableType getCableConnectionType(AEPartLocation aePartLocation) {
        return this.world.getBlockState(this.pos).getValue(BlockMETransformer.TYPE) == BlockMETransformer.TransformerType.DENSE ? AECableType.DENSE : AECableType.SMART;
    }

    @Override
    public void securityBreak() {

    }

    @Override
    public boolean canConnect() {
        return true;
    }

    @Override
    public boolean canConnectCable(WireType cableType, TargetingInfo target) {
        boolean isDense = this.world.getBlockState(this.pos).getValue(BlockMETransformer.TYPE) == BlockMETransformer.TransformerType.DENSE;
        return (isDense ? cableType == WireRegistry.Wire.FLUIX_DENSE.getType() : cableType == WireRegistry.Wire.FLUIX.getType()) && limitType == null;
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
        if( this.connection != null && !this.world.isRemote ) {
            this.connection.destroy();
            this.connection = null;
        }

        super.removeCable(connection);
    }

    @Override
    public Vec3d getRaytraceOffset(IImmersiveConnectable link) {
        return new Vec3d(0.5D, 1.2D, 0.5D);
    }

    @Override
    public Vec3d getConnectionOffset(ImmersiveNetHandler.Connection con) {
        return new Vec3d(0.5D, 1.1D, 0.5D);
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

    public void createAELink() {
        if( !this.world.isRemote ) {
            if( this.gridNode == null ) {
                this.gridNode = AEApi.instance().createGridNode(this);
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
                if( this.connection != null ) {
                    this.connection.destroy();
                    this.connection = null;
                }
                this.connection = AEApi.instance().createGridConnection(nodeA, nodeB);
            } catch( FailedConnection ex ) {
                IWConstants.LOG.log(Level.DEBUG, ex.getMessage());
            }
        }
    }

    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.pos.getX(), this.pos.getY() - 1, this.pos.getZ(), this.pos.getX() + 1, this.pos.getY() + 2, this.pos.getZ() + 1);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);

        if( this.gridNode == null ) {
            this.createAELink();
        }
    }
}
