/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.tileentity;

import appeng.api.networking.GridFlags;
import appeng.api.networking.GridNotification;
import appeng.api.networking.IGrid;
import appeng.api.networking.events.MENetworkChannelsChanged;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import de.sanandrew.mods.immersivewiring.block.BlockTransformerFluix;
import de.sanandrew.mods.immersivewiring.block.BlockRegistry;
import de.sanandrew.mods.immersivewiring.wire.WireRegistry;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class TileEntityTransformerFluix
        extends TileEntityFluixConnectable
{
    private boolean isActive;

    @Override
    public double getIdlePowerUsage() {
        return 15;//TODO: add ImmersiveIntegration.cfg.meTransformerPowerDrain;
    }

    @Override
    public EnumSet<EnumFacing> getConnectableSides() {
        return EnumSet.of(this.world.getBlockState(this.pos).getValue(BlockDirectional.FACING).getOpposite());
    }

    @Override
    public EnumSet<GridFlags> getFlags() {
        return this.isDense() ? EnumSet.of(GridFlags.DENSE_CAPACITY) : EnumSet.noneOf(GridFlags.class);
    }

    @Override
    public ItemStack getMachineRepresentation() {
        return new ItemStack(BlockRegistry.TRANSFORMER_FLUIX, 1, this.getBlockMetadata() & 1);
    }

    @Override
    public AECableType getCableConnectionType(AEPartLocation aePartLocation) {
        return this.isDense() ? AECableType.DENSE : AECableType.SMART;
    }

    @Override
    public boolean canConnectCable(WireType cableType, TargetingInfo target) {
        return (this.isDense() ? cableType == WireRegistry.Wire.FLUIX_DENSE.getType() : cableType == WireRegistry.Wire.FLUIX.getType()) && this.limitType == null;
    }

    @Override
    public Vec3d getRaytraceOffset(IImmersiveConnectable link) {
        EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockDirectional.FACING);
        return new Vec3d(0.5D + facing.getFrontOffsetX() * 0.5D, 0.5D + facing.getFrontOffsetY() * 0.5D, 0.5D + facing.getFrontOffsetZ() * 0.5D);
    }

    @Override
    public Vec3d getConnectionOffset(ImmersiveNetHandler.Connection con) {
        EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockDirectional.FACING);
        return new Vec3d(0.5D + facing.getFrontOffsetX() * 0.4D, 0.5D + facing.getFrontOffsetY() * 0.4D, 0.5D + facing.getFrontOffsetZ() * 0.4D);
    }

    private boolean isDense() {
        return !this.world.isAirBlock(this.pos) && this.world.getBlockState(this.pos).getValue(BlockTransformerFluix.TYPE) == BlockTransformerFluix.Type.FLUIX_DENSE;
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
}
