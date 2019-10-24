package com.sdu.inas.beans;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Word {

    /**
     *
     */
    private int id;

    /**
     * 内容
     */
    private String cont;

    /**
     * 词性
     */
    private String pos;

    /**
     * 词性
     */
    private  String ne;

    /**
     * 父节点的id
     */
    private Integer parent;

    /**
     * 依存关系
     */
    private String relate;

    /**
     * 词性
     */
    private Integer semparent;

    /**
     * 语义关系依存分析
     */
    private String semrelate;


    private List<Arg> args = new ArrayList<>();

    public Word() {
    }

}
