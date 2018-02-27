package de.sanandrew.mods.immersivecables.item;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.IWireCoil;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.IESaveData;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import de.sanandrew.mods.immersivecables.util.ICConstants;
import de.sanandrew.mods.immersivecables.util.ICCreativeTab;
import de.sanandrew.mods.immersivecables.wire.Wires;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemCoil
        extends Item
        implements IWireCoil
{
    public ItemCoil() {
        this.setUnlocalizedName(ICConstants.ID + ":wire_coil");
        this.setRegistryName(ICConstants.ID, "wire_coil");
        setHasSubtypes(true);
        setCreativeTab(ICCreativeTab.INSTANCE);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = Math.min(stack.getItemDamage(), Wires.VALUES.length - 1);
        return getUnlocalizedName() + '.' + Wires.VALUES[meta].key;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if( this.isInCreativeTab(tab) ) {
            for( int i = 0; i < Wires.VALUES.length; i++ ) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public WireType getWireType(ItemStack stack) {
        return Wires.VALUES[Math.min(stack.getItemDamage(), Wires.VALUES.length - 1)].type;
    }

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn) {
        if( stack.getTagCompound() != null && stack.getTagCompound().hasKey("linkingPos") ) {
            int[] link = stack.getTagCompound().getIntArray("linkingPos");
            if( link.length > 3 ) {
                tooltip.add(net.minecraft.util.text.translation.I18n.translateToLocalFormatted(Lib.DESC_INFO + "attachedToDim", link[1], link[2], link[3], link[0]));
            }
        }
    }

    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        TileEntity tile = world.getTileEntity(pos);
        ItemStack stack = player.getHeldItem(hand);
        if( tile instanceof IImmersiveConnectable && ((IImmersiveConnectable) tile).canConnect() ) {
            TargetingInfo target = new TargetingInfo(side, hitX, hitY, hitZ);
            WireType wire = this.getWireType(stack);
            BlockPos masterPos = ((IImmersiveConnectable) tile).getConnectionMaster(wire, target);
            tile = world.getTileEntity(masterPos);
            Vec3i offset = pos.subtract(masterPos);
            if( tile instanceof IImmersiveConnectable && ((IImmersiveConnectable) tile).canConnect() ) {
                if( !((IImmersiveConnectable) tile).canConnectCable(wire, target, offset) ) {
                    if( !world.isRemote ) {
                        player.sendMessage(new TextComponentTranslation(Lib.CHAT_WARN + "wrongCable"));
                    }

                    return EnumActionResult.FAIL;
                } else {
                    if( !world.isRemote ) {
                        if( !ItemNBTHelper.hasKey(stack, "linkingPos") ) {
                            ItemNBTHelper.setIntArray(stack, "linkingPos", new int[] {world.provider.getDimension(), masterPos.getX(), masterPos.getY(), masterPos.getZ()});
                            NBTTagCompound targetNbt = new NBTTagCompound();
                            target.writeToNBT(targetNbt);
                            ItemNBTHelper.setTagCompound(stack, "targettingInfo", targetNbt);
                        } else {
                            WireType type = this.getWireType(stack);
                            int[] array = ItemNBTHelper.getIntArray(stack, "linkingPos");
                            BlockPos linkPos = new BlockPos(array[1], array[2], array[3]);
                            TileEntity tileEntityLinkingPos = world.getTileEntity(linkPos);
                            int distanceSq = (int) Math.ceil(linkPos.distanceSq(masterPos));
                            if( array[0] != world.provider.getDimension() ) {
                                player.sendMessage(new TextComponentTranslation(Lib.CHAT_WARN + "wrongDimension"));
                            } else if( linkPos.equals(masterPos) ) {
                                player.sendMessage(new TextComponentTranslation(Lib.CHAT_WARN + "sameConnection"));
                            } else if( distanceSq > type.getMaxLength() * type.getMaxLength() ) {
                                player.sendMessage(new TextComponentTranslation(Lib.CHAT_WARN + "tooFar"));
                            } else {
                                TargetingInfo targetLink = TargetingInfo.readFromNBT(ItemNBTHelper.getTagCompound(stack, "targettingInfo"));
                                if( tileEntityLinkingPos instanceof IImmersiveConnectable && ((IImmersiveConnectable) tileEntityLinkingPos).canConnectCable(wire, targetLink, offset)
                                        && (!(tileEntityLinkingPos instanceof ICoilConnectable) || ((ICoilConnectable) tileEntityLinkingPos).canConnectCable(tile, wire)) ) {
                                    IImmersiveConnectable nodeHere = (IImmersiveConnectable) tile;
                                    IImmersiveConnectable nodeLink = (IImmersiveConnectable) tileEntityLinkingPos;
                                    boolean connectionExists = false;
                                    Set<ImmersiveNetHandler.Connection> outputs = ImmersiveNetHandler.INSTANCE.getConnections(world, Utils.toCC(nodeHere));
                                    if( outputs != null ) {

                                        for( ImmersiveNetHandler.Connection con : outputs ) {
                                            if( con.end.equals(Utils.toCC(nodeLink)) ) {
                                                connectionExists = true;
                                            }
                                        }
                                    }

                                    if( connectionExists ) {
                                        player.sendMessage(new TextComponentTranslation(Lib.CHAT_WARN + "connectionExists"));
                                    } else {
                                        Vec3i offsetLink = Vec3i.NULL_VECTOR;
                                        if (array.length == 7)
                                            offsetLink = new Vec3i(array[4], array[5], array[6]);
                                        Set<BlockPos> ignore = new HashSet<>();
                                        ignore.addAll(nodeHere.getIgnored(nodeLink));
                                        ignore.addAll(nodeLink.getIgnored(nodeHere));
                                        ImmersiveNetHandler.Connection tmpConn = new ImmersiveNetHandler.Connection(Utils.toCC(nodeHere), Utils.toCC(nodeLink), wire,
                                                                                                                    (int) Math.sqrt(distanceSq));
                                        Vec3d start = nodeHere.getConnectionOffset(tmpConn, target, pos.subtract(masterPos)).addVector(tile.getPos().getX(),
                                                                                                                                       tile.getPos().getY(), tile.getPos().getZ());
                                        Vec3d end = nodeLink.getConnectionOffset(tmpConn, targetLink, offsetLink).addVector(tileEntityLinkingPos.getPos().getX(),
                                                                                                                            tileEntityLinkingPos.getPos().getY(), tileEntityLinkingPos.getPos().getZ());
                                        boolean canSee = ApiUtils.raytraceAlongCatenaryRelative(tmpConn, (p) -> {
                                            if (ignore.contains(p.getLeft())) {
                                                return false;
                                            }
                                            IBlockState state = world.getBlockState(p.getLeft());
                                            return ApiUtils.preventsConnection(world, p.getLeft(), state, p.getMiddle(), p.getRight());
                                        }, (p) -> {
                                        }, start, end);
                                        if (canSee) {
                                            ImmersiveNetHandler.Connection conn = ImmersiveNetHandler.INSTANCE.addAndGetConnection(world, Utils.toCC(nodeHere), Utils.toCC(nodeLink),
                                                                                                                                   (int) Math.sqrt(distanceSq), wire);


                                            nodeHere.connectCable(wire, target, nodeLink, offset);
                                            nodeLink.connectCable(wire, targetLink, nodeHere, offsetLink);
                                            ImmersiveNetHandler.INSTANCE.addBlockData(world, conn);
                                            IESaveData.setDirty(world.provider.getDimension());

                                            if( !player.capabilities.isCreativeMode ) {
                                                stack.shrink(1);
                                            }

                                            ((TileEntity) nodeHere).markDirty();
                                            world.addBlockEvent(masterPos, ((TileEntity) nodeHere).getBlockType(), -1, 0);
                                            IBlockState state = world.getBlockState(masterPos);
                                            world.notifyBlockUpdate(masterPos, state, state, 3);
                                            ((TileEntity) nodeLink).markDirty();
                                            world.addBlockEvent(linkPos, ((TileEntity) nodeLink).getBlockType(), -1, 0);
                                            state = world.getBlockState(linkPos);
                                            world.notifyBlockUpdate(linkPos, state, state, 3);
                                        } else {
                                            player.sendMessage(new TextComponentTranslation(Lib.CHAT_WARN + "cantSee"));
                                        }
                                    }
                                } else {
                                    player.sendMessage(new TextComponentTranslation(Lib.CHAT_WARN + "invalidPoint"));
                                }
                            }

                            ItemNBTHelper.remove(stack, "linkingPos");
                            ItemNBTHelper.remove(stack, "targettingInfo");
                        }
                    }

                    return EnumActionResult.SUCCESS;
                }
            } else {
                return EnumActionResult.PASS;
            }
        } else {
            return EnumActionResult.PASS;
        }
    }
}
