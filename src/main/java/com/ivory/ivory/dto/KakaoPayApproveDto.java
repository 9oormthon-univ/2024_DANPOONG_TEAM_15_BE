package com.ivory.ivory.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoPayApproveDto {
    private String aid;
    private String tid;
    private String cid;
    private String sid;
    private String partner_order_id;
    private String partner_user_id;
    private Amount amount;
    private String item_name;
    private String item_code;
    private int quantity;
    private String create_at;
    private String approved_at;
    private String payload;
}
