package com.ohayoo.whitebird.excel;

import com.ohayoo.common.excel.ExcelDataService;
import com.ohayoo.common.excel.ReadExcelUtil;
import com.ohayoo.common.excel.genjava.GenJavaFile;
import com.ohayoo.whitebird.boot.SystemServiceImpl;
import com.ohayoo.whitebird.boot.GlobalContext;
import com.ohayoo.whitebird.compoent.LoggerUtil;
import com.ohayoo.whitebird.config.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-07-23
 */
public class ExcelSystemService implements SystemServiceImpl {
    private static Logger log = LoggerFactory.getLogger(ExcelSystemService.class);
    private static int headRowNum = 1;

    @Override
    public void startService() {
        try {
            List<Integer> skipList = new ArrayList<>();
            skipList.add(1);
            skipList.add(2);
            ServerConfig serverConfig = GlobalContext.serverConfig();
            String absolutePath = new File( serverConfig.getExcelConfigPath()).getAbsolutePath();
            String excelResourcePath = new File( serverConfig.getExcelResourcePath()).getAbsolutePath();
            ReadExcelUtil.initExcel(absolutePath ,excelResourcePath + File.separator ,headRowNum ,skipList );
            log.info(ExcelDataService.dataMap.size() +" 表读取完成");
        } catch (Exception e) {
            LoggerUtil.error(e.getMessage());
            throw new RuntimeException("excel init fail!");
        }
    }

    /**
     * 代码生成执行  添加如下vm参数 注意修改路径 生成的代码注意实现 getIdKey 方法
     * -Dgen.config.projectRootPath=D:\Code\ohayoo_egret_dogwar\ -Dgen.config.configPath=config\excel.xml
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
