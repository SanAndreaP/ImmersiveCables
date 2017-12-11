/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.block;

import appeng.api.AEApi;
import appeng.api.util.AEPartLocation;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.common.IESaveData;
import blusunrize.immersiveengineering.common.util.Utils;
import de.sanandrew.mods.immersivewiring.tileentity.TileEntityMETransformer;
import de.sanandrew.mods.immersivewiring.util.IWConstants;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.List;

public class BlockMETransformer
        extends Block
{
    public static final PropertyEnum<TransformerType> TYPE = PropertyEnum.create("type", TransformerType.class);

    public BlockMETransformer() {
        super(Material.IRON);
        this.setHardness(2.5F);
        this.blockSoundType = SoundType.METAL;
        this.setUnlocalizedName(IWConstants.ID + ":me_transformer");
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, TransformerType.REGULAR));
        this.setRegistryName(IWConstants.ID, "me_transformer");
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityMETransformer();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(TYPE).ordinal();
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta).withProperty(TYPE, meta >= 0 && meta < TransformerType.VALUES.length ? TransformerType.VALUES[meta] : TransformerType.REGULAR);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{TYPE}, new IUnlistedProperty[0]);
    }

    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IBlockState state2 = super.getExtendedState(state, world, pos);
        if (state2 instanceof IExtendedBlockState ) {
            IExtendedBlockState ext = (IExtendedBlockState)state;
            TileEntity te = world.getTileEntity(pos);
            if (!(te instanceof TileEntityImmersiveConnectable)) {
                return state;
            }

            state = ext.withProperty(IEProperties.CONNECTIONS, ((TileEntityImmersiveConnectable)te).genConnBlockstate());
        }

        return state;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return super.canPlaceBlockAt(world, pos) && world.getBlockState(pos.up(1)).getBlock().isReplaceable(world, pos.up(1));
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if( !worldIn.isRemote ) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if( tile instanceof TileEntityMETransformer ) {
                ImmersiveNetHandler.INSTANCE.clearAllConnectionsFor(Utils.toCC(tile), worldIn, new TargetingInfo(EnumFacing.UP, 0.0F, 0.0F, 0.0F));
                IESaveData.setDirty(worldIn.provider.getDimension());
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean isSideSolid(IBlockState baseState, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if( !world.isRemote && placer instanceof EntityPlayer ) {
            TileEntity transformer = world.getTileEntity(pos);
            if( transformer instanceof TileEntityMETransformer ) {
                ((TileEntityMETransformer) transformer).getGridNode(AEPartLocation.INTERNAL).setPlayerID(AEApi.instance().registries().players().getID((EntityPlayer) placer));
            }
        }
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        for( int i = 0; i < TransformerType.VALUES.length; i++ ) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    public enum TransformerType
            implements IStringSerializable
    {
        REGULAR, DENSE;

        public static final TransformerType[] VALUES = values();

        @Override
        public String getName() {
            return this.name().toLowerCase();
        }

        @Override
        public String toString() {
            return this.getName();
        }
    }
}
