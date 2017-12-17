/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.tileentity.ae;

import appeng.api.networking.events.MENetworkChannelsChanged;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import de.sanandrew.mods.immersivewiring.block.ae2.BlockTransformerFluix;
import de.sanandrew.mods.immersivewiring.block.ae2.FluixType;
import de.sanandrew.mods.immersivewiring.util.IWConfiguration;
import de.sanandrew.mods.immersivewiring.wire.IWireFluixType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class TileEntityTransformerFluix
        extends TileEntityFluixConnectable
{
    private boolean isActive;

    @Override
    public double getIdlePowerUsage() {
        IWireFluixType type = this.getType();
        return type == FluixType.FLUIX_DENSE
                       ? IWConfiguration.ae2DenseTransformerPowerDrain
                       : IWConfiguration.ae2FluixTransformerPowerDrain;
    }

    @Override
    public EnumSet<EnumFacing> getConnectableSides() {
        return EnumSet.of(this.getFacing().getOpposite());
    }

    @Override
    public boolean canConnectCable(WireType cableType, TargetingInfo target) {
        return super.canConnectCable(cableType, target) && this.limitType == null;
    }

    @Override
    public Vec3d getRaytraceOffset(IImmersiveConnectable link) {
        EnumFacing facing = this.getFacing();
        return new Vec3d(0.5D + facing.getFrontOffsetX() * 0.5D, 0.5D + facing.getFrontOffsetY() * 0.5D, 0.5D + facing.getFrontOffsetZ() * 0.5D);
    }

    @Override
    public Vec3d getConnectionOffset(ImmersiveNetHandler.Connection con) {
        EnumFacing facing = this.getFacing();
        return new Vec3d(0.5D + facing.getFrontOffsetX() * 0.4D, 0.5D + facing.getFrontOffsetY() * 0.4D, 0.5D + facing.getFrontOffsetZ() * 0.4D);
    }

    @Override
    public void createAELink() {
        super.createAELink();
        this.updateOnGridChange(false);
    }

    @Override
    public void gridChanged() {
        super.gridChanged();
        this.updateOnGridChange(false);
    }

    @MENetworkEventSubscribe
    public void powerRender(MENetworkPowerStatusChange c) {
        this.updateOnGridChange(true);
    }

    @MENetworkEventSubscribe
    public void channelUpdated(MENetworkChannelsChanged c) {
        this.updateOnGridChange(true);
    }

    public boolean isMeActive() {
        return this.isActive;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        this.isActive = pkt.getNbtCompound().getBoolean("meActive");
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos).withProperty(BlockTransformerFluix.ACTIVE, this.isActive), 3);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        SPacketUpdateTileEntity packet = super.getUpdatePacket();
        if( packet == null ) {
            packet = new SPacketUpdateTileEntity(this.pos, 0, new NBTTagCompound());
        }
        packet.getNbtCompound().setBoolean("meActive", this.isActive);
        return packet;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = super.getUpdateTag();
        nbt.setBoolean("meActive", this.gridNode != null && this.gridNode.isActive());
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        this.isActive = tag.getBoolean("meActive");
    }

    protected IWireFluixType getType() {
        return !this.world.isAirBlock(this.pos) ? this.world.getBlockState(this.pos).getValue(FluixType.TYPE) : FluixType.FLUIX;
    }

    private void updateOnGridChange(boolean force) {
        if( !this.world.isRemote && this.gridNode != null && !this.world.isAirBlock(this.pos) ) {
            boolean prevActive = this.isActive;
            this.isActive = this.gridNode.isActive();
            if( prevActive != this.isActive || force ) {
                this.markDirty();
                this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos).withProperty(BlockTransformerFluix.ACTIVE, this.isActive), 3);
            }
        }
    }

    @Override
    public List<AxisAlignedBB> getAdvancedSelectionBounds() {
        if( this.cachedSelectionBounds == null ) {
            if( this.loaded ) {
                EnumFacing facing = this.getFacing();
                switch( facing ) {
                    case UP:
                        this.cachedSelectionBounds = Arrays.asList(new AxisAlignedBB(0, 0, 0, 1, 0.5625, 1).offset(this.pos),
                                                                   new AxisAlignedBB(0.3125, 0.5625, 0.3125, 0.6875, 1, 0.6875).offset(this.pos));
                        break;
                    case DOWN:
                        this.cachedSelectionBounds = Arrays.asList(new AxisAlignedBB(0, 0.4375, 0, 1, 1, 1).offset(this.pos),
                                                                   new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 0.4375, 0.6875).offset(this.pos));
                        break;
                    case NORTH:
                        this.cachedSelectionBounds = Arrays.asList(new AxisAlignedBB(0, 0, 0.4375, 1, 1, 1).offset(this.pos),
                                                                   new AxisAlignedBB(0.3125, 0.3125, 0, 0.6875, 0.6875, 0.4375).offset(this.pos));
                        break;
                    case SOUTH:
                        this.cachedSelectionBounds = Arrays.asList(new AxisAlignedBB(0, 0, 0, 1, 1, 0.5625).offset(this.pos),
                                                                   new AxisAlignedBB(0.3125, 0.3125, 0.5625, 0.6875, 0.6875, 1).offset(this.pos));
                        break;
                    case EAST:
                        this.cachedSelectionBounds = Arrays.asList(new AxisAlignedBB(0, 0, 0, 0.5625, 1, 1).offset(this.pos),
                                                                   new AxisAlignedBB(0.5625, 0.3125, 0.3125, 1, 0.6875, 0.6875).offset(this.pos));
                        break;
                    case WEST:
                        this.cachedSelectionBounds = Arrays.asList(new AxisAlignedBB(0.4375, 0, 0, 1, 1, 1).offset(this.pos),
                                                                   new AxisAlignedBB(0, 0.3125, 0.3125, 0.4375, 0.6875, 0.6875).offset(this.pos));
                        break;
                }
            } else {
                return Collections.emptyList();
            }
        }

        return this.cachedSelectionBounds;
    }
}