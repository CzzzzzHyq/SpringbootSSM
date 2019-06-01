package com.train.shop.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.train.shop.mapper.AuctionCustomerMapper;
import com.train.shop.mapper.AuctionMapper;
import com.train.shop.mapper.AuctionrecordMapper;
import com.train.shop.pojo.Auction;
import com.train.shop.pojo.AuctionCustomer;
import com.train.shop.pojo.AuctionExample;
import com.train.shop.pojo.AuctionExample.Criteria;
import com.train.shop.pojo.Auctionrecord;
import com.train.shop.tools.CustomerException;

@Service
@Transactional
public class AuctionServiceImpl implements AuctionService {

	@Autowired
	private AuctionMapper auctionMapper;

	@Autowired
	private AuctionCustomerMapper auctionCustomerMapper;

	@Autowired
	private AuctionrecordMapper recordMapper;

	@Override
	public List<Auction> queryAllAuctions(Auction auction) {

		AuctionExample example = new AuctionExample();

		Criteria createCriteria = example.createCriteria();
		// 条件拼接查询

		// 代表用户有去选择其中一个条件

		if (auction != null) {
			// 再去判断用户有没有输入 拍卖品名称
			// 拍卖品名称的模糊查询
			if (auction.getAuctionname() != null && !"".equals(auction.getAuctionname())) {
				createCriteria.andAuctionnameLike("%" + auction.getAuctionname() + "%");
			}

			// 判断拍卖品描述
			if (auction.getAuctiondesc() != null && !"".equals(auction.getAuctiondesc())) {
				createCriteria.andAuctiondescLike("%" + auction.getAuctiondesc() + "%");
			}

			// 判断拍卖商品的起始时间 大于
			if (auction.getAuctionstarttime() != null) {
				// 查询大于拍卖品的起始时间的数据
				createCriteria.andAuctionstarttimeGreaterThan(auction.getAuctionstarttime());
			}
			// 判断拍卖品的结束时间 小于
			if (auction.getAuctionendtime() != null) {
				createCriteria.andAuctionendtimeLessThan(auction.getAuctionendtime());
			}
			// 起拍价
			if (auction.getAuctionstartprice() != null) {
				createCriteria.andAuctionstartpriceEqualTo(auction.getAuctionstartprice());
			}

		}

		// 根据起拍开始时间降序
		example.setOrderByClause("auctionstarttime desc");

		List<Auction> list = this.auctionMapper.selectByExample(example);
		return list;
	}

	@Override
	public Auction selectAuctionAndRecodList(int auctionId) {
		Auction auction = this.auctionCustomerMapper.selectAuctionAndRecodList(auctionId);
		return auction;
	}

	/***
	 * 
	 * 竞价业务规则:
	 * 
	 * 1.判断结束时间，不能过期 (用你提交的竞拍时间和 之前竞拍品种结束时间比较)
	 * 
	 * 
	 * 
	 * 2.判断价格：
	 * 
	 * 思考：怎么知道某个商品有没有竞价记录？？
	 * 
	 * -- 如果商品从未竞价，价格必须高于起拍价 -- 后续的竞价，价格必须高于所有竞价的最高价
	 * 
	 * @throws Exception
	 * 
	 * 
	 * 
	 */
	@Override
	public void saveAuctionRecord(Auctionrecord record) throws Exception {

		// Auction auction =
		// this.auctionMapper.selectByPrimaryKey(record.getAuctionid());

		Auction auction = this.auctionCustomerMapper.selectAuctionAndRecodList(record.getAuctionid());

		// 判断结束时间，不能过期 (用你提交的竞拍时间和 之前竞拍品种结束时间比较)
		if (auction.getAuctionendtime().after(new Date()) == false) {
			//throw new Exception("竞拍时间已经结束！！");
			throw new CustomerException("竞拍时间已经结束！！");
		}

		// 价格的判断

		if (auction.getAuctionrecodList().size() > 0) {
			// 有竞价记录
			// 取出所有竞价记录中的最高价 ?????

			Auctionrecord maxRecord = auction.getAuctionrecodList().get(0); // 取出的就是所有竞价记录中的最高价

			// 用你出的价格和最高价做一个比较 // BigDecimal compareTo 1 0 -1
			if (record.getAuctionprice().compareTo(maxRecord.getAuctionprice()) < 1) {
				//throw new Exception("竞拍价格不能低于所有竞拍记录的最高价！！");
				throw new  CustomerException("竞拍价格不能低于所有竞拍记录的最高价！！");
			}
		} else {
			// 没做任何竞价记录 你出的价格和原来的起拍价格对比
			if (record.getAuctionprice().compareTo(auction.getAuctionstartprice()) < 1) {
				//throw new Exception("竞拍价格不能低于起拍价格！！");
				throw new  CustomerException("竞拍价格不能低于起拍价格！！");
			}

		}

		// 处理业务逻辑
		this.recordMapper.insert(record);

	}

	@Override
	public List<AuctionCustomer> findAuctionendtime() {
		return this.auctionCustomerMapper.findAuctionendtime();
	}

	@Override
	public List<Auction> findAuctionNoendtime() {
		
		return this.auctionCustomerMapper.findAuctionNoendtime();
	}

	@Override
	public void addAuction(Auction auction) {
		this.auctionMapper.insert(auction);
		
	}

}
