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

import java.util.EnumSet;

public enum QuartzType
        implements IStringSerializable, IWireFluixType
{
    QUARTZ;

    public static final PropertyEnum<QuartzType> TYPE = PropertyEnum.create("type", QuartzType.class);
    public static final QuartzType[] VALUES = values();

    @Override
    public EnumSet<GridFlags> getFlags() {
        return EnumSet.of(GridFlags.CANNOT_CARRY);
    }

    @Override
    public AECableType getCableType() {
        return AECableType.GLASS;
    }

    @Override
    public WireType getWireType() {
        return Wires.QUARTZ.type;
    }

    @Override
    public double getRelayOffset() {
        return -0.1D;
    }

    @Override
    public double getRelayHeight() {
        return 0.4375D;
    }

    @Override
    public String getName() {
        return this.name().toLowerCase();
    }
}
