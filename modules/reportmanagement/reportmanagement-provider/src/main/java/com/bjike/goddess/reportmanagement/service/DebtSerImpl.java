package com.bjike.goddess.reportmanagement.service;

import com.bjike.goddess.common.api.exception.SerException;
import com.bjike.goddess.common.jpa.service.ServiceImpl;
import com.bjike.goddess.common.utils.bean.BeanTransform;
import com.bjike.goddess.reportmanagement.bo.*;
import com.bjike.goddess.reportmanagement.dto.DebtDTO;
import com.bjike.goddess.reportmanagement.dto.DebtStructureAdviceDTO;
import com.bjike.goddess.reportmanagement.dto.FormulaDTO;
import com.bjike.goddess.reportmanagement.entity.Asset;
import com.bjike.goddess.reportmanagement.entity.Debt;
import com.bjike.goddess.reportmanagement.enums.DebtType;
import com.bjike.goddess.reportmanagement.enums.Form;
import com.bjike.goddess.reportmanagement.enums.Type;
import com.bjike.goddess.reportmanagement.to.DebtTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 负债表业务实现
 *
 * @Author: [ chenjunhao ]
 * @Date: [ 2017-06-19 11:21 ]
 * @Description: [ 负债表业务实现 ]
 * @Version: [ v1.0.0 ]
 * @Copy: [ com.bjike ]
 */
@CacheConfig(cacheNames = "reportmanagementSerCache")
@Service
public class DebtSerImpl extends ServiceImpl<Debt, DebtDTO> implements DebtSer {
    @Autowired
    private FormulaSer formulaSer;
    @Autowired
    private DebtStructureAdviceSer debtStructureAdviceSer;

    @Override
    public DebtBO save(DebtTO to) throws SerException {
        Debt entity = BeanTransform.copyProperties(to, Debt.class, true);
        super.save(entity);
        return BeanTransform.copyProperties(entity, DebtBO.class);
    }

//    @Override
//    public DebtBO find(String id, String startTime, String endTime) throws SerException {
//        Debt entity = super.findById(id);
//        if (entity == null) {
//            throw new SerException("该对象不存在");
//        }
//        DebtBO bo = BeanTransform.copyProperties(entity, DebtBO.class);
//        bo.setStartTime(startTime);
//        bo.setEndTime(endTime);
//        return bo;
//    }

    @Override
    public List<DebtBO> list(DebtDTO dto) throws SerException {
        FormulaDTO formulaDTO = new FormulaDTO();
        BeanUtils.copyProperties(dto, formulaDTO);
        dto.getSorts().add("debtType=ASC");
        List<Debt> list = super.findAll();
        List<DebtBO> boList = new ArrayList<DebtBO>();
        boolean b1 = true;
        boolean b2 = true;
        boolean b3 = true;
        boolean b4 = true;
        double beginSum = 0;
        double endSum = 0;
        double debtBegin = 0;
        double debtEnd = 0;    //负债合计
        double countBegin = 0;
        double countEnd = 0;       //负债和所有权益合计
        for (Debt debt : list) {
            List<FormulaBO> formulaBOs = formulaSer.findByFid(debt.getId(), formulaDTO);
            if (formulaBOs != null) {
                if (DebtType.AFLOW.equals(debt.getDebtType()) && b1) {
                    DebtBO debtBO = new DebtBO();
                    debtBO.setDebt("流动负债：");
                    boList.add(debtBO);
                    b1 = false;
                } else if (DebtType.BLONG.equals(debt.getDebtType()) && b2) {
                    DebtBO sumBO = new DebtBO();
                    sumBO.setDebt("流动负债合计");
                    sumBO.setBeginDebt(beginSum);
                    sumBO.setEndDebt(endSum);
                    boList.add(sumBO);
                    debtBegin += beginSum;
                    debtEnd += endSum;
                    beginSum = 0;
                    endSum = 0;    //置为0
                    DebtBO debtBO = new DebtBO();
                    debtBO.setDebt("长期负债：");
                    boList.add(debtBO);
                    b2 = false;
                } else if (DebtType.CTAX.equals(debt.getDebtType()) && b3) {
                    DebtBO sumBO = new DebtBO();
                    sumBO.setDebt("长期负债合计");
                    sumBO.setBeginDebt(beginSum);
                    sumBO.setEndDebt(endSum);
                    boList.add(sumBO);
                    debtBegin += beginSum;
                    debtEnd += endSum;
                    beginSum = 0;
                    endSum = 0;    //置为0
                    DebtBO debtBO = new DebtBO();
                    debtBO.setDebt("递延税项：");
                    boList.add(debtBO);
                    b3 = false;
                } else if (DebtType.DALL.equals(debt.getDebtType()) && b4) {
                    DebtBO sumBO = new DebtBO();
                    sumBO.setDebt("负债合计");
                    debtBegin += beginSum;
                    debtEnd += endSum;
                    sumBO.setBeginDebt(debtBegin);
                    sumBO.setEndDebt(debtEnd);
                    boList.add(sumBO);
                    beginSum = 0;
                    endSum = 0;    //置为0
                    DebtBO debtBO = new DebtBO();
                    debtBO.setDebt("所有者权益(或股东权益)：");
                    boList.add(debtBO);
                    b4 = false;
                }
                FormulaBO formulaBO = formulaBOs.get(formulaBOs.size() - 1);
                DebtBO bo = BeanTransform.copyProperties(debt, DebtBO.class);
                bo.setBeginDebt(formulaBO.getBegin());
                bo.setEndDebt(formulaBO.getEnd());
                if (Type.ADD.equals(debt.getType())) {
                    beginSum += bo.getBeginDebt();
                    endSum += bo.getEndDebt();
                    countBegin += bo.getBeginDebt();
                    countEnd += bo.getEndDebt();
                } else if (Type.REMOVE.equals(debt.getType())) {
                    beginSum -= bo.getBeginDebt();
                    endSum -= bo.getEndDebt();
                    countBegin -= bo.getBeginDebt();
                    countEnd -= bo.getEndDebt();
                }
                boList.add(bo);
            }
        }
        DebtBO lastTwo = new DebtBO();
        lastTwo.setDebt("所有者权益(或股东权益)合计");
        lastTwo.setBeginDebt(beginSum);
        lastTwo.setEndDebt(endSum);
        boList.add(lastTwo);
        DebtBO lastBO = new DebtBO();
        lastBO.setDebt("负债和所有者权益(或股东权益)总计");
        lastBO.setBeginDebt(countBegin);
        lastBO.setEndDebt(countEnd);
        boList.add(lastBO);
        return boList;
    }

