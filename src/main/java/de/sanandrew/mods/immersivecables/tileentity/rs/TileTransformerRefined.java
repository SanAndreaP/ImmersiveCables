/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.tileentity.rs;

import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import com.raoulvdberge.refinedstorage.api.network.INetwork;
import com.raoulvdberge.refinedstorage.api.network.INetworkNodeVisitor;
import com.raoulvdberge.refinedstorage.api.network.node.INetworkNodeProxy;
import de.sanandrew.mods.immersivecables.block.rs.BlockRegistryRS;
import de.sanandrew.mods.immersivecables.block.rs.BlockTransformerRefined;
import de.sanandrew.mods.immersivecables.util.ICConfiguration;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class TileTransformerRefined
        extends TileRefinedConnectable
        implements ITickable
{
    protected boolean loaded;
    private boolean isActive;

    @Override
    public void update() {
        if( !this.loaded ) {
            this.loaded = true;
            TileEntity connected = this.world.getTileEntity(this.pos.offset(this.getFacing().getOpposite()));
            if( connected instanceof INetworkNodeProxy ) {
                INetworkNodeProxy node = (INetworkNodeProxy) connected;
                if( node.getNode().getNetwork() != null ) {
                    node.getNode().getNetwork().getNodeGraph().rebuild();
                }
            }
        }
    }

    @Override
    public void onConnectionChanged(INetwork iNetworkMaster) {
        if( !this.world.isAirBlock(this.pos) ) {
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos).withProperty(BlockTransformerRefined.ACTIVE, this.isActive), 3);
        }
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

    @Override
    public int getEnergyUsage() {
        return ICConfiguration.rsTransformerPowerDrain;
    }

    @Override
    public void visitNodes(INetworkNodeVisitor.Operator operator) {
        EnumFacing facing = this.getFacing();
        operator.apply(this.world, this.pos.offset(facing.getOpposite()), facing);
    }

    @Override
    public boolean canConduct(@Nullable EnumFacing direction) {
        return direction == this.getFacing().getOpposite();
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(BlockRegistryRS.TRANSFORMER_RS, 1, 0);
    }

    public boolean isRsActive() {
        return this.isActive;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        this.isActive = pkt.getNbtCompound().getBoolean("rsActive");
        if( !this.world.isAirBlock(this.pos) ) {
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos).withProperty(BlockTransformerRefined.ACTIVE, this.isActive), 3);
        }
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        this.writeCustomNBT(nbt, true);
        nbt.setBoolean("rsActive", this.getNode().getNetwork() != null);
        return new SPacketUpdateTileEntity(this.pos, 1, nbt);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = super.getUpdateTag();
        nbt.setBoolean("rsActive", this.getNode().getNetwork() != null);
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        this.isActive = tag.getBoolean("rsActive");
    }
}
