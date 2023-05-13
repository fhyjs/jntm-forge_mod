package cn.fhyjs.jntm.utility;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class FileManager {
    public static FileManager instance;
    public FileManager(){
        instance=this;
    }
    public String GetFilePath() {
        JFileChooser filechooser = new JFileChooser(".");//当前路径文件
        filechooser.showOpenDialog(frame);//打开文件对话框,父窗口为JFrame
        frame.setAlwaysOnTop(true); //窗体置顶
        frame.pack();
        if (filechooser.getSelectedFile()==null)
            return null;
        return filechooser.getSelectedFile().toString();
    }
    JFrame frame=new JFrame("FILE");
    AbstractAction open;
}
