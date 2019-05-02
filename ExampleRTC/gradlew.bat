package com.senate_system.baac.UI.Model;

import java.util.List;

public class ConfirmCart {

    /**
     * ErrorCode : 1
     * ErrorSubCode : 0
     * Title : null
     * ErrorMesg :
     * ErrorDetail :
     * FlowControl : 0
     * Data : [{"border_id":"41","district_name":"ทุ่งสองห้อง","wsp_id":"2","remark":"iodev shop","content_path":null,"address":"12 ห้อง 1 ชั้น 500 io io dev ม.2 ซอย 567 ถนน ja ต.ทุ่งสองห้อง อ.หลักสี่ จ.กรุงเทพมหานคร 10210","pic_path_local":null,"type_group":"4","border_name":"หลักสี่","tax_inclusive":true,"email":"iodev@gmail.com","member_type":null,"create_date":"2018-06-29T10:20:33.477","postcode":"10210","branch_tax":null,"moo":"2","create_by":"10028","latitude":13.905086517333984,"create_date_bean":{"Year":2018,"Month":6,"Day":29,"Hour":10,"Minute":20,"Second":33,"Millisecond":477,"Ticks":"636658644334770000"},"province_name":"กรุงเทพมหานคร","longitude":100.52114868164062,"date_of_birth":null,"edit_date_bean":{"Year":2018,"Month":7,"Day":5,"Hour":20,"Minute":15,"Second":39,"Millisecond":200,"Ticks":"636664185392000000"},"tax_id":null,"f_name":null,"edit_by":"200038","recv_news":true,"inventory":true,"edit_date":"2018-07-05T20:15:39.2","floor":"500","mobile":null,"district_id":"136","inactive":false,"outlet_id":"200038","village":"io dev","tel":"0298745612","outlet_name3":null,"ref_outlet_id":null,"wh_id":"200038","content_id":null,"building":"io","room_no":"1","outlet_type":"2","road":"ja","id_card":"4056679840093","l_name":null,"date_of_birth_bean":null,"province_id":"111","outlet_code":"200038","addr_no":"12","soi":"567","outlet_name1":null,"outlet_name2":null,"outlet_name4":null,"calc_tax":true,"outlet_name":"iodev","laser_id":null,"pic_path":null,"branch_no":null,"merchant_id":null,"outlet_name5":null}]
     */

    private List<DataBean> Data;

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * border_id : 41
         * district_name : ทุ่ง