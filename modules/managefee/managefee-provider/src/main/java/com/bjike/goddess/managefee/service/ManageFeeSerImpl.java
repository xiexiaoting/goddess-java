package com.bjike.goddess.managefee.service;

import com.bjike.goddess.common.api.exception.SerException;
import com.bjike.goddess.common.jpa.service.ServiceImpl;
import com.bjike.goddess.common.provider.utils.RpcTransmit;
import com.bjike.goddess.common.utils.bean.BeanTransform;
import com.bjike.goddess.managefee.bo.ManageFeeBO;
import com.bjike.goddess.managefee.dto.ManageFeeDTO;
import com.bjike.goddess.managefee.entity.ManageFee;
import com.bjike.goddess.managefee.to.GuidePermissionTO;
import com.bjike.goddess.managefee.to.ManageFeeTO;
import com.bjike.goddess.managefee.type.GuideAddrStatus;
import com.bjike.goddess.user.api.UserAPI;
import com.bjike.goddess.user.bo.UserBO;
import com.bjike.goddess.voucher.api.VoucherGenerateAPI;
import com.bjike.goddess.voucher.bo.VoucherGenerateBO;
import com.bjike.goddess.voucher.dto.VoucherGenerateDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理费业务实现
 *
 * @Author: [ tanghaixiang ]
 * @Date: [ 2017-04-27 09:38 ]
 * @Description: [ 管理费业务实现 ]
 * @Version: [ v1.0.0 ]
 * @Copy: [ com.bjike ]
 */
@CacheConfig(cacheNames = "managefeeSerCache")
@Service
public class ManageFeeSerImpl extends ServiceImpl<ManageFee, ManageFeeDTO> implements ManageFeeSer {

    @Autowired
    private VoucherGenerateAPI voucherGenerateAPI;
    @Autowired
    private UserAPI userAPI;
    @Autowired
    private CusPermissionSer cusPermissionSer;


    @Override
    public Long countManageFee(ManageFeeDTO manageFeeDTO) throws SerException {
        Long count = super.count(manageFeeDTO);
        return count;
    }

    /**
     * 检查权限
     *
     * @throws SerException
     */
    private void checkPermission() throws SerException {
        Boolean flag = false;
        String userToken = RpcTransmit.getUserToken();
        UserBO userBO = userAPI.currentUser();
        RpcTransmit.transmitUserToken(userToken);
        String userName = userBO.getUsername();
        //财务模块权限
        if (!"admin".equals(userName.toLowerCase())) {
            flag = cusPermissionSer.busCusPermission("1");
        } else {
            flag = true;
        }
        if (!flag) {
            throw new SerException("您不是财务人员,没有该操作权限");
        }
        RpcTransmit.transmitUserToken(userToken);

    }

