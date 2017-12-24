/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.block.ae2;

import appeng.api.networking.GridFlags;
import appeng.api.util.AECableType;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import de.sanandrew.mods.immersivecables.wire.Wires;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum FluixType
        implements IStringSerializable
{
    FLUIX(Wires.FLUIX.getType(), AECableType.SMART),
    FLUIX_DENSE(Wires.FLUIX_DENSE.getType(), AECableType.DENSE_SMART, GridFlags.DENSE_CAPACITY);

    public static final PropertyEnum<FluixType> TYPE = PropertyEnum.create("type", FluixType.class);
    public static final FluixType[] VALUES = values();

    public final GridFlags[] flags;
    public final AECableType cableType;
    public final WireType wireType;

    FluixType(WireType wireType, AECableType cableType, GridFlags... flags) {
        this.wireType = wireType;
        this.cableType = cableType;
        this.flags = flags == null ? new GridFlags[0] : flags;
    }

    @Override
    public String getName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
