package com.zifang.util.parser.proto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Proto Service 模型。
 *
 * @author zifang
 */
public class ProtoService {

    private final String name;
    private final List<ProtoRpc> rpcs;

    public ProtoService(String name) {
        this.name = name;
        this.rpcs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<ProtoRpc> getRpcs() {
        return rpcs;
    }

    public void addRpc(ProtoRpc rpc) {
        rpcs.add(rpc);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProtoService that = (ProtoService) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(rpcs, that.rpcs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, rpcs);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("service ").append(name).append(" {");
        for (ProtoRpc rpc : rpcs) {
            sb.append(" ").append(rpc.toString());
        }
        sb.append(" }");
        return sb.toString();
    }
}
