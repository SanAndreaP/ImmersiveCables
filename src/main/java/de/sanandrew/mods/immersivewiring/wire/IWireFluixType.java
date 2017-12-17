/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivewiring.wire;

import appeng.api.networking.GridFlags;
import appeng.api.util.AECableType;
import blusunrize.immersiveengineering.api.energy.wires.WireType;

import java.util.EnumSet;

public interface IWireFluixType
{
    EnumSet<GridFlags> getFlags();

    AECableType getCableType();

    WireType getWireType();

    double getRelayOffset();

    double getRelayHeight();
}
