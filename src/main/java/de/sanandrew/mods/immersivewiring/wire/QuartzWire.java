package de.sanandrew.mods.immersivewiring.wire;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import de.sanandrew.mods.immersivewiring.item.ItemRegistryAE2;
import de.sanandrew.mods.immersivewiring.util.IWConfiguration;
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
        return 0xE8E2DB;
    }

    @Override
    public int getMaxLength() {
        return IWConfiguration.ae2QuartzWireMaxLength;
    }

    @Override
    public ItemStack getWireCoil() {
        return new ItemStack(ItemRegistryAE2.WIRE_COIL, 1, Wires.QUARTZ.ordinal());
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
