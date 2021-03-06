package com.bjike.goddess.materialsummary.dao;

import com.bjike.goddess.common.jpa.dao.JpaRep;
import com.bjike.goddess.materialsummary.dto.MaterialClassifyWeekSumDTO;
import com.bjike.goddess.materialsummary.entity.MaterialClassifyWeekSum;

/**
 * 物资分类周汇总持久化接口, 继承基类可使用ｊｐａ命名查询
 *
 * @Author: [ sunfengtao ]
 * @Date: [ 2017-05-22 10:45 ]
 * @Description: [ 物资分类周汇总持久化接口, 继承基类可使用ｊｐａ命名查询 ]
 * @Version: [ v1.0.0 ]
 * @Copy: [ com.bjike ]
 */
public interface MaterialClassifyWeekSumRep extends JpaRep<MaterialClassifyWeekSum, MaterialClassifyWeekSumDTO> {

}