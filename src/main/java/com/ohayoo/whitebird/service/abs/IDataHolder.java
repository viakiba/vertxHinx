package com.ohayoo.whitebird.service.abs;


import com.ohayoo.whitebird.data.model.GameData;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-05-12
 */
public interface IDataHolder {

    default void checkNullAndInit(GameData gameData){}

    default void dailyReset(GameData gameData){}

    default void checkEveryRequest(GameData gameData){}

    default void checkEveryRequestAll(GameData gameData){}

    default void checkNullAndInitAll(GameData gameData){}

}
