package masterselect;

import java.io.Serializable;

/**
 * 数据节点信息
 */
public class RunningData  implements Serializable{
    private static final long serialVersionUID = -507508900856811018L;

    private Long cid;

    private String name;

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
