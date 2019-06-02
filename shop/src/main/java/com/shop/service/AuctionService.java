package com.shop.service;

import com.shop.pojo.Auction;
import com.shop.pojo.AuctionCustomer;
import com.shop.pojo.Auctionrecord;

import java.util.List;

public interface AuctionService {
    /*List<Auction> queryAllAuctions();*/

    List<Auction> queryAllAuctions(Auction auction);


    Auction selectAuctionAndAuctionRecordList(Integer auctionId);

    void saveAuctionRecord(Auctionrecord auctionrecord) throws Exception;


    /**
     * @查询已经结束的拍卖商品
     */
    List<AuctionCustomer> selectAuctionendtime();


    /**
     * 查询正在拍卖的商品
     */
    List<Auction> selectAuctionNoendtime();

    //新增拍卖品
    void addAuction(Auction auction);

    //删除拍卖品
    int deleteAuction(Integer Auctionid);

    //根据ID查询拍卖品
    Auction selectByPrimaryKey(Integer Auctionid);

    //更新拍卖品
    int updateByPrimaryKey(Auction auction);
}
