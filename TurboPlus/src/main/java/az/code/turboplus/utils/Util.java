package az.code.turboplus.utils;

import az.code.turboplus.dtos.UserData;
import az.code.turboplus.exceptions.EmailNotVerified;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

public class Util {
    public static Pageable preparePage(Integer pageNo, Integer pageSize, String sortBy) {
        return PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
    }

    public static Pageable preparePage(Integer pageNo, Integer pageSize) {
        return PageRequest.of(pageNo, pageSize);
    }

    public static <T> List<T> getResult(Page<T> pageResult) {
        if (pageResult.hasContent()) {
            return pageResult.getContent();
        } else {
            return new LinkedList<>();
        }
    }

    public static UserData convertToken(String auth) throws JsonProcessingException {
        UserData user = new UserData();
        String[] chunks = auth.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String data = new String(decoder.decode(chunks[1]));
        JsonNode payload = new ObjectMapper().readValue(data, JsonNode.class);
        if (!payload.get("email_verified").booleanValue())
            throw new EmailNotVerified();
        user.setUsername(payload.get("preferred_username").textValue());
        user.setName(payload.get("name").textValue());
        user.setPhoneNumber(payload.get("phoneNumber").textValue());
        user.setEmail(payload.get("email").textValue());
        user.setRegistrationTime(
                LocalDateTime.ofEpochSecond(payload.get("createdDate").longValue(),
                        0,
                        ZoneOffset.ofHours(4)));
        return user;
    }
}
