package balance.server;

import java.io.Serializable;

/**
 * 服务器信息
 */
public class ServerData implements Serializable,Comparable<ServerData> {
    private static final long serialVersionUID = 5474544887231289524L;
    /**服务器host**/
    private String host;
    /**端口号**/
    private Integer port;
    /**负载数**/
    private Integer balance;

    public ServerData() {
        super();
    }

    public ServerData(String host, Integer port, Integer balance) {
        this.host = host;
        this.port = port;
        this.balance = balance;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }



    @Override
    public int compareTo(ServerData o) {
        return this.getBalance().compareTo(o.getBalance());
    }
}
