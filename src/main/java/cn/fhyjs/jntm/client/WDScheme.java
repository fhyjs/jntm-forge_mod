/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package cn.fhyjs.jntm.client;

import net.montoyo.mcef.api.IScheme;
import net.montoyo.mcef.api.ISchemeResponseData;
import net.montoyo.mcef.api.ISchemeResponseHeaders;
import net.montoyo.mcef.api.SchemePreResponse;



public class WDScheme implements IScheme {


    @Override
    public SchemePreResponse processRequest(String s) {
        return null;
    }

    @Override
    public void getResponseHeaders(ISchemeResponseHeaders iSchemeResponseHeaders) {

    }

    @Override
    public boolean readResponse(ISchemeResponseData iSchemeResponseData) {
        return false;
    }
}
