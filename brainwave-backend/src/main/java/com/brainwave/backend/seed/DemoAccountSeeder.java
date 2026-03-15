package com.brainwave.backend.seed;

import com.brainwave.backend.config.SeedProperties;
import com.brainwave.core.utils.PasswordUtil;
import com.brainwave.service.member.entity.MemberEntity;
import com.brainwave.service.member.repository.MemberRepository;
import com.brainwave.service.user.entity.UserEntity;
import com.brainwave.service.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 啟動時依設定開關（app.seed.demo-accounts-enabled）自動建立示範帳號：
 * - 後台管理員：username=demo, password=123456
 * - 前台會員：username=demo, password=123456
 * 若已存在則不重複建立。
 */
@Configuration
public class DemoAccountSeeder {

    private static final Logger log = LoggerFactory.getLogger(DemoAccountSeeder.class);

    private static final String DEMO_USERNAME = "demo";
    private static final String DEMO_PASSWORD_RAW = "123456";
    private static final String DEMO_USER_NAME = "Demo 帳號";
    private static final String DEMO_USER_EMAIL = "demo@brainwave.local";
    private static final String DEMO_MEMBER_NAME = "Demo 會員";
    private static final String DEMO_MEMBER_EMAIL = "demo@brainwave-front.local";

    @Bean
    @Order(100)
    ApplicationRunner demoAccountSeederRunner(
            SeedProperties seedProperties,
            UserRepository userRepository,
            MemberRepository memberRepository) {
        return args -> {
            if (!seedProperties.isDemoAccountsEnabled()) {
                log.debug("app.seed.demo-accounts-enabled=false，略過示範帳號建立");
                return;
            }
            if (userRepository.findByUsername(DEMO_USERNAME).isEmpty()) {
                UserEntity user = new UserEntity();
                user.setUsername(DEMO_USERNAME);
                user.setPassword(PasswordUtil.encode(DEMO_PASSWORD_RAW));
                user.setName(DEMO_USER_NAME);
                user.setEmail(DEMO_USER_EMAIL);
                user.setPhone(null);
                userRepository.save(user);
                log.info("已建立示範管理員帳號: {}（密碼 123456）", DEMO_USERNAME);
            }
            if (memberRepository.findByUsername(DEMO_USERNAME).isEmpty()) {
                MemberEntity member = new MemberEntity();
                member.setUsername(DEMO_USERNAME);
                member.setPassword(PasswordUtil.encode(DEMO_PASSWORD_RAW));
                member.setName(DEMO_MEMBER_NAME);
                member.setEmail(DEMO_MEMBER_EMAIL);
                member.setPhone(null);
                memberRepository.save(member);
                log.info("已建立示範會員帳號: {}（密碼 123456）", DEMO_USERNAME);
            }
        };
    }
}
