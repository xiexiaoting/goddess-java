package com.bjike.goddess.reportmanagement.service;

import com.bjike.goddess.common.api.exception.SerException;
import com.bjike.goddess.common.jpa.service.ServiceImpl;
import com.bjike.goddess.common.utils.bean.BeanTransform;
import com.bjike.goddess.reportmanagement.bo.*;
import com.bjike.goddess.reportmanagement.dto.*;
import com.bjike.goddess.reportmanagement.entity.Asset;
import com.bjike.goddess.reportmanagement.enums.AssetType;
import com.bjike.goddess.reportmanagement.enums.Form;
import com.bjike.goddess.reportmanagement.enums.Type;
import com.bjike.goddess.reportmanagement.to.AssetTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 资产表业务实现
 *
 * @Author: [ chenjunhao ]
 * @Date: [ 2017-06-19 11:19 ]
 * @Description: [ 资产表业务实现 ]
 * @Version: [ v1.0.0 ]
 * @Copy: [ com.bjike ]
 */
@CacheConfig(cacheNames = "reportmanagementSerCache")
@Service
public class AssetSerImpl extends ServiceImpl<Asset, AssetDTO> implements AssetSer {
    @Autowired
    private FormulaSer formulaSer;
    @Autowired
    private DebtSer debtSer;
    @Autowired
    private RepayAnalyzeAdviceSer repayAnalyzeAdviceSer;
    @Autowired
    private AssetStructureAdviceSer assetStructureAdviceSer;

    @Override
    @Transactional(rollbackFor = SerException.class)
    public AssetBO save(AssetTO to) throws SerException {
        Asset entity = BeanTransform.copyProperties(to, Asset.class, true);
        super.save(entity);
        return BeanTransform.copyProperties(entity, AssetBO.class);
    }

    @Override
    @Transactional(rollbackFor = SerException.class)
    public void edit(AssetTO to) throws SerException {
        Asset entity = super.findById(to.getId());
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
        Asset entity = super.findById(id);
        if (entity == null) {
            throw new SerException("该对象不存在");
        }
        super.remove(id);
    }

//    @Override
//    public AssetBO find(String id, String startTime, String endTime) throws SerException {
//        Asset entity = super.findById(id);
//        if (entity == null) {
//            throw new SerException("该对象不存在");
//        }
//        AssetBO bo = BeanTransform.copyProperties(entity, AssetBO.class);
//        bo.setStartTime(startTime);
//        bo.setEndTime(endTime);
//        return bo;
//    }

