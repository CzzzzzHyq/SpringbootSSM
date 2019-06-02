package com.shop.service;

import com.shop.mapper.AuctionCustomerMapper;
import com.shop.mapper.AuctionMapper;
import com.shop.mapper.AuctionrecordMapper;
import com.shop.pojo.Auction;
import com.shop.pojo.AuctionCustomer;
import com.shop.pojo.AuctionExample;
import com.shop.pojo.Auctionrecord;
import com.shop.utils.AuctionPriceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AuctionServiceImpl implements AuctionService {
    @Autowired
    private AuctionMapper auctionMapper;
    @Autowired
    private AuctionCustomerMapper auctionCustomerMapper;
    @Autowired
    private AuctionrecordMapper auctionrecordMapper;
    @Override
    public List<Auction> queryAllAuctions(Auction auction) {

        AuctionExample example = new AuctionExample();
        AuctionExample.Criteria criteria = example.createCriteria();

        // 判断 用户传入的多个条件或者没有传条件
        if(auction!=null) {
            // 竞拍商品的名称
            if(auction.getAuctionname()!=null&&!"".equals(auction.getAuctionname())) {
                // 模糊查询
                criteria.andAuctionnameLike("%"+auction.getAuctionname()+"%");
            }
            // 竞拍品描述的查询
            if(auction.getAuctiondesc()!=null&&!"".equals(auction.getAuctiondesc())) {
                // 匹配查询
                criteria.andAuctiondescEqualTo(auction.getAuctiondesc());
            }
            // 大于开始时间
            if(auction.getAuctionstarttime()!=null) {
               criteria.andAuctionstarttimeGreaterThan(auction.getAuctionstarttime());
            }

            // 小于结束时间
            if(auction.getAuctionendtime()!=null) {
                criteria.andAuctionendtimeLessThan(auction.getAuctionendtime());
            }

            // 大于起拍价
            if(auction.getAuctionstartprice()!=null) {
              criteria.andAuctionstartpriceGreaterThan(auction.getAuctionstartprice());
            }
        }

        // 设置起拍时间的降序
        example.setOrderByClause("auctionstarttime desc");
        List<Auction> auctionList = this.auctionMapper.selectByExample(example);
        return auctionList;
    }

    @Override
    public Auction selectAuctionAndAuctionRecordList(Integer auctionId) {
        Auction auction = this.auctionCustomerMapper.selectAuctionAndAuctionRecordList(auctionId);
        return auction;
    }

    @Override
    public void saveAuctionRecord(Auctionrecord auctionrecord) throws Exception {
        Auction auction = this.auctionCustomerMapper.selectAuctionAndAuctionRecordList(auctionrecord.getAuctionid());
        // 判断结束时间，不能过期
        if(auction.getAuctionendtime().after(new Date())==false) {
           throw new AuctionPriceException("当前竞拍活动已经结束了");
        }

        // 判断价格：
        if(auction.getAuctionrecodList().size()>0) {
            Auctionrecord maxRecord = auction.getAuctionrecodList().get(0);
            if(auctionrecord.getAuctionprice().compareTo(maxRecord.getAuctionprice())<1) {
               throw  new AuctionPriceException("您出的价格不能低于最高价");
            }

        }
        //如果商品从未竞价，价格必须高于起拍价
        else {
          if(auctionrecord.getAuctionprice().compareTo(auction.getAuctionstartprice())<1) {
              throw new AuctionPriceException("您出的价格不能低于起拍价");
          }
        }
        this.auctionrecordMapper.insert(auctionrecord);
    }

    @Override
    public List<AuctionCustomer> selectAuctionendtime() {
        return this.auctionCustomerMapper.selectAuctionendtime();
    }

    @Override
    public List<Auction> selectAuctionNoendtime() {
        return this.auctionCustomerMapper.selectAuctionNoendtime();
    }

    @Override
    public void addAuction(Auction auction) {
        this.auctionMapper.insert(auction);
    }

    @Override
    public int deleteAuction(Integer Auctionid) {
        return this.auctionMapper.deleteByPrimaryKey(Auctionid);
    }

    @Override
    public Auction selectByPrimaryKey(Integer Auctionid) {
        return this.auctionMapper.selectByPrimaryKey(Auctionid);
    }

    @Override
    public int updateByPrimaryKey(Auction auction) {
        return this.auctionMapper.updateByPrimaryKey(auction);
    }
}
