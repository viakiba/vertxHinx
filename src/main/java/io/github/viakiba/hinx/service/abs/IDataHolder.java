package io.github.viakiba.hinx.service.abs;


import io.github.viakiba.hinx.data.model.GameData;

/**
 * @createTime 2021-05-12
 */
public interface IDataHolder {

    default void checkNullAndInit(GameData gameData){}

    default void dailyReset(GameData gameData){}

    default void checkEveryRequest(GameData gameData){}

    default void checkEveryRequestAll(GameData gameData){}

    default void checkNullAndInitAll(GameData gameData){}

}
