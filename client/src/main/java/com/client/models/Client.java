package com.client.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Clients")
public class Client {

    @Id
    private String id;
    private String nameClient;
    private String typeClient;
    private String identityDocument;
    private String phoneNumber;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date createDate;
}
