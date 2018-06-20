package com.nsl.app.pojo;

import java.util.ArrayList;

/**
 * 
 * first level item
 * 
 */
public class MyWayProductSecondLevel {

	private String pName;
    String pId;

	private ArrayList<ItemList> mSubCategoryList;

	public MyWayProductSecondLevel(String pName, ArrayList<ItemList> mSubCategoryList, String pId) {
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

	public ArrayList<ItemList> getmSubCategoryList() {
		return mSubCategoryList;
	}

	public void setmSubCategoryList(ArrayList<ItemList> mSubCategoryList) {
		this.mSubCategoryList = mSubCategoryList;
	}



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
   public ItemList(String batterylevelinpercent, String travldtimeinsec, String WayDest, String vehiclenumber,
				   String MobileNo, String totaldistanceinkm, String disttravldinkm,
				   String WaySource, String vehicleqrcode, String vehicleimage, String WayName, String WayID,
				   String OwnerName, String totaltimeinsec, String flag, String wayStatus){
	super();
	this.batterylevelinpercent=batterylevelinpercent;
	this.travldtimeinsec=travldtimeinsec;
	this.WayDest=WayDest;
	this.vehiclenumber=vehiclenumber;
	this.MobileNo=MobileNo;
	this.totaldistanceinkm=totaldistanceinkm;
	this.disttravldinkm=disttravldinkm;
	this.WaySource=WaySource;
	this.vehicleqrcode=vehicleqrcode;
	this.vehicleimage=vehicleimage;
	this.WayName=WayName;
	this.WayID=WayID;
	this.OwnerName=OwnerName;
	this.totaltimeinsec=totaltimeinsec;
	this.flag=flag;
	this.wayStatus=wayStatus;

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


