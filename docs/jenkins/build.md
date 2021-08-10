## jenkins 构建流程

#### 参数
```text 
参数名称          参数选项                  描述
serverVersion   master                  选择构建的分支
upload          不上传、上传测试、上传生产   是否上传运维平台
update          不更新、更新              是否更新内网测试服
```

#### 构建脚本
```shell
export LANG="en_US.UTF-8"
cd $WORKSPACE
ttgversion="`date +%Y%m%d%H%M%S`"
echo $ttgversion
if [ ! -d "heromaster_server" ];then
  git clone git@code.byted.org:whitebird/heromaster_server.git
  cd heromaster_server
else
  cd heromaster_server
fi
rm -rf build
git remote update
git fetch 
git checkout -- *
git pull
git checkout $serverVersion
git pull
# 还原所有文件 切换分支 更新代码
cd config/excelConfig/resource/
ls
rm -rf *
ls
/usr/local/bin/svn export --username huangpeng.12 --password hpzc1996Qazz --depth immediates http://svn.bytedance.com/ohayoo_whitebird_project4/common/trunk/conf/excel
ls
mv excel/* ./
rm -rf excel
# 更新资源
cd $WORKSPACE/heromaster_server
/usr/local/bin/gradle build -x test -Dorg.gradle.java.home=/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home
# 构建完成
if [ "$upload" != "不上传" ];then
	if [ "$upload" != "上传测试" ];then
        chmod -R 777 ../
        ./docs/ttgops/ttgopsmac code upload -f ./build/distributions/heromaster-1.0.0.zip -m heromaster -t $ttgversion -t prod -t $serverVersion -v $ttgversion -c ./docs/ttgops/ttgops.yaml
    else
        chmod -R 777 ../
        ./docs/ttgops/ttgopsmac code upload -f ./build/distributions/heromaster-1.0.0.zip -m heromaster -t $ttgversion -t test -t $serverVersion -v $ttgversion -c ./docs/ttgops/ttgops.yaml
    fi
else
	echo "无需上传 "
fi
if [ "$update" != "不更新" ];then
   curl -X POST -L --user admin:11da476dcd3f1b8aa276c85ac0687f8b3d http://10.79.19.238:8099/job/BX_OPS_Server/buildWithParameters?update=更新
   cd $WORKSPACE
else
   echo "无需更新代码 "
fi
# 压缩文件
fileName="`date +%Y%m%d%H%M%S`"${xx//\//.}.squarepro-server.zip
cp $WORKSPACE/heromaster_server/build/distributions/heromaster-1.0.0.zip /Users/egretstudio/.jenkins/file/heromaster/$fileName 
#迁移文件
json="{\"msg_type\":\"text\",\"content\":{\"text\":\"new heromaster server build success http://10.79.19.238:8098/heromaster/$fileName build paramters: $serverVersion\"}}"
curl -X POST -H Content-Type:application/json https://open.feishu.cn/open-apis/bot/v2/hook/e09f8b1d-be34-4c21-a5df-9df8d66122c6 -d "$json"
#发送通知
```

## 58ops

#### 参数
```text 
参数名称          参数选项                  描述
update          不更新、更新              是否更新代码
```

#### 构建脚本
```shell
ssh root@10.79.19.58 "cd /software/bx&&./start-test.sh stop"
if [ "$upload" != "不更新" ];then
   ssh root@10.79.19.58 "rm -rf /software/bx/config/application.yml"
      ssh root@10.79.19.58 "rm -rf /software/bx/config/dev-server.txt"
   ssh root@10.79.19.58 "rm -rf /software/bx/config/application-dev-profile.yml"
   ssh root@10.79.19.58 "rm -rf /software/bx/config/excelConfig/excel.xml"
   ssh root@10.79.19.58 "rm -rf /software/bx/heromaster-1.0.0.jar"
   scp /Users/egretstudio/.jenkins/workspace/BX_Server/heromaster_server/config/application.yml root@10.79.19.58:/software/bx/config
   scp /Users/egretstudio/.jenkins/workspace/BX_Server/heromaster_server/config/dev-server.txt root@10.79.19.58:/software/bx/config
   scp /Users/egretstudio/.jenkins/workspace/BX_Server/heromaster_server/config/application-dev-profile.yml root@10.79.19.58:/software/bx/config
   scp /Users/egretstudio/.jenkins/workspace/BX_Server/heromaster_server/config/excelConfig/excel.xml root@10.79.19.58:/software/bx/config/excelConfig
   scp /Users/egretstudio/.jenkins/workspace/BX_Server/heromaster_server/build/libs/heromaster-1.0.0.jar root@10.79.19.58:/software/bx
   cd $WORKSPACE
else
   echo "无需更新代码 "
fi
ssh root@10.79.19.58 "cd /software/bx/config/excelConfig/resource&&ls&&rm -rf *&&ls&&svn export --force --username huangpeng.12 --password hpzc1996Qazz --depth immediates http://svn.bytedance.com/ohayoo_whitebird_project4/common/trunk/conf/excel/ ./"
ssh root@10.79.19.58 "cd /software/bx&&source /etc/profile&&./start-test.sh start"
json="{\"msg_type\":\"text\",\"content\":{\"text\":\"inner heromaster server update over restart ok $update code\"}}"
curl -i -X POST -H Content-Type:application/json https://open.feishu.cn/open-apis/bot/v2/hook/e09f8b1d-be34-4c21-a5df-9df8d66122c6 -d "$json"
#发送通知
```