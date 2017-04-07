package com.bjike.goddess.marketdevelopment.api;

import com.bjike.goddess.common.api.exception.SerException;
import com.bjike.goddess.marketdevelopment.bo.MarketMeasureBO;
import com.bjike.goddess.marketdevelopment.service.MarketMeasureSer;
import com.bjike.goddess.marketdevelopment.to.MarketMeasureTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 市场测算业务接口实现
 *
 * @Author: [ dengjunren ]
 * @Date: [ 2017-03-22 07:19 ]
 * @Description: [ 市场测算业务接口实现 ]
 * @Version: [ v1.0.0 ]
 * @Copy: [ com.bjike ]
 */
@Service("marketMeasureApiImpl")
public class MarketMeasureApiImpl implements MarketMeasureAPI {

    @Autowired
    private MarketMeasureSer marketMeasureSer;

    @Override
    public MarketMeasureBO save(MarketMeasureTO to) throws SerException {
        return marketMeasureSer.save(to);
    }

    @Override
    public MarketMeasureBO update(MarketMeasureTO to) throws SerException {
        return marketMeasureSer.update(to);
    }

    @Override
    public MarketMeasureBO delete(MarketMeasureTO to) throws SerException {
        return marketMeasureSer.delete(to);
    }

    @Override
    public List<MarketMeasureBO> findByType(String type) throws SerException {
        return marketMeasureSer.findByType(type);
    }

    @Override
    public List<MarketMeasureBO> findByCourse(String course) throws SerException {
        return marketMeasureSer.findByCourse(course);
    }

    @Override
    public List<MarketMeasureBO> findByCourseType(String type, String course) throws SerException {
        return marketMeasureSer.findByCourseType(type, course);
    }
}