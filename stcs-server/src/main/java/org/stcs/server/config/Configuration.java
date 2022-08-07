package org.stcs.server.config;

import javax.annotation.PostConstruct;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
@Data
@ToString
@Slf4j
public class Configuration {
    //  default values is 7 days
    @Value("${delayNoticeTime:604800}")
    private long delayNoticeTime;
    //  default values is 1 times
    @Value("${dailyNoticeLimit:1}")
    private long dailyNoticeLimit;
    //  default values is 3 times
    @Value("${UsedTimesLimit:3}")
    private int voiceFeatureUsedTimesLimit;

    @PostConstruct
    public void init() {

        log.info(":::delayNoticeTime:{}", delayNoticeTime);
        log.info(":::dailyNoticeLimit:{}", dailyNoticeLimit);
        log.info(":::voiceFeatureUsedTimesLimit:{}", voiceFeatureUsedTimesLimit);
    }
}