    /**
     * 核对查看权限（部门级别）
     */
    private Boolean guideIdentity() throws SerException {
        Boolean flag = false;
        String userToken = RpcTransmit.getUserToken();
        UserBO userBO = userAPI.currentUser();
        RpcTransmit.transmitUserToken(userToken);
        String userName = userBO.getUsername();
        if (!"admin".equals(userName.toLowerCase())) {
            flag = cusPermissionSer.busCusPermission("1");
        } else {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean sonPermission() throws SerException {
        String userToken = RpcTransmit.getUserToken();
        Boolean flagSee = guideIdentity();
        RpcTransmit.transmitUserToken(userToken);
        if (flagSee) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean guidePermission(GuidePermissionTO guidePermissionTO) throws SerException {
        String userToken = RpcTransmit.getUserToken();
        GuideAddrStatus guideAddrStatus = guidePermissionTO.getGuideAddrStatus();
        Boolean flag = true;
        switch (guideAddrStatus) {
            case LIST:
                flag = guideIdentity();
                break;
            case ADD:
                flag = guideIdentity();
                break;
            case EDIT:
                flag = guideIdentity();
                break;
            case DELETE:
                flag = guideIdentity();
                break;
            case COLLECT:
                flag = guideIdentity();
                break;
            default:
                flag = true;
                break;
        }

        RpcTransmit.transmitUserToken(userToken);
        return flag;
    }

    @Override
    public ManageFeeBO getOneById(String id) throws SerException {
        if (StringUtils.isBlank(id)) {
            throw new SerException("id不能为空");
        }
        ManageFee manageFee = super.findById(id);
        return BeanTransform.copyProperties(manageFee, ManageFeeBO.class);
    }

    @Override
    public List<ManageFeeBO> listManageFee(ManageFeeDTO manageFeeDTO) throws SerException {
        checkPermission();
        manageFeeDTO.getSorts().add("createTime=desc");
        List<ManageFee> list = super.findByCis(manageFeeDTO, true);

        return BeanTransform.copyProperties(list, ManageFeeBO.class);
    }


    @Transactional(rollbackFor = SerException.class)
    @Override
    public ManageFeeBO addManageFee(ManageFeeTO manageFeeTO) throws SerException {
        checkPermission();
        if (manageFeeTO.getTargetFee() == null) {
            throw new SerException("目标管理费不能为空");
        }
        int year = Integer.parseInt(manageFeeTO.getYear().trim());
        int month = Integer.parseInt(manageFeeTO.getMonth().trim());
        LocalDate startTime = LocalDate.of(year, month, 1);
        LocalDate endTime = startTime.with(TemporalAdjusters.lastDayOfMonth());
        VoucherGenerateDTO voucherGenerateDTO = new VoucherGenerateDTO();
        voucherGenerateDTO.setArea(manageFeeTO.getArea());
        voucherGenerateDTO.setProjectName(manageFeeTO.getProject());
        voucherGenerateDTO.setProjectGroup(manageFeeTO.getProjectGroup());
        voucherGenerateDTO.setStartTime(startTime + "");
        voucherGenerateDTO.setEndTime(endTime + "");
        List<VoucherGenerateBO> voucherList = voucherGenerateAPI.listStatistic(voucherGenerateDTO, "manageFee");
        //记账凭证里面的费用

        Double money = 0d;
        if (voucherList != null && voucherList.size() > 0) {
            money = voucherList.stream().filter(str -> str.getBorrowMoney() != null).mapToDouble(VoucherGenerateBO::getBorrowMoney).sum();
        }

        ManageFee manageFee = BeanTransform.copyProperties(manageFeeTO, ManageFee.class, true);
        if (money == null && manageFeeTO.getActualFee() == null) {
            money = 0d;
        }
        manageFee.setActualFee(money);
        manageFee.setRate(manageFee.getActualFee() / manageFee.getTargetFee());
        manageFee.setBalance(manageFee.getActualFee() - manageFee.getTargetFee());
        manageFee.setCreateTime(LocalDateTime.now());
        super.save(manageFee);
        return BeanTransform.copyProperties(manageFee, ManageFeeBO.class);
    }

    @Transactional(rollbackFor = SerException.class)
    @Override
    public ManageFeeBO editManageFee(ManageFeeTO manageFeeTO) throws SerException {
        checkPermission();
        if (manageFeeTO.getTargetFee() == null) {
            throw new SerException("目标管理费不能为空");
        }
        if (manageFeeTO.getActualFee() == null) {
            throw new SerException("实际管理费不能为空");
        }

        ManageFee manageFee = BeanTransform.copyProperties(manageFeeTO, ManageFee.class, true);
        ManageFee cusLevel = super.findById(manageFeeTO.getId());

        BeanUtils.copyProperties(manageFee, cusLevel, "id", "createTime");
        cusLevel.setRate(cusLevel.getActualFee() / cusLevel.getTargetFee());
        cusLevel.setBalance(cusLevel.getActualFee() - cusLevel.getTargetFee());
        cusLevel.setModifyTime(LocalDateTime.now());
        super.update(cusLevel);
        return BeanTransform.copyProperties(cusLevel, ManageFeeBO.class);
    }

    @Transactional(rollbackFor = SerException.class)
    @Override
    public void deleteManageFee(String id) throws SerException {
        checkPermission();
        if (StringUtils.isBlank(id)) {
            throw new SerException("id不能为空");
        }
        super.remove(id);
    }

    @Override
    public List<ManageFeeBO> collectArea(ManageFeeDTO manageFeeDTO) throws SerException {
        checkPermission();
        String startTime = manageFeeDTO.getStartTime();
        String endTime = manageFeeDTO.getEndTime();

        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now();
        if (StringUtils.isNotBlank(startTime)) {
            start = LocalDate.parse(startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            end = LocalDate.parse(endTime);
        }

        //如果没有选地区，汇总表头：（地区/日期/目标管理费/实际管理费/比例/差额）
        String[] field = new String[]{"area", "targetFee", "actualFee", "rate", "balance"};
        String sql = "";
        List<ManageFeeBO> list = new ArrayList<>();
        int yearBegin = start.getYear();
        int yearEnd = end.getYear();
        if (StringUtils.isBlank(manageFeeDTO.getArea())) {
            sql = "select area , sum(targetFee) as targetFee , sum(actualFee) as actualFee ," +
                    "  (sum(actualFee)/sum(targetFee)) as rate , (sum(actualFee)-sum(targetFee)) as balance from managefee_managefee where 1= 1";
            if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                sql = sql + " and year between '" + yearBegin + "' and '" + yearEnd + "' and month between '" + start.getMonthValue() + "' and '" + end.getMonthValue() + "' ";
            }
            sql = sql + " group by area  order by area desc ";
            list = super.findBySql(sql, ManageFeeBO.class, field);
            list.stream().forEach(str -> {
                str.setYear(yearBegin + "-" + yearEnd);
            });
        } else {
            //如果有选地区，汇总表头：(地区/年份/月份/项目组/项目名称/类别/目标管理费/实际管理费/比例/差额)
            field = new String[]{"area", "year", "month", "projectGroup", "project", "type", "targetFee", "actualFee", "rate", "balance"};
            sql = "select area , year , month ,projectGroup , project,type, targetFee , actualFee ," +
                    "  (actualFee/targetFee) as rate , (actualFee-targetFee) as balance from managefee_managefee where 1=1 ";
            if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                sql = sql + " and year between '" + start.getYear() + "' and '" + end.getYear() + "' and month between '" + start.getMonthValue() + "' and '" + end.getMonthValue() + "' ";
            }

            sql = sql + " and area = '" + manageFeeDTO.getArea() + "' order by area desc ";
            list = super.findBySql(sql, ManageFeeBO.class, field);

        }

        return list;
    }

    @Override
    public List<ManageFeeBO> collectGroup(ManageFeeDTO manageFeeDTO) throws SerException {
        checkPermission();
        String startTime = manageFeeDTO.getStartTime();
        String endTime = manageFeeDTO.getEndTime();
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now();
        if (StringUtils.isNotBlank(startTime)) {
            start = LocalDate.parse(startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            end = LocalDate.parse(endTime);
        }
        //如果没有选地区，汇总表头：（地区/日期/目标管理费/实际管理费/比例/差额）
        String[] field = new String[]{"projectGroup", "targetFee", "actualFee", "rate", "balance"};
        String sql = "";
        List<ManageFeeBO> list = new ArrayList<>();
        int yearBegin = start.getYear();
        int yearEnd = end.getYear();
        if (StringUtils.isBlank(manageFeeDTO.getProjectGroup())) {
            sql = "select projectGroup ,  sum(targetFee) as targetFee , sum(actualFee) as actualFee ," +
                    "  (sum(actualFee)/sum(targetFee)) as rate , (sum(actualFee)-sum(targetFee)) as balance from managefee_managefee where 1= 1";
            if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                sql = sql + " and year between '" + yearBegin + "' and '" + yearEnd + "' and month between '" + start.getMonthValue() + "' and '" + end.getMonthValue() + "' ";
            }
            sql = sql + " group by projectGroup  order by projectGroup desc ";
            list = super.findBySql(sql, ManageFeeBO.class, field);
            list.stream().forEach(str -> {
                str.setYear(yearBegin + "-" + yearEnd);
            });
        } else {
            //如果有选地区，汇总表头：(地区/年份/月份/项目组/项目名称/类别/目标管理费/实际管理费/比例/差额)
            field = new String[]{"area", "year", "month", "projectGroup", "project", "type", "targetFee", "actualFee", "rate", "balance"};
            sql = "select area , year , month ,projectGroup , project,type, targetFee , actualFee ," +
                    "  (actualFee/targetFee) as rate , (actualFee-targetFee) as balance from managefee_managefee where 1=1 ";
            if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                sql = sql + " and year between '" + start.getYear() + "' and '" + end.getYear() + "' and month between '" + start.getMonthValue() + "' and '" + end.getMonthValue() + "' ";
            }
            sql = sql + " and projectGroup = '" + manageFeeDTO.getProjectGroup() + "' order by projectGroup desc ";
            list = super.findBySql(sql, ManageFeeBO.class, field);
        }


        return list;


    }

    @Override
    public List<ManageFeeBO> collectProject(ManageFeeDTO manageFeeDTO) throws SerException {
        checkPermission();
        String startTime = manageFeeDTO.getStartTime();
        String endTime = manageFeeDTO.getEndTime();
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now();
        if (StringUtils.isNotBlank(startTime)) {
            start = LocalDate.parse(startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            end = LocalDate.parse(endTime);
        }
        //如果没有选地区，汇总表头：（地区/日期/目标管理费/实际管理费/比例/差额）
        String[] field = new String[]{"project", "targetFee", "actualFee", "rate", "balance"};
        String sql = "";
        List<ManageFeeBO> list = new ArrayList<>();
        int yearBegin = start.getYear();
        int yearEnd = end.getYear();
        if (StringUtils.isBlank(manageFeeDTO.getProject())) {
            sql = "select project ,  sum(targetFee) as targetFee , sum(actualFee) as actualFee ," +
                    "  (sum(actualFee)/sum(targetFee)) as rate , (sum(actualFee)-sum(targetFee)) as balance from managefee_managefee where 1= 1";
            if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                sql = sql + " and year between '" + yearBegin + "' and '" + yearEnd + "' and month between '" + start.getMonthValue() + "' and '" + end.getMonthValue() + "' ";
            }
            sql = sql + " group by project  order by project desc ";
            list = super.findBySql(sql, ManageFeeBO.class, field);
            list.stream().forEach(str -> {
                str.setYear(yearBegin + "-" + yearEnd);
            });
        } else {
            //如果有选地区，汇总表头：(地区/年份/月份/项目组/项目名称/类别/目标管理费/实际管理费/比例/差额)
            field = new String[]{"area", "year", "month", "projectGroup", "project", "type", "targetFee", "actualFee", "rate", "balance"};
            sql = "select area , year , month ,projectGroup , project,type, targetFee , actualFee ," +
                    "  (actualFee/targetFee) as rate , (actualFee-targetFee) as balance from managefee_managefee where 1=1 ";
            if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                sql = sql + " and year between '" + start.getYear() + "' and '" + end.getYear() + "' and month between '" + start.getMonthValue() + "' and '" + end.getMonthValue() + "' ";
            }
            sql = sql + " and project = '" + manageFeeDTO.getProject() + "' order by project desc ";
            list = super.findBySql(sql, ManageFeeBO.class, field);
        }


        return list;

    }


