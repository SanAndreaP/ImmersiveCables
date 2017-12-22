/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.core;

import de.sanandrew.mods.immersivecables.util.ICConstants;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

public class ASMHelper
{
    public static byte[] createBytes(ClassNode cnode, int cwFlags) {
        ClassWriter cw = new ClassWriter(cwFlags);
        cnode.accept(cw);
        byte[] bArr = cw.toByteArray();
        ICConstants.LOG.log(Level.INFO, String.format("Class %s successfully transformed!", cnode.name));
        return bArr;
    }

    public static ClassNode createClassNode(byte[] bytes) {
        ClassNode cnode = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(cnode, ClassReader.EXPAND_FRAMES);
        return cnode;
    }

    public static AbstractInsnNode findFirstNodeFromNeedle(InsnList haystack, InsnList needle) {
        List<AbstractInsnNode> ret = InstructionComparator.insnListFindStart(haystack, needle);

        if( ret.size() != 1 ) {
            throw new InvalidNeedleException(ret.size());
        }

        return ret.get(0);
    }

    public static MethodNode findMethodNode(ClassNode cnode, String name, String desc) {
        for( MethodNode mnode : cnode.methods ) {
            if( name.equals(mnode.name) && desc.equals(mnode.desc) ) {
                return mnode;
            }
        }
        throw new MethodNotFoundException(name, desc);
    }

    public static class InvalidNeedleException
            extends RuntimeException
    {
        private static final long serialVersionUID = -913530798954926801L;

        public InvalidNeedleException(int count) {
            super(count > 1 ? "Multiple Needles found in Haystack!" : count < 1 ? "Needle not found in Haystack!" : "Wait, Needle was found!? o.O");
        }
    }

    public static class MethodNotFoundException
            extends RuntimeException
    {
        private static final long serialVersionUID = 7439846361566319105L;

        public MethodNotFoundException(String methodName, String methodDesc) {
            super(String.format("Could not find any method matching the name < %s > and description < %s >", methodName, methodDesc));
        }
    }
}
