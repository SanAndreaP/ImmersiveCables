package de.sanandrew.mods.immersivewiring.item;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.IWireCoil;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.IESaveData;
import blusunrize.immersiveengineering.common.util.IEAchievements;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import de.sanandrew.mods.immersivewiring.util.IWConstants;
import de.sanandrew.mods.immersivewiring.wire.WireRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemCoil
        extends Item
        implements IWireCoil
{
    public ItemCoil() {
        this.setUnlocalizedName(IWConstants.ID + ":wire_coil");
        this.setRegistryName(IWConstants.ID, "wire_coil");
        setHasSubtypes(true);
        setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int meta = Math.min(stack.getItemDamage(), WireRegistry.Wire.VALUES.length - 1);
        return getUnlocalizedName() + WireRegistry.Wire.VALUES[meta].key;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List items) {
        for( int i = 0; i < WireRegistry.Wire.VALUES.length; i++ ) {
            items.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public WireType getWireType(ItemStack stack) {
        return WireRegistry.Wire.VALUES[Math.min(stack.getItemDamage(), WireRegistry.Wire.VALUES.length - 1)].getType();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean adv) {
        if( stack.getTagCompound() != null && stack.getTagCompound().hasKey("linkingPos") ) {
            int[] link = stack.getTagCompound().getIntArray("linkingPos");
            if( link.length > 3 ) {
                list.add(I18n.translateToLocalFormatted(Lib.DESC_INFO + "attachedToDim", link[1], link[2], link[3], link[0]));
            }
        }
    }

    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if( tileEntity instanceof IImmersiveConnectable && ((IImmersiveConnectable) tileEntity).canConnect() ) {
            TargetingInfo target = new TargetingInfo(side, hitX, hitY, hitZ);
            WireType wire = this.getWireType(stack);
            BlockPos masterPos = ((IImmersiveConnectable) tileEntity).getConnectionMaster(wire, target);
            tileEntity = world.getTileEntity(masterPos);
            if( tileEntity instanceof IImmersiveConnectable && ((IImmersiveConnectable) tileEntity).canConnect() ) {
                if( !((IImmersiveConnectable) tileEntity).canConnectCable(wire, target) ) {
                    if( !world.isRemote ) {
                        player.sendMessage(new TextComponentTranslation("chat.immersiveengineering.warning.wrongCable"));
                    } else {
                        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, side, hand, hitX, hitY, hitZ));
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
                                player.sendMessage(new TextComponentTranslation("chat.immersiveengineering.warning.wrongDimension"));
                            } else if( linkPos.equals(masterPos) ) {
                                player.sendMessage(new TextComponentTranslation("chat.immersiveengineering.warning.sameConnection"));
                            } else if( distanceSq > type.getMaxLength() * type.getMaxLength() ) {
                                player.sendMessage(new TextComponentTranslation("chat.immersiveengineering.warning.tooFar"));
                            } else {
                                TargetingInfo targetLink = TargetingInfo.readFromNBT(ItemNBTHelper.getTagCompound(stack, "targettingInfo"));
                                if( tileEntityLinkingPos instanceof IImmersiveConnectable && ((IImmersiveConnectable) tileEntityLinkingPos).canConnectCable(wire, targetLink) ) {
                                    IImmersiveConnectable nodeHere = (IImmersiveConnectable) tileEntity;
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
                                        player.sendMessage(new TextComponentTranslation("chat.immersiveengineering.warning.connectionExists"));
                                    } else {
                                        Vec3d rtOff0 = nodeHere.getRaytraceOffset(nodeLink).addVector(masterPos.getX(), masterPos.getY(), masterPos.getZ());
                                        Vec3d rtOff1 = nodeLink.getRaytraceOffset(nodeHere).addVector(linkPos.getX(), linkPos.getY(), linkPos.getZ());
                                        Set<BlockPos> ignore = new HashSet<>();
                                        ignore.addAll(nodeHere.getIgnored(nodeLink));
                                        ignore.addAll(nodeLink.getIgnored(nodeHere));
                                        boolean canSee = Utils.rayTraceForFirst(rtOff0, rtOff1, world, ignore) == null;
                                        if( canSee ) {
                                            ImmersiveNetHandler.INSTANCE.addConnection(world, Utils.toCC(nodeHere), Utils.toCC(nodeLink), (int) Math.sqrt(distanceSq), type);
                                            nodeHere.connectCable(type, target, nodeLink);
                                            nodeLink.connectCable(type, targetLink, nodeHere);
                                            IESaveData.setDirty(world.provider.getDimension());
                                            player.addStat(IEAchievements.connectWire);
                                            if( !player.capabilities.isCreativeMode ) {
                                                --stack.stackSize;
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
                                            player.sendMessage(new TextComponentTranslation("chat.immersiveengineering.warning.cantSee"));
                                        }
                                    }
                                } else {
                                    player.sendMessage(new TextComponentTranslation("chat.immersiveengineering.warning.invalidPoint"));
                                }
                            }

                            ItemNBTHelper.remove(stack, "linkingPos");
                            ItemNBTHelper.remove(stack, "targettingInfo");
                        }
                    } else {
                        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, side, hand, hitX, hitY, hitZ));
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

    //  @Override
    //  public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    //    if( !world.isRemote && world.getTileEntity(x, y, z) instanceof IImmersiveConnectable && ((IImmersiveConnectable) world.getTileEntity(x, y, z)).canConnect() ) {
    //      TargetingInfo target = new TargetingInfo(side, hitX, hitY, hitZ);
    //      if( !((IImmersiveConnectable) world.getTileEntity(x, y, z)).canConnectCable(getWireType(stack), target) || (world.getTileEntity(x, y, z) instanceof TileEntityConnectorLV) ) {
    //        player.addChatMessage(new ChatComponentTranslation(Lib.CHAT_WARN + "wrongCable"));
    //        return false;
    //      }
    //
    //      if( !ItemNBTHelper.hasKey(stack, "linkingPos") ) {
    //        ItemNBTHelper.setIntArray(stack, "linkingPos", new int[] {world.provider.dimensionId, x, y, z});
    //        target.writeToNBT(stack.getTagCompound());
    //      } else {
    //        WireType type = getWireType(stack);
    //        int[] pos = ItemNBTHelper.getIntArray(stack, "linkingPos");
    //        int distance = (int) Math.ceil(Math.sqrt((pos[1] - x) * (pos[1] - x) + (pos[2] - y) * (pos[2] - y) + (pos[3] - z) * (pos[3] - z)));
    //        if( pos[0] != world.provider.dimensionId )
    //          player.addChatMessage(new ChatComponentTranslation(Lib.CHAT_WARN + "wrongDimension"));
    //        else if( pos[1] == x && pos[2] == y && pos[3] == z )
    //          player.addChatMessage(new ChatComponentTranslation(Lib.CHAT_WARN + "sameConnection"));
    //        else if( distance > type.getMaxLength() )
    //          player.addChatMessage(new ChatComponentTranslation(Lib.CHAT_WARN + "tooFar"));
    //        else if( !(world.getTileEntity(x, y, z) instanceof IImmersiveConnectable) || !(world.getTileEntity(pos[1], pos[2], pos[3]) instanceof IImmersiveConnectable) )
    //          player.addChatMessage(new ChatComponentTranslation(Lib.CHAT_WARN + "invalidPoint"));
    //        else {
    //          IImmersiveConnectable nodeHere = (IImmersiveConnectable) world.getTileEntity(x, y, z);
    //          IImmersiveConnectable nodeLink = (IImmersiveConnectable) world.getTileEntity(pos[1], pos[2], pos[3]);
    //          boolean connectionExists = false;
    //          if( nodeHere != null && nodeLink != null ) {
    //            Set<ImmersiveNetHandler.Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(world, Utils.toCC(nodeHere));
    //            if( connections != null ) {
    //              for( ImmersiveNetHandler.Connection con : connections ) {
    //                if( con.end.equals(Utils.toCC(nodeLink)) )
    //                  connectionExists = true;
    //              }
    //            }
    //          }
    //          if( connectionExists )
    //            player.addChatMessage(new ChatComponentTranslation(Lib.CHAT_WARN + "connectionExists"));
    //          else {
    //            Vec3 rtOff0 = nodeHere.getRaytraceOffset(nodeLink).addVector(x, y, z);
    //            Vec3 rtOff1 = nodeLink.getRaytraceOffset(nodeHere).addVector(pos[1], pos[2], pos[3]);
    //            boolean canSee = Utils.canBlocksSeeOther(world, new ChunkCoordinates(x, y, z), new ChunkCoordinates(pos[1], pos[2], pos[3]), rtOff0, rtOff1);
    //            if( canSee ) {
    //              TargetingInfo targetLink = TargetingInfo.readFromNBT(stack.getTagCompound());
    //              ImmersiveNetHandler.INSTANCE.addConnection(world, Utils.toCC(nodeHere), Utils.toCC(nodeLink), distance, type);
    //              nodeHere.connectCable(type, target);
    //              nodeLink.connectCable(type, targetLink);
    //              IESaveData.setDirty(world.provider.dimensionId);
    //
    //              if( !player.capabilities.isCreativeMode )
    //                stack.stackSize--;
    //              ((TileEntity) nodeHere).markDirty();
    //              world.markBlockForUpdate(x, y, z);
    //              ((TileEntity) nodeLink).markDirty();
    //              world.markBlockForUpdate(pos[1], pos[2], pos[3]);
    //
    //              TileEntity tileEntity = world.getTileEntity(x, y, z);
    //              if( tileEntity instanceof IWireConnector ) {
    //                ((IWireConnector) tileEntity).connectTo(pos[1], pos[2], pos[3]);
    //              }
    //            } else
    //              player.addChatMessage(new ChatComponentTranslation(Lib.CHAT_WARN + "cantSee"));
    //          }
    //        }
    //        ItemNBTHelper.remove(stack, "linkingPos");
    //        ItemNBTHelper.remove(stack, "side");
    //        ItemNBTHelper.remove(stack, "hitX");
    //        ItemNBTHelper.remove(stack, "hitY");
    //        ItemNBTHelper.remove(stack, "hitZ");
    //      }
    //      return true;
    //    }
    //    return false;
    //  }
}
