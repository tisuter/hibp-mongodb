package com.example.hibp;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/api")
@Slf4j
public class Controller {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ObjectMapper om;

    private static final String HEX_CHARS = "0123456789ABCDEF";
//    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    @GetMapping("/{key}")
    public List<String> getHashes(@PathVariable String key) {
        String regex = convertToRegex(key);

        Query query = new Query(Criteria.where("_id").regex(regex)); //Criteria.where("_id").regex("^0")
        query.limit(1000);
        List<PwdHash> pwndpsswds = mongoTemplate.find(query, PwdHash.class, "pwndpsswds");
        return pwndpsswds.stream()
                .map(PwdHash::get_id)
                .collect(Collectors.toList());

//        return pwndpsswds.stream()
//                .map(Document::toJson)
//                .map(this::convert)
//                .map(this::extractHash)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
    }

    private String convertToRegex(String key) {
        if (key.length() != 5) {
            throw new IllegalArgumentException("Key should be size 5");
        }

        String illegalChars = key.toUpperCase().chars()
                .mapToObj(c -> (char) c)
                .map(Object::toString)
                .filter(c -> !HEX_CHARS.contains(c))
                .distinct()
                .collect(Collectors.joining(","));
        if(illegalChars.length() > 0) {
            throw new IllegalArgumentException("Key can only contain HEX Chars. Wrong chars: " + illegalChars);
        }

        return "^" + key.toUpperCase();
    }

//    private String extractHash(Map<String, Object> document) {
//        try {
//            Map<String, Map<String, String>> id = (Map<String, Map<String, String>>) document.get("_id");
//            return id.get("$binary").get("base64");
//        } catch (Exception ex) {
//            return null;
//        }
//    }

//    private Map<String, Object> convert(String j) {
//        try {
//            return om.readValue(j, new TypeReference<>() {
//            });
//        } catch (com.fasterxml.jackson.core.JsonProcessingException ex) {
//            throw new RuntimeException(ex);
//        }
//    }

//    private String encodeHexString(byte[] byteArray) {
//        StringBuffer hexStringBuffer = new StringBuffer();
//        for (int i = 0; i < byteArray.length; i++) {
//            hexStringBuffer.append(byteToHex(byteArray[i]));
//        }
//        return hexStringBuffer.toString();
//    }

//    private String byteToHex(byte num) {
//        char[] hexDigits = new char[2];
//        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
//        hexDigits[1] = Character.forDigit((num & 0xF), 16);
//        return new String(hexDigits);
//    }

//    private static String bytesToHex(byte[] bytes) {
//        char[] hexChars = new char[bytes.length * 2];
//        for (int j = 0; j < bytes.length; j++) {
//            int v = bytes[j] & 0xFF;
//            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
//            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
//        }
//        return new String(hexChars);
//    }
}
