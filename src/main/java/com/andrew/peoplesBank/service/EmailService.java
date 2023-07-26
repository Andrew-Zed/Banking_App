package com.andrew.peoplesBank.service;

import com.andrew.peoplesBank.dto.EmailDetails;
import org.springframework.stereotype.Service;


public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);

}
