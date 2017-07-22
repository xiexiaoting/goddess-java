package com.bjike.goddess.recruit.service;

import com.bjike.goddess.common.api.exception.SerException;
import com.bjike.goddess.common.api.service.Ser;
import com.bjike.goddess.recruit.bo.FailFirstInterviewReasonBO;
import com.bjike.goddess.recruit.dto.FailFirstInterviewReasonDTO;
import com.bjike.goddess.recruit.entity.FailFirstInterviewReason;
import com.bjike.goddess.recruit.to.FailFirstInterviewReasonTO;
import com.bjike.goddess.recruit.to.GuidePermissionTO;
import com.bjike.goddess.recruit.vo.SonPermissionObject;

import java.util.List;

/**
 * 未应约初试原因
 *
 * @Author: [sunfengtao]
 * @Date: [2017-03-11 10:59]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.bjike]
 */
public interface FailFirstInterviewReasonSer extends Ser<FailFirstInterviewReason, FailFirstInterviewReasonDTO> {
    /**
     * 下拉导航权限
     */
    default List<SonPermissionObject> sonPermission() throws SerException {
        return null;
    }

    /**
     * 工能导航权限
     */
    default Boolean guidePermission(GuidePermissionTO guidePermissionTO) throws SerException {
        return null;
    }

    /**
     * 分页查询所有未应约初试原因
     *
     * @return
     * @throws SerException
     */
    List<FailFirstInterviewReasonBO> list(FailFirstInterviewReasonDTO dto) throws SerException;

    /**
     * 保存未应约初试原因
     *
     * @param failFirstInterviewReasonTO
     * @return
     * @throws SerException
     */
    FailFirstInterviewReasonBO save(FailFirstInterviewReasonTO failFirstInterviewReasonTO) throws SerException;

    /**
     * 根据id删除未应约初试原因
     *
     * @param id
     * @throws SerException
     */
    void remove(String id) throws SerException;

    /**
     * 更新未应约初试原因
     *
     * @param failFirstInterviewReasonTO
     * @throws SerException
     */
    void update(FailFirstInterviewReasonTO failFirstInterviewReasonTO) throws SerException;
}
