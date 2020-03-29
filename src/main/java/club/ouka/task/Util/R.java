package club.ouka.task.Util;

import java.util.HashMap;

/**
 * Description:
 * User: Ouka
 * Date: 2020-03-29
 * Time: 12:05
 */
public class R extends HashMap<String,Object> {

    private static  final String SUCCESS = "200";
    private static final String FAIL = "500";
    private  String code;
    private String msg;

    public R(){}

    public R(String code){
        this.put("code",code);
    }

    public R(String code, String msg){
        super.put("code",code);
        super.put("msg",msg);
    }

    public  static R ok(){
        return new R(SUCCESS);
    }

    public  static R customError(String code,String msg){
        return new R(code).put("msg",msg);
    }

    public  static R ok(String msg){
        return new R(SUCCESS,msg);
    }

    public  static R error(){
        return new R(FAIL);
    }

    public  static R error(String msg){
        return new R(FAIL,msg);
    }

    @Override
    public R put(String key, Object o){
        super.put(key,o);
        return this;
    }
}