    @Override
    public List<ManageFeeBO> collectType(ManageFeeDTO manageFeeDTO) throws SerException {
        checkPermission();
        String startTime = manageFeeDTO.getStartTime();
        String endTime = manageFeeDTO.getEndTime();
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now();
        if (StringUtils.isNotBlank(startTime)) {
            start = LocalDate.parse(startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            end = LocalDate.parse(endTime);
        }
        //如果没有选地区，汇总表头：（地区/日期/目标管理费/实际管理费/比例/差额）
        String[] field = new String[]{"type", "targetFee", "actualFee", "rate", "balance"};
        String sql = "";
        List<ManageFeeBO> list = new ArrayList<>();
        int yearBegin = start.getYear();
        int yearEnd = end.getYear();
        if (StringUtils.isBlank(manageFeeDTO.getType())) {
            sql = "select type ,  sum(targetFee) as targetFee , sum(actualFee) as actualFee ," +
                    "  (sum(actualFee)/sum(targetFee)) as rate , (sum(actualFee)-sum(targetFee)) as balance from managefee_managefee where 1= 1";
            if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                sql = sql + " and year between '" + yearBegin + "' and '" + yearEnd + "' and month between '" + start.getMonthValue() + "' and '" + end.getMonthValue() + "' ";
            }
            sql = sql + " group by type  order by type desc ";
            list = super.findBySql(sql, ManageFeeBO.class, field);
            list.stream().forEach(str -> {
                str.setYear(yearBegin + "-" + yearEnd);
            });
        } else {
            //如果有选地区，汇总表头：(地区/年份/月份/项目组/项目名称/类别/目标管理费/实际管理费/比例/差额)
            field = new String[]{"area", "year", "month", "projectGroup", "project", "type", "targetFee", "actualFee", "rate", "balance"};
            sql = "select area , year , month ,projectGroup , project,type, targetFee , actualFee ," +
                    "  (actualFee/targetFee) as rate , (actualFee-targetFee) as balance from managefee_managefee where 1=1 ";
            if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
                sql = sql + " and year between '" + start.getYear() + "' and '" + end.getYear() + "' and month between '" + start.getMonthValue() + "' and '" + end.getMonthValue() + "' ";
            }
            sql = sql + " and type = '" + manageFeeDTO.getType() + "' order by type desc ";
            list = super.findBySql(sql, ManageFeeBO.class, field);
        }


