package cn.fhyjs.jntm.utility;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.Header;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class MediaPlayer {
    public static Map<Object,Object> tasks = new HashMap<>();
    public static void mp3Player(Object id,InputStream inputStream) {
        if (getPlayer(id)!=null)
            getPlayer(id).stop();
        new Thread(() -> new MediaPlayer().mp3Player0(id,inputStream)).start();
    }
    public static @Nullable AdvancedPlayer getPlayer(Object id){
        if (!tasks.containsKey(id)) return null;
        Object t = tasks.get(id);
        if (t instanceof AdvancedPlayer)
            return (AdvancedPlayer) t;
        return null;
    }
    private void mp3Player0(Object id, InputStream inputStream) {
        try {
            Bitstream bitstream = new Bitstream(inputStream);
            Header header = bitstream.readFrame();
            int totalFrames = header.framesize;

            AdvancedPlayer player = new AdvancedPlayer(inputStream);
            tasks.put(id,player);
            player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackFinished(PlaybackEvent evt) {
                    tasks.remove(id);
                }
            });

            player.play();

            // 播放音频的时间，根据需要适当调整
            Thread.sleep((long) (totalFrames * header.ms_per_frame()));

            player.close();
            bitstream.close();
            inputStream.close();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
