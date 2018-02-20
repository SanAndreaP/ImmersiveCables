package de.sanandrew.mods.immersivecables.wire;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import de.sanandrew.mods.immersivecables.util.ICConfiguration;
import de.sanandrew.mods.immersivecables.util.ItemBlockRegistry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class FluixWire
        extends WireType
{
    @Override
    public String getUniqueName() {
        return "AE2FLUIX";
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
        return WireColors.ae2FluixWireColor;
    }

    @Override
    public int getMaxLength() {
        return ICConfiguration.ae2FluixWireMaxLength;
    }

    @Override
    public ItemStack getWireCoil() {
        return new ItemStack(ItemBlockRegistry.WIRE_COIL, 1, Wires.FLUIX.ordinal());
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

    @Nullable
    @Override
    public String getCategory() {
        return "IC_ME_S";
    }
}
