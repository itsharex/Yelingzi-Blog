package com.yeling.yelingziblog.other.service;

import com.yeling.yelingziblog.other.entity.ClassesTable;

import java.util.List;

public interface ClassesTableService {

    /**
     * 查询课程表
     * @return
     */

    List<ClassesTable> list(String semester);

    void addClassTable(ClassesTable classesTable);

    List<ClassesTable> getClassTable();

    Boolean editClassTable(ClassesTable classesTable);

}
