
package com.nsl.app.enums;




import java.util.ArrayList;
import java.util.List;


/**
 * Created by Venkateswarlu SKP on 25-08-2016.
 */

public enum RoleEnum


{


    MO(7, "MO"),
    RM(6, "RM"),
    AM(5, "AM");


    private final int roleId;

    private final String title;



    RoleEnum(int roleId, String title) {

        this.roleId = roleId;

        this.title = title;



    }


    public String getTitle() {
        return title;
    }

    public int getRoleId() {
        return roleId;
    }



    public static RoleEnum getRoleTitle(int roleId) {
        for (RoleEnum pnData : list) {
            if (pnData.getRoleId() == roleId) {
                return pnData;
            }
        }
        return null;
    }


    static List<RoleEnum> list = new ArrayList<>();
    static List<RoleEnum> visitListById = new ArrayList<>();


    static {

        for (RoleEnum pnData : RoleEnum.values()) {

            list.add(pnData);

        }

    }


    public static List<RoleEnum> getList() {
        return list;
    }





}


