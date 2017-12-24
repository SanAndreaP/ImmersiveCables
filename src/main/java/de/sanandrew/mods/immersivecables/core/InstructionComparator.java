/* ******************************************************************************************************************
   * Authors:   SanAndreasP
   * Copyright: SanAndreasP
   * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
   *                http://creativecommons.org/licenses/by-nc-sa/4.0/
   *******************************************************************************************************************/
package de.sanandrew.mods.immersivecables.core;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.LinkedList;
import java.util.List;

import static org.objectweb.asm.tree.AbstractInsnNode.*;

public final class InstructionComparator
{
    public static boolean fieldInsnEqual(FieldInsnNode insn1, FieldInsnNode insn2) {
        return insn1.owner.equals(insn2.owner) && insn1.name.equals(insn2.name) && insn1.desc.equals(insn2.desc);
    }

    public static boolean iincInsnEqual(IincInsnNode node1, IincInsnNode node2) {
        return node1.var == node2.var && node1.incr == node2.incr;
    }

    public static boolean insnEqual(AbstractInsnNode node1, AbstractInsnNode node2) {
        if( node1.getType() != node2.getType() ) {
            return false;
        } else if( node1.getOpcode() != node2.getOpcode() ) {
            return false;
        }

        switch( node2.getType() ) {
            case VAR_INSN:
                return varInsnEqual((VarInsnNode) node1, (VarInsnNode) node2);
            case TYPE_INSN:
                return typeInsnEqual((TypeInsnNode) node1, (TypeInsnNode) node2);
            case FIELD_INSN:
                return fieldInsnEqual((FieldInsnNode) node1, (FieldInsnNode) node2);
            case METHOD_INSN:
                return methodInsnEqual((MethodInsnNode) node1, (MethodInsnNode) node2);
            case LDC_INSN:
                return ldcInsnEqual((LdcInsnNode) node1, (LdcInsnNode) node2);
            case IINC_INSN:
                return iincInsnEqual((IincInsnNode) node1, (IincInsnNode) node2);
            case INT_INSN:
                return intInsnEqual((IntInsnNode) node1, (IntInsnNode) node2);
            default:
                return true;
        }
    }

    public static List<Integer> insnListFind(InsnList haystack, InsnList needle) {
        LinkedList<Integer> list = new LinkedList<>();

        for( int start = 0; start <= haystack.size() - needle.size(); start++ ) {
            if( insnListMatches(haystack, needle, start) ) {
                list.add(start);
            }
        }

        return list;
    }

    public static List<AbstractInsnNode> insnListFindStart(InsnList haystack, InsnList needle) {
        LinkedList<AbstractInsnNode> callNodes = new LinkedList<>();

        for( int callPoint : insnListFind(haystack, needle) ) {
            callNodes.add(haystack.get(callPoint));
        }
        return callNodes;
    }

    public static boolean insnListMatches(InsnList haystack, InsnList needle, int start) {
        if( haystack.size() - start < needle.size() ) {
            return false;
        }

        for( int i = 0; i < needle.size(); i++ ) {
            if( !insnEqual(haystack.get(i + start), needle.get(i)) ) {
                return false;
            }
        }
        return true;
    }

    public static boolean intInsnEqual(IntInsnNode node1, IntInsnNode node2) {
        return node1.operand == -1 || node2.operand == -1 || node1.operand == node2.operand;

    }

    public static boolean ldcInsnEqual(LdcInsnNode insn1, LdcInsnNode insn2) {
        return insn1.cst.equals("~") || insn2.cst.equals("~") || insn1.cst.equals(insn2.cst);

    }

    public static boolean methodInsnEqual(MethodInsnNode insn1, MethodInsnNode insn2) {
        return insn1.owner.equals(insn2.owner) && insn1.name.equals(insn2.name) && insn1.desc.equals(insn2.desc);
    }

    public static boolean typeInsnEqual(TypeInsnNode insn1, TypeInsnNode insn2) {
        return insn1.desc.equals("~") || insn2.desc.equals("~") || insn1.desc.equals(insn2.desc);

    }

    public static boolean varInsnEqual(VarInsnNode insn1, VarInsnNode insn2) {
        return insn1.var == -1 || insn2.var == -1 || insn1.var == insn2.var;

    }
}
