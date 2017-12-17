/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.block.ae2;

import appeng.api.networking.GridFlags;
import appeng.api.util.AECableType;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import de.sanandrew.mods.immersivewiring.wire.IWireFluixType;
import de.sanandrew.mods.immersivewiring.wire.Wires;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

import java.util.Arrays;
import java.util.EnumSet;

public enum FluixType
        implements IStringSerializable, IWireFluixType
{
    FLUIX(Wires.FLUIX.getType(), AECableType.SMART, 0.1D, 0.625D),
    FLUIX_DENSE(Wires.FLUIX_DENSE.getType(), AECableType.DENSE, 0.3D, 0.875D, GridFlags.DENSE_CAPACITY);

    public static final PropertyEnum<FluixType> TYPE = PropertyEnum.create("type", FluixType.class);
    public static final FluixType[] VALUES = values();

    public final EnumSet<GridFlags> flags;
    public final AECableType cableType;
    public final WireType wireType;
    public final double relayOffset;
    public final double relayHeight;

    FluixType(WireType wireType, AECableType cableType, double relayOffset, double relayHeight, GridFlags... flags) {
        this.wireType = wireType;
        this.cableType = cableType;
        this.relayOffset = relayOffset;
        this.relayHeight = relayHeight;
        this.flags = flags == null || flags.length < 1 ? EnumSet.noneOf(GridFlags.class) : EnumSet.copyOf(Arrays.asList(flags));
    }

    @Override
    public String getName() {
        return this.name().toLowerCase();
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public EnumSet<GridFlags> getFlags() {
        return this.flags;
    }

    @Override
    public AECableType getCableType() {
        return this.cableType;
    }

    @Override
    public double getRelayOffset() {
        return this.relayOffset;
    }

    @Override
    public WireType getWireType() {
        return this.wireType;
    }

    @Override
    public double getRelayHeight() {
        return relayHeight;
    }
}
