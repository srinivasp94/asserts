package com.nsl.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by laxmanamurthy on 10/13/2016.
 */

public class Country implements Serializable {

    public String country_name = "",country_shortcode="",country_id="";

    public List<State> states = new ArrayList<State>();
}
