package com.jiahe.iot.common.util;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jiahe.iot.common.bean.TokenClient;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    /**
     * 设置过期时间及密匙
     * CALENDAR_FIELD 时间单位
     * CALENDAR_INTERVAL 有效时间
     * SECRET_KEY 密匙
     */
    public static final int CALENDAR_FIELD = Calendar.MINUTE;
    public static final int CALENDAR_INTERVAL = 60 * 24;
    private static final String SECRET_KEY = "6A50A18D70FA63636645C65459F1D78A";

    public static String createToken(TokenClient object, Date expireTime) {
        Map<String, Object> headerMap = new HashMap<>(4);
        headerMap.put("alg", "HS256");
        headerMap.put("typ", "JWT");
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create().withHeader(headerMap)
                .withIssuedAt(Calendar.getInstance().getTime())
                .withExpiresAt(expireTime)
                .withSubject("object")
                .withClaim("object", JSON.toJSONString(object))
                .sign(algorithm);
    }

    public static String createToken(TokenClient object) {
        Calendar time = Calendar.getInstance();
        Date now = time.getTime();
        time.add(CALENDAR_FIELD, CALENDAR_INTERVAL);
        Date expireTime = time.getTime();
        return createToken(object, expireTime);
    }

    public static String verifyToken(String token) {
        DecodedJWT verifier = null;
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        try {
            verifier = JWT.require(algorithm).build().verify(token);
        } catch (Exception e) {
            return null;
        }
        return verifier.getClaim("object").asString();
    }
}

