package com.example.BookingSystemBackend.Utils;

import com.example.BookingSystemBackend.Enum.Country;
import com.example.BookingSystemBackend.Enum.DurationType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ZoneDateTimeHelper {

    private ZoneId getZoneIdByCountry(Country country) {
        return switch (country) {
            case SINGAPORE -> ZoneId.of("Asia/Singapore");
            case MYANMAR -> ZoneId.of("Asia/Yangon");
        };
    }

    public ZonedDateTime calculatePurchaseDate(Country country) {

        ZoneId zoneId = getZoneIdByCountry(country);

        return ZonedDateTime.now(zoneId);
    }
    public ZonedDateTime calculateExpiryDate(Country country, int durationValue, DurationType durationType) {

        ZoneId zoneId = getZoneIdByCountry(country);
        ZonedDateTime now = ZonedDateTime.now(zoneId);

        return switch (durationType) {
            case MONTH -> now.plusMonths(durationValue);
            case DAY -> now.plusDays(durationValue);
        };
    }

    public ZonedDateTime localToZonedDateTimeConvertor(LocalDateTime localDateTime, Country country) {
        return ZonedDateTime.of(localDateTime, getZoneIdByCountry(country));
    }

    public ZonedDateTime convertStringToZonedDateTime(String dateTimeString, Country country) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return localDateTime.atZone(getZoneIdByCountry(country));
    }
}
