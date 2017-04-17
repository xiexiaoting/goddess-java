package com.bjike.goddess.firmreward.action.firmreward;

import com.bjike.goddess.common.api.entity.ADD;
import com.bjike.goddess.common.api.exception.ActException;
import com.bjike.goddess.common.api.exception.SerException;
import com.bjike.goddess.common.api.restful.Result;
import com.bjike.goddess.common.consumer.restful.ActResult;
import com.bjike.goddess.common.utils.bean.BeanTransform;
import com.bjike.goddess.firmreward.api.RewardPeopleNoStatAPI;
import com.bjike.goddess.firmreward.bo.AwardDetailBO;
import com.bjike.goddess.firmreward.bo.RewardPeopleNoStatBO;
import com.bjike.goddess.firmreward.dto.RewardPeopleNoStatDTO;
import com.bjike.goddess.firmreward.to.RewardPeopleNoStatTO;
import com.bjike.goddess.firmreward.vo.AwardDetailVO;
import com.bjike.goddess.firmreward.vo.RewardPeopleNoStatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 奖励人数统计
 *
 * @Author: [ sunfengtao ]
 * @Date: [ 2017-04-12 05:45 ]
 * @Description: [  ]
 * @Version: [ v1.0.0 ]
 * @Copy: [ com.bjike ]
 */
@RestController
@RequestMapping("rewardpeoplenostat")
public class RewardPeopleNoStatAct {

    @Autowired
    private RewardPeopleNoStatAPI rewardPeopleNoStatAPI;

    /**
     * 分页查询奖励人数统计
     *
     * @param dto 奖励人数统计dto
     * @return class RewardPeopleNoStatVO
     * @throws ActException
     * @version v1
     */
    @GetMapping("v1/list")
    public Result list(RewardPeopleNoStatDTO dto) throws ActException {
        try {
            List<RewardPeopleNoStatBO> boList = rewardPeopleNoStatAPI.list(dto);
            List<RewardPeopleNoStatVO> voList = BeanTransform.copyProperties(boList, RewardPeopleNoStatVO.class);
            return ActResult.initialize(voList);
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 添加奖励人数统计
     *
     * @param to 奖励人数统计to
     * @return class RewardPeopleNoStatVO
     * @throws ActException
     * @version v1
     */
    @PostMapping("v1/add")
    public Result add(@Validated({ADD.class}) RewardPeopleNoStatTO to) throws ActException {
        try {
            RewardPeopleNoStatBO bo = rewardPeopleNoStatAPI.save(to);
            RewardPeopleNoStatVO vo = BeanTransform.copyProperties(bo, RewardPeopleNoStatVO.class);
            return ActResult.initialize(vo);
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 根据id删除奖励人数统计
     *
     * @param id 奖励人数统计唯一标识
     * @throws ActException
     * @version v1
     */
    @DeleteMapping("v1/delete/{id}")
    public Result delete(@PathVariable String id) throws ActException {
        try {
            rewardPeopleNoStatAPI.remove(id);
            return new ActResult("delete success!");
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 编辑奖励人数统计
     *
     * @param to 奖励人数统计to
     * @throws ActException
     * @version v1
     */
    @PutMapping("v1/edit")
    public Result edit(RewardPeopleNoStatTO to) throws ActException {
        try {
            rewardPeopleNoStatAPI.update(to);
            return new ActResult("edit success!");
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 添加获奖明细
     *
     * @param to 奖励人数统计to
     * @throws ActException
     * @version v1
     */
    @PostMapping("v1/addAwardDetails")
    public Result addAwardDetails(RewardPeopleNoStatTO to) throws ActException {
        try {
            rewardPeopleNoStatAPI.addAwardDetails(to);
            return new ActResult("addAwardDetails success!");
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 更新获奖明细
     *
     * @param to 奖励人数统计to
     * @throws ActException
     * @version v1
     */
    @PostMapping("v1/updateAwardDetails")
    public Result updateAwardDetails(RewardPeopleNoStatTO to) throws ActException {
        try {
            rewardPeopleNoStatAPI.updateAwardDetails(to);
            return new ActResult("updateAwardDetails success!");
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

    /**
     * 查看获奖明细
     *
     * @param statId 奖励人数统计id
     * @return class AwardDetailVO
     * @throws ActException
     * @version v1
     */
    @GetMapping("v1/checkAwardDetails")
    public Result checkAwardDetails(String statId) throws ActException {
        try {
            List<AwardDetailBO> boList = rewardPeopleNoStatAPI.checkAwardDetails(statId);
            List<AwardDetailVO> voList = BeanTransform.copyProperties(boList, AwardDetailVO.class);
            return ActResult.initialize(voList);
        } catch (SerException e) {
            throw new ActException(e.getMessage());
        }
    }

}