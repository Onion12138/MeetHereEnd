package com.ecnu.meethere.site.bookingtime.controller;

import com.ecnu.meethere.site.bookingtime.service.SiteBookingTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Validator;

@RestController
@RequestMapping("/site/booking-time/")
public class SiteBookingTimeController {
    @Autowired
    private SiteBookingTimeService siteBookingTimeService;

    @Autowired
    private Validator validator;

}