package com.sdu.inas.util;

import com.sdu.inas.beans.HbaseModel;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by J on  17-10-27.
 */
public class HbaseModelUtil {

    public static final String BASICTABLE ="Objects";

    public static final String BASIC_TABLE ="Objects";

    public static final String BASIC_RAW = "Rawinfo";

    public static final String RAW_TEXT="Rawtext";

    public static final String BASIC_EVENT = "Eventlist";

    public static final String EVENTS_TABLE = "Event_Infos";

    public static final String EVENTS_PARAMS ="EventParam";


    public static final String CF1 = "rawinfo";

    public static final String COLUMN1 = "realname";

    public static final String COLUMN2 = "rawtext";

    public static final String CF2 = "timeline";



    public static final String CONNTABLE = "Connection";

    public static final String CONN_IN = "Inconn";

    public static final String CONN_OUT = "Outconn";

    public static final String RELEVANCES_TABLE = "Rele_Infos";

    public static final String RELEVANCES_PARAMS = "ReleParam";


    public static final String DEFAULT = "default";

    public static HbaseModel kvToHbaseModel(KeyValue kv) {
        HbaseModel hbaseModel = new HbaseModel();
        hbaseModel.setRow(Bytes.toString(kv.getRow()));
        hbaseModel.setFamilyName(Bytes.toString(kv.getFamily()));
        hbaseModel.setQualifier(Bytes.toString(kv.getQualifier()));
        hbaseModel.setValue(Bytes.toString(kv.getValue()));
        return hbaseModel;
    }
}
