/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.block.ae2;

import appeng.api.AEApi;
import appeng.api.util.AEPartLocation;
import de.sanandrew.mods.immersivewiring.block.BlockConnectable;
import de.sanandrew.mods.immersivewiring.tileentity.ae.TileEntityFluixConnectable;
import de.sanandrew.mods.immersivewiring.tileentity.ae.TileEntityRelayFluix;
import de.sanandrew.mods.immersivewiring.util.IWConstants;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockRelayFluix
        extends BlockConnectable
{
    public BlockRelayFluix() {
        super(Material.IRON);
        this.setHardness(2.5F);
        this.blockSoundType = SoundType.METAL;
        this.setUnlocalizedName(IWConstants.ID + ":relay_fluix");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FluixType.TYPE, FluixType.FLUIX).withProperty(FACING, EnumFacing.UP));
        this.setRegistryName(IWConstants.ID, "relay_fluix");
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(FluixType.TYPE).ordinal();
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FluixType.TYPE, FluixType.VALUES[meta & 1])
                   .withProperty(FACING, EnumFacing.VALUES[(meta >> 1) & 7]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(FluixType.TYPE).ordinal() & 1) | ((state.getValue(FACING).getIndex() & 7) << 1);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityRelayFluix();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FluixType.TYPE, FACING);
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        return false;
    }


    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if( !world.isRemote && placer instanceof EntityPlayer ) {
            TileEntity transformer = world.getTileEntity(pos);
            if( transformer instanceof TileEntityFluixConnectable ) {
                ((TileEntityFluixConnectable) transformer).getGridNode(AEPartLocation.INTERNAL).setPlayerID(AEApi.instance().registries().players().getID((EntityPlayer) placer));
            }
        }
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        for( int i = 0; i < FluixType.VALUES.length; i++ ) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName() {
        return super.getUnlocalizedName();
    }
}
