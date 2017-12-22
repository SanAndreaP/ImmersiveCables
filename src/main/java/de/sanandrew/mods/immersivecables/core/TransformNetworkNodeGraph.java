/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.core;

import de.sanandrew.mods.immersivecables.util.ICConfiguration;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TransformNetworkNodeGraph
        implements IClassTransformer
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if( ICConfiguration.rsEnabled && "com.raoulvdberge.refinedstorage.apiimpl.network.NetworkNodeGraph".equals(transformedName) ) {
            ClassNode cls = ASMHelper.createClassNode(basicClass);

            MethodNode fkt = ASMHelper.findMethodNode(cls, "rebuild", "(Lnet/minecraft/util/math/BlockPos;Z)V");
            InsnList needle = new InsnList();
            needle.add(new VarInsnNode(Opcodes.ALOAD, 8));
            needle.add(new TypeInsnNode(Opcodes.INSTANCEOF, "com/raoulvdberge/refinedstorage/tile/TileNetworkTransmitter"));

            AbstractInsnNode insertPt = ASMHelper.findFirstNodeFromNeedle(fkt.instructions, needle);

            InsnList injectData = new InsnList();
            injectData.add(new VarInsnNode(Opcodes.ALOAD, 8));
            injectData.add(new TypeInsnNode(Opcodes.INSTANCEOF, "de/sanandrew/mods/immersivecables/core/IRefinedAdvNode"));
            LabelNode lbl = new LabelNode();
            injectData.add(new JumpInsnNode(Opcodes.IFEQ, lbl));
            injectData.add(new LabelNode());
            injectData.add(new VarInsnNode(Opcodes.ALOAD, 8));
            injectData.add(new TypeInsnNode(Opcodes.CHECKCAST, "de/sanandrew/mods/immersivecables/core/IRefinedAdvNode"));
            injectData.add(new VarInsnNode(Opcodes.ALOAD, 5));
            injectData.add(new VarInsnNode(Opcodes.ALOAD, 6));
            injectData.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "de/sanandrew/mods/immersivecables/core/IRefinedAdvNode", "onNodeTraverse", "(Ljava/util/Set;Ljava/util/Queue;)V", true));
            injectData.add(lbl);

            fkt.instructions.insertBefore(insertPt, injectData);

            return ASMHelper.createBytes(cls, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        }

        return basicClass;
    }
}
