package cn.fhyjs.jntm.utility;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class MediaPlayer {
    static Thread checkerT=new Thread(new Checker());
    public static Map<Object,Object> tasks = new HashMap<>();
    public static List<Object> needCheck = new ArrayList<>();
    public static void mp3Player(Object id,InputStream inputStream) {
        if (getMp3Player(id)!=null)
            getMp3Player(id).stop();
        new Thread(() -> new MediaPlayer().mp3Player0(id,inputStream)).start();
    }
    public static void tickMp3Player(Object id, InputStream inputStream) {
        if (checkerT.getState().equals(Thread.State.NEW)||checkerT.getState().equals(Thread.State.TERMINATED)) {
            checkerT.start();
        }
        if (getMp3Player(id)!=null&&isPlaying(id)) {
            ((Checker) ObfuscationReflectionHelper.getPrivateValue(Thread.class, checkerT, "target")).update(id);
        }else {
            new Thread(() -> {
                new MediaPlayer().mp3Player0(id, inputStream,true);
            }).start();
        }
    }
    public static @Nullable AdvancedPlayer getMp3Player(Object id){
        if (!tasks.containsKey(id)) return null;
        Object t = tasks.get(id);
        if (t instanceof AdvancedPlayer)
            return (AdvancedPlayer) t;
        return null;
    }
    public static boolean isPlaying(Object id) {
        return tasks.containsKey(id);
    }
    public boolean isPlaying;
    private void mp3Player0(Object id, InputStream inputStream,boolean... arg) {
        try {
            Bitstream bitstream = new Bitstream(inputStream);
            Header header = bitstream.readFrame();
            int totalFrames = header.framesize;

            AdvancedPlayer player = new AdvancedPlayer(inputStream);
            tasks.put(id, player);
            if (arg.length>0&&arg[0]){
                needCheck.add(id);
            }
            player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackFinished(PlaybackEvent evt) {
                    tasks.remove(id);
                    isPlaying=false;
                    if (arg.length>0&&arg[0]){
                        needCheck.remove(id);
                    }
                }
            });

            player.play();
            isPlaying=true;
            // 播放音频的时间，根据需要适当调整
            Thread.sleep((long) (totalFrames * header.ms_per_frame()));

            player.close();
            bitstream.close();
            inputStream.close();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    private static class Checker implements Runnable{
        Map<Object,Long> time = new HashMap<>();
        long cTime=0;
        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(1000/20);
                    for (Object o : needCheck) {
                        if (!time.containsKey(o)){
                            time.put(o,cTime);
                        }
                        if (cTime-time.get(o)>5){
                            time.remove(o);
                            needCheck.remove(o);
                            getMp3Player(o).stop();
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                cTime++;
            }
        }
        public void update(Object o){
            time.put(o,cTime);
        }
    }
}