    @Override
    public List<AssetBO> list(AssetDTO dto) throws SerException {
        FormulaDTO formulaDTO = new FormulaDTO();
        BeanUtils.copyProperties(dto, formulaDTO);
        dto.getSorts().add("assetType=ASC");
        List<Asset> list = super.findAll();
        List<AssetBO> boList = new ArrayList<AssetBO>();
        boolean b1 = true;
        boolean b2 = true;
        boolean b3 = true;
        boolean b4 = true;
        boolean b5 = true;
        double beginSum = 0;
        double currentSum = 0;
        double endSum = 0;
        double countBegin = 0;
        double countCurrent = 0;
        double countEnd = 0;       //总资产
        for (Asset asset : list) {
            List<FormulaBO> formulaBOs = formulaSer.findByFid(asset.getId(), formulaDTO);
            if ((formulaBOs != null) && (!formulaBOs.isEmpty())) {
                if (AssetType.AFLOW.equals(asset.getAssetType()) && b1) {
                    AssetBO assetBO = new AssetBO();
                    assetBO.setAsset("流动资产：");
                    boList.add(assetBO);
                    b1 = false;
                } else if (AssetType.BLONG.equals(asset.getAssetType()) && b2) {
                    AssetBO sumBO = new AssetBO();
                    sumBO.setAsset("流动资产合计");
                    sumBO.setBeginAsset(beginSum);
                    sumBO.setCurrent(currentSum);
                    sumBO.setEndAsset(endSum);
                    boList.add(sumBO);
                    beginSum = 0;
                    currentSum = 0;
                    endSum = 0;    //置为0
                    AssetBO assetBO = new AssetBO();
                    assetBO.setAsset("长期资产：");
                    boList.add(assetBO);
                    b2 = false;
                } else if (AssetType.CFIX.equals(asset.getAssetType()) && b3) {
                    AssetBO sumBO = new AssetBO();
                    sumBO.setAsset("长期资产合计");
                    sumBO.setBeginAsset(beginSum);
                    sumBO.setCurrent(currentSum);
                    sumBO.setEndAsset(endSum);
                    boList.add(sumBO);
                    beginSum = 0;
                    currentSum = 0;
                    endSum = 0;    //置为0
                    AssetBO assetBO = new AssetBO();
                    assetBO.setAsset("固定资产：");
                    boList.add(assetBO);
                    b3 = false;
                } else if (AssetType.DINVISIBLE.equals(asset.getAssetType()) && b4) {
                    AssetBO sumBO = new AssetBO();
                    sumBO.setAsset("固定资产合计");
                    sumBO.setBeginAsset(beginSum);
                    sumBO.setCurrent(currentSum);
                    sumBO.setEndAsset(endSum);
                    boList.add(sumBO);
                    beginSum = 0;
                    currentSum = 0;
                    endSum = 0;    //置为0
                    AssetBO assetBO = new AssetBO();
                    assetBO.setAsset("无形资产及其他资产：");
                    boList.add(assetBO);
                    b4 = false;
                } else if (AssetType.ETAX.equals(asset.getAssetType()) && b5) {
                    AssetBO sumBO = new AssetBO();
                    sumBO.setAsset("无形资产及其他资产合计");
                    sumBO.setBeginAsset(beginSum);
                    sumBO.setCurrent(currentSum);
                    sumBO.setEndAsset(endSum);
                    boList.add(sumBO);
                    beginSum = 0;
                    currentSum = 0;
                    endSum = 0;    //置为0
                    AssetBO assetBO = new AssetBO();
                    assetBO.setAsset("递延税款：");
                    boList.add(assetBO);
                    b5 = false;
                }
                FormulaBO formulaBO = formulaBOs.get(formulaBOs.size() - 1);
                AssetBO bo = BeanTransform.copyProperties(asset, AssetBO.class);
                bo.setBeginAsset(formulaBO.getBegin());
                bo.setCurrent(formulaBO.getCurrent());
                bo.setEndAsset(formulaBO.getEnd());
                if (Type.ADD.equals(asset.getType())) {
                    beginSum += bo.getBeginAsset();
                    currentSum += bo.getCurrent();
                    endSum += bo.getEndAsset();
                    countBegin += bo.getBeginAsset();
                    countCurrent += bo.getCurrent();
                    countEnd += bo.getEndAsset();
                } else if (Type.REMOVE.equals(asset.getType())) {
                    bo.setAsset("减：" + asset.getAsset());
                    beginSum = beginSum - bo.getBeginAsset();
                    currentSum -= bo.getCurrent();
                    endSum = endSum - bo.getEndAsset();
                    countBegin = countBegin - bo.getBeginAsset();
                    countCurrent -= bo.getCurrent();
                    countEnd = countEnd - bo.getEndAsset();
                }
                boList.add(bo);
            } else {
                boList.add(BeanTransform.copyProperties(asset, AssetBO.class));
            }
        }
        AssetBO lastBO = new AssetBO();
        lastBO.setAsset("资产总计");
        lastBO.setBeginAsset(countBegin);
        lastBO.setCurrent(countCurrent);
        lastBO.setEndAsset(countEnd);
        boList.add(lastBO);
        return boList;
    }

