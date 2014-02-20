package com.feedthebeast.Transformer;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TeamClassTransformer implements IClassTransformer
{
	@Override
	public byte[] transform(String arg0, String arg1, byte[] arg2)
	{
		if (arg0.equals("net.minecraft.client.renderer.entity.RendererLivingEntity"))
		{
			System.out.println("[TEAM] Patching Renderer Class Dev");
			return patchRendererClass(arg2, false);
		}
		else if (arg0.equals("bhb"))
		{
			System.out.println("[TEAM] Patching Renderer Class Obfuscated");
			return patchRendererClass(arg2, true);
		}
		else
		{
			return arg2;
		}
	}

	private byte[] patchRendererClass(byte[] bytes, boolean obfuscated)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);

		String renderLabel = obfuscated ? "a" : "renderLivingLabel";
		String setColor = obfuscated ? "a" : "setColorRGBA_F";
		String fontRenderer = obfuscated ? "bfq" : "net/minecraft/client/gui/FontRenderer";
		String drawString = obfuscated? "b":"drawString";

		List<MethodNode> methods = classNode.methods;
		ArrayList<AbstractInsnNode> toRemove = new ArrayList<AbstractInsnNode>();
		
		for (MethodNode mn : methods)
		{
			if (mn.name.equals(renderLabel))
			{
				System.out.println("Found renderLabel");
				for (int i = 0; i < mn.instructions.toArray().length; i++)
				{
					AbstractInsnNode in = mn.instructions.get(i);
					if (in instanceof MethodInsnNode)
					{
						MethodInsnNode mi = (MethodInsnNode) in;
						if (mi.owner.equals(fontRenderer) && mi.name.equals(drawString))
						{
							System.out.println("Found a drawString");
							toRemove.add(mi);
							toRemove.add(mn.instructions.get(i-10));
							MethodInsnNode toInsert = new MethodInsnNode(INVOKESTATIC,"com/feedthebeast/Handler/CoreRedirector", "drawPlayerString", "(Ljava/lang/String;III)I");
							mn.instructions.insertBefore(mi, toInsert);
						}
					}
				}
			}
		}

		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(cw);
		return cw.toByteArray();
	}
}
