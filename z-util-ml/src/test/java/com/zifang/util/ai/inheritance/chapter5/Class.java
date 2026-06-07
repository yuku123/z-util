package com.zifang.util.ml.inheritance.chapter5;

/**
 * A simple class abstraction -- basically a container for class, group, module, professor, timeslot, and room IDs
 */
/**
 * Class类。
 */
public class Class {
    private final int classId;
    private final int groupId;
    private final int moduleId;
    private int professorId;
    private int timeslotId;
    private int roomId;

    /**
     * Initialize new Class
     *
     * @param classId
     * @param groupId
     * @param moduleId
     */
    /**
     * Class方法。
     *      * @param classId int类型参数
     * @param groupId int类型参数
     * @param moduleId int类型参数
     */
    public Class(int classId, int groupId, int moduleId) {
        this.classId = classId;
        this.moduleId = moduleId;
        this.groupId = groupId;
    }

    /**
     * Add professor to class
     *
     * @param professorId
     */
    /**
     * addProfessor方法。
     *      * @param professorId int类型参数
     */
    public void addProfessor(int professorId) {
        this.professorId = professorId;
    }

    /**
     * Add timeslot to class
     *
     * @param timeslotId
     */
    /**
     * addTimeslot方法。
     *      * @param timeslotId int类型参数
     */
    public void addTimeslot(int timeslotId) {
        this.timeslotId = timeslotId;
    }

    /**
     * Add room to class
     *
     * @param roomId
     */
    /**
     * setRoomId方法。
     *      * @param roomId int类型参数
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    /**
     * Get classId
     *
     * @return classId
     */
    /**
     * getClassId方法。
     * @return int类型返回值
     */
    public int getClassId() {
        return this.classId;
    }

    /**
     * Get groupId
     *
     * @return groupId
     */
    /**
     * getGroupId方法。
     * @return int类型返回值
     */
    public int getGroupId() {
        return this.groupId;
    }

    /**
     * Get moduleId
     *
     * @return moduleId
     */
    /**
     * getModuleId方法。
     * @return int类型返回值
     */
    public int getModuleId() {
        return this.moduleId;
    }

    /**
     * Get professorId
     *
     * @return professorId
     */
    /**
     * getProfessorId方法。
     * @return int类型返回值
     */
    public int getProfessorId() {
        return this.professorId;
    }

    /**
     * Get timeslotId
     *
     * @return timeslotId
     */
    /**
     * getTimeslotId方法。
     * @return int类型返回值
     */
    public int getTimeslotId() {
        return this.timeslotId;
    }

    /**
     * Get roomId
     *
     * @return roomId
     */
    /**
     * getRoomId方法。
     * @return int类型返回值
     */
    public int getRoomId() {
        return this.roomId;
    }
}

