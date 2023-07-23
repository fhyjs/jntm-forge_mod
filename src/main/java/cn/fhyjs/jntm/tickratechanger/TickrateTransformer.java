package cn.fhyjs.jntm.tickratechanger;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

public class TickrateTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String name2, byte[] bytes) {
        if(bytes == null) return null;

        // Gladly, no obfuscation needed, since all methods and classes patched are obfuscated
        // MinecraftServer - Main class of the server, the name is not obfuscated because of server scripts
        // run - Method implemented from Runnable
        // SoundSystem - Class from the sound system library. Libraries are not obfuscated
        // setPitch - Also from the sound system library

        try {
            if(name.equals("net.minecraft.server.MinecraftServer")) {
                return patchServerTickrate(bytes);
            } else if(name.equals("paulscode.sound.SoundSystem")) {
                return patchSoundSystem(bytes);
            } else if(name.equals("net.minecraft.server.management.PlayerChunkMap")) {
                //return patchPlayerChunkMap(bytes);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return bytes;
    }

    private byte[] patchPlayerChunkMap(byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                // 你要修改的目标方法名称，例如 "doRender"，以及方法描述符 "([Lnet/minecraft/entity/EntityLivingBase;DDDFF)V"
                String targetMethodName = "tick";
                String targetMethodDesc = "()V";

                if (name.equals(targetMethodName) && desc.equals(targetMethodDesc)) {
                    return new MyMethodVisitorCMPT(super.visitMethod(access, name, desc, signature, exceptions));
                }

                return super.visitMethod(access, name, desc, signature, exceptions);
            }
        };

        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }
    private static class MyMethodVisitorCMPT extends MethodVisitor {

        public MyMethodVisitorCMPT(MethodVisitor methodVisitor) {
            super(Opcodes.ASM4, methodVisitor);
        }

        @Override
        public void visitCode() {
            // 使用 Label 来标记目标代码块的位置
            Label startLabel = new Label();
            Label endLabel = new Label();

            // 加载 this.dirtyEntries，将其替换为新的代码
            super.visitVarInsn(Opcodes.ALOAD, 0);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/server/management/PlayerChunkMap", "dirtyEntries", "Ljava/util/Set;");
            // 判断 this.dirtyEntries 是否为空，如果为空跳转到目标代码块的开始位置
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Set", "isEmpty", "()Z", true);
            super.visitJumpInsn(Opcodes.IFNE, startLabel);

            // 开始循环，使用 i 作为计数器
            super.visitInsn(Opcodes.ICONST_0); // 初始化 i = 0
            super.visitVarInsn(Opcodes.ISTORE, 1); // 将计数器存储到局部变量表中

            // 加载 this.dirtyEntries，并调用 size() 方法获取集合大小
            super.visitVarInsn(Opcodes.ALOAD, 0);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/server/management/PlayerChunkMap", "dirtyEntries", "Ljava/util/Set;");
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Set", "size", "()I", true);
            // 使用 i 和集合大小比较，判断循环是否继续
            super.visitVarInsn(Opcodes.ILOAD, 1); // 将计数器加载到栈中
            super.visitJumpInsn(Opcodes.IF_ICMPGE, endLabel); // 循环结束条件

            // 加载 this.dirtyEntries，并调用 get(i) 方法获取指定索引的元素
            super.visitVarInsn(Opcodes.ALOAD, 0);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/server/management/PlayerChunkMap", "dirtyEntries", "Ljava/util/Set;");
            super.visitVarInsn(Opcodes.ILOAD, 1); // 将计数器加载到栈中
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;", true);
            super.visitTypeInsn(Opcodes.CHECKCAST, "net/minecraft/server/management/PlayerChunkMapEntry");
            // 调用 update() 方法
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/server/management/PlayerChunkMapEntry", "update", "()V", false);

            // 计数器 i++
            super.visitIincInsn(1, 1);
            // 继续循环，跳转到开始位置
            super.visitJumpInsn(Opcodes.GOTO, startLabel);

            // 结束循环，跳转到目标代码块的结束位置
            super.visitLabel(endLabel);

            // 清空 this.dirtyEntries
            super.visitVarInsn(Opcodes.ALOAD, 0);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/server/management/PlayerChunkMap", "dirtyEntries", "Ljava/util/Set;");
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Set", "clear", "()V", true);

            // 声明目标代码块的开始位置
            super.visitLabel(startLabel);

            // 继续访问原始方法的字节码
            super.visitCode();
        }

        // 如果你想在方法的结尾插入代码，可以重写 visitInsn 方法并在其中添加字节码指令

        // 更多的方法访问回调可以根据需要进行重写
        // 例如，visitVarInsn，visitTypeInsn，visitFieldInsn 等等
    }

    public byte[] patchServerTickrate(byte[] bytes) {
        TickrateChanger.LOGGER.info("Applying ASM to Minecraft methods...");

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext()) {
            MethodNode method = methods.next();
            if((method.name.equals("run")) && (method.desc.equals("()V"))) {
                InsnList list = new InsnList();
                for(AbstractInsnNode node : method.instructions.toArray()) {

                    if(node instanceof LdcInsnNode) {
                        LdcInsnNode ldcNode = (LdcInsnNode)node;
                        if((ldcNode.cst instanceof Long) && ((Long)ldcNode.cst == 50L)) {
                            list.add(new FieldInsnNode(Opcodes.GETSTATIC, "cn/fhyjs/jntm/tickratechanger/TickrateChanger", "MILISECONDS_PER_TICK", "J"));
                            continue;
                        }
                    }

                    list.add(node);
                }

                method.instructions.clear();
                method.instructions.add(list);
            }

        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public byte[] patchSoundSystem(byte[] bytes) {
        TickrateChanger.LOGGER.info("Patching sound system...");

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext()) {
            MethodNode method = methods.next();
            if(method.name.equals("setPitch") && method.desc.equals("(Ljava/lang/String;F)V")) {
                InsnList inst = new InsnList();
                inst.add(new VarInsnNode(Opcodes.FLOAD, 2));
                inst.add(new FieldInsnNode(Opcodes.GETSTATIC, "cn/fhyjs/jntm/tickratechanger/TickrateChanger", "GAME_SPEED", "F"));
                inst.add(new InsnNode(Opcodes.FMUL));
                inst.add(new VarInsnNode(Opcodes.FSTORE, 2));
                inst.add(method.instructions);
                method.instructions = inst;
                break;
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

}
