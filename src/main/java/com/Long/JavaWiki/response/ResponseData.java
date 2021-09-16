package com.Long.JavaWiki.response;

import com.Long.JavaWiki.exception.EnumCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> {

    private int code;       // 状态码
    private String message; // 提示消息
    private T content;         // 响应结果集数据

    /**
     * 枚举类常量解析器
     * 快速解析枚举类常量信息，解析数据并放入到标准响应类ResponseData的属性中
     */
    public void parserEnum(EnumCode enumCode) {
        this.code = enumCode.getCode();// 获取枚举常量的状态码，赋值给属性
        this.message = enumCode.getDesc();// 获取枚举常量的描述信息
    }

    /**
     * 定义请求成功的：状态码，描述，结果集数据
     * @param content 传递的响应结果集数据
     * @return 有成功状态码，描述，结果集数据的标准格式对象
     */
    public static <T> ResponseData<T> success(T content) {
        // 创建响应标准格式对象
        ResponseData<T> responseData = new ResponseData<>();
        // 调用转换器方法，将（成功）枚举常量解析，放入到标准响应数据中。
        responseData.parserEnum(EnumCode.SUCCESS);
        // 放入响应数据
        responseData.setContent(content);
        return responseData;
    }

    /**
     * 定义请求失败的：
     * @param enumCode 失败时传递的常见错误枚举常量
     * @return 有失败状态码，描述，结果集可为null，也可为自定义异常信息
     */
    public static <T> ResponseData<T> error(EnumCode enumCode, T content) {
        // 创建响应标准格式对象
        ResponseData<T> responseData = new ResponseData<>();
        // 调用转换器方法，将（错误）枚举常量解析。
        responseData.parserEnum(enumCode);
        responseData.setContent(content);
        return responseData;
    }

    /**
     * 状态描述不一定能全部满足需求（枚举类有限），自定义方法
     * @param code    自定义的状态码
     * @param message 自定义的错误信息
     * @return 有失败自定义状态码，自定义描述，结果集可为null，也可为自定义异常信息
     */
    public static <T> ResponseData<T> generator(int code, String message, T content) {
        // 创建响应标准格式对象
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setCode(code);
        responseData.setMessage(message);
        responseData.setContent(content);
        return responseData;
    }

}