    @Override
    public List<StructureBO> debtStructure(DebtDTO dto) throws SerException {
        FormulaDTO formulaDTO = new FormulaDTO();
        BeanUtils.copyProperties(dto, formulaDTO);
        dto.getSorts().add("debtType=ASC");
        List<Debt> list = super.findAll();
        List<StructureBO> boList = new ArrayList<StructureBO>();
        boolean b1 = true;
        boolean b2 = true;
        boolean b3 = true;
        double flowSum = 0;
        double longSum = 0;
        double allSum = 0;
        double currentSum = 0;
        double countCurrent = 0;       //负债和所有权益合计
        for (Debt debt : list) {
            List<FormulaBO> formulaBOs = formulaSer.findByFid(debt.getId(), formulaDTO);
            if (formulaBOs != null) {
                if (DebtType.BLONG.equals(debt.getDebtType()) && b1) {
                    flowSum = currentSum;
                    currentSum = 0;    //置为0
                    b1 = false;
                } else if (DebtType.CTAX.equals(debt.getDebtType()) && b2) {
                    longSum = currentSum;
                    currentSum = 0;    //置为0
                    b2 = false;
                } else if (DebtType.DALL.equals(debt.getDebtType()) && b3) {
                    currentSum = 0;    //置为0
                    b3 = false;
                }
                FormulaBO formulaBO = formulaBOs.get(formulaBOs.size() - 1);
                DebtBO bo = BeanTransform.copyProperties(debt, DebtBO.class);
                bo.setCurrent(formulaBO.getCurrent());
                if (Type.ADD.equals(debt.getType())) {
                    currentSum += bo.getCurrent();
                    countCurrent += bo.getCurrent();
                } else if (Type.REMOVE.equals(debt.getType())) {
                    currentSum -= bo.getCurrent();
                    countCurrent -= bo.getCurrent();
                }
            }
        }
        allSum = currentSum;
        StructureBO flowBO = new StructureBO();
        flowBO.setProject("流动负债合计");
        flowBO.setFee(flowSum);
        String flow = String.format("%.2f", (flowSum / countCurrent) * 100);
        flowBO.setScale(flow + "%");
        boList.add(flowBO);
        StructureBO longBO = new StructureBO();
        longBO.setProject("长期负债合计");
        longBO.setFee(longSum);
        String l = String.format("%.2f", (longSum / countCurrent) * 100);
        longBO.setScale(l + "%");
        boList.add(longBO);
        StructureBO allBO = new StructureBO();
        allBO.setProject("所有者权益合计");
        allBO.setFee(allSum);
        String all = String.format("%.2f", (allSum / countCurrent) * 100);
        allBO.setScale(all + "%");
        boList.add(allBO);
        StructureBO sumBO = new StructureBO();
        sumBO.setProject("负债与所有者权益总计");
        sumBO.setFee(countCurrent);
        allBO.setScale("100%");
        boList.add(allBO);
        StructureBO rate = new StructureBO();
        rate.setProject("比例说明");
        rate.setBestScale("低负债资本、高权益资本可以降低企业财务风险，" +
                "减少企业发生债务危机的比率，但是会增加企业资本成本，不能有效发挥债务资本的财务杠杆效益。");
        boList.add(rate);
        String advice = debtStructureAdvice(flow, l, all);
        StructureBO adviceBO = new StructureBO();
        adviceBO.setProject("管理建议");
        adviceBO.setBestScale(advice);
        boList.add(adviceBO);
        return boList;
    }

