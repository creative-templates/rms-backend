package com.restaurant.ms.core.utils;

import java.security.SecureRandom;
import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OtpCode {
  private static final SecureRandom random = new SecureRandom();
  private static final Logger logger = LoggerFactory.getLogger(OtpCode.class);

  public static String generateOtp() {
    int val = random.nextInt(999999);
    String otp = new DecimalFormat("000000").format(val);

    logger.info("Generated OTP: {}", otp);

    return otp;
  }
}
