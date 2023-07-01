package com.blaquesystems.backend.security.services;

import com.blaquesystems.backend.exception.SmsSendingException;
import com.blaquesystems.backend.models.Channel;
import com.blaquesystems.backend.models.Notification;
import com.blaquesystems.backend.models.User;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.blaquesystems.backend.repository.NotificationRepository;
import com.blaquesystems.backend.utils.SmsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class SmsService {

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    SmsUtils smsUtils;

    public void sendSms(String message, User user, Channel channel) throws SmsSendingException, UnsupportedEncodingException {
        message = java.net.URLEncoder.encode(message, StandardCharsets.UTF_8).replace("+", "%20");
        String fullUrl = smsUtils.getFullSmsUrl(message, 1);
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.post(fullUrl).asString();
            int statusCode = response.getStatus();
            if (statusCode >= 200 && statusCode < 300) {
                saveNotification(user, message, channel);
            } else {
                throw new SmsSendingException("Failed to send SMS. HTTP status code: " + statusCode);
            }
        } catch (Exception e) {
            throw new SmsSendingException("Failed to send SMS.", e);
        }
    }

    private void saveNotification(User user, String message, Channel channel) {
        Date now = new Date();
        Notification notification = new Notification(user, message, channel, now, now);
        notificationRepository.save(notification);
    }
}