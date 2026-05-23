package com.zifang.util.parser.ini;

import java.util.ArrayList;
import java.util.List;

/**
 * INI 文件数据模型
 */
public class IniFile {

    private List<IniSection> sections;

    public IniFile() {
        this.sections = new ArrayList<>();
    }

    public List<IniSection> getSections() {
        return sections;
    }

    public void setSections(List<IniSection> sections) {
        this.sections = sections;
    }

    public void addSection(IniSection section) {
        sections.add(section);
    }

    /**
     * 获取指定名称的 Section
     */
    public IniSection getSection(String name) {
        for (IniSection section : sections) {
            if (section.getName() != null && section.getName().equals(name)) {
                return section;
            }
        }
        return null;
    }

    /**
     * 获取全局 Section（第一个 name 为 null 的 Section）
     */
    public IniSection getGlobalSection() {
        for (IniSection section : sections) {
            if (section.getName() == null || section.getName().isEmpty()) {
                return section;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (IniSection section : sections) {
            sb.append(section.toString());
        }
        return sb.toString();
    }
}
