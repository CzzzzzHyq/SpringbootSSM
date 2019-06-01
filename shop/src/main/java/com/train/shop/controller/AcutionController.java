package com.train.shop.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.train.shop.pojo.Auction;
import com.train.shop.pojo.AuctionCustomer;
import com.train.shop.pojo.Auctionrecord;
import com.train.shop.pojo.User;
import com.train.shop.service.AuctionService;

@Controller
public class AcutionController {
	
	// 每页的记录数
	
	private static final int PAGE_SIZE = 5;
	

	@Autowired
	private AuctionService auctionService;

	@RequestMapping(value = "/queryAllAuctions")
	public ModelAndView queryAllAuctions(@ModelAttribute("condition")Auction auction,
			@RequestParam(name="pageNum",required=false,defaultValue="1")int pageNum) {
		ModelAndView mv = new ModelAndView();
		
		
		PageHelper.startPage(pageNum, PAGE_SIZE);
		
		List<Auction> auctionList = this.auctionService.queryAllAuctions(auction);
		
		PageInfo page = new PageInfo(auctionList);
		// 上一页
		page.getPrePage();
		
		// 下一页
		page.getNextPage();
		
		// 尾页
		page.getPages();
		
		// 总记录数
		page.getTotal();
		
		// 当前页码
		page.getPageNum();
		
		
		// 将分页的对象存在model域中
		mv.addObject("page", page);
		
		mv.addObject("auctionList", auctionList);
		mv.setViewName("index");
		return mv;
	}
	
	// 根据拍卖品的id 查询拍卖信息记录
	
	@RequestMapping(value="/getAuctionDetail/{auctionid}")
	public ModelAndView getAuctionDetail(@PathVariable int auctionid)
	{
		
		ModelAndView mv = new ModelAndView();
	  Auction auctionDetail = this.auctionService.selectAuctionAndRecodList(auctionid);	
	  mv.addObject("auctionDetail", auctionDetail);
	  mv.setViewName("auctionDetail");
	  return mv;
	}
	
	@RequestMapping(value="/saveAuctionRecord")
	public String saveAuctionRecord(Auctionrecord record,
			HttpSession session) throws Exception
	{
		
		User user = (User) session.getAttribute("user");
		
		// 从session域中取出userId
		
		// 存储userid到 竞价记录中
		record.setUserid(user.getUserid());
		
		
		// 设置竞拍时间
		record.setAuctiontime(new Date());
		
		this.auctionService.saveAuctionRecord(record);
		return "redirect:/getAuctionDetail/"+record.getAuctionid();
	}
	
	@RequestMapping(value="/toAuctionResult")
	public ModelAndView toAuctionResult()
	{
		
		ModelAndView mv = new ModelAndView();
		List<AuctionCustomer> endtimeList = this.auctionService.findAuctionendtime();
		
		List<Auction> noendtimeList = this.auctionService.findAuctionNoendtime();
		
		mv.addObject("endtimeList", endtimeList);
		mv.addObject("noendtimeList", noendtimeList);
		
		mv.setViewName("auctionResult");
		return mv;
	}
	
	// 跳转到发布拍卖品页面
	@RequestMapping(value="/toAcution")
	public String toAcution()
	{
		 return "addAuction";
	}
	
	@RequestMapping(value="/publishAuctions")
	public String addAuction(Auction auction,
			MultipartFile pic) throws IllegalStateException, IOException
	{
		
		
		// 将图片名称设置到 数据库的对应字段保存起来
	
		auction.setAuctionpic(pic.getOriginalFilename());
		// 保存图片的格式类型
		auction.setAuctionpictype(pic.getContentType());
				
		
		File file = new File("D:\\pic", pic.getOriginalFilename());
		
		pic.transferTo(file);

		this.auctionService.addAuction(auction);
		
	 return "redirect:/queryAllAuctions";	
	}
}
