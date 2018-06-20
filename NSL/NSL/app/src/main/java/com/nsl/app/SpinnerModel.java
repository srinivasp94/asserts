package com.nsl.app;

/**
 * Created by admin on 2/17/2017.
 */

public class SpinnerModel {

    public  String BankName="";
    public  String AccountNo="";
    public  String Ifsc="";

    /*********** Set Methods ******************/
    public void setBankName(String BankName)
    {
        this.BankName = BankName;
    }

    public void setAccountNo(String AccountNo)
    {
        this.AccountNo = AccountNo;
    }

    public void setIfsc(String Ifsc)
    {
        this.Ifsc = Ifsc;
    }

    /*********** Get Methods ****************/
    public String getBankName()
    {
        return this.BankName;
    }

    public String getAccountNo()
    {
        return this.AccountNo;
    }

    public String getIfsc()
    {
        return this.Ifsc;
    }
}