    @Override
    public List<StructureBO> assetStructure(AssetDTO dto) throws SerException {
        FormulaDTO formulaDTO = new FormulaDTO();
        BeanUtils.copyProperties(dto, formulaDTO);
        dto.getSorts().add("assetType=ASC");
        List<Asset> list = super.findAll();
        List<StructureBO> boList = new ArrayList<StructureBO>();
        boolean b = true;
        double flowSum = 0;
        double currentSum = 0;
        double countCurrent = 0;       //总资产
        for (Asset asset : list) {
            List<FormulaBO> formulaBOs = formulaSer.findByFid(asset.getId(), formulaDTO);
            if ((formulaBOs != null) && (!formulaBOs.isEmpty())) {
                if (AssetType.BLONG.equals(asset.getAssetType()) && b) {
                    flowSum = currentSum;
                    b = false;
                }
                FormulaBO formulaBO = formulaBOs.get(formulaBOs.size() - 1);
                AssetBO bo = BeanTransform.copyProperties(asset, AssetBO.class);
                bo.setCurrent(formulaBO.getCurrent());
                if (Type.ADD.equals(asset.getType())) {
                    currentSum += bo.getCurrent();
                    countCurrent += bo.getCurrent();
                } else if (Type.REMOVE.equals(asset.getType())) {
                    currentSum -= bo.getCurrent();
                    countCurrent -= bo.getCurrent();
                }
            }
        }
        StructureBO flowBO = new StructureBO();
        flowBO.setProject("流动资产合计");
        flowBO.setFee(flowSum);
        String flow = String.format("%.2f", (flowSum / countCurrent) * 100);
        flowBO.setScale(flow + "%");
        boList.add(flowBO);
        double otherSum = countCurrent - flowSum;
        StructureBO otherBO = new StructureBO();
        otherBO.setProject("非流动资产合计");
        otherBO.setFee(otherSum);
        String other = String.format("%.2f", (otherSum / countCurrent) * 100);
        otherBO.setScale(other + "%");
        boList.add(otherBO);
        StructureBO sumBO = new StructureBO();
        sumBO.setProject("资产总计");
        sumBO.setFee(countCurrent);
        sumBO.setScale("100%");
        boList.add(sumBO);
        StructureBO rate = new StructureBO();
        rate.setProject("比例说明");
        rate.setBestScale("流动资产比重较高会占用大量资金，降低流动资产周转率，从而影响企业的资金利用效率。" +
                "非流动资产比例过低会影响企业的获利能力，从而影响企业未来的发展。");
        boList.add(rate);
        String advice = assetStructureAdvice(flow, other);
        StructureBO adviceBO = new StructureBO();
        adviceBO.setProject("管理建议");
        adviceBO.setBestScale(advice);
        boList.add(adviceBO);
        return boList;
    }

    /**
     * 获取资产结构管理建议
     *
     * @param flow
     * @param other
     * @return
     * @throws SerException
     */
    private String assetStructureAdvice(String flow, String other) throws SerException {
        List<AssetStructureAdviceBO> advices = assetStructureAdviceSer.list(new AssetStructureAdviceDTO());
        String advice = null;
        if (advices != null && !advices.isEmpty()) {
            for (AssetStructureAdviceBO r : advices) {
                boolean b1 = Double.parseDouble(flow) >= r.getFlowMin() && Double.parseDouble(flow) <= r.getFlowMax();
                boolean b2 = Double.parseDouble(other) >= r.getNotFlowMin() && Double.parseDouble(other) <= r.getNotFlowMax();
                if (b1 && b2) {
                    advice = r.getAdvice();
                }
            }
        }
        return advice;
    }

    @Override
    public List<RepayAnalyzeBO> repayAnalyze(AssetDTO dto) throws SerException {
        double flowAsset = assetStructure(dto).get(0).getFee();
        double flowDebt = finds(dto).get(0);
        double asset = assetStructure(dto).get(2).getFee();
        double debt = finds(dto).get(1);
        double all = finds(dto).get(2);
        double fund = list(dto).get(1).getCurrent();   //货币资金
        double stock = list(dto).get(10).getCurrent();  //存货净额
        List<RepayAnalyzeBO> list = new ArrayList<>();
        RepayAnalyzeBO firstBO = new RepayAnalyzeBO();
        firstBO.setProject("一、短期偿债能力分析");
        list.add(firstBO);
        RepayAnalyzeBO flowBO = new RepayAnalyzeBO();
        flowBO.setProject("流动比率");
        String flow = String.format("%.2f", (flowAsset / flowDebt) * 100);
        flowBO.setScale(flow + "%");
        flowBO.setBestScale("200%");
        flowBO.setExplain("流动比率越高，反映企业短期偿债能力越强，但是流动比率过高则表明企业流动资产占用较多，" +
                "会影响企业的资金利用效率，进而降低企业的获利能力。");
        list.add(flowBO);
        RepayAnalyzeBO rateBO = new RepayAnalyzeBO();
        rateBO.setProject("速动比率");
        String rate = String.format("%.2f", ((flowAsset - stock) / flowDebt) * 100);
        rateBO.setScale(rate + "%");
        rateBO.setBestScale("100%");
        rateBO.setExplain("速动比例较高说明公司不用动用存货，仅仅依靠速动资产就能偿还债务，偿还流动负债的能力较强，" +
                "但过高的速动比率也会造成资金的闲置，影响企业的盈利能力。");
        list.add(rateBO);
        RepayAnalyzeBO cashBO = new RepayAnalyzeBO();
        cashBO.setProject("现金比率");
        String cash = String.format("%.2f", (fund / flowDebt) * 100);
        cashBO.setScale(cash + "%");
        cashBO.setBestScale("20%");
        cashBO.setExplain("现金比率越高，表明企业的直接偿付能力越强，信用也就越可靠。" +
                "但是由于现金是企业收益率最低的资产，现金比率过高将会影响企业的盈利能力。");
        list.add(cashBO);
        RepayAnalyzeBO secondBO = new RepayAnalyzeBO();
        secondBO.setProject("二、长期偿债能力分析");
        list.add(secondBO);
        RepayAnalyzeBO assetdebtBO = new RepayAnalyzeBO();
        assetdebtBO.setProject("资产负债率");
        String assetdebt = String.format("%.2f", (debt / asset) * 100);
        assetdebtBO.setScale(assetdebt + "%");
        assetdebtBO.setBestScale("40%-60%");
        assetdebtBO.setExplain("对于经营风险比较高的企业，为减少财务风险应选择比较低的资产负债率；" +
                "对于经营风险低的企业，为增加股东收益应选择比较高的资产负债率。");
        list.add(assetdebtBO);
        RepayAnalyzeBO equityBO = new RepayAnalyzeBO();
        equityBO.setProject("产权比率");
        String equity = String.format("%.2f", (debt / all) * 100);
        equityBO.setScale(equity + "%");
        equityBO.setBestScale("100%");
        equityBO.setExplain("较低的产权比率表明企业采用了低风险、低报酬的资本结构，债权人的利益受保护程度较高，企业财务风险较小。" +
                "但是过低的产权比率也意味着企业不能充分发挥负债带来的财务杠杆作用。产权比率高，是高风险、高报酬的财务结构");
        list.add(equityBO);
        String advice = repayAdvice(flow, rate, cash, assetdebt, equity);
        RepayAnalyzeBO adviceBO = new RepayAnalyzeBO();
        adviceBO.setProject("三、管理建议");
        adviceBO.setExplain(advice);
        list.add(adviceBO);
        return list;
    }

