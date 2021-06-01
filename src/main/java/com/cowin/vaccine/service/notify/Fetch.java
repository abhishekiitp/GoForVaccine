package com.cowin.vaccine.service.notify;

import com.cowin.vaccine.model.Center;
import com.cowin.vaccine.model.Centers;
import com.cowin.vaccine.model.Session;
import com.cowin.vaccine.model.UserRegistrationConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalCause;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class Fetch {

    private static final Logger LOG = LoggerFactory.getLogger(Fetch.class);
    public static final String URL = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict";
    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd-MM-yyyy");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final String TODAY = "T";
    private static final String WEEK = "W";

    public static LoadingCache<String, String> districtSet;
    public static Map<String, Map<String, Centers>> centerSet = new HashMap<>();

    public static final int CACHE_TIMEOUT = 15;
    private static final long DELAY = 2 * 60 * 1000;
    private static final int NEXT_WEEK = 7;

    public void add(String district) {
        try {
            districtSet.get(district); // cache for 5 minutes
        } catch (ExecutionException e) {
            LOG.error("", e);
        }
    }

    public List<Center> get(String district, boolean twoWeeks) {
        add(district);
        Map<String, Centers> center = centerSet.get(district);
        if (!CollectionUtils.isEmpty(center)) {
            List<Center> list = center.get(TODAY).getCenters();
            if (!CollectionUtils.isEmpty(list)) {
                if (twoWeeks && center.get(WEEK) != null) {
                    list.addAll(center.get(WEEK).getCenters());
                }
                return list;
            }
        }
        return null;
    }

    @PostConstruct
    public void init() {
        districtSet =
            CacheBuilder
                .newBuilder()
                .expireAfterAccess(CACHE_TIMEOUT, TimeUnit.MINUTES)
                .removalListener(
                    notification -> {
                        if (notification.getCause() == RemovalCause.EXPIRED) {
                            centerSet.remove(notification.getKey());
                        }
                    }
                )
                .build(
                    new CacheLoader<>() {
                        @Override
                        public String load(String key) {
                            return key;
                        }
                    }
                );
    }

    @Scheduled(fixedDelay = DELAY)
    public void retrieve() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, NEXT_WEEK);
            for (String district : districtSet.asMap().keySet()) {
                Map<String, Centers> nmap = new HashMap<>();
                nmap.put(TODAY, makeCall(district, new Date()));
                nmap.put(WEEK, makeCall(district, cal.getTime()));
                centerSet.put(district, nmap);
            }
        } catch (Exception e) {
            LOG.error("", e);
        }
        LOG.info("Executed " + SIMPLE_DATE_FORMAT.format(new Date()));
    }

    public static void checkUserRegistration(String district, Centers centersMap) {
        UserRegistration userRegistration = new UserRegistration();
        List<UserRegistrationConstraints> userRegistrationConstraints = userRegistration.getUserRegistrationConstraints().get(district);

        if (centersMap != null && centersMap.getCenters() != null && centersMap.getCenters().size() > 0) {
            for (Center center : centersMap.getCenters()) {
                if (userRegistrationConstraints != null && userRegistrationConstraints.size() > 0) {
                    for (UserRegistrationConstraints userRegistrationConstraints1 : userRegistrationConstraints) {
                        SlotConstraintValidator slotConstraintValidator = new SlotConstraintValidator();
                        List<Session> sessions = slotConstraintValidator.getValidCenter(center, userRegistrationConstraints1);
                        if (sessions != null && sessions.size() > 0) {
                            //playSound();
                            //append to email and send email.
                            //                            StringBuilder emailContent = new StringBuilder(center.getCenterDetails());
                            //                            sessions.forEach(i -> emailContent.append(i).append("\n"));
                            //                            System.out.println(emailContent);
                            //new EmailSender().sendEmail(emailContent, userRegistrationConstraints1.getEmail());
                        }
                    }
                }
            }
        }
    }

    public static Centers makeCall(String district, Date date) {
        DefaultHttpClient httpClient = null;
        try {
            httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(URL + "?district_id=" + district + "&date=" + FORMATTER.format(date));
            getRequest.addHeader("Connection", "gzip, deflate, br");
            getRequest.addHeader("Connection", "keep-alive");
            getRequest.addHeader("Accept", "*/*");
            getRequest.addHeader("User-Agent", "PostmanRuntime/7.28.0");

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(responseString, Centers.class);
            }
        } catch (ClientProtocolException e) {
            LOG.error("", e);
        } catch (IOException e) {
            LOG.error("", e);
        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
        return null;
    }

    public static synchronized void playSound() {
        new Thread(
            new Runnable() {
                // The wrapper thread is unnecessary, unless it blocks on the
                // Clip finishing; see comments.
                public void run() {
                    try {
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            getClass().getClassLoader().getResourceAsStream("bell.wav")
                        );
                        clip.open(inputStream);
                        clip.start();
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        LOG.error("", e);
                    }
                }
            }
        )
            .start();
    }
}
