package de.sanandrew.mods.immersivewiring.wire;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import de.sanandrew.mods.immersivewiring.item.ItemRegistry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class FluixWire
        extends WireType
{
    @Override
    public String getUniqueName() {
        return "FLUIX";
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
        return 0xD29BFF;
    }

    @Override
    public int getMaxLength() {
        return 16;//TODO: add ImmersiveIntegration.cfg.fluixWireRange;
    }

    @Override
    public ItemStack getWireCoil() {
        return new ItemStack(ItemRegistry.WIRE_COIL, 1, WireRegistry.Wire.FLUIX.ordinal());
    }

    @Override
    public double getRenderDiameter() {
        return 0.06f;
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
