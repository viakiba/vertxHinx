package com.ohayoo.whitebird.bonus.reason

enum class CurrencyReason(var content: String) {
    NULL("未知物品原因"), GM("GM"), ACHIEVE_BONUS("成就获得"), TASK_BONUS("任务获得"), ACTIVE_BONUS("活跃度获得"), NO_COST_ENERGY("免费领体力"), AD_ENERGY(
        "广告领体力"
    ),
}