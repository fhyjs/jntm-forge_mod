package cn.fhyjs.jntm.utility;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    public static FileManager instance;
    public FileManager(){
        instance=this;
    }
    public String GetFilePath() {
        frame.setAlwaysOnTop(true); //窗体置顶
        frame.pack();
        JFileChooser filechooser = new JFileChooser(".");//当前路径文件
        filechooser.showOpenDialog(frame);//打开文件对话框,父窗口为JFrame
        frame.dispose();
        if (filechooser.getSelectedFile()==null)
            return null;
        return filechooser.getSelectedFile().toString();
    }
    JFrame frame=new JFrame("FILE");
    AbstractAction open;
    public void write_file(String path,String content,boolean overwrite) throws IOException {
        File file =new File(path);
        if (!overwrite&&file.exists()){
            return;
        }
        if(!file.exists()){
            file.createNewFile();
        }
        //使用true，即进行append file
        FileWriter fileWritter = new FileWriter(file.getName(),false);
        fileWritter.write(content);
        fileWritter.close();
    }
}
