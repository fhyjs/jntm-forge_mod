package cn.fhyjs.jntm.utility;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class unzip {
    /**
     * 进度接口
     *
     * @author lzy
     *
     */
    public interface ProgressListener {
        void onStart();

        void onProgress(long progress);

        void onError(Exception e);

        void onCompleted();
    }

    /**
     * @param zp 文件路径
     * @param op 输出路径
     * @param pw 密码
     * @param df 完成后是否删除
     * @param lg 日志
     * @throws Exception
     */
    public static volatile boolean running = true;
    public static void run(String zp,String op,String pw,boolean df,Logger lg) {
        running = true;
        try {
            unZipFileWithProgress(zp, op, pw,
                    new unzip.ProgressListener() {

                        @Override
                        public void onStart() {
                            lg.debug("--onStart--");
                            unzip.running=true;
                        }

                        @Override
                        public void onProgress(long progress) {
                            lg.debug("--onProgress--" + progress);
                        }

                        @Override
                        public void onError(Exception e) {
                            lg.debug("--onCompleted--" + e.getMessage());
                            unzip.running=false;
                        }

                        @Override
                        public void onCompleted() {
                            lg.debug("--onCompleted--");
                            unzip.running=false;
                        }
                    }, df);

            while(running){
                continue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压缩zip,带解压进度
     *
     * @param zipFilePath
     * @param filePath
     * @param password
     * @param listener
     * @param isDeleteZip
     * @throws ZipException
     */
    public static void unZipFileWithProgress(final String zipFilePath, final String filePath, final String password,
                                             final ProgressListener listener, final boolean isDeleteZip) throws ZipException {

        final File zipFile = new File(zipFilePath);

        ZipFile zFile = new ZipFile(zipFile);
        zFile.setCharset(StandardCharsets.UTF_8);

        if (!zFile.isValidZipFile()) { //
            throw new ZipException("exception!");
        }
        File destDir = new File(filePath);
        if (destDir.isDirectory() && !destDir.exists()) {
            destDir.mkdir();
        }
        if (zFile.isEncrypted()) {
            zFile.setPassword(password.toCharArray()); // 设置解压密码
        }
        JFrame jf = new JFrame("解压缩/Unzipping:");
        JProgressBar bar = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
        JTextField txt1 =new JTextField("Downloading:");
        final ProgressMonitor progressMonitor = zFile.getProgressMonitor();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                //TODO 创建进度条
                jf.setPreferredSize(new Dimension(250,100));
                //设置进度条的属性
                bar.setStringPainted(true);
                bar.setBorderPainted(true);
                txt1.setPreferredSize(new Dimension(220,20));
                bar.setPreferredSize(new Dimension(200, 20));
                txt1.setEditable(false);
                jf.setLayout(new FlowLayout());
                jf.setAlwaysOnTop(true); //窗体置顶
                jf.add(txt1);
                jf.setUndecorated(true);
                jf.add(bar);
                jf.setLocationRelativeTo(null);// 使窗口显示在屏幕中央
                jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                jf.pack();
                jf.setVisible(true);
                try {
                    int precentDone = 0;
                    if (listener == null) {
                        return;
                    }
                    listener.onStart();
                    while (true && !progressMonitor.isCancelAllTasks()) {
                        // 每隔50ms,发送一个解压进度出去
                        Thread.sleep(50);
                        precentDone = progressMonitor.getPercentDone();
                        listener.onProgress(precentDone);
                        bar.setValue((int) (precentDone));
                        txt1.setText("解压中/Unzipping:");
                        if (precentDone >= 100) {
                            break;
                        }
                    }
                    jf.dispose();
                    listener.onCompleted();
                } catch (InterruptedException e) {
                    listener.onError(e);
                } finally {
                    if (isDeleteZip) {
                        zipFile.delete();// 将原压缩文件删除
                    }
                }
            }
        });
        thread.start();

        zFile.setRunInThread(true); // true 在子线程中进行解压 , false主线程中解压
        try {
            zFile.extractAll(filePath); // 将压缩文件解压到filePath中...
        } catch (Exception e) {
            progressMonitor.setCancelAllTasks(true);
            listener.onError(e);
        }
    }
}
