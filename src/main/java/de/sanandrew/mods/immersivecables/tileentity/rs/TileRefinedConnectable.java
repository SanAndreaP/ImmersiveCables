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
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.util.Utils;
import com.raoulvdberge.refinedstorage.api.network.INetworkMaster;
import com.raoulvdberge.refinedstorage.api.network.INetworkNode;
import de.sanandrew.mods.immersivecables.core.IRefinedAdvNode;
import de.sanandrew.mods.immersivecables.item.ICoilConnectable;
import de.sanandrew.mods.immersivecables.wire.Wires;
import net.minecraft.block.BlockDirectional;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public abstract class TileRefinedConnectable
        extends TileEntityImmersiveConnectable
        implements IEBlockInterfaces.IAdvancedSelectionBounds, INetworkNode, ICoilConnectable, IRefinedAdvNode
{
    protected List<AxisAlignedBB> cachedSelectionBounds;
    protected INetworkMaster controller;

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

        if( this.controller != null ) {
            this.controller.getNodeGraph().rebuild();
        }
    }

    protected EnumFacing getFacing() {
        return !this.world.isAirBlock(this.pos) ? this.world.getBlockState(this.pos).getValue(BlockDirectional.FACING) : EnumFacing.UP;
    }

    @Override
    public BlockPos getPosition() {
        return this.pos;
    }

    @Override
    public void onConnected(INetworkMaster iNetworkMaster) {
        this.controller = iNetworkMaster;
    }

    @Override
    public void connectCable(WireType cableType, TargetingInfo target, IImmersiveConnectable other) {
        super.connectCable(cableType, target, other);
        if( other instanceof TileRefinedConnectable ) {
            TileRefinedConnectable refinedTile = (TileRefinedConnectable) other;
            if( this.controller != null && refinedTile.controller == null ) {
                this.controller.getNodeGraph().rebuild();
            }
        }
    }

    @Override
    public void removeCable(ImmersiveNetHandler.Connection connection) {
        super.removeCable(connection);
        if( this.controller != null ) {
            this.controller.getNodeGraph().rebuild();
        }
    }

    @Override
    public void onDisconnected(INetworkMaster iNetworkMaster) {
        this.controller = null;
    }

    @Override
    public boolean isConnected() {
        return this.controller != null;
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public INetworkMaster getNetwork() {
        return this.controller;
    }

    @Override
    public World getNodeWorld() {
        return this.world;
    }

    @Override
    public boolean canConnectCable(TileEntity targetEntity, WireType type) {
        if( targetEntity instanceof INetworkNode ) {
            INetworkMaster tgtController = ((INetworkNode) targetEntity).getNetwork();
            return tgtController == null || this.controller == null || tgtController == this.controller;
        }
        return false;
    }

    @Override
    public boolean canConnectCable(WireType cableType, TargetingInfo target) {
        return cableType == Wires.REFINED.type;
    }

    @Override
    public void onNodeTraverse(Set<BlockPos> checked, Queue<BlockPos> toCheck) {
        Set<ImmersiveNetHandler.Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(this.world, Utils.toCC(this));
        if( connections != null ) {
            for( ImmersiveNetHandler.Connection connection : connections ) {
                BlockPos opposite = connection.end;
                if( opposite.equals(Utils.toCC(this)) ) {
                    continue;
                }

                TileEntity connTile = this.world.getTileEntity(opposite);
                if( connTile instanceof TileRefinedConnectable ) {
                    if( checked.add(connTile.getPos()) ) {
                        toCheck.add(connTile.getPos());
                    }
                }
            }
        }
    }
}
