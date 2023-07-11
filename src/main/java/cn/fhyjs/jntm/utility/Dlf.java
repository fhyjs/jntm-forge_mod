package cn.fhyjs.jntm.utility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Dlf {
    public static Logger l1;
    public static void run(String u, String p, Logger l) {
        l1=l;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse resp = httpClient.execute( new HttpGet( u ) );
            HttpEntity entity = resp.getEntity();
            int length = (int) entity.getContentLength();//这个就是下载的文件（不单指文件）大小
            InputStream is = entity.getContent();
            ProgressBarThread pbt = new ProgressBarThread( length );//创建进度条
            new Thread( pbt ).start(); //开启线程，刷新进度条
            byte[] buf = new byte[1024];
            int size = 0;
            FileOutputStream fos = new FileOutputStream( new File( p ) ); //

            while ((size = is.read( buf )) > -1) { //循环读取
                fos.write( buf, 0, size );
                pbt.updateProgress( size );//写完一次，更新进度条
            }
            pbt.finish(); //文件读取完成，关闭进度条
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static class ProgressBarThread implements Runnable {
        private ArrayList<Integer> proList = new ArrayList<Integer>();
        private int progress; //当前进度
        private int totalSize; //总大小
        private boolean run = true;
        private String JDS;

        JFrame jf = new JFrame("下载中/Downloading");
        JProgressBar bar = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
        JTextField txt1 =new JTextField("Downloading:");
        public ProgressBarThread(int totalSize) {
            this.totalSize = totalSize;
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


        }

        /**
         * @param progress 进度
         */
        public void updateProgress(int progress) {
            synchronized (this.proList) {
                if (this.run) {
                    this.proList.add( progress );
                    this.proList.notify();
                }
            }
        }

        public void finish() {
            this.run = false;
            //关闭进度条
        }

        @Override
        public void run() {
            synchronized (this.proList) {
                try {
                    while (this.run) {
                        if (this.proList.size() == 0) {
                            this.proList.wait();
                        }
                        synchronized (proList) {
                            this.progress += this.proList.remove( 0 );
                            //TODO 更新进度条
                            DecimalFormat decimalFormat = new DecimalFormat( "0.00" );
                            JDS =( decimalFormat.format( this.progress / (float) this.totalSize * 100 ) + "%" );
                            bar.setValue((int) (this.progress / (float) this.totalSize * 100));
                            txt1.setText("下载中/Downloading:");
                            l1.debug(JDS);
                            if(( this.progress / (float) this.totalSize * 100 )>=100){
                                break;
                            }
                        }
                    }
                    jf.dispose();
                    System.out.println( "下载完成" );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
