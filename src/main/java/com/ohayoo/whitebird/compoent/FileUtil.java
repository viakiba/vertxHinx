package com.ohayoo.whitebird.compoent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

enum FileSuffixType {
    Txt("txt"),
    Json("json"),
    ;
    private String value;

    FileSuffixType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static Collection<byte[]> readBytesLinesFromFile(File file) {
        List<byte[]> strs = new LinkedList<byte[]>();

        DataInputStream dis = null;
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));

            dis = new DataInputStream(bis);
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                int length = dis.readInt();
                byte[] bytes = new byte[length];
                dis.read(bytes);
                dis.read(new byte["\n".getBytes().length]);
                strs.add(bytes);
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }
        return strs;
    }

    public static Collection<byte[]> readBytesLinesFromFile(String file) {
        return readBytesLinesFromFile(new File(file));
    }

    public static void writeString2File(String fileName, FileSuffixType suffix, String content, String path) {
        BufferedWriter out = null;
        try {
            if (fileName.contains(".")) {
                fileName = fileName.substring(0, fileName.indexOf("."));
            }
            String filePath = path + File.separator + fileName + "." + suffix.getValue();
            File file = new File(filePath);

            if (!file.exists()) {
                file.createNewFile();
            }
            out = new BufferedWriter(new FileWriter(file));
            out.write(content);
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }
    }

    public static void writeBytesLines2File(String fileName, String suffix, Collection<byte[]> lines, String path) {
        DataOutputStream dos = null;
        BufferedOutputStream bos = null;

        try {
            if (fileName.contains(".")) {
                fileName = fileName.substring(0, fileName.indexOf("."));
            }
            String filePath = path + File.separator + fileName + "." + suffix;
            File file = new File(filePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            bos = new BufferedOutputStream(new FileOutputStream(file, true));
            dos = new DataOutputStream(bos);
            dos.writeInt(lines.size());
            for (Iterator<byte[]> it = lines.iterator(); it.hasNext(); ) {
                byte[] bytes = it.next();
                dos.writeInt(bytes.length);
                dos.write(bytes);
                dos.write("\n".getBytes());
            }

        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }
    }

    /**
     * 创建 Properties
     *
     * @param file
     * @return
     */
    public static Properties createProperties(File file) {
        InputStream in = null;
        Properties p = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            p = new Properties();
            p.load(in);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return p;
    }

    /**
     * 读取文件内容
     */
    public static String readFileContent(File file, boolean isNewline) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            StringBuilder stringBuilder = new StringBuilder();
            for (String str = in.readLine(); str != null; ) {
                stringBuilder.append(str);
                if (isNewline) {
                    stringBuilder.append("\n");
                }
                str = in.readLine();
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    /**
     * 读取文件内容
     */
    public static List<String> readFileLineContent(File file) {
        List<String> strs = new LinkedList<String>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            for (String str = in.readLine(); str != null; ) {
                strs.add(str);
                str = in.readLine();
            }
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return strs;
    }

    /**
     * 读取路径下后缀为endsWith文件内容
     */
    public static List<String> readFileContentFromDirectory(String path, String endsWith) {
        List<String> contents = new LinkedList<String>();
        File dirFile = new File(path);

        if (!dirFile.exists()) {
            return contents;
        }

        if (!dirFile.isDirectory()) {
            String content = readFileContent(dirFile, false);
            if (content != null && !content.equals("")) {
                contents.add(content);
            }
            return contents;
        }

        File[] files = dirFile.listFiles();
        for (File file : files) {
            if (!file.isFile() || !file.getName().endsWith(endsWith)) {
                continue;
            }
            String sqlStr = readFileContent(file, false);
            if (sqlStr == null || sqlStr.equals("")) {
                continue;
            }
            contents.add(sqlStr);
        }
        return contents;
    }

    /**
     * 获得文件夹下所有文件的名字
     */
    public static List<String> getAllFileName(String workPath) {
        List<String> names = new ArrayList<String>();
        File dir = new File(workPath);
        File file[] = dir.listFiles();
        for (int i = 0; i < file.length; i++) {
            names.add(file[i].getName());
        }
        return names;

    }

    /**
     * 获得文件夹下所有制定后缀名文件的名字(不包括后缀名)
     */
    public static List<String> getAllFileName(String workPath, String extensionName) {
        List<String> names = new ArrayList<String>();
        File dir = new File(workPath);
        File file[] = dir.listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].getName().contains(extensionName)) {
                String name = file[i].getName().replace(extensionName, "");
                names.add(name);
            }
        }
        return names;
    }

    /**
     * 获得文件夹下所有制定后缀名文件的路径和名字
     */
    public static List<String> getAllFilePath(String workPath, String extensionName) {
        List<String> paths = new ArrayList<String>();
        File dir = new File(workPath);
        File file[] = dir.listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].getName().contains(extensionName)) {
                String path = file[i].getPath();
                paths.add(path);
            }
        }
        return paths;
    }

    /**
     * 递归获得文件夹下所有 指定后缀名的 指定文件名带有特定字符串的 文件的路径和名字
     */
    public static List<String> getRecursionAllFilePath(String workPath, String extensionName, String middleStr,
                                                       List<String> paths) {
        File dir = new File(workPath);
        File file[] = dir.listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isDirectory()) {
                getRecursionAllFilePath(file[i].getPath(), extensionName, middleStr, paths);
            } else {
                if (file[i].getName().contains(extensionName) && file[i].getName().contains(middleStr)) {
                    String path = file[i].getPath();
                    paths.add(path);
                }
            }
        }
        return paths;
    }

    /**
     * 不递归，只删除当前目录下所有指定后缀名名的文件
     */
    public static void deleteAll(File file, String extensionName) {
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (!fileList[i].getName().contains(extensionName)) {
                fileList[i].delete();
            }
        }
    }

    /**
     * 递归删除指定文件夹下的所有文件（不删除任何文件夹）
     */
    public static void deleteAll(File file) {
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteAll(f);// 递归删除每一个文件
            }
        }
    }

    /**
     * 获得文件名（不带后缀）
     */
    public static String getSubFileName(String s, String extensionName) {
        if (s.contains(extensionName)) {
            s = s.replace(extensionName, "");
        }
        return s;
    }

    /**
     * 创建文件夹
     */
    public static void createDirectory(String filePaht) {
        File path = new File(filePaht);
        if (!path.exists()) {
            path.mkdirs();
        }
    }

    /**
     * 删除文件夹的所有文件及其文件夹（不包括本文件夹）
     */
    public static boolean removeAllFileFromDirectory(String filePaht) {
        File path = new File(filePaht);
        if (!path.exists()) {
            return false;
        }

        if (!path.isDirectory()) {
            // 如果不是路径就删除文件
            path.delete();
            return false;
        }

        File[] files = path.listFiles();
        for (File f : files) {
            removeFile(f);
        }
        return true;
    }

    /**
     * 递归删除文件或文件夹，及其下所有文件夹文件（包括本文件夹）
     */
    private static void removeFile(File file) {
        if (!file.isDirectory()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                removeFile(f);
            }
            file.delete();
        }
    }

    public static String getCurrentParentPath() {
        File directory = new File("..");// 设定为当前文件夹
        String workspacePath = null;
        try {
            workspacePath = directory.getCanonicalPath();
        } catch (IOException ex) {
            logger.error("", ex);
        }
        return workspacePath;
    }

    public static void writeFileContext(List<String>  strings, String path) throws IOException {
        BufferedWriter writer = null;
        try {
            File file = new File(path);
            //如果没有文件就创建
            if (!file.isFile()) {
                file.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(path));
            for (String l : strings) {
                writer.write(l + "\r\n");
            }
        }catch (Exception e){
            logger.error("",e);
            throw e;
        }finally {
            if(writer !=null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error("",e);
                }
            }
        }

    }

    public static void main(String[] args) throws Exception {
        List<String> strings = readFileLineContent(new File("C:\\Users\\Admin\\Desktop\\game_entity.txt"));
        ArrayList<String> strings1 = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i);
            String[] split = s.split("\t");
            String s1 = split[0];
            String s2 = split[1];
            JSONObject jsonObject1 = JSON.parseObject(s1);
            JSONObject jsonObject2 = JSON.parseObject(s2);
            String logId = jsonObject2.getString("logId");
            if(StringUtils.isEmpty(logId)){
                continue;
            }
            int arenaScoreMax = jsonObject1.getIntValue("arenaScoreMax");
            int arenaScore = jsonObject1.getIntValue("arenaScore");
            strings1.add(logId+","+arenaScore+","+arenaScoreMax);
        }

        writeFileContext(strings1,"C:\\Users\\Admin\\Desktop\\logid2score2scoreMaxcpa.txt");

    }
}
