package com.j13.ryze.utils;

import java.io.*;

public class RuanZhuUtil {

    private static String nteemoPath = "/Users/sunbo/project/j13/v10/nteemo";
    private static File targetFile = new File("/Users/sunbo/project/j13/v10/" + System.currentTimeMillis() + ".txt");
    private static BufferedWriter wr = null;
    public static void main(String[] args) throws Exception {

        File nteemo = new File(nteemoPath);
         wr = new BufferedWriter(new FileWriter(RuanZhuUtil.targetFile));

        String[] innerFilePathList = nteemo.list();
        for (String filePath : innerFilePathList) {
            findAndWrite(new File(nteemo.getCanonicalPath(), filePath));
        }
        wr.close();
    }

    public static void findAndWrite(File file) throws Exception {
        if (file.isDirectory()) {
            if (file.getCanonicalPath().indexOf("unpackage") > 0) {
                System.out.println("unpackage folder. break " + file.getCanonicalFile());
                return;
            }
            if (file.getCanonicalPath().indexOf("git") > 0) {
                System.out.println("git folder. break " + file.getCanonicalFile());
                return;
            }
            // 目录
            String[] inner = file.list();
            for (String f : inner) {
                findAndWrite(new File(file.getCanonicalPath(), f));
            }
        } else {
            // 文件
            if (file.getName().indexOf(".js") > 0 ||
                    file.getName().indexOf(".vue") > 0 ||
                    file.getName().indexOf(".json") > 0 ||
                    file.getName().indexOf(".scss") > 0
            ) {
                // 需要抽取内容的文件

                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = "";
                while ((line = br.readLine()) != null) {
                    wr.write(line+"\n");
                }
                br.close();
                System.out.println("parse. " + file.getCanonicalFile());

            } else {
                System.out.println("jump. " + file.getCanonicalFile());
                return;
            }
        }


    }
}
