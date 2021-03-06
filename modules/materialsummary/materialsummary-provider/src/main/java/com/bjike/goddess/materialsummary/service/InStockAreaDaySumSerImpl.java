package com.bjike.goddess.materialsummary.service;

import com.bjike.goddess.common.api.exception.SerException;
import com.bjike.goddess.common.jpa.service.ServiceImpl;
import com.bjike.goddess.common.utils.bean.BeanTransform;
import com.bjike.goddess.materialsummary.bo.InStockAreaDaySumBO;
import com.bjike.goddess.materialsummary.dto.InStockAreaDaySumDTO;
import com.bjike.goddess.materialsummary.entity.InStockAreaDaySum;
import com.bjike.goddess.materialsummary.to.InStockAreaDaySumTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 入库地区日汇总业务实现
 *
 * @Author: [ sunfengtao ]
 * @Date: [ 2017-05-22 11:13 ]
 * @Description: [ 入库地区日汇总业务实现 ]
 * @Version: [ v1.0.0 ]
 * @Copy: [ com.bjike ]
 */
@CacheConfig(cacheNames = "materialsummarySerCache")
@Service
public class InStockAreaDaySumSerImpl extends ServiceImpl<InStockAreaDaySum, InStockAreaDaySumDTO> implements InStockAreaDaySumSer {

    /**
     * 分页查询入库地区日汇总记录
     *
     * @param dto 入库地区日汇总记录dto
     * @return class InStockAreaDaySumBO
     * @throws SerException
     */
    @Override
    public List<InStockAreaDaySumBO> list(InStockAreaDaySumDTO dto) throws SerException {
        List<InStockAreaDaySum> list = super.findByPage(dto);
        List<InStockAreaDaySumBO> listBO = BeanTransform.copyProperties(list, InStockAreaDaySumBO.class);
        return listBO;
    }

    /**
     * 保存入库地区日汇总记录
     *
     * @param to 保存入库地区日汇总记录to
     * @return class InStockAreaDaySumBO
     * @throws SerException
     */
    @Override
    @Transactional(rollbackFor = SerException.class)
    public InStockAreaDaySumBO save(InStockAreaDaySumTO to) throws SerException {
        InStockAreaDaySum marketServeRecord = BeanTransform.copyProperties(to, InStockAreaDaySum.class, true);
        marketServeRecord = super.save(marketServeRecord);
        InStockAreaDaySumBO bo = BeanTransform.copyProperties(marketServeRecord, InStockAreaDaySumBO.class);
        return bo;
    }

    /**
     * 更新入库地区日汇总记录
     *
     * @param to 入库地区日汇总记录to
     * @throws SerException
     */
    @Override
    @Transactional(rollbackFor = SerException.class)
    public void update(InStockAreaDaySumTO to) throws SerException {
        if (StringUtils.isNotEmpty(to.getId())) {
            InStockAreaDaySum model = super.findById(to.getId());
            if (model != null) {
                updateInStockAreaDaySum(to, model);
            } else {
                throw new SerException("更新对象不能为空");
            }
        } else {
            throw new SerException("更新ID不能为空!");
        }

    }

    /**
     * 更新入库地区日汇总记录
     *
     * @param to
     * @param model
     * @throws SerException
     */
    private void updateInStockAreaDaySum(InStockAreaDaySumTO to, InStockAreaDaySum model) throws SerException {
        BeanTransform.copyProperties(to, model, true);
        model.setModifyTime(LocalDateTime.now());
        super.update(model);
    }

    /**
     * 根据id删除入库地区日汇总记录
     *
     * @param id 入库地区日汇总记录唯一标识
     * @throws SerException
     */
    @Override
    @Transactional(rollbackFor = SerException.class)
    public void remove(String id) throws SerException {
        super.remove(id);
    }

    /**
     * 入库地区日汇总
     *
     * @return class InStockAreaDaySumBO
     * @throws SerException
     */
    @Override
    @Transactional(rollbackFor = SerException.class)
    public List<InStockAreaDaySumBO> summary() throws SerException {
        return null;
    }

}