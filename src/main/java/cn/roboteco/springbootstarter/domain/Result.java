package cn.roboteco.springbootstarter.domain;

import java.io.Serializable;

import cn.roboteco.springbootstarter.common.Constants;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 响应信息主体
 *
 * @param <T>
 */
@Builder
@ToString
@Accessors(chain = true)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String code = Constants.OK;

    @Getter
    @Setter
    private String msg = Constants.SUCCESS;

    @Getter
    @Setter
    private T data;

    public Result() {
        super();
    }

    public Result(T data) {
        super();
        this.data = data;
    }

    public Result(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public Result(String code, String msg, T data) {
        super();
        this.data = data;
        this.msg = msg;
        this.code = code;
    }

    public Result(Throwable e) {
        super();
        this.msg = e.getMessage();
        this.code = Constants.ERROR;
    }
}
