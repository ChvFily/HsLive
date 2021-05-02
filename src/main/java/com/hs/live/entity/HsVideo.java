package com.hs.live.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 录制的视频 对应的数据库和绑定
 * </p>
 *
 * @author LIBO
 * @since 2020-12-31 
 * 
 */
@TableName("hs_video")
public class HsVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type = IdType.AUTO)
    private int id;

    /**
     * 录制视频的客户端id
     */
    @TableField("client_id")
    private int clientId;

    /**
     * 录制视频的客户端ip
     */
    @TableField("client_ip")
    private String clientIp;
    
    /**
     * 是否删除，0-正常,1-删除
     */
    @TableField("delete_flag")
    private int deleteFlag;
    
    /**
     * 公开类型：1-公开，其他-不公开（登录后可见）
     */
    @TableField("public_type")
    private int publicType;

    /**
     * srs 虚拟主机
     */
    @TableField("srs_vhost")
    private String srsVhost;

    /**
     * srs ip
     */
    @TableField("srs_ip")
    private String srsIp;

    @TableField("srs_app")
    private String srsApp;

    @TableField("srs_stream")
    private String srsStream;

    @TableField("srs_param")
    private String srsParam;

    /**
     * srs 当前工作目录
     */
    @TableField("srs_cwd")
    private String srsCwd;

    /**
     * srs 文件路径，如/var/www/html/live/test.1609384972543.flv
     */
    @TableField("srs_file")
    private String srsFile;

    /**
     * 保存日期
     */
    @TableField("save_time")
    private Date saveTime;

    /**
     * 视频标题
     */
    @TableField("video_title")
    private String videoTitle;

    /**
     * 视频分类
     */
    @TableField("video_type")
    private String videoType;

    /**
     * 视频描述信息\\n
     */
    @TableField("video_desc")
    private String videoDesc;

    /**
     * 可以是上传的视频的相对路径地址，也可以是其他文档服务器的绝对路径地址
     */
    @TableField("video_url")
    private String videoUrl;

    @TableField(exist = false)
    private String saveTimeStr;
    
    @TableField(exist = false)
    private String pubicText;
    
    public String getPubicText() {
    	return 1==publicType?"公开":"不公开";
    }
    
    public String getSaveTimeStr() {
        return new SimpleDateFormat("yyyy年MM月dd日").format(saveTime);
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
    public String getSrsVhost() {
        return srsVhost;
    }

    public int getPublicType() {
		return publicType;
	}

	public void setPublicType(int publicType) {
		this.publicType = publicType;
	}

	public void setSrsVhost(String srsVhost) {
        this.srsVhost = srsVhost;
    }
    public String getSrsIp() {
        return srsIp;
    }

    public void setSrsIp(String srsIp) {
        this.srsIp = srsIp;
    }
    public String getSrsApp() {
        return srsApp;
    }

    public void setSrsApp(String srsApp) {
        this.srsApp = srsApp;
    }
    public String getSrsStream() {
        return srsStream;
    }

    public void setSrsStream(String srsStream) {
        this.srsStream = srsStream;
    }
    public String getSrsParam() {
        return srsParam;
    }

    public void setSrsParam(String srsParam) {
        this.srsParam = srsParam;
    }
    public String getSrsCwd() {
        return srsCwd;
    }

    public void setSrsCwd(String srsCwd) {
        this.srsCwd = srsCwd;
    }
    public String getSrsFile() {
        return srsFile;
    }

    public void setSrsFile(String srsFile) {
        this.srsFile = srsFile;
    }
    public Date getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Date saveTime) {
        this.saveTime = saveTime;
    }
    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }
    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }
    public String getVideoDesc() {
        return videoDesc;
    }

    public void setVideoDesc(String videoDesc) {
        this.videoDesc = videoDesc;
    }
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public String toString() {
        return "HsVideo{" +
            "id=" + id +
            ", clientId=" + clientId +
            ", clientIp=" + clientIp +
            ", srsVhost=" + srsVhost +
            ", srsIp=" + srsIp +
            ", srsApp=" + srsApp +
            ", srsStream=" + srsStream +
            ", srsParam=" + srsParam +
            ", srsCwd=" + srsCwd +
            ", srsFile=" + srsFile +
            ", saveTime=" + saveTime +
            ", videoTitle=" + videoTitle +
            ", videoType=" + videoType +
            ", videoDesc=" + videoDesc +
            ", videoUrl=" + videoUrl +
        "}";
    }
}
