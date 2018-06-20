package com.nsl.app.pojo;

import java.util.ArrayList;

/**
 * 
 * first level item
 * 
 */
public class MyWayProduct {

	private String pName;
    String pId;

	private ArrayList<SubCategory> mSubCategoryList;

	public MyWayProduct(String pName, ArrayList<SubCategory> mSubCategoryList, String pId) {
		super();
		this.pName = pName;
        this.pId = pId;
		this.mSubCategoryList = mSubCategoryList;
	}

	public String getpName() {
		return pName;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}
    public String getpId() {
        return pId;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

	public ArrayList<SubCategory> getmSubCategoryList() {
		return mSubCategoryList;
	}

	public void setmSubCategoryList(ArrayList<SubCategory> mSubCategoryList) {
		this.mSubCategoryList = mSubCategoryList;
	}

	/**
	 * 
	 * second level item
	 * 
	 */

	public static class SubCategory {

		private String pSubCatName;
        String pSubCatId;
		private ArrayList<ItemList> mItemListArray;

		public SubCategory(String pSubCatName,
						   ArrayList<ItemList> mItemListArray, String pSubCatId) {
			super();
			this.pSubCatName = pSubCatName;
			this.mItemListArray = mItemListArray;
            this.pSubCatId=pSubCatId;
		}

		public String getpSubCatName() {
			return pSubCatName;
		}

		public void setpSubCatName(String pSubCatName) {
			this.pSubCatName = pSubCatName;
		}public String getpSubCatId() {
            return pSubCatId;
        }

        public void setpSubCatId(String pSubCatId) {
            this.pSubCatId = pSubCatId;
        }


		public ArrayList<ItemList> getmItemListArray() {
			return mItemListArray;
		}

		public void setmItemListArray(ArrayList<ItemList> mItemListArray) {
			this.mItemListArray = mItemListArray;
		}

		/**
		 * 
		 * third level item
		 * 
		 */
		/*public static class ItemList {

			private String itemName;
			private String itemPrice;
            String itemId;

			public ItemList(String itemName, String itemPrice, String itemId) {
				super();
				this.itemName = itemName;
				this.itemPrice = itemPrice;
                this.itemId=itemId;
			}

			public String getItemName() {
				return itemName;
			}

			public void setItemName(String itemName) {
				this.itemName = itemName;
			}

            public String getitemId() {
                return itemId;
            }

            public void setitemId(String itemId) {
                this.itemId = itemId;
            }

			public String getItemPrice() {
				return itemPrice;
			}

			public void setItemPrice(String itemPrice) {
				this.itemPrice = itemPrice;
			}

		}*/
		public static class ItemList {
			private String batterylevelinpercent;

			private String travldtimeinsec;

			private String WayDest;

			private String vehiclenumber;

			private String MobileNo;

			private String totaldistanceinkm;

			private String disttravldinkm;

			private String WaySource;

			private String vehicleqrcode;

			private String vehicleimage;

			private String WayName;

			private String WayID;

			private String OwnerName;

			private String totaltimeinsec;

			private String flag;

			private String wayStatus;

			private String customer_name;
			private String customer_code;
			private String customer_id;

			public String getCustomer_name() {
				return customer_name;
			}

			public void setCustomer_name(String customer_name) {
				this.customer_name = customer_name;
			}

			public String getCustomer_code() {
				return customer_code;
			}

			public void setCustomer_code(String customer_code) {
				this.customer_code = customer_code;
			}

			public String getCustomer_id() {
				return customer_id;
			}

			public void setCustomer_id(String customer_id) {
				this.customer_id = customer_id;
			}

			public String getAbs() {
				return abs;
			}

			public void setAbs(String abs) {
				this.abs = abs;
			}

			public String getOrderdate() {
				return orderdate;
			}

			public void setOrderdate(String orderdate) {
				this.orderdate = orderdate;
			}

			public String getQuantity() {
				return quantity;
			}

			public void setQuantity(String quantity) {
				this.quantity = quantity;
			}

			public String getRate() {
				return rate;
			}

			public void setRate(String rate) {
				this.rate = rate;
			}

			private String abs;
			private String orderdate;
			private String quantity;
			private String rate;

			public ItemList(String customer_name, String customer_code, String customer_id, String abs,
				   String orderdate, String quantity, String rate
				   ){
	super();
	this.customer_name=customer_name;
	this.customer_code=customer_code;
	this.orderdate=orderdate;
	this.quantity=quantity;
	this.rate=rate;
	this.customer_id=customer_id;
	this.abs=abs;


}
			public String getWayStatus() {
				return wayStatus;
			}

			public void setWayStatus(String wayStatus) {
				this.wayStatus = wayStatus;
			}


			public String getFlag() {
				return flag;
			}

			public void setFlag(String flag) {
				this.flag = flag;
			}


			public String getBatterylevelinpercent ()
			{
				return batterylevelinpercent;
			}

			public void setBatterylevelinpercent (String batterylevelinpercent)
			{
				this.batterylevelinpercent = batterylevelinpercent;
			}

			public String getTravldtimeinsec ()
			{
				return travldtimeinsec;
			}

			public void setTravldtimeinsec (String travldtimeinsec)
			{
				this.travldtimeinsec = travldtimeinsec;
			}

			public String getWayDest ()
			{
				return WayDest;
			}

			public void setWayDest (String WayDest)
			{
				this.WayDest = WayDest;
			}

			public String getVehiclenumber ()
			{
				return vehiclenumber;
			}

			public void setVehiclenumber (String vehiclenumber)
			{
				this.vehiclenumber = vehiclenumber;
			}

			public String getMobileNo ()
			{
				return MobileNo;
			}

			public void setMobileNo (String MobileNo)
			{
				this.MobileNo = MobileNo;
			}

			public String getTotaldistanceinkm ()
			{
				return totaldistanceinkm;
			}

			public void setTotaldistanceinkm (String totaldistanceinkm)
			{
				this.totaldistanceinkm = totaldistanceinkm;
			}

			public String getDisttravldinkm ()
			{
				return disttravldinkm;
			}

			public void setDisttravldinkm (String disttravldinkm)
			{
				this.disttravldinkm = disttravldinkm;
			}

			public String getWaySource ()
			{
				return WaySource;
			}

			public void setWaySource (String WaySource)
			{
				this.WaySource = WaySource;
			}

			public String getVehicleqrcode ()
			{
				return vehicleqrcode;
			}

			public void setVehicleqrcode (String vehicleqrcode)
			{
				this.vehicleqrcode = vehicleqrcode;
			}

			public String getVehicleimage ()
			{
				return vehicleimage;
			}

			public void setVehicleimage (String vehicleimage)
			{
				this.vehicleimage = vehicleimage;
			}

			public String getWayName ()
			{
				return WayName;
			}

			public void setWayName (String WayName)
			{
				this.WayName = WayName;
			}

			public String getWayID ()
			{
				return WayID;
			}

			public void setWayID (String WayID)
			{
				this.WayID = WayID;
			}

			public String getOwnerName ()
			{
				return OwnerName;
			}

			public void setOwnerName (String OwnerName)
			{
				this.OwnerName = OwnerName;
			}

			public String getTotaltimeinsec ()
			{
				return totaltimeinsec;
			}

			public void setTotaltimeinsec (String totaltimeinsec)
			{
				this.totaltimeinsec = totaltimeinsec;
			}

		}
	}

}