    /**
     * 获取负债与权益结构管理建议
     *
     * @param flow
     * @param l
     * @param all
     * @return
     * @throws SerException
     */
    private String debtStructureAdvice(String flow, String l, String all) throws SerException {
        List<DebtStructureAdviceBO> advices = debtStructureAdviceSer.list(new DebtStructureAdviceDTO());
        String advice = null;
        if (advices != null && !advices.isEmpty()) {
            for (DebtStructureAdviceBO r : advices) {
                boolean b1 = Double.parseDouble(flow) >= r.getFlowMin() && Double.parseDouble(flow) <= r.getFlowMax();
                boolean b2 = Double.parseDouble(l) >= r.getLongMin() && Double.parseDouble(l) <= r.getLongMax();
                boolean b3 = Double.parseDouble(all) >= r.getAllMin() && Double.parseDouble(all) <= r.getAllMax();
                if (b1 && b2 && b3) {
                    advice = r.getAdvice();
                }
            }
        }
        return advice;
    }

    @Override
    public List<DetailBO> findDetails(String id, DebtDTO dto) throws SerException {
        String startTime = dto.getStartTime();
        String endTime = dto.getEndTime();
        FormulaDTO formulaDTO = new FormulaDTO();
        BeanUtils.copyProperties(dto, formulaDTO);
        List<FormulaBO> list = formulaSer.findByFid(id, formulaDTO);
        List<DetailBO> boList = new ArrayList<>();
        if ((list != null) && (!list.isEmpty())) {
            FormulaBO last = list.get(list.size() - 1);
            double begin = last.getBegin();
            double current = last.getCurrent();
            Form form = last.getForm();
            double currentSum = 0;
            String project = findByID(id).getDebt();
            String term = startTime + "~" + endTime;
            DetailBO currentBO = new DetailBO();
            currentBO.setProject(project);
            currentBO.setTerm(term);
            currentBO.setState("本期合计");
            currentBO.setForm(form);
            if (Form.DEBIT.equals(form)) {
                currentSum = begin + current;
                currentBO.setDebit(current);
            } else if (Form.CREDIT.equals(form)) {
                currentSum = begin - current;
                currentBO.setCredit(current);
            }
            currentBO.setRemain(currentSum);
            double year = currentSum;
            DetailBO beginBO = new DetailBO();
            beginBO.setProject(project);
            beginBO.setTerm(term);
            beginBO.setState("期初余额");
            beginBO.setForm(form);
            beginBO.setRemain(begin);
            boList.add(beginBO);
            boList.add(currentBO);
            DetailBO yearBO = new DetailBO();
            yearBO.setTerm(term);
            yearBO.setState("本年累计");
            yearBO.setForm(form);
            yearBO.setRemain(year);
            boList.add(yearBO);
        }
        return boList;
    }

    @Override
    public DebtBO findByID(String id) throws SerException {
        Debt entity = super.findById(id);
        if (entity == null) {
            throw new SerException("该对象不存在");
        }
        return BeanTransform.copyProperties(entity, DebtBO.class);
    }

    @Override
    public Long count(DebtDTO dto) throws SerException {
        return super.count(dto);
    }

    @Override
    @Transactional(rollbackFor = SerException.class)
    public void edit(DebtTO to) throws SerException {
        Debt entity = super.findById(to.getId());
        if (entity == null) {
            throw new SerException("该对象不存在");
        }
        LocalDateTime a = entity.getCreateTime();
        entity = BeanTransform.copyProperties(to, Asset.class, true);
        entity.setCreateTime(a);
        entity.setModifyTime(LocalDateTime.now());
        super.update(entity);
    }

    @Override
    @Transactional(rollbackFor = SerException.class)
    public void delete(String id) throws SerException {
        Debt entity = super.findById(id);
        if (entity == null) {
            throw new SerException("该对象不存在");
        }
        super.remove(id);
    }
}