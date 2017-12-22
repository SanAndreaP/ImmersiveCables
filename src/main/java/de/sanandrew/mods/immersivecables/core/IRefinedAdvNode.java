/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.core;

import net.minecraft.util.math.BlockPos;

import java.util.Queue;
import java.util.Set;

@SuppressWarnings("unused")
public interface IRefinedAdvNode
{
    void onNodeTraverse(Set<BlockPos> checked, Queue<BlockPos> toCheck);
}
