package de.sanandrew.mods.immersivewiring.wire;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import de.sanandrew.mods.immersivewiring.item.ItemRegistryAE2;
import de.sanandrew.mods.immersivewiring.util.IWConfiguration;
import net.minecraft.item.ItemStack;

public class FluixDenseWire
        extends FluixWire
{
    @Override
    public String getUniqueName() {
        return "AE2DENSE";
    }

    @Override
    public int getMaxLength() {
        return IWConfiguration.ae2DenseWireMaxLength;
    }

    @Override
    public int getColour(ImmersiveNetHandler.Connection connection) {
        return 0x8C60AF;
    }

    @Override
    public ItemStack getWireCoil() {
        return new ItemStack(ItemRegistryAE2.WIRE_COIL, 1, Wires.FLUIX_DENSE.ordinal());
    }

    @Override
    public double getRenderDiameter() {
        return 0.1f;
    }

    @Override
    public double getSlack() {
        return 1.008;
    }
}