        return list;

    }

    @Override
    public List<String> yearList() throws SerException {
        //获取所有年
        List<String> yearList = new ArrayList<>();
        LocalDate now = LocalDate.now();
        int start = 1900;
        int end = now.getYear();
        String year = "";
        while (start <= end) {
            year = start + "";
            start = start + 1;
            yearList.add(year);
        }
        return yearList;
    }

    @Override
    public List<String> areaList() throws SerException {
        String[] field = new String[]{"area"};
        String sql = "select area   from managefee_managefee group by area ";
        List<ManageFee> manageFeeList = super.findBySql(sql, ManageFee.class, field);
        List<String> list = manageFeeList.stream().map(ManageFee::getArea).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<String> groupList() throws SerException {
        String[] field = new String[]{"projectGroup"};
        String sql = "select projectGroup  from managefee_managefee group by projectGroup ";
        List<ManageFee> manageFeeList = super.findBySql(sql, ManageFee.class, field);
        List<String> list = manageFeeList.stream().map(ManageFee::getProjectGroup).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<String> projectList() throws SerException {
        String[] field = new String[]{"project"};
        String sql = "select project  from managefee_managefee group by project ";
        List<ManageFee> manageFeeList = super.findBySql(sql, ManageFee.class, field);
        List<String> list = manageFeeList.stream().map(ManageFee::getProject).collect(Collectors.toList());
        return list;
    }
}