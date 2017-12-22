/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.client.util;

import de.sanandrew.mods.immersivecables.util.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class ClientProxy
        extends CommonProxy
{
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        ModelRegistry.registerModelPre112();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void sendTryUseItemOnBlock(BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, side, hand, hitX, hitY, hitZ));
    }
}
