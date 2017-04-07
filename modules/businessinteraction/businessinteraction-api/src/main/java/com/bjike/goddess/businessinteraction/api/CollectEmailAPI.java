package com.bjike.goddess.businessinteraction.api;

import com.bjike.goddess.common.api.exception.SerException;
import com.bjike.goddess.businessinteraction.bo.CollectEmailBO;
import com.bjike.goddess.businessinteraction.dto.CollectEmailDTO;
import com.bjike.goddess.businessinteraction.to.CollectEmailTO;

import java.util.List;

/**
 * 汇总邮件发送定制业务接口
 *
 * @Author: [ tanghaixiang ]
 * @Date: [ 2017-03-16T19:08:18.882 ]
 * @Description: [ 汇总邮件发送定制业务接口 ]
 * @Version: [ v1.0.0 ]
 * @Copy: [ com.bjike ]
 */
public interface CollectEmailAPI {


    /**
     * 邮件汇总列表
     *
     * @return class CollectEmailBO
     */
    default List<CollectEmailBO> listCollectEmail(CollectEmailDTO collectEmailDTO) throws SerException {
        return null;
    }

    /**
     * 添加
     *
     * @param collectEmailTO 邮件汇总信息
     * @return class CollectEmailBO
     */
    default CollectEmailBO addCollectEmail(CollectEmailTO collectEmailTO) throws SerException {
        return null;
    }

    /**
     * 编辑
     *
     * @param collectEmailTO 邮件汇总信息
     * @return class CollectEmailBO
     */
    default CollectEmailBO editCollectEmail(CollectEmailTO collectEmailTO) throws SerException {
        return null;
    }

    /**
     * 删除邮件汇总
     *
     * @param id id
     */
    default void deleteCollectEmail(String id) throws SerException {
        return;
    }

    ;

    /**
     * 冻结邮件汇总
     *
     * @param id id
     */
    default void congealCollectEmail(String id) throws SerException {
        return;
    }

    ;

    /**
     * 解冻邮件汇总
     *
     * @param id id
     */
    default void thawCollectEmail(String id) throws SerException {
        return;
    }

    ;

    /**
     * 汇总
     *
     * @param works works
     * @return class CollectEmailBO
     */
    default List<CollectEmailBO> collectCollectEmail(String[] works) throws SerException {
        return null;
    }

    ;



}