    /**
     * 获取偿债能力分析管理建议
     *
     * @param flow
     * @param rate
     * @param cash
     * @param assetdebt
     * @param equity
     * @return
     * @throws SerException
     */
    private String repayAdvice(String flow, String rate, String cash, String assetdebt, String equity) throws SerException {
        List<RepayAnalyzeAdviceBO> advices = repayAnalyzeAdviceSer.list(new RepayAnalyzeAdviceDTO());
        String advice = null;
        if (advices != null && !advices.isEmpty()) {
            for (RepayAnalyzeAdviceBO r : advices) {
                boolean b1 = Double.parseDouble(flow) >= r.getFlowMin() && Double.parseDouble(flow) <= r.getFlowMax();
                boolean b2 = Double.parseDouble(rate) >= r.getRateMin() && Double.parseDouble(rate) <= r.getRateMax();
                boolean b3 = Double.parseDouble(cash) >= r.getMoneyMin() && Double.parseDouble(cash) <= r.getMoneyMax();
                boolean b4 = Double.parseDouble(assetdebt) >= r.getAssestMin() && Double.parseDouble(assetdebt) <= r.getAssestMax();
                boolean b5 = Double.parseDouble(equity) >= r.getEquityMin() && Double.parseDouble(equity) <= r.getEquityMax();
                if (b1 && b2 && b3 && b4 && b5) {
                    advice = r.getAdvice();
                }
            }
        }
        return advice;
    }

    @Override
    public List<DetailBO> findDetails(String id, AssetDTO dto) throws SerException {
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
            String project = findByID(id).getAsset();
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
    public AssetBO findByID(String id) throws SerException {
        Asset entity = super.findById(id);
        if (entity == null) {
            throw new SerException("该对象不存在");
        }
        return BeanTransform.copyProperties(entity, AssetBO.class);
    }

    /**
     * 查找流水负债,负债总额，所有者权益
     *
     * @param dto
     * @return
     * @throws SerException
     */
    private List<Double> finds(AssetDTO dto) throws SerException {
        DebtDTO debtDTO = new DebtDTO();
        BeanUtils.copyProperties(dto, debtDTO);
        List<StructureBO> list = debtSer.debtStructure(debtDTO);
        double flow = 0;
        double all = 0;
        double sum = 0;
        double debt = 0;
        if ((list != null) && (!list.isEmpty())) {
            flow = list.get(0).getFee();
            all = list.get(2).getFee();
            sum = list.get(3).getFee();
            debt = sum - all;
        }
        List<Double> doubles = new ArrayList<>();
        doubles.add(flow);
        doubles.add(debt);
        doubles.add(all);
        return doubles;
    }

    @Override
    public Long count(AssetDTO dto) throws SerException {
        return super.count(dto);
    }
}