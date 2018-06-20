
package com.nsl.app.enums;




import java.util.ArrayList;
import java.util.List;


/**
 * Created by Venkateswarlu SKP on 25-08-2016.
 */

public enum PlannerEnum


{


    MARKETING(1, "Marketing", new int[]{1, 2, 3}),
    ADVANCE_BOOKING(2, "Advance Booking", new int[]{1}),
    ORDER_INDENT(3, "Order Indent", new int[]{1}),
    STOCK_MOVEMENT(4, "Stock Supply", new int[]{1,2}),
    PAYMENT_COLLECTION(5, "Payment Collection", new int[]{1}),
    TRAINING(6, "Training", new int[]{3}),
    PROMOTING(7, "FD planning", new int[]{1, 2, 3}),//Purpose of visit- change promoting to FD planning
    COMPLAINT(8, "Complaint", new int[]{2}),
    FEEDBACK(9, "Feedback", new int[]{2}),
    YIELD_ESTIMATION(10, "Yield Inspection", new int[]{3}), // Purpose of visit- change Yield estimation to field inspection
    MARKET_INTELLIGENCE(11, "Market Intelligence", new int[]{1, 2, 3});
    // PROMISING(12,"Promising","2");


    private final int roleId;

    private final String title;

    private final int[] visitTypes;


    PlannerEnum(int roleId, String title, int[] visitTypes) {

        this.roleId = roleId;

        this.title = title;

        this.visitTypes = visitTypes;


    }


    public String getTitle() {
        return title;
    }

    public int getRoleId() {
        return roleId;
    }

    public int[] getVisitTypes() {
        return visitTypes;
    }


    public static PlannerEnum getPNTitle(int roleId) {
        for (PlannerEnum pnData : list) {
            if (pnData.getRoleId() == roleId) {
                return pnData;
            }
        }
        return null;
    }


    static List<PlannerEnum> list = new ArrayList<>();
    static List<PlannerEnum> visitListById = new ArrayList<>();


    static {

        for (PlannerEnum pnData : PlannerEnum.values()) {

            list.add(pnData);

        }

    }


    public static List<PlannerEnum> getList() {
        return list;
    }

    public static List<PlannerEnum> getListByVisitTypeID(int visitTypeID) {
        visitListById.clear();
        for (PlannerEnum plannerEnum : PlannerEnum.values()) {
            int[] visitTypes = plannerEnum.getVisitTypes();
            for (int i = 0; i < visitTypes.length; i++) {
                if (visitTypes[i] == visitTypeID) {
                    visitListById.add(plannerEnum);

                }


            }
        }


        return visitListById;
    }




}


