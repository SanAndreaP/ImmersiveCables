/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.tileentity.ae;

import appeng.api.networking.GridFlags;
import appeng.api.networking.events.MENetworkChannelsChanged;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import de.sanandrew.mods.immersivecables.block.ae2.BlockTransformerFluix;
import de.sanandrew.mods.immersivecables.block.ae2.FluixType;
import de.sanandrew.mods.immersivecables.util.ICConfiguration;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class TileTransformerFluix
        extends TileFluixConnectable
{
    private boolean isActive;

    @Override
    public double getIdlePowerUsage() {
        return this.getType() == FluixType.FLUIX_DENSE
                       ? ICConfiguration.ae2DenseTransformerPowerDrain
                       : ICConfiguration.ae2FluixTransformerPowerDrain;
    }

    @Override
    public EnumSet<EnumFacing> getConnectableSides() {
        return EnumSet.of(this.getFacing().getOpposite());
    }

    @Override
    public GridFlags[] getFlags() {
        return this.getType().flags;
    }

    @Override
    public AECableType getCableConnectionType(AEPartLocation aePartLocation) {
        return this.getType().cableType;
    }

    @Override
    public Vec3d getConnectionOffset(ImmersiveNetHandler.Connection con) {
        EnumFacing facing = this.getFacing();
        return new Vec3d(0.5D + facing.getFrontOffsetX() * 0.4D, 0.5D + facing.getFrontOffsetY() * 0.4D, 0.5D + facing.getFrontOffsetZ() * 0.4D);
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
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        this.isActive = pkt.getNbtCompound().getBoolean("meActive");
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos).withProperty(BlockTransformerFluix.ACTIVE, this.isActive), 3);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        this.writeCustomNBT(nbt, true);
        nbt.setBoolean("meActive", this.proxy.getNode() != null && this.proxy.getNode().isActive());
        return new SPacketUpdateTileEntity(this.pos, 1, nbt);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = super.getUpdateTag();
        nbt.setBoolean("meActive", this.proxy.getNode() != null && this.proxy.getNode().isActive());
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        this.isActive = tag.getBoolean("meActive");
    }

    private FluixType getType() {
        return this.world.isAirBlock(this.pos) ? FluixType.FLUIX : this.world.getBlockState(this.pos).getValue(FluixType.TYPE);
    }

    @Override
    protected String getWireCategory() {
        return this.getType().wireType.getCategory();
    }

    private void updateOnGridChange(boolean force) {
        if( !this.world.isRemote && !this.world.isAirBlock(this.pos) ) {
            boolean prevActive = this.isActive;
            this.isActive = this.proxy.getNode() != null && this.proxy.getNode().isActive();
            if( prevActive != this.isActive || force ) {
                this.markDirty();
                this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos).withProperty(BlockTransformerFluix.ACTIVE, this.isActive), 3);
            }
        }
    }

    @Override
    public List<AxisAlignedBB> getAdvancedSelectionBounds() {
        if( this.cachedSelectionBounds == null ) {
            EnumFacing facing = this.getFacing();
            switch( facing ) {
                case UP:
                    this.cachedSelectionBounds = Arrays.asList(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D).offset(this.pos),
                                                               new AxisAlignedBB(0.3125D, 0.5625D, 0.3125D, 0.6875D, 1.0D, 0.6875D).offset(this.pos));
                    break;
                case DOWN:
                    this.cachedSelectionBounds = Arrays.asList(new AxisAlignedBB(0.0D, 0.4375D, 0.0D, 1.0D, 1.0D, 1.0D).offset(this.pos),
                                                               new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.4375D, 0.6875D).offset(this.pos));
                    break;
                case NORTH:
                    this.cachedSelectionBounds = Arrays.asList(new AxisAlignedBB(0.0D, 0.0D, 0.4375D, 1.0D, 1.0D, 1.0D).offset(this.pos),
                                                               new AxisAlignedBB(0.3125D, 0.3125D, 0.0D, 0.6875D, 0.6875D, 0.4375D).offset(this.pos));
                    break;
                case SOUTH:
                    this.cachedSelectionBounds = Arrays.asList(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5625D).offset(this.pos),
                                                               new AxisAlignedBB(0.3125D, 0.3125D, 0.5625D, 0.6875D, 0.6875D, 1.0D).offset(this.pos));
                    break;
                case EAST:
                    this.cachedSelectionBounds = Arrays.asList(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5625D, 1.0D, 1.0D).offset(this.pos),
                                                               new AxisAlignedBB(0.5625D, 0.3125D, 0.3125D, 1.0D, 0.6875D, 0.6875D).offset(this.pos));
                    break;
                case WEST:
                    this.cachedSelectionBounds = Arrays.asList(new AxisAlignedBB(0.4375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D).offset(this.pos),
                                                               new AxisAlignedBB(0.0D, 0.3125D, 0.3125D, 0.4375D, 0.6875D, 0.6875D).offset(this.pos));
                    break;
            }
        }

        return this.cachedSelectionBounds;
    }
}