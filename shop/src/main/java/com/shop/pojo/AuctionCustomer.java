package com.shop.pojo;


public class AuctionCustomer extends Auction {
	private String auctionprice;
	private String username;
	public String getAuctionprice() {
		return auctionprice;
	}
	public void setAuctionprice(String auctionprice) {
		this.auctionprice = auctionprice;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
