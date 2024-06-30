package jp.co.jim.entity;

import lombok.Data;

@Data
public class DGMainEntity {

    private int item_no;
    private String item_name;

    public DGMainEntity(int item_no, String item_name) {
        this.item_no = item_no;
        this.item_name = item_name;
    }

}
