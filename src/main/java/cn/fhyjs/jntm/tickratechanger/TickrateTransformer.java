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
            else if(name.equals("net.minecraft.world.WorldServer")) {
                //return patchWorldServer(bytes);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return bytes;
    }
    private byte[] patchWorldServer(byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                // 你要修改的目标方法名称，例如 "doRender"，以及方法描述符 "([Lnet/minecraft/entity/EntityLivingBase;DDDFF)V"
                String targetMethodName = "func_175712_a";
                String targetMethodDesc = "(Lnet/minecraft/world/gen/structure/StructureBoundingBox;Z)Ljava/util/List;";

                if ((name.equals(targetMethodName)||name.equals("getPendingBlockUpdates")) && desc.equals(targetMethodDesc)) {
                    return new MyMethodVisitorWSGPBU(super.visitMethod(access, name, desc, signature, exceptions));
                }

                return super.visitMethod(access, name, desc, signature, exceptions);
            }
        };

        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
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
    private static class MyMethodVisitorWSGPBU extends MethodVisitor {

        public MyMethodVisitorWSGPBU(MethodVisitor methodVisitor) {
            super(Opcodes.ASM4, methodVisitor);
        }

        @Override
        public void visitCode() {
            // 使用 Label 来标记原始代码块的位置
            Label originalCodeLabel = new Label();
            Label startLabel = new Label();
            Label endLabel = new Label();

            // 加载 i，判断 i 是否为 0
            super.visitVarInsn(Opcodes.ILOAD, 1);
            super.visitJumpInsn(Opcodes.IFEQ, originalCodeLabel); // 如果 i == 0，跳转到原始代码块

            // i == 0 的情况，使用新的代码
            // 创建 TreeSet 和 ArrayList，并复制集合中的元素
            super.visitVarInsn(Opcodes.ALOAD, 0);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/WorldServer", "pendingTickListEntriesTreeSet", "Ljava/util/SortedSet;");
            super.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/TreeSet", "<init>", "(Ljava/util/Collection;)V", false);
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/SortedSet", "iterator", "()Ljava/util/Iterator;", true);
            super.visitJumpInsn(Opcodes.GOTO, startLabel);

            // 原始代码块
            super.visitLabel(originalCodeLabel);
            super.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            super.visitVarInsn(Opcodes.ILOAD, 1);
            super.visitJumpInsn(Opcodes.IFNE, endLabel); // 如果 i != 0，跳转到原始代码块结束

            // i != 0 的情况，使用新的代码
            // 创建 ArrayList，并复制集合中的元素
            super.visitVarInsn(Opcodes.ALOAD, 0);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/WorldServer", "pendingTickListEntriesThisTick", "Ljava/util/List;");
            super.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/ArrayList", "<init>", "(Ljava/util/Collection;)V", false);
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "iterator", "()Ljava/util/Iterator;", true);

            // 声明开始位置
            super.visitLabel(startLabel);

            // 将 iterator 存储到局部变量表
            super.visitVarInsn(Opcodes.ASTORE, 2);

            // 开始循环，使用 i 作为计数器
            super.visitInsn(Opcodes.ICONST_0); // 初始化 i = 0
            super.visitVarInsn(Opcodes.ISTORE, 3); // 将计数器存储到局部变量表中

            // 开始循环
            super.visitJumpInsn(Opcodes.GOTO, endLabel);

            // 循环体开始位置
            super.visitLabel(startLabel);

            // 加载 iterator 并调用 hasNext() 方法判断是否有下一个元素
            super.visitVarInsn(Opcodes.ALOAD, 2);
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
            super.visitJumpInsn(Opcodes.IFEQ, endLabel); // 如果没有下一个元素，跳转到循环结束位置

            // 加载 iterator 并调用 next() 方法获取下一个元素
            super.visitVarInsn(Opcodes.ALOAD, 2);
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
            super.visitTypeInsn(Opcodes.CHECKCAST, "net/minecraft/world/NextTickListEntry");

            // 下面是原始循环体中的代码，这里只是将代码拷贝过来
            // 你可以在这里根据需要添加代码
            // 注意，由于现在已经在一个方法中进行了两个集合的遍历，如果你不需要修改原始代码，
            // 可以将原始代码保留在这里，并在新的循环体中执行其他操作。

            super.visitVarInsn(Opcodes.ASTORE, 4);
            super.visitVarInsn(Opcodes.ALOAD, 4);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/NextTickListEntry", "position", "Lnet/minecraft/util/math/BlockPos;");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getX", "()I", false);
            super.visitVarInsn(Opcodes.ILOAD, 5);
            super.visitJumpInsn(Opcodes.IF_ICMPLT, startLabel);
            super.visitVarInsn(Opcodes.ALOAD, 4);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/NextTickListEntry", "position", "Lnet/minecraft/util/math/BlockPos;");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getX", "()I", false);
            super.visitVarInsn(Opcodes.ILOAD, 6);
            super.visitJumpInsn(Opcodes.IF_ICMPGE, endLabel);
            super.visitVarInsn(Opcodes.ALOAD, 4);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/NextTickListEntry", "position", "Lnet/minecraft/util/math/BlockPos;");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getZ", "()I", false);
            super.visitVarInsn(Opcodes.ILOAD, 7);
            super.visitJumpInsn(Opcodes.IF_ICMPLT, startLabel);
            super.visitVarInsn(Opcodes.ALOAD, 4);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/NextTickListEntry", "position", "Lnet/minecraft/util/math/BlockPos;");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getZ", "()I", false);
            super.visitVarInsn(Opcodes.ILOAD, 8);
            super.visitJumpInsn(Opcodes.IF_ICMPLT, endLabel);
            super.visitLabel(startLabel);
            super.visitFrame(Opcodes.F_FULL, 5, new Object[] {"net/minecraft/world/WorldServer", Opcodes.INTEGER, "java/util/Iterator", "net/minecraft/world/NextTickListEntry", "net/minecraft/util/math/BlockPos"}, 0, new Object[] {});
            super.visitVarInsn(Opcodes.ALOAD, 4);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/NextTickListEntry", "position", "Lnet/minecraft/util/math/BlockPos;");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getX", "()I", false);
            super.visitVarInsn(Opcodes.ALOAD, 9);
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getX", "()I", false);
            super.visitVarInsn(Opcodes.ILOAD, 10);
            super.visitJumpInsn(Opcodes.IF_ICMPGE, endLabel);
            super.visitVarInsn(Opcodes.ALOAD, 4);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/NextTickListEntry", "position", "Lnet/minecraft/util/math/BlockPos;");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getZ", "()I", false);
            super.visitVarInsn(Opcodes.ALOAD, 11);
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getZ", "()I", false);
            super.visitVarInsn(Opcodes.ILOAD, 12);
            super.visitJumpInsn(Opcodes.IF_ICMPGE, endLabel);
            super.visitVarInsn(Opcodes.ALOAD, 13);
            super.visitJumpInsn(Opcodes.IFNULL, endLabel);
            super.visitVarInsn(Opcodes.ALOAD, 14);
            super.visitTypeInsn(Opcodes.INSTANCEOF, "net/minecraft/world/NextTickListEntry");
            super.visitJumpInsn(Opcodes.IFEQ, endLabel);
            super.visitTypeInsn(Opcodes.NEW, "net/minecraft/world/NextTickListEntry");
            super.visitInsn(Opcodes.DUP);
            super.visitVarInsn(Opcodes.ALOAD, 4);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/NextTickListEntry", "position", "Lnet/minecraft/util/math/BlockPos;");
            super.visitVarInsn(Opcodes.ALOAD, 4);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/NextTickListEntry", "block", "Lnet/minecraft/block/Block;");
            super.visitVarInsn(Opcodes.ALOAD, 4);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/NextTickListEntry", "scheduledTime", "J");
            super.visitMethodInsn(Opcodes.INVOKESPECIAL, "net/minecraft/world/NextTickListEntry", "<init>", "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;J)V", false);
            super.visitVarInsn(Opcodes.ASTORE, 15);
            super.visitVarInsn(Opcodes.ALOAD, 14);
            super.visitTypeInsn(Opcodes.CHECKCAST, "net/minecraft/world/NextTickListEntry");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/NextTickListEntry", "getPriority", "()I", false);
            super.visitVarInsn(Opcodes.ALOAD, 15);
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/NextTickListEntry", "getPriority", "()I", false);
            super.visitJumpInsn(Opcodes.IF_ICMPLE, endLabel);
            super.visitVarInsn(Opcodes.ALOAD, 15);
            super.visitTypeInsn(Opcodes.CHECKCAST, "net/minecraft/world/NextTickListEntry");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/NextTickListEntry", "getPriority", "()I", false);
            super.visitVarInsn(Opcodes.ISTORE, 16);
            super.visitVarInsn(Opcodes.ALOAD, 14);
            super.visitTypeInsn(Opcodes.CHECKCAST, "net/minecraft/world/NextTickListEntry");
            super.visitVarInsn(Opcodes.ILOAD, 16);
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/NextTickListEntry", "setPriority", "(I)V", false);
            super.visitLabel(endLabel);
            super.visitFrame(Opcodes.F_CHOP,3, null, 0, null);
            super.visitIincInsn(3, 1);
            super.visitJumpInsn(Opcodes.GOTO, startLabel);
            super.visitLabel(endLabel);
            super.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            super.visitVarInsn(Opcodes.ALOAD, 2);
            super.visitInsn(Opcodes.ACONST_NULL);
            super.visitJumpInsn(Opcodes.IF_ACMPNE, endLabel);
            super.visitVarInsn(Opcodes.ALOAD, 1);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/util/math/StructureBoundingBox", "minX", "I");
            super.visitVarInsn(Opcodes.ALOAD, 1);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/util/math/StructureBoundingBox", "maxX", "I");
            super.visitVarInsn(Opcodes.ALOAD, 1);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/util/math/StructureBoundingBox", "minZ", "I");
            super.visitVarInsn(Opcodes.ALOAD, 1);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/util/math/StructureBoundingBox", "maxZ", "I");
            super.visitMethodInsn(Opcodes.INVOKESTATIC, "com/google/common/collect/Lists", "newArrayList", "()Ljava/util/ArrayList;", false);
            super.visitVarInsn(Opcodes.ASTORE, 2);
            super.visitVarInsn(Opcodes.ALOAD, 0);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/WorldServer", "pendingTickListEntries", "Ljava/util/Map;");
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Map", "values", "()Ljava/util/Collection;", true);
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Collection", "iterator", "()Ljava/util/Iterator;", true);
            super.visitVarInsn(Opcodes.ASTORE, 4);
            super.visitLabel(startLabel);
            super.visitFrame(Opcodes.F_FULL, 5, new Object[] {"net/minecraft/world/WorldServer", "net/minecraft/util/math/StructureBoundingBox", Opcodes.INTEGER, "java/util/Iterator", "java/util/List"}, 0, new Object[] {});
            super.visitVarInsn(Opcodes.ALOAD, 4);
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
            super.visitJumpInsn(Opcodes.IFEQ, endLabel);
            super.visitVarInsn(Opcodes.ALOAD, 4);
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
            super.visitTypeInsn(Opcodes.CHECKCAST, "net/minecraft/world/NextTickListEntry");
            super.visitVarInsn(Opcodes.ASTORE, 5);
            super.visitVarInsn(Opcodes.ALOAD, 5);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/NextTickListEntry", "position", "Lnet/minecraft/util/math/BlockPos;");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getX", "()I", false);
            super.visitVarInsn(Opcodes.ALOAD, 1);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/util/math/StructureBoundingBox", "minX", "I");
            super.visitVarInsn(Opcodes.ILOAD, 6);
            super.visitJumpInsn(Opcodes.IF_ICMPLT, startLabel);
            super.visitVarInsn(Opcodes.ALOAD, 5);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/NextTickListEntry", "position", "Lnet/minecraft/util/math/BlockPos;");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getX", "()I", false);
            super.visitVarInsn(Opcodes.ALOAD, 1);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/util/math/StructureBoundingBox", "maxX", "I");
            super.visitVarInsn(Opcodes.ILOAD, 7);
            super.visitJumpInsn(Opcodes.IF_ICMPGE, endLabel);
            super.visitVarInsn(Opcodes.ALOAD, 5);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/NextTickListEntry", "position", "Lnet/minecraft/util/math/BlockPos;");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getZ", "()I", false);
            super.visitVarInsn(Opcodes.ALOAD, 1);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/util/math/StructureBoundingBox", "minZ", "I");
            super.visitVarInsn(Opcodes.ILOAD, 8);
            super.visitJumpInsn(Opcodes.IF_ICMPLT, startLabel);
            super.visitVarInsn(Opcodes.ALOAD, 5);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/NextTickListEntry", "position", "Lnet/minecraft/util/math/BlockPos;");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getZ", "()I", false);
            super.visitVarInsn(Opcodes.ALOAD, 1);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/util/math/StructureBoundingBox", "maxZ", "I");
            super.visitVarInsn(Opcodes.ILOAD, 9);
            super.visitJumpInsn(Opcodes.IF_ICMPGE, endLabel);
            super.visitVarInsn(Opcodes.ALOAD, 2);
            super.visitVarInsn(Opcodes.ALOAD, 5);
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
            super.visitInsn(Opcodes.POP);
            super.visitJumpInsn(Opcodes.GOTO, startLabel);
            super.visitLabel(endLabel);
            super.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            super.visitVarInsn(Opcodes.ALOAD, 2);
            super.visitJumpInsn(Opcodes.IFNULL, endLabel);
            super.visitVarInsn(Opcodes.ALOAD, 2);
            super.visitInsn(Opcodes.ARETURN);
            super.visitLabel(endLabel);
            super.visitFrame(Opcodes.F_CHOP,1, null, 0, null);
            super.visitVarInsn(Opcodes.ALOAD, 1);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/util/math/StructureBoundingBox", "minX", "I");
            super.visitVarInsn(Opcodes.ALOAD, 1);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/util/math/StructureBoundingBox", "maxX", "I");
            super.visitVarInsn(Opcodes.ALOAD, 1);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/util/math/StructureBoundingBox", "minZ", "I");
            super.visitVarInsn(Opcodes.ALOAD, 1);
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/util/math/StructureBoundingBox", "maxZ", "I");
            super.visitMethodInsn(Opcodes.INVOKESTATIC, "com/google/common/collect/Lists", "newArrayList", "(II)Ljava/util/ArrayList;", false);
            super.visitInsn(Opcodes.ARETURN);
            super.visitMaxs(5, 18);
            super.visitEnd();
        }

        // 如果你想在方法的结尾插入代码，可以重写 visitInsn 方法并在其中添加字节码指令

        // 更多的方法访问回调可以根据需要进行重写
        // 例如，visitVarInsn，visitTypeInsn，visitFieldInsn 等等
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
