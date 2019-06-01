package com.shop.myauction.mapper;

import com.shop.myauction.pojo.Auction;
import com.shop.myauction.pojo.AuctionCustomer;

import java.util.List;

public interface AuctionCustomerMapper {

   Auction  selectAuctionAndAuctionRecordList(Integer auctionId);


   /**
    *
    * @查询已经结束的拍卖商品
    */
  List<AuctionCustomer> selectAuctionendtime();




  /**查询正在拍卖的商品*/
   List<Auction> selectAuctionNoendtime();

}
