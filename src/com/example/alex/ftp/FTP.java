package com.example.alex.ftp;

import com.example.alex.common.Log;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;

public class FTP {
    private String ftpPath;
    private String ftpName;
    private String ftpPassword;
    private String ftpServerIP;

    public FTP() {
        this.ftpPath = "/";
        this.ftpName = "ftphudong";
        this.ftpPassword = "ftphudong";
        this.ftpServerIP = "172.16.3.240";
    }


    /**
     * Method name: saveInFTP <BR>
     * Description: 把文件存储在FTP上 <BR>
     * Remark: <BR>
     *
     * @param FolderName 示例"xxx/xxx/"
     * @param FileName   示例"thefilename"
     * @param data       byte[]数组
     * @return boolean<BR>
     */
    public boolean saveInFTP(String FolderName, String FileName, byte[] data) {
        boolean flag = false;

        // 创建FTP客户端  
        FTPClient ftpClient = new FTPClient();
        // 输入流用于读取文件  
//      FileInputStream fis = null;  
        ByteArrayInputStream bis = null;

        try {
            // 如果FolderName 和 FileName都不符合基本要求, 那么就没有必要进行ftp操作  
            if (FolderName != null
                    && FolderName.compareTo("") != 0
                    && FileName != null
                    && FileName.compareTo("") != 0) {

                // 建立FTP连接  
                ftpClient.connect(this.ftpServerIP);

                // 如果登录成功后, 才进行创建输入流  
                if (ftpClient.login(this.ftpName, this.ftpPassword)) {
//                  File srcClientFile = new File("C:/ParseXML.xml");  

                    // 实例化输入流  
//                  fis = new FileInputStream(srcClientFile);  

                    if (ftpClient.changeWorkingDirectory(FolderName)) {
                        // 将byte[]写入到输入流中, 实例化  
                        bis = new ByteArrayInputStream(data);

                        // 设置缓冲  
                        ftpClient.setBufferSize(1024);

                        // 设置文件类型(二进制类型)  
                        if (ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE)) {
                            flag = ftpClient.storeFile(FileName, bis);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            flag = false;
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            try {
                // 关闭输入流  
                IOUtils.closeQuietly(bis);
                // 关闭连接  
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return flag;
    }

    /**
     * Method name: getFromFTP <BR>
     * Description: 从FTP上读取文件 <BR>
     * Remark: <BR>
     *
     * @return boolean<BR>
     */
    public boolean getFromFTP() {
        boolean flag = false;

        // 创建FTP客户端  
        FTPClient ftpClient = new FTPClient();
        // 输出流用于输出文件  
        FileOutputStream fos = null;

        try {
            // 建立FTP连接  
            ftpClient.connect(this.ftpServerIP);
            // 如果登录成功后, 才进行创建输出流  
            if (ftpClient.login(this.ftpName, this.ftpPassword)) {
                // FTP文件  
                String distinationFile = "/name/xxx/xxx/xxx文件";
                // 实例化输出流  
                fos = new FileOutputStream("C:/ParseXML_InFTP.xml");

                // 设置缓冲  
                ftpClient.setBufferSize(1024);

                // 设置文件类型(二进制类型)  
                if (ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE)) {
                    ftpClient.retrieveFile(distinationFile, fos);
                    flag = true;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            flag = false;
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            try {
                // 关闭输出流  
                IOUtils.closeQuietly(fos);
                // 关闭连接  
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return flag;
    }

    public boolean createDirectory() {
        boolean flag = false;

        // 创建FTP客户端  
        FTPClient ftpClient = new FTPClient();

        try {
            // 建立FTP连接  
            ftpClient.connect(this.ftpServerIP);
            // 如果登录成功后, 才进行操作  
            if (ftpClient.login(this.ftpName, this.ftpPassword)) {

                // 切换文件路径, 到FTP上的"NNDD3"文件夹下  
                if (this.ftpPath != null && this.ftpPath.compareTo("") != 0
                        && ftpClient.changeWorkingDirectory(this.ftpPath)) {
                    SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
                    String time = f.format(new Date());

                    String FolderName = time + "_ReTransmit";
                    ftpClient.makeDirectory(FolderName);
                    flag = true;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            flag = false;
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            try {
                // 关闭连接  
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return flag;
    }

    public String[] getAllFolderNames() {
        // 创建FTP客户端  
        FTPClient ftpClient = new FTPClient();

        try {
            // 建立FTP连接  
            ftpClient.connect(this.ftpServerIP);

            // 如果登录成功后, 才进行操作  
            if (ftpClient.login(this.ftpName, this.ftpPassword)) {
                Log.d("login success");
                if (this.ftpPath != null && this.ftpPath.compareTo("") != 0
                        && ftpClient.changeWorkingDirectory(this.ftpPath)) {
                    String[] allNames = ftpClient.listNames();
                    Log.d("listNames scuess.");
                    return allNames;
                } else {
                    Log.d("ftpPath error.");
                }
            } else {
                Log.d("login fail.");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            Log.e(e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(e);
        } finally {
            try {
                // 关闭连接  
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
