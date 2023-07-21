package cn.fhyjs.jntm.utility;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TelnetServer implements Runnable{
    public TelnetServer(){
        super();
    }

    @Override
    public void run() {
        int portNumber = 23; // Telnet默认端口号为23

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

    private static class ClientHandler extends Thread implements ICommandSender{
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        int code;
        public String exec(String cmd){
            getServer().commandManager.executeCommand(this,cmd);
            //out.println(code);
            return "";
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

                    out.println(exec(inputLine));


                }
            } catch (IOException e) {
                System.err.println("processing <" + clientSocket.getInetAddress().getHostAddress() + "> with an error:" + e.getMessage());
                close();
            }
            close();
        }
        private void close(){
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

