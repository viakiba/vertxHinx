package io.github.viakiba.hinx.message;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.google.protobuf.UnknownFieldSet;
import io.github.viakiba.hinx.annotate.BizServiceAnnotate;
import io.github.viakiba.hinx.boot.GlobalContext;
import io.github.viakiba.hinx.exception.CustomException;
import io.github.viakiba.hinx.generate.message.CommonMessage;
import io.github.viakiba.hinx.player.model.IPlayer;
import io.github.viakiba.hinx.service.abs.BaseService;
import io.github.viakiba.hinx.compoent.ClassScanUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.viakiba.hinx.compoent.ClassScanUtil.getClzFromPkg;

/**
 * @author viakiba@gmail.com
 * @createTime 2021-07-27
 */
@Slf4j
public class ProtoMsgHandler implements MsgHandler{
    public static Map<Integer, Message.Builder> msgProtoMap ;
    @Override
    public void init() {
        msgProtoMap = new ConcurrentHashMap<>();
        initService();
        try {
            //initByWrite(msgProtoMap);
            initByDesc(msgProtoMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initService() {
        String[] bizServicePkgPath = GlobalContext.serverConfig().getBizServicePkgPath();
        for (String path : bizServicePkgPath) {
            Set<Class<?>> glazes = ClassScanUtil.getClzFromPkg(path, BizServiceAnnotate.class);
            try {
                for (Class<?> c : glazes) {
                    Object bizService = c.getDeclaredConstructor().newInstance();
                    BaseService bizServiceBase = (BaseService) bizService;
                    bizServiceMap.put(bizServiceBase.getClass(), bizServiceBase);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("biz service init fail");
            }
        }
    }

    private void initByWrite(Map<Integer, Message.Builder> msgProtoMaps) {
        String[] bizServicePkgPath = GlobalContext.serverConfig().getBizServicePkgPath();
        for (String path : bizServicePkgPath) {
            Set<Class<?>> glazes = ClassScanUtil.getClzFromPkg(path, BizServiceAnnotate.class);
            try {
                for (Class<?> c : glazes) {
                    Object bizService = c.getDeclaredConstructor().newInstance();
                    BaseService bizServiceBase = (BaseService) bizService;
                    msgProtoMaps.putAll(bizServiceBase.getProtoMessageRecognize());
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("biz service init fail");
            }
        }
    }

    public Message recognizedMsg(Integer msgId,byte[] body) throws Exception {
        Message.Builder builder = msgProtoMap.get(msgId);
        if(builder == null){
            log.error("MessageId找不到"+body);
            throw new CustomException(CommonMessage.ErrorCode.MsgNotFoundException);
        }
        Message.Builder clone = builder.clone();
        Message.Builder builder1 = clone.mergeFrom(body);
        return builder1.build();
    }

    @Override
    public void handler(IPlayer player, Integer msgId, byte[] body) throws Exception {
        Message msg = recognizedMsg(msgId, body);
        BaseService baseService = recognizedBizService(msgId);
        baseService.handler(msgId,msg,player);
    }

    public static void initByDesc(Map<Integer, Message.Builder> msgId2GeneratedMessageV3Builder) throws Exception {
        DescriptorProtos.FileDescriptorSet fdSet = DescriptorProtos.FileDescriptorSet.parseFrom(new FileInputStream("config"+ File.separator+"message.desc"));
        for (DescriptorProtos.FileDescriptorProto fileDescriptorProto : fdSet.getFileList()) {
            for (DescriptorProtos.DescriptorProto descriptorProto : fileDescriptorProto.getMessageTypeList()) {
                String className = fileDescriptorProto.getOptions().getJavaPackage() + "." + fileDescriptorProto.getOptions().getJavaOuterClassname() + "$" + descriptorProto.getName();
                UnknownFieldSet uf = descriptorProto.getOptions().getUnknownFields();
                for (Map.Entry<Integer, UnknownFieldSet.Field> entry : uf.asMap().entrySet()) {
                    if (entry.getKey() == 54321) {// entry.getKey() 与 options 的字段一致。 注册到map上
                        msgId2GeneratedMessageV3Builder.put(Math.toIntExact(entry.getValue().getVarintList().get(0)), (Message.Builder)Class.forName(className).getMethod("newBuilder").invoke(null));
                    }
                    System.out.println("额外的字段  key:" + entry.getKey() + " ,字段的值" + entry.getValue().getVarintList());
                }
            }
        }
    }
}
