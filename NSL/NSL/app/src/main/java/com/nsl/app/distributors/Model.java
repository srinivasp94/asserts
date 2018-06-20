package com.nsl.app.distributors;

public class Model {


    public static final int RM_TYPE=0;
    public static final int MO_TYPE=1;


    public int type;
    public String customer_id;
    public String customer_name;
    public String customer_code;
    public String company_id;
    public String company_code;
    public String mo_name;
    public String sap_id;


    public Model(int type, String customer_id, String customer_name, String customer_code, String company_id, String company_code, String mo_name, String sap_id) {
        this.type = type;
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.customer_code = customer_code;
        this.company_id = company_id;
        this.company_code = company_code;
        this.mo_name = mo_name;
        this.sap_id = sap_id;
    }





   /* public Model(int type, String customer_id, int data)
    {
        this.type=type;
        this.data=data;
        this.customer_id=customer_id;

    }*/

}