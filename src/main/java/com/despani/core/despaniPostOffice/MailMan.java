package com.despani.core.despaniPostOffice;


import com.despani.core.config.beans.DespaniConfigProperties;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class MailMan {

////    @Value("${despaniuseremail.sendgrid.api.key}")
//    private String sendGridApiKey;




    @Autowired
    DespaniConfigProperties props;

    //sendgridkey



    public void  send(String subjectOfEmail, String contentOfEmail) {

        Email from = new Email(props.getProps().getFromEmail());
        Email to = new Email(props.getProps().getToEmail());
        String subject = subjectOfEmail;

        Content newContent = new Content("text/plain", contentOfEmail);

        Mail mail = new Mail(from,subject,to,newContent);

        Request request = new Request();

        try {

            SendGrid sg = new SendGrid(props.getProps().getSendgridkey());
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void send(Mail mail) {
        log.info(">> Starting to send Email");
        try {
            SendGrid sg = new SendGrid(props.getProps().getSendgridkey());
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(">>>> "+response.getStatusCode());
            log.info(""+response.getStatusCode());
            log.info(response.getBody());
            log.info("Headers: "+response.getHeaders().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
