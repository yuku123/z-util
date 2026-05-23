package com.zifang.util.parser.proto;

import java.util.*;

/**
 * Proto Message 模型。
 *
 * @author zifang
 */
public class ProtoMessage {

    private final String name;
    private final List<ProtoField> fields;
    private final List<ProtoEnum> enums;
    private final List<ProtoMessage> messages;

    public ProtoMessage(String name) {
        this.name = name;
        this.fields = new ArrayList<>();
        this.enums = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<ProtoField> getFields() {
        return fields;
    }

    public void addField(ProtoField field) {
        fields.add(field);
    }

    public List<ProtoEnum> getEnums() {
        return enums;
    }

    public void addEnum(ProtoEnum protoEnum) {
        enums.add(protoEnum);
    }

    public List<ProtoMessage> getMessages() {
        return messages;
    }

    public void addMessage(ProtoMessage message) {
        messages.add(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProtoMessage that = (ProtoMessage) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(fields, that.fields) &&
                Objects.equals(enums, that.enums) &&
                Objects.equals(messages, that.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, fields, enums, messages);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("message ").append(name).append(" {");
        for (ProtoEnum e : enums) {
            sb.append(" ").append(e.toString());
        }
        for (ProtoMessage m : messages) {
            sb.append(" ").append(m.toString());
        }
        for (ProtoField f : fields) {
            sb.append(" ").append(f.toString());
        }
        sb.append(" }");
        return sb.toString();
    }
}
