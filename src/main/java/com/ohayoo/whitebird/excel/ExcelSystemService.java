package com.ohayoo.whitebird.excel;

import com.ohayoo.common.excel.ReadExcelUtil;
import com.ohayoo.common.excel.genjava.GenJavaFile;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.boot.SystemServiceImpl;
import com.ohayoo.whitebird.config.ServerConfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public class ExcelSystemService implements SystemServiceImpl {
    private static int headRowNum = 1;

    @Override
    public void start() {
        try {
            List<Integer> skipList = new ArrayList<>();
            skipList.add(1);
            skipList.add(2);
            ServerConfig serverConfig = GlobalContext.serverConfig();
            String absolutePath = new File( serverConfig.getExcelConfigPath()).getAbsolutePath();
            String excelResourcePath = new File( serverConfig.getExcelResourcePath()).getAbsolutePath();
            ReadExcelUtil.initExcel(absolutePath ,excelResourcePath + File.separator ,headRowNum ,skipList );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("excel init fail!");
        }
    }

    @Override
    public void stop() {

    }

    /**
     * 代码生成执行  添加如下vm参数 注意修改路径 生成的代码注意实现 getIdKey 方法
     *  -Dgen.config.configPath=config\excelConfig\excel.xml -Dgen.config.projectRootPath=D:\Code\heromaster_server\
     *  -Dgen.config.configPath=config\excelConfig\excel.xml -Dgen.config.projectRootPath=D:\work\heromaster_server\
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        GenJavaFile.headNum = 1;
        GenJavaFile.dataDesc = 1;
        GenJavaFile.dataType = 2;
        GenJavaFile.main(args);
    }
}
