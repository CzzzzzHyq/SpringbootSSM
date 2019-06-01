package com.train.shop.mapper;

import com.train.shop.pojo.Auction;
import com.train.shop.pojo.AuctionCustomer;
import com.train.shop.pojo.AuctionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AuctionCustomerMapper {
 Auction selectAuctionAndRecodList(int auctionId);
 
 
 // 查询拍卖时间结束的商品
List<AuctionCustomer> findAuctionendtime();

// 查询拍卖时间没有结束的商品
List<Auction>findAuctionNoendtime();
}