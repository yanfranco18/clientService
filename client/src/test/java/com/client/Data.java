package com.client;

import com.client.models.Client;

import java.util.Date;

public class Data {

    public static Client getList(){
        Client cli = new Client();
        cli.setId("12233d");
        cli.setNameClient("Empresa Agp");
        cli.setTypeClient("Empresarial");
        cli.setIdentityDocument("24353456765");
        cli.setPhoneNumber("4564567");
        cli.setCreateDate(new Date(2022-02-16));
        return cli;
    }
}
