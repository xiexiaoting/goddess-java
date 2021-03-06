package com.bjike.goddess.recruit.service;

import com.bjike.goddess.common.api.exception.SerException;
import com.bjike.goddess.common.jpa.service.ServiceImpl;
import com.bjike.goddess.common.utils.bean.BeanTransform;
import com.bjike.goddess.recruit.bo.FirstPhoneRecordBO;
import com.bjike.goddess.recruit.dto.FirstPhoneRecordDTO;
import com.bjike.goddess.recruit.entity.FirstPhoneRecord;
import com.bjike.goddess.recruit.to.FirstPhoneRecordTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 第一次电访记录
 *
 * @Author: [sunfengtao]
 * @Date: [2017-03-14 09:03]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.bjike]
 */
@Service
public class FirstPhoneRecordSerImpl extends ServiceImpl<FirstPhoneRecord, FirstPhoneRecordDTO> implements FirstPhoneRecordSer {

    /**
     * 分页查询第一次电访记录
     *
     * @param dto
     * @return
     * @throws SerException
     */
    @Override
    @Transactional(rollbackFor = {SerException.class})
    public List<FirstPhoneRecordBO> list(FirstPhoneRecordDTO dto) throws SerException {
        List<FirstPhoneRecord> list = super.findByPage(dto);
        List<FirstPhoneRecordBO> listBO = BeanTransform.copyProperties(list, FirstPhoneRecordBO.class);
        return listBO;
    }

    /**
     * 保存第一次电访记录
     *
     * @param to
     * @return
     * @throws SerException
     */
    @Override
    @Transactional(rollbackFor = {SerException.class})
    public FirstPhoneRecordBO save(FirstPhoneRecordTO to) throws SerException {
        FirstPhoneRecord failFirstInterviewReason = BeanTransform.copyProperties(to, FirstPhoneRecord.class, true);
        failFirstInterviewReason = super.save(failFirstInterviewReason);
        FirstPhoneRecordBO bo = BeanTransform.copyProperties(failFirstInterviewReason, FirstPhoneRecordBO.class);
        return bo;
    }

    /**
     * 更新第一次电访记录
     *
     * @param to 第一次电访记录to
     * @throws SerException
     */
    @Override
    @Transactional(rollbackFor = SerException.class)
    public void update(FirstPhoneRecordTO to) throws SerException {
        if (StringUtils.isNotEmpty(to.getId())) {
            FirstPhoneRecord model = super.findById(to.getId());
            if (model != null) {
                updateFirstPhoneRecord(to, model);
            } else {
                throw new SerException("更新对象不能为空");
            }
        } else {
            throw new SerException("更新ID不能为空!");
        }

    }

    /**
     * 更新第一次电访记录
     *
     * @param to
     * @param model
     * @throws SerException
     */
    private void updateFirstPhoneRecord(FirstPhoneRecordTO to, FirstPhoneRecord model) throws SerException {
        BeanTransform.copyProperties(to, model, true);
        model.setModifyTime(LocalDateTime.now());
        super.update(model);
    }

    /**
     * 删除第一次电访记录
     *
     * @param entity
     * @throws SerException
     */
    @Override
    @Transactional(rollbackFor = {SerException.class})
    public void remove(FirstPhoneRecord entity) throws SerException {
        super.remove(entity);
    }
}
