package de.sanandrew.mods.immersivecables.wire;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import de.sanandrew.mods.immersivecables.util.ICConfiguration;
import de.sanandrew.mods.immersivecables.util.ItemBlockRegistry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class QuartzWire
        extends WireType
{
    @Override
    public String getUniqueName() {
        return "AE2QUARTZ";
    }

    @Override
    public double getLossRatio() {
        return 0;
    }

    @Override
    public int getTransferRate() {
        return 0;
    }

    @Override
    public int getColour(ImmersiveNetHandler.Connection connection) {
        return WireColors.ae2QuartzWireColor;
    }

    @Override
    public int getMaxLength() {
        return ICConfiguration.ae2QuartzWireMaxLength;
    }

    @Override
    public ItemStack getWireCoil() {
        return new ItemStack(ItemBlockRegistry.WIRE_COIL, 1, Wires.QUARTZ.ordinal());
    }

    @Override
    public double getRenderDiameter() {
        return 0.04f;
    }

    @Override
    public boolean isEnergyWire() {
        return false;
    }

    @Override
    public double getSlack() {
        return 1.005;
    }

    @Override
    public TextureAtlasSprite getIcon(ImmersiveNetHandler.Connection connection) {
        return iconDefaultWire;
    }
}
