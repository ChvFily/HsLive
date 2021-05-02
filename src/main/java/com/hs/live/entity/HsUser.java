package com.hs.live.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LIBO
 * @since 2021-01-11
 */
@TableName("hs_user")
public class HsUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("username")
    private String username;

    @TableField("userpwd")
    private String userpwd;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUserpwd(){
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    @Override
    public String toString() {
        return "HsUser{" +
            "username=" + username +
            ", userpwd=" + userpwd +
        "}";
    }
}
