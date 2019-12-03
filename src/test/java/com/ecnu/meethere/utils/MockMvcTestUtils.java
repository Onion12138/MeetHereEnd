package com.ecnu.meethere.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ecnu.meethere.common.result.Result;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class MockMvcTestUtils {
    public static String jsonSerialize(Object o) {
        return JSON.toJSONString(o);
    }

    public static <T> Result<T> parseMvcResult(MvcResult result, Class<T> responseType) throws UnsupportedEncodingException {
        if (responseType != null)
            return JSON.parseObject(result.getResponse().getContentAsString(),
                    new TypeReference<>() {
                    });

        return JSON.parseObject(result.getResponse().getContentAsString(), Result.class);
    }
}
