package com.bjike.goddess.businessproject.service;

import com.bjike.goddess.businessproject.bo.DispatchSheetBO;
import com.bjike.goddess.businessproject.to.DispatchSheetTO;
import com.bjike.goddess.common.api.dto.Restrict;
import com.bjike.goddess.common.api.exception.SerException;
import com.bjike.goddess.common.jpa.service.ServiceImpl;
import com.bjike.goddess.businessproject.dto.DispatchSheetDTO;
import com.bjike.goddess.businessproject.entity.DispatchSheet;
import com.bjike.goddess.common.utils.bean.BeanTransform;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商务项目派工单信息管理业务实现
 *
 * @Author: [ tanghaixiang ]
 * @Date: [ 2017-03-21 10:06 ]
 * @Description: [ 商务项目派工单信息管理业务实现 ]
 * @Version: [ v1.0.0 ]
 * @Copy: [ com.bjike ]
 */
@CacheConfig(cacheNames = "businessprojectSerCache")
@Service
public class DispatchSheetSerImpl extends ServiceImpl<DispatchSheet, DispatchSheetDTO> implements DispatchSheetSer {

    @Cacheable
    @Override
    public List<DispatchSheetBO> listDispatchSheet(DispatchSheetDTO dispatchSheetDTO) throws SerException {
        List<DispatchSheet> list = super.findByPage(dispatchSheetDTO);
        List<DispatchSheetBO> dispatchSheetBOList = BeanTransform.copyProperties(list, DispatchSheetBO.class);
        return dispatchSheetBOList;
    }

    @Transactional(rollbackFor = SerException.class)
    @Override
    public DispatchSheetBO addDispatchSheet(DispatchSheetTO dispatchSheetTO) throws SerException {
        DispatchSheet dispatchSheet = BeanTransform.copyProperties(dispatchSheetTO, DispatchSheet.class, true);
        dispatchSheet.setCreateTime(LocalDateTime.now());
        super.save(dispatchSheet);

        DispatchSheetBO dispatchSheetBO = BeanTransform.copyProperties(dispatchSheet, DispatchSheetBO.class);

        return dispatchSheetBO;
    }

    @Transactional(rollbackFor = SerException.class)
    @Override
    public DispatchSheetBO editDispatchSheet(DispatchSheetTO dispatchSheetTO) throws SerException {
        DispatchSheet dispatchSheet = BeanTransform.copyProperties(dispatchSheetTO, DispatchSheet.class, true);
        dispatchSheet.setModifyTime(LocalDateTime.now());
        super.update(dispatchSheet);

        DispatchSheetBO dispatchSheetBO = BeanTransform.copyProperties(dispatchSheet, DispatchSheetBO.class);

        return dispatchSheetBO;
    }

    @Transactional(rollbackFor = SerException.class)
    @Override
    public void deleteDispatchSheet(String id) throws SerException {

        super.remove(id);
    }

    @Cacheable
    @Override
    public List<DispatchSheetBO> searchDispatchSheet(DispatchSheetDTO dispatchSheetDTO) throws SerException {
        DispatchSheetDTO dto = dispatchSheetDTO;
        /**
         * 业务类型
         */
        if (dispatchSheetDTO.getBusinessType() != null) {
            dto.getConditions().add(Restrict.eq("businessType", dispatchSheetDTO.getBusinessType()));
        }
        /**
         * 业务方向科目
         */
        if (StringUtils.isNotBlank(dispatchSheetDTO.getBusinessSubject())) {
            dto.getConditions().add(Restrict.like("businessSubject", dispatchSheetDTO.getBusinessSubject()));
        }
        /**
         * 合作方式
         */
        if (StringUtils.isNotBlank(dispatchSheetDTO.getBusinessCooperate())) {
            dto.getConditions().add(Restrict.eq("businessCooperate", dispatchSheetDTO.getBusinessCooperate()));
        }
        /**
         * 总包单位名称
         */
        if (StringUtils.isNotBlank(dispatchSheetDTO.getMajorCompany())) {
            dto.getConditions().add(Restrict.eq("majorCompany", dispatchSheetDTO.getMajorCompany()));
        }
        /**
         * 分包单位名称
         */
        if (StringUtils.isNotBlank(dispatchSheetDTO.getSubCompany())) {
            dto.getConditions().add(Restrict.eq("subCompany", dispatchSheetDTO.getSubCompany()));
        }
        /**
         * 地区
         */
        if (StringUtils.isNotBlank(dispatchSheetDTO.getArea())) {
            dto.getConditions().add(Restrict.eq("area", dispatchSheetDTO.getArea()));
        }
        /**
         * 派工单名称
         */
        if (StringUtils.isNotBlank(dispatchSheetDTO.getDispatchProject())) {
            dto.getConditions().add(Restrict.eq("dispatchProject", dispatchSheetDTO.getDispatchProject()));
        }
        /**
         * 派工单编号
         */
        if (StringUtils.isNotBlank(dispatchSheetDTO.getDispatchNum())) {
            dto.getConditions().add(Restrict.eq("dispatchNum", dispatchSheetDTO.getDispatchNum()));
        }

        List<DispatchSheet> dispatchSheetList = super.findByCis(dto);

        List<DispatchSheetBO> dispatchSheetBOList = BeanTransform.copyProperties(dispatchSheetList, DispatchSheetBO.class);

        return dispatchSheetBOList;
    }

    @Override
    public List<String> listArea() throws SerException {
        String[] fields = new String[]{"area"};
        List<DispatchSheetBO> dispatchSheetBOList =super.findBySql("select area,1 from businessproject_dispatchsheet order by area asc ", DispatchSheetBO.class, fields);

        List<String> areaList  = dispatchSheetBOList.stream().map(DispatchSheetBO::getArea)
                .filter(area -> (area != null || !"".equals(area.trim())) ).distinct().collect(Collectors.toList());


        return areaList;
    }

    @Override
    public List<String> listDispatchName() throws SerException {
        String[] fields = new String[]{"dispatchProject"};
        List<DispatchSheetBO> dispatchSheetBOList =super.findBySql("select dispatchProject,1 from businessproject_dispatchsheet order by area asc ", DispatchSheetBO.class, fields);

        List<String> dispatchProjectList  = dispatchSheetBOList.stream().map(DispatchSheetBO::getDispatchProject)
                .filter(str -> (str != null || !"".equals(str.trim())) ).distinct().collect(Collectors.toList());


        return dispatchProjectList;
    }
}