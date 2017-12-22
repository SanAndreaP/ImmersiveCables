package de.sanandrew.mods.immersivecables.wire;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import de.sanandrew.mods.immersivecables.util.ICConfiguration;
import de.sanandrew.mods.immersivecables.util.ItemBlockRegistry;
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
        return ICConfiguration.ae2DenseWireMaxLength;
    }

    @Override
    public int getColour(ImmersiveNetHandler.Connection connection) {
        return 0x8C60AF;
    }

    @Override
    public ItemStack getWireCoil() {
        return new ItemStack(ItemBlockRegistry.WIRE_COIL, 1, Wires.FLUIX_DENSE.ordinal());
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
