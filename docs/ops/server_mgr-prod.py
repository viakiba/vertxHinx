# -*- coding: utf-8 -*-
# encoding: utf-8
# encoding=utf8
import os
import subprocess

installPath = "/software"
bxDirName = installPath + "/bx/"
bxBakDirName = installPath + "/bxbak/"
opsShell = "start-prod.sh"

# 环境变量
envServiceOperate = "PROCESS_ENV_OPS"  # 操作类型 init start stop update
envServiceUrl = "PROCESS_ENV_URL"  # 项目包下载url

# --- 文件 ---
def fileExists(path):
    return os.path.exists(path)

# --- executeCommand ---
def executeCommand(command):
    print("exec commond ", command)
    ex = subprocess.Popen(command, stdout=subprocess.PIPE, shell=True)
    out, err = ex.communicate()
    status = ex.wait()
    resultStr = out.decode(encoding='utf8')
    print("exec commond result",status, resultStr)
    return resultStr

def getEnv(key):
    return os.getenv(key)

def downloadbx(url):
    # 进入 项目目录 下载  解压
    download = "cd " + bxDirName + "&&wget -q " + url + "&&unzip -q *"
    print("下载项目包 url=", url)
    print("download=", download)
    executeCommand(download)

def startService():
    start = "cd " + bxDirName + "&&bash ./" + opsShell + " start"
    print("startService  : " + start)
    executeCommand(start)

def stopService():
    if fileExists(bxDirName+opsShell):
        print("stopService")
        startCommand ="bash ./"+opsShell+" stop"
        executeCommand("cd "+bxDirName + "&&"+startCommand)
    else:
        print("not stopService")

def updateService():
    print("updateService")
    # 做备份
    rmCommand = "rm -rf " + bxBakDirName + "*"
    executeCommand(rmCommand)
    mvCommand = "mv " + bxDirName + "/* " + bxBakDirName
    executeCommand(mvCommand)
    # 下载项目包
    downloadbx(getEnv(envServiceUrl))

def main():
    # init update start stop
    operate = getEnv(envServiceOperate)
    if(operate == "notdo"):
        return
    split = operate.split(",")
    # 获取操作系统信息
    for ops in split:
        chmodCommand = 'chmod 777 -R '+bxDirName
        executeCommand(chmodCommand)
        if ops == "update":
            print(ops +"当前操作 update")
            updateService()
        elif ops == "start":
            print(ops +"当前操作 start")
            startService()
        elif ops == "stop":
            print(ops +"当前操作 stop")
            stopService()
        else:
            print("未知操作" + ops)

main()
