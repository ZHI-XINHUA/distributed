package pullpush;

import java.io.Serializable;

/**
 * WorkServer 服务器基本信息
 */
public class ServerData implements Serializable{


    private static final long serialVersionUID = 4794639708660543842L;

    /**服务器地址**/
    private String address;

    /**id**/
    private Integer id;

    /**名称**/
    private String name;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ServerData [address=" + address + ", id=" + id + ", name=" + name + "]";
    }
}
