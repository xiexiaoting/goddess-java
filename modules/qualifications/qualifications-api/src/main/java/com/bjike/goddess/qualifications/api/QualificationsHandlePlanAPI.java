package com.bjike.goddess.qualifications.api;

import com.bjike.goddess.common.api.exception.SerException;
import com.bjike.goddess.qualifications.bo.QualificationsHandlePlanBO;
import com.bjike.goddess.qualifications.dto.QualificationsHandlePlanDTO;
import com.bjike.goddess.qualifications.to.GuidePermissionTO;
import com.bjike.goddess.qualifications.to.QualificationsHandlePlanTO;

import java.util.List;

/**
 * 资质办理计划业务接口
 *
 * @Author: [ dengjunren ]
 * @Date: [ 2017-03-31 04:46 ]
 * @Description: [ 资质办理计划业务接口 ]
 * @Version: [ v1.0.0 ]
 * @Copy: [ com.bjike ]
 */
public interface QualificationsHandlePlanAPI {

    /**
     * 保存
     *
     * @param to 资质办理计划传输对象
     * @return
     * @throws SerException
     */
    default QualificationsHandlePlanBO save(QualificationsHandlePlanTO to) throws SerException {
        return null;
    }

    /**
     * 修改
     *
     * @param to 资质办理计划传输对象
     * @return
     * @throws SerException
     */
    default QualificationsHandlePlanBO update(QualificationsHandlePlanTO to) throws SerException {
        return null;
    }

    /**
     * 删除
     *
     * @param id 资质办理计划id
     * @return
     * @throws SerException
     */
    default QualificationsHandlePlanBO delete(String id) throws SerException {
        return null;
    }

    /**
     * 根据资质办理查询计划
     *
     * @param handleId 资质办理ID
     * @return
     * @throws SerException
     */
    default List<QualificationsHandlePlanBO> findByHandle(String handleId) throws SerException {
        return null;
    }

    /**
     * 列表
     *
     * @param dto 资质办理计划数据传输对象
     * @return
     * @throws SerException
     */
    default List<QualificationsHandlePlanBO> maps(QualificationsHandlePlanDTO dto) throws SerException {
        return null;
    }

    /**
     * 获取总条数
     *
     * @return
     * @throws SerException
     */
    default Integer getTotal() throws SerException {
        return null;
    }

    /**
     * 根据id获取数据
     *
     * @param id 数据id
     * @return
     * @throws SerException
     */
    default QualificationsHandlePlanBO getById(String id) throws SerException {
        return null;
    }
    /**
     * 导航权限
     */
    default Boolean guidePermission(GuidePermissionTO guidePermissionTO) throws SerException {
        return null;
    }
}