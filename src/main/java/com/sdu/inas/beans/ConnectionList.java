package com.sdu.inas.beans;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.ArrayList;

@Data
@XStreamAlias("connectionlist")
public class ConnectionList {

    @XStreamImplicit
    private ArrayList<Connection> connections;

    public ConnectionList() {
    }

    public ConnectionList(ArrayList<Connection> connections) {
        this.connections = connections;
    }


}
