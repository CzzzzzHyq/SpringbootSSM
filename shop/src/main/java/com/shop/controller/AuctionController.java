package com.shop.controller;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shop.pojo.Auction;
import com.shop.pojo.AuctionCustomer;
import com.shop.pojo.Auctionrecord;
import com.shop.pojo.User;
import com.shop.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
@Controller
public class AuctionController {
    // 每页记录数
    private static final  int PAGE_SIZE = 10;

    @Autowired
    private AuctionService auctionService;

    @RequestMapping(value = "/queryAllAuctions")
    public ModelAndView queryAllAuctions(@ModelAttribute("condition") Auction auction,
                                         @RequestParam(name = "pageNum",
                                                 required = false,
                                                 defaultValue = "1") int pageNum) {
        ModelAndView modelAndView = new ModelAndView();
        PageHelper.startPage(pageNum,PAGE_SIZE);
        List<Auction> auctionList = this.auctionService.queryAllAuctions(auction);
        PageInfo page  = new PageInfo(auctionList);
        page.getPrePage();
        page.getNextPage();
        page.getTotal();
        page.getPages();
        page.getPageNum();
        modelAndView.addObject("auctionList",auctionList);
        modelAndView.addObject("page",page);
        modelAndView.setViewName("index");
        return modelAndView;
    }


    // 查询出竞拍详情
    @RequestMapping(value = "/findAuctionDetial/{auctionid}")
    public ModelAndView findAuctionDetial(@PathVariable int auctionid) {
        ModelAndView modelAndView = new ModelAndView();
        Auction auctionDetail = this.auctionService.selectAuctionAndAuctionRecordList(auctionid);
        modelAndView.addObject("auctionDetail",auctionDetail);
        modelAndView.setViewName("auctionDetail");
        return modelAndView;
    }


    @RequestMapping(value = "/saveAuctionRecord")
    //竞拍具体实现
    public String saveAuctionRecord(Auctionrecord auctionrecord,HttpSession session) throws Exception {
        User user = (User) session.getAttribute("user");
        auctionrecord.setUserid(user.getUserid());
        auctionrecord.setAuctiontime(new Date());
        this.auctionService.saveAuctionRecord(auctionrecord);
        return "redirect:/findAuctionDetial/"+auctionrecord.getAuctionid();
    }

    @RequestMapping(value = "/toAuctionResult")
    public ModelAndView toAuctionResult() {
        ModelAndView modelAndView = new ModelAndView();
        List<AuctionCustomer> endtimeList = this.auctionService.selectAuctionendtime();
        List<Auction> noendtimeList = this.auctionService.selectAuctionNoendtime();
        modelAndView.addObject("endtimeList",endtimeList);
        modelAndView.addObject("noendtimeList",noendtimeList);
        modelAndView.setViewName("auctionResult");
        return modelAndView;
    }

    // 跳转到发布拍卖品页面
    @RequestMapping(value = "/toAuctionPage")
    public String toAuctionPage()
    {
        return "addAuction";
    }

    @RequestMapping(value = "/publishAuctions")
    public String publishAuctions(Auction auction, MultipartFile pic) throws IOException {

        auction.setAuctionpictype(pic.getContentType());
        auction.setAuctionpic(pic.getOriginalFilename());
        File file = new File("E:\\pic\\"+pic.getOriginalFilename());
        // 上传
        pic.transferTo(file);
       this.auctionService.addAuction(auction);
       return "redirect:/queryAllAuctions";
    }

    //删除拍卖品
    @RequestMapping(value = "DeleteAuction/{auctionid}")
    public String DeleteAuction(@PathVariable Integer auctionid){
        int row=this.auctionService.deleteAuction(auctionid);
        if (row>0){
            return "redirect:/queryAllAuctions";
        }
        else {
            return "redirect:/queryAllAuctions";
        }
    }

    //跳转更新拍卖品页面
    @RequestMapping(value = "ToUpdateAuction")
    public String ToUpdateAuction(Integer auctionid,Model model){
        model.addAttribute("auctionid",auctionid);
        Auction auction=auctionService.selectByPrimaryKey(auctionid);
        model.addAttribute("auction",auction);
        return "updateAuction";
    }

    //更新拍卖品
    @RequestMapping(value = "updateAuction")
    public String updateAuction(Auction auction,MultipartFile pic) throws Exception{
        auction.setAuctionpictype(pic.getContentType());
        auction.setAuctionpic(pic.getOriginalFilename());
        File file = new File("E:\\pic\\"+pic.getOriginalFilename());
        pic.transferTo(file);
        System.out.println(auction.toString());
        int row=auctionService.updateByPrimaryKey(auction);
        if (row>0){
            return "redirect:/queryAllAuctions";
        }
        else {
            return "redirect:/queryAllAuctions";
        }
    }
}
