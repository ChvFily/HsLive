package com.hs.live.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 视频文件下载的服务器配置
 * </p>
 *
 * @author LIBO
 * @since 2020-12-31
 */
@TableName("hs_file_server")
public class HsFileServer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件下载的ip地址或域名
     */
    @TableId("server_ip")
    private String serverIp;

    /**
     * http 或https
     */
    @TableField("server_schema")
    private String serverSchema;

    /**
     * 端口
     */
    @TableField("server_port")
    private int serverPort;

    /**
     * 服务器文档根目录，即视频文件放置在此目录下
     */
    @TableField("server_doc_dir")
    private String serverDocDir;

    /**
     * srs的回调api端口
     */
    @TableField("srs_api_port")
    private int srsApiPort;
    /**
     * srs的rtmp流端口
     */
    @TableField("srs_rtmp_port")
    private int srsRtmpPort;
    /**
     * srs的flv流端口
     */
    @TableField("srs_http_port")
    private int srsHttpPort;
    
    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
    public String getServerSchema() {
        return serverSchema;
    }

    public int getSrsApiPort() {
		return srsApiPort;
	}

	public void setSrsApiPort(int srsApiPort) {
		this.srsApiPort = srsApiPort;
	}

	public void setServerSchema(String serverSchema) {
        this.serverSchema = serverSchema;
    }
    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
    public String getServerDocDir() {
        return serverDocDir;
    }

    public void setServerDocDir(String serverDocDir) {
        this.serverDocDir = serverDocDir;
    }
    
    public int getSrsRtmpPort() {
		return srsRtmpPort;
	}

	public void setSrsRtmpPort(int srsRtmpPort) {
		this.srsRtmpPort = srsRtmpPort;
	}

	public int getSrsHttpPort() {
		return srsHttpPort;
	}

	public void setSrsHttpPort(int srsHttpPort) {
		this.srsHttpPort = srsHttpPort;
	}

	@Override
    public String toString() {
        return "HsFileServer{" +
            "serverIp=" + serverIp +
            ", serverSchema=" + serverSchema +
            ", serverPort=" + serverPort +
            ", serverDocDir=" + serverDocDir +
        "}";
    }
}
