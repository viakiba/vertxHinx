package com.ohayoo.whitebird.compoent;

import com.ohayoo.whitebird.enums.VictoryType;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-08-10
 */
public class EloUtil {

    // 常量系数 分段越高，K值越小。如此设计，能够令玩家的积分在前期快速趋近其真实水平，同时避免少数的几场对局就改变顶尖玩家的排名。
    // 所以K值的选择取决于，这个游戏需要以什么样的方式来统计选手的积分，并根据玩家、玩家数量之类的参数微调。
    private static double GetKCoefficient(int rating) {
        if (rating < 2400) {
            return 32;
        } else {
            return 10;
        }
    }

    private static double CalculateExpectedScore(int rA, int rB) {
        double expectedScore;
        expectedScore = rB - rA;
        expectedScore = expectedScore / 400;
        expectedScore = Math.pow(10, expectedScore);
        expectedScore = 1 / (1 + expectedScore);
        return expectedScore;
    }

    private static double GetActualScore(VictoryType victoryType) {
        switch (victoryType) {
            case Win:
                return 1;
            case Draw:
                return 0.5;
            default:
               return 0;
        }
    }

    public static int calEloScore(int srcScore, int distScore, VictoryType victoryType){
        double kCoefficient = GetKCoefficient(srcScore); // 获取 k 值
        double actualScore = GetActualScore(victoryType);// 获取 SA 值
        double expectedScore = CalculateExpectedScore(srcScore, distScore);
        double ratingChange = kCoefficient * (actualScore - expectedScore);
        return (int)Math.round(srcScore + ratingChange);
    }

}
