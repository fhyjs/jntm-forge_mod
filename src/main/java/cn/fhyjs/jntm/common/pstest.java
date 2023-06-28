package cn.fhyjs.jntm.common;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.tickratechanger.api.TickrateAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

/* Generated by noteblockstudio2java. You may need to perform the "Alt + enter" operation manually */
public class pstest extends Thread{
    private final EntityPlayer entityplayer; private final World world;private final BlockPos bp;private String[] split;

    public pstest(EntityPlayer entityplayer, World world, BlockPos bp,ResourceLocation rl) {
        this.entityplayer=entityplayer;
        this.world=world;
        this.bp=bp;
        try {
            InputStream is;
            String iss;
            is=Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(rl);
            iss=readLine(is);
            iss=iss.replace("\r","");
            this.split=iss.split("\n");
        } catch (IOException e) {
            Jntm.logger.error(new RuntimeException(e));
        }
    }
    public pstest(EntityPlayer entityplayer, World world, BlockPos bp,File f) {
        this.entityplayer=entityplayer;
        this.world=world;
        this.bp=bp;
        try {
            InputStream is;
            String iss;
            is=new FileInputStream(f);
            iss=readLine(is);
            iss=iss.replace("\r","");
            this.split=iss.split("\n");
        } catch (IOException e) {
            Jntm.logger.error(new RuntimeException(e));
        }
    }
    @Override
    public void run(){
        for (int i=0;i<=split.length;i++){
            String[] ts;
            try {
                 ts= split[i].split(" ");
                if (Objects.equals(ts[0], "playsound")){
                    ts[1]=ts[1].replace("note_block","note");
                    world.playSound(entityplayer,bp,SoundEvent.REGISTRY.getObject(new ResourceLocation(ts[1])),SoundCategory.getByName(ts[2]),Float.parseFloat(ts[3]),Float.parseFloat(ts[4]));
                }
                if (Objects.equals(ts[0], "timeout")){
                    try {
                        Thread.sleep((long) (Integer.parseInt(ts[1])* (20/TickrateAPI.getServerTickrate())));
                    } catch (InterruptedException e) {
                        Jntm.logger.error(new RuntimeException(e));
                    }
                }
            }catch (ArrayIndexOutOfBoundsException ignored){}
        }
    }
    /**
     * Read a line of data from the underlying inputstream
     *
     * @return a line stripped of line terminators
     */
    public String readLine(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
