package com.iptv.online.player.live.tv.smart.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/* loaded from: classes5.dex */
public class XtreamUserInfo implements Serializable {
    @SerializedName("server_info")
    private ServerInfo serverInfo;
    @SerializedName("user_info")
    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public ServerInfo getServerInfo() {
        return this.serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    /* loaded from: classes5.dex */
    public static class UserInfo implements Serializable {
        @SerializedName("active_cons")
        private String activeCons;
        @SerializedName("exp_date")
        private String expDate;
        @SerializedName("is_trial")
        private String isTrial;
        @SerializedName("max_connections")
        private String maxConnections;
        @SerializedName("password")
        private String password;
        @SerializedName("status")
        private String status;
        @SerializedName("username")
        private String username;

        public String getUsername() {
            return this.username;
        }

        public String getStatus() {
            return this.status;
        }

        public String getExpDate() {
            return this.expDate;
        }

        public String getMaxConnections() {
            return this.maxConnections;
        }
    }

    /* loaded from: classes5.dex */
    public static class ServerInfo implements Serializable {
        @SerializedName("https_port")
        private String httpsPort;
        @SerializedName("port")
        private String port;
        @SerializedName("rtmp_port")
        private String rtmpPort;
        @SerializedName("server_protocol")
        private String serverProtocol;
        @SerializedName("timezone")
        private String timezone;
        @SerializedName("url")
        private String url;

        public String getUrl() {
            return this.url;
        }

        public String getPort() {
            return this.port;
        }

        public String getServerProtocol() {
            return this.serverProtocol;
        }
    }
}
