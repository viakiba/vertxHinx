package com.ohayoo.whitebird.data.model.gamedata;

import com.ohayoo.whitebird.bonus.reason.BonusReason;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-06-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogReason {
    private BonusReason bonusReason = BonusReason.NULL;
    private List<String> param = new ArrayList<>();
    private float moneyUp = 1.0f;
}
