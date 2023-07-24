package cn.fhyjs.jntm.utility;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.config.ConfigCore;
import cn.fhyjs.jntm.network.Opt_Ply_Message;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TelnetServer implements Runnable{
    public static Map<String,ClientHandler> funcs = new HashMap<>();
    public TelnetServer(){
        super();
    }
    @Override
    public void run() {
        int portNumber = ConfigCore.telnetPort; // Telnet默认端口号为23

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Telnet was started on port" + portNumber);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("a new connection:" + clientSocket.getInetAddress().getHostAddress());

                // 每个客户端连接都创建一个新线程来处理
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Telnet error :" + e.getMessage());
        }
    }

    public static class ClientHandler extends Thread implements ICommandSender{
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        public void send(String str){
            if (out!=null)
                out.println(str);
        }
        public String exec(String cmd){
            if (cmd.startsWith(".")){
                interal(cmd.substring(1));
                return "";
            }
            if (cmd.startsWith("#")){
                regFunc(cmd.substring(1));
                return "";
            }
            //getServer().commandManager.executeCommand(this,cmd);
            executeCommandInMainThread(this,cmd);
            //out.println(code);
            return "";
        }
        // 这个方法用于在多线程中执行指令
        IThreadListener mainThread = FMLCommonHandler.instance().getMinecraftServerInstance();
        public void executeCommandInMainThread(final ICommandSender sender, final String rawCommand) {
            // 示例：在另一个线程中执行指令
            Runnable task = () -> getServer().commandManager.executeCommand(sender,rawCommand);
            mainThread.addScheduledTask(task);
        }
        private void regFunc(String name) {
            getServer().commandManager.executeCommand(this,String.format("say %s %s", I18n.translateToLocal("telnet.reg"),name));
            TelnetServer.funcs.put(name,this);
        }

        private void interal(String cmd) {
            if (cmd.equals("refresh"))
                Jntm.proxy.refreshAllBlocks();
            CommonProxy.INSTANCE.sendToServer(new Opt_Ply_Message(null,cmd));
        }

        PrintWriter out = null;
        BufferedReader in = null;
        @Override
        public void run() {
            setUncaughtExceptionHandler(this::error);
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),StandardCharsets.UTF_8));

                out.println("hello from The Project Jntm mod!");

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    //inputLine = new String(inputLine.getBytes(StandardCharsets.US_ASCII),StandardCharsets.UTF_8);
                    if ("exit".equalsIgnoreCase(inputLine)) {
                        break;
                    }

                    exec(inputLine);


                }
            } catch (IOException e) {
                System.err.println("processing <" + clientSocket.getInetAddress().getHostAddress() + "> with an error:" + e.getMessage());
                close();
            }
            close();
        }
        private void close(){
            for (String s : new ArrayList<>(TelnetServer.funcs.keySet())) {
                if (TelnetServer.funcs.get(s)==this)
                    TelnetServer.funcs.remove(s);
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                clientSocket.close();
                System.out.println("connection<" + clientSocket.getInetAddress().getHostAddress() + ">was closed。");
            } catch (IOException e) {
                System.err.println("closing<" + clientSocket.getInetAddress().getHostAddress() + ">connection with a error" + e.getMessage());
            }
        }
        private void error(Thread thread, Throwable throwable) {
            StringWriter sw =new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            out.println(sw);
            Jntm.logger.warn(sw);
            close();
        }

        @Override
        public boolean canUseCommand(int permLevel, String commandName) {
            return true;
        }

        @Override
        public World getEntityWorld() {
            return getServer().getEntityWorld();
        }

        @Nullable
        @Override
        public MinecraftServer getServer() {
            return FMLCommonHandler.instance().getMinecraftServerInstance();
        }

        @Override
        public void sendMessage(ITextComponent component) {
            ICommandSender.super.sendMessage(component);
            StringBuilder s = new StringBuilder();
            for (Object formatArg : ((TextComponentTranslation) component).getFormatArgs()) {
                s.append(" ");
                s.append(formatArg);
            }
            out.println(((TextComponentTranslation) component).getKey()+s);
        }
    }
}

