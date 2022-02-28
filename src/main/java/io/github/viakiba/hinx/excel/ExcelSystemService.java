package io.github.viakiba.hinx.excel;

import io.github.viakiba.hinx.boot.GlobalContext;
import io.github.viakiba.hinx.boot.SystemServiceImpl;
import io.github.viakiba.hinx.config.ServerConfig;
import org.viakiba.exceltool.ReadExcelUtil;
import org.viakiba.exceltool.genjava.GenJavaFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author viakiba@gmail.com
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

    /**
     * 代码生成执行  添加如下vm参数 注意修改路径 生成的代码注意实现 getIdKey 方法
     *  -Dgen.config.configPath=/Users/dd/Documents/vertxHinx/config/excel.xml -Dgen.config.projectRootPath=/Users/dd/Documents/vertxHinx/
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
