package io.github.viakiba.hinx.match.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-08-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchPlayer {

    private long playerId;
    private int score;
    private long startMatchTime;


}
